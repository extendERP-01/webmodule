package nirmalya.aathithya.webmodule.production.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipePolishingModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeProductionModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPipePolishingController {

	Logger logger = LoggerFactory.getLogger(ProductionMotherCoilSlitController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view pipe polishing page
	 */
	@GetMapping("/view-pipe-polishing")
	public String viewPipepolishing(Model model, HttpSession session) {

		logger.info("Method : viewPipepolishing   starts");

		logger.info("Method : viewPipepolishing   ends");

		return "production/view-pipe-polishing";
	}

	/*
	 * For view polishing throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-pipe-polishing-throughajax")
	public @ResponseBody DataTableResponse viewPipepolishing(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewPipepolishing statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			JsonResponse<List<ProductionPipeProductionModel>> jsonResponse = new JsonResponse<List<ProductionPipeProductionModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "viewPolishingDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipeProductionModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipeProductionModel>>() {
					});

			String s = "";

			for (ProductionPipeProductionModel m : motherCoilSlit) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.gettMotherCoilBatch().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.gettMotherCoilThickness().getBytes());
				byte[] encodeId3 = Base64.getEncoder().encode(m.gettPipeSlitBatch().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  ";
				s = s + "<a href='view-pipe-polishing-edit?batchid=" + new String(encodeId) + "&mthick="
						+ new String(encodeId2) + "&slitbatch=" + new String(encodeId3)
						+ "' ><button class=\"btn btn-info\">Add Polishing Dtls" + "</button></a>";

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(motherCoilSlit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPipepolishing ends");
		return response;
	}

	/*
	 * For Modal pipe polishing
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-polishing-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalAssignmentDepnd(Model model,
			@RequestBody ProductionPipePolishingModel index, BindingResult result) {

		logger.info("Method :modalAssignmentDepnd starts");

		byte[] batchId = Base64.getDecoder().decode(index.gettMotherCoilBatch().getBytes());
		byte[] thickId = Base64.getDecoder().decode(index.gettMotherCoilThickness().getBytes());
		byte[] slitbatchId = Base64.getDecoder().decode(index.gettPipeSlitBatch().getBytes());

		String mBatchId = (new String(batchId));
		String mThickId = (new String(thickId));
		String slitBatchId = (new String(slitbatchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getProduction() + "viewMotherCoilById?mBatchId=" + mBatchId
					+ "&mThickId=" + mThickId + "&slitBatchId=" + slitBatchId, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalAssignmentDepnd  ends ");
		return response;
	}

	/**
	 * edit pipe polishing
	 * 
	 * @param model
	 * @param mBatchId1
	 * @param mThickId1
	 * @param slitBatchId1
	 * @param session
	 * @return
	 */

	@GetMapping("view-pipe-polishing-edit")
	public String editMotherCoil(Model model, @RequestParam("batchid") String mBatchId1,
			@RequestParam("mthick") String mThickId1, @RequestParam("slitbatch") String slitBatchId1,
			HttpSession session) {

		logger.info("Method : editMotherCoilSlit starts");
		byte[] encodeByte = Base64.getDecoder().decode(mBatchId1.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(mThickId1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(slitBatchId1.getBytes());

		String mBatchId = (new String(encodeByte));
		String mThickId = (new String(encodeByte1));
		String slitBatchId = (new String(encodeByte2));

		try {

			ProductionPipePolishingModel[] motherCoil = restClient
					.getForObject(env.getProduction() + "edit-polishing-ById?mBatchId=" + mBatchId + "&mThickId="
							+ mThickId + "&slitBatchId=" + slitBatchId, ProductionPipePolishingModel[].class);
			List<ProductionPipePolishingModel> mCoilList = Arrays.asList(motherCoil);
			if (!mCoilList.isEmpty()) {
				model.addAttribute("edit", "for Edit");

				model.addAttribute("mCoilList", mCoilList);
			}
			if (!mCoilList.isEmpty()) {
				// Drop Down Grade
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getthicknessEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade(),
							DropDownModel[].class);
					List<DropDownModel> thicknessList = Arrays.asList(MCoilGrade);

					model.addAttribute("thicknessList", thicknessList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!mCoilList.isEmpty()) {
				// Drop Down Grade
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getSlitWidthEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade()
									+ "&thickness=" + mCoilList.get(0).gettMotherCoilThickness(),
							DropDownModel[].class);
					List<DropDownModel> slitWidthList = Arrays.asList(MCoilGrade);

					model.addAttribute("slitWidthList", slitWidthList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!mCoilList.isEmpty()) {
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getPipeSizeit?grade=" + mCoilList.get(0).gettMotherCoilGrade()
									+ "&thickness=" + mCoilList.get(0).gettMotherCoilThickness(),
							DropDownModel[].class);
					List<DropDownModel> pipeSizeList = Arrays.asList(MCoilGrade);
					model.addAttribute("pipeSizeList", pipeSizeList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editMotherCoilSlit ends");
		return "production/add-pipe-polishing";

	}

	/*
	 * for add pipe polishing through ajax
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-polishing-add-ajax")
	public @ResponseBody JsonResponse<Object> addpolishingPlanning(
			@RequestBody List<ProductionPipePolishingModel> productionPipePolishingModelList, HttpSession session) {

		logger.info("Method : addpolishingPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (ProductionPipePolishingModel r : productionPipePolishingModelList) {
				r.settPipeCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getProduction() + "addPolishingDetails",
					productionPipePolishingModelList, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : addpolishingPlanning function Ends");
		return resp;
	}

	/*
	 * For Modal polishing details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-polishing-details-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalDetails(Model model, @RequestBody MotherCoilSlitModel index,
			BindingResult result) {

		logger.info("Method :modalDetails starts");

		byte[] batchId = Base64.getDecoder().decode(index.gettMotherCoilBatch().getBytes());
		byte[] thickId = Base64.getDecoder().decode(index.gettMotherCoilThickness().getBytes());
		byte[] slitbatchId = Base64.getDecoder().decode(index.gettPipeSlitBatch().getBytes());
		byte[] slitWidthId = Base64.getDecoder().decode(index.gettPipeSlitWidth().getBytes());
		byte[] pipeSizeId = Base64.getDecoder().decode(index.gettPipeSize().getBytes());

		String mBatchId = (new String(batchId));
		String mThickId = (new String(thickId));
		String slitBatchId = (new String(slitbatchId));
		String slitWidth = (new String(slitWidthId));
		String pipeSize = (new String(pipeSizeId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(
					env.getProduction() + "getPolishingDetailsModal?mBatchId=" + mBatchId + "&mThickId=" + mThickId
							+ "&slitBatchId=" + slitBatchId + "&slitWidth=" + slitWidth + "&pipeSize=" + pipeSize,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalDetails  ends ");
		return response;
	}

	/*
	 * Get Mapping view pipe production page
	 */
	@GetMapping("/view-pipe-polishing-details")
	public String viewPipeProductionDetails(Model model, HttpSession session) {

		logger.info("Method : viewPipeProduction   starts");

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Drop Down mother coil batch
		try {
			DropDownModel[] MCoilBatchList = restClient.getForObject(env.getProduction() + "getMotherCoilBatchEdit",
					DropDownModel[].class);
			List<DropDownModel> MCoilBatchLists = Arrays.asList(MCoilBatchList);

			model.addAttribute("MCoilBatchLists", MCoilBatchLists);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPipeProduction   ends");

		return "production/pipe-polishing-details";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-pipe-polishing-details-ajax")
	public @ResponseBody DataTableResponse viewPipeProductionDetailscThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3, @RequestParam String param4) {

		logger.info("Method : viewPipeProductionDetailscThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			JsonResponse<List<ProductionPipePolishingModel>> jsonResponse = new JsonResponse<List<ProductionPipePolishingModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "getPolishingDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipePolishingModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipePolishingModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(motherCoilSlit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPipeProductionDetailscThroughAjax ends");

		return response;
	}

}
