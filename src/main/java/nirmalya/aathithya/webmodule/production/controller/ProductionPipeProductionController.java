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
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeProductionModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPipeProductionController {
	Logger logger = LoggerFactory.getLogger(ProductionMotherCoilSlitController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view pipe production page
	 */
	@GetMapping("/view-production")
	public String viewPipeProduction(Model model, HttpSession session) {

		logger.info("Method : viewPipeProduction   starts");

		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
				
		logger.info("Method : viewPipeProduction   ends");

		return "production/view-pipe-production";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-throughajax")
	public @ResponseBody DataTableResponse viewProductionThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewProductionThroughAjax statrs");

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

			JsonResponse<List<ProductionPipeProductionModel>> jsonResponse ;

			jsonResponse = restClient.postForObject(env.getProduction() + "viewMixProduction", tableRequest,
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
 
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  ";
				s = s + "<a href='view-pipe-production-edit?batchid=" + new String(encodeId) + "&mthick="
						+ new String(encodeId2) + "&slitbatch=" + new String(encodeId3)
						+ "' ><button class=\"btn btn-info\">Add Dtls" + "</button></a>";

				m.setAction(s); 

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(motherCoilSlit);

		} catch (Exception e) { 
			e.printStackTrace();
		}

		logger.info("Method : viewProductionThroughAjax ends");

		return response;
	}

	/*
	 * Get Mapping view pipe production page
	 */
	@GetMapping("/view-pipe-production-details")
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

		return "production/pipe-production-details";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-pipe-production-details-ajax")
	public @ResponseBody DataTableResponse viewPipeProductionDetailscThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

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

			JsonResponse<List<ProductionPipeProductionModel>> jsonResponse = new JsonResponse<List<ProductionPipeProductionModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "getProductionDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipeProductionModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipeProductionModel>>() {
					});

			String s = "";

			for (ProductionPipeProductionModel m : motherCoilSlit) {
				s = "";

				/*byte[] encodeId = Base64.getEncoder().encode(m.gettMotherCoilBatch().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.gettMotherCoilThickness().getBytes());
				byte[] encodeId3 = Base64.getEncoder().encode(m.gettPipeSlitBatch().getBytes()); */

				s = "";
			/*	s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  "; */
			 

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

		logger.info("Method : viewPipeProductionDetailscThroughAjax ends");

		return response;
	}
 
	/*
	 * For Modal pipe production
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-production-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalPipeProduction(Model model,
			@RequestBody ProductionPipeProductionModel index, BindingResult result) {

		logger.info("Method :modalPipeProduction starts");

		byte[] batchId = Base64.getDecoder().decode(index.gettMotherCoilBatch().getBytes());
		byte[] thickId = Base64.getDecoder().decode(index.gettMotherCoilThickness().getBytes());
		byte[] slitbatchId = Base64.getDecoder().decode(index.gettPipeSlitBatch().getBytes());

		String mBatchId = (new String(batchId));
		String mThickId = (new String(thickId));
		String slitBatchId = (new String(slitbatchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(
					env.getProduction() + "viewMotherCoilById?mBatchId=" + mBatchId + "&mThickId=" + mThickId
							+ "&slitBatchId=" + slitBatchId, // +"pipeSzId="+pipeSzId,
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
		logger.info("Method : modalPipeProduction  ends ");
		return response;
	}

	/*
	 * pipe production edit
	 */
	@GetMapping("view-pipe-production-edit")
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

			ProductionPipeProductionModel[] motherCoil = restClient
					.getForObject(env.getProduction() + "edit-MotherCoil-ById?mBatchId=" + mBatchId + "&mThickId="
							+ mThickId + "&slitBatchId=" + slitBatchId, ProductionPipeProductionModel[].class);
			List<ProductionPipeProductionModel> mCoilList = Arrays.asList(motherCoil);

			mCoilList.get(0).setEditId("Edit");
			model.addAttribute("edit", "for Edit");
			model.addAttribute("mCoilList", mCoilList); 
			/*
			 * if (!mCoilList.isEmpty()) { // Drop Down Grade try { DropDownModel[]
			 * MCoilGrade = restClient.getForObject( env.getProduction() +
			 * "getthicknessEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade(),
			 * DropDownModel[].class); List<DropDownModel> thicknessList =
			 * Arrays.asList(MCoilGrade);
			 * 
			 * model.addAttribute("thicknessList", thicknessList); } catch
			 * (RestClientException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */
			/*
			 * if (!mCoilList.isEmpty()) { // Drop Down Grade try { DropDownModel[]
			 * MCoilGrade = restClient.getForObject( env.getProduction() +
			 * "getSlitWidthEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade() +
			 * "&thickness=" + mCoilList.get(0).gettMotherCoilThickness(),
			 * DropDownModel[].class); List<DropDownModel> slitWidthList =
			 * Arrays.asList(MCoilGrade);
			 * 
			 * model.addAttribute("slitWidthList", slitWidthList); } catch
			 * (RestClientException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */
			/*
			 * if (!mCoilList.isEmpty()) { try { DropDownModel[] MCoilGrade =
			 * restClient.getForObject( env.getProduction() + "getPipeSizeit?grade=" +
			 * mCoilList.get(0).gettMotherCoilGrade() + "&thickness=" +
			 * mCoilList.get(0).gettMotherCoilThickness(), DropDownModel[].class);
			 * List<DropDownModel> pipeSizeList = Arrays.asList(MCoilGrade);
			 * model.addAttribute("pipeSizeList", pipeSizeList); } catch
			 * (RestClientException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
 
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

		logger.info("Method : editMotherCoilSlit ends");
		return "production/add-pipe-production";

	}

	/**
	 * Web controller add production data through ajax
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-production-add-ajax")
	public @ResponseBody JsonResponse<Object> addProductionPlanning(
			@RequestBody List<ProductionPipeProductionModel> productionPipeProductionModelList, HttpSession session) {

		logger.info("Method : addProductionPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (ProductionPipeProductionModel r : productionPipeProductionModelList) {
				r.settPipeCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getProduction() + "add-production-dtls",
					productionPipeProductionModelList, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : addProductionPlanning function Ends");
		return resp;
	}

	/*
	 * For Modal production details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-production-details-modalView")
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
					env.getProduction() + "getProductionDetailsModal?mBatchId=" + mBatchId + "&mThickId=" + mThickId
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
}
