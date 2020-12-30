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

@Controller
@RequestMapping(value = "production")
public class ProductionMotherCoilSlitController {
	Logger logger = LoggerFactory.getLogger(ProductionMotherCoilSlitController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("/mother-coil-slit")
	public String addMotherCoil(Model model, HttpSession session) {

		logger.info("Method : addMotherCoil starts");

		MotherCoilSlitModel motherCoil = new MotherCoilSlitModel();
		MotherCoilSlitModel sessionMotherCoil = (MotherCoilSlitModel) session.getAttribute("mcoil");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionMotherCoil != null) {
			model.addAttribute("motherCoil", sessionMotherCoil);
			session.setAttribute("motherCoil", null);
		} else {
			model.addAttribute("motherCoil", motherCoil);
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

		// Drop Down mother coil batch
		try {
			DropDownModel[] MCoilBatchList = restClient.getForObject(env.getProduction() + "getMotherCoilBatch",
					DropDownModel[].class);
			List<DropDownModel> MCoilBatchLists = Arrays.asList(MCoilBatchList);

			model.addAttribute("MCoilBatchLists", MCoilBatchLists);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addMotherCoil ends");
		return "production/mother-coil-slit";
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/mother-coil-slit-ajax")
	public @ResponseBody JsonResponse<Object> addProductionPlanning(@RequestBody List<MotherCoilSlitModel> motherCoil,
			HttpSession session) {

		logger.info("Method : addProductionPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (MotherCoilSlitModel r : motherCoil) {
				r.settPipeCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getProduction() + "add-mother-coil-slit", motherCoil,
					JsonResponse.class);

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
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "mother-coil-slit-getthickness" })

	public @ResponseBody JsonResponse<DropDownModel> getThickness(Model model,

			@RequestBody String grade, BindingResult result) {
		logger.info("Method : getThickness starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getThicknessByGradeMotherCoil?id=" + grade,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getThickness ends");
		return res;
	}

	/*
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "mother-coil-slit-getSlitWidth" })

	public @ResponseBody JsonResponse<DropDownModel> getSlitWidth(

			@RequestParam String grade, @RequestParam String thickness) {
		logger.info("Method : getSlitWidth starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(
					env.getProduction() + "getSlitWidthByThickness?id=" + grade + "&thickness=" + thickness,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getSlitWidth ends");
		return res;
	}

	/*
	 * dropDown value for thickness
	 */
	@SuppressWarnings("unchecked")

	@PostMapping(value = { "mother-coil-slit-getPipeSize" })

	public @ResponseBody JsonResponse<DropDownModel> getPipeSize(

			@RequestParam String grade, @RequestParam String thickness, @RequestParam String slitWidth) {
		logger.info("Method : getPipeSize starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getPipeSizeBySlitWidth?id=" + grade + "&thickness="
					+ thickness + "&slitWidth=" + slitWidth, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getPipeSize ends");
		return res;
	}

	// ***********************************23-03-2020********
	/*
	 * Get Mapping view employee dependent
	 */
	@GetMapping("/view-mother-coil-slit")
	public String viewMotherCoil(Model model, HttpSession session) {

		logger.info("Method : viewMotherCoil Slit starts");

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		// Drop Down mother coil batch
		try {
			DropDownModel[] MCoilBatchList = restClient.getForObject(env.getProduction() + "getMotherCoilBatchEdit",
					DropDownModel[].class);
			List<DropDownModel> MCoilBatchLists = Arrays.asList(MCoilBatchList);

			model.addAttribute("MCoilBatchLists", MCoilBatchLists);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewMotherCoil Slit ends");

		return "production/view-mother-coil-slit";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-mother-coil-slit-ThroughAjax")
	public @ResponseBody DataTableResponse viewMotherCoilSlitAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewMotherCoilSlitAjax statrs");

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
			JsonResponse<List<MotherCoilSlitModel>> jsonResponse = new JsonResponse<List<MotherCoilSlitModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "viewMotherCoil", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<MotherCoilSlitModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<MotherCoilSlitModel>>() {
					});

			String s = "";

			for (MotherCoilSlitModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.gettMotherCoilBatch().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.gettMotherCoilThickness().getBytes());
				byte[] encodeId3 = Base64.getEncoder().encode(m.gettPipeSlitBatch().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a> &nbsp; &nbsp; ";
				if (m.getStatus() == 0) {
					s = s + "<a href='view-mother-coil-slit-edit?batchid=" + new String(encodeId) + "&mthick="
							+ new String(encodeId2) + "&slitbatch=" + new String(encodeId3)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>";
				}

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewMotherCoilSlitAjax ends");

		return response;
	}

	/* 25-03-2020 */

	/*
	 * For Modal other employee dependent
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-mother-coil-slit-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalAssignmentDepnd(Model model,
			@RequestBody MotherCoilSlitModel index, BindingResult result) {

		logger.info("Method :modalAssignmentDepnd starts");

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
		logger.info("Method : modalAssignmentDepnd  ends ");
		return response;
	}
/**
 *  for slit width  edit
 * @param model
 * @param mBatchId1
 * @param mThickId1
 * @param slitBatchId1
 * @param session
 * @return
 */
	@GetMapping("view-mother-coil-slit-edit")
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

			MotherCoilSlitModel[] motherCoil = restClient
					.getForObject(env.getProduction() + "edit-MotherCoil-ById?mBatchId=" + mBatchId + "&mThickId="
							+ mThickId + "&slitBatchId=" + slitBatchId, MotherCoilSlitModel[].class);
			List<MotherCoilSlitModel> mCoilList = Arrays.asList(motherCoil);
			mCoilList.get(0).setEditId("Edit");
			model.addAttribute("edit", "for Edit");
			model.addAttribute("mCoilList", mCoilList);
			/*if (!mCoilList.isEmpty()) {
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
			}*/
			if (!mCoilList.isEmpty()) {
				 
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
		return "production/mother-coil-slit";

	}

	/**
	 * DROP DOWN DATA FOR STATE TO DISTRICT NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "mother-coil-slit-onchange-mgrade-list" })
	public @ResponseBody JsonResponse<DropDownModel> gradeOnchangeThicknessList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : gradeOnchangeThicknessList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getProduction() + "rest-mother-coil-grade-onchange-thickness-list?proCat=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : gradeOnchangeThicknessList ends");
		return res;
	}

	/*
	 * value for get details by batch code
	 */
	@SuppressWarnings("unchecked")

	@GetMapping(value = { "mother-coil-slit-getDetails-by-batchCode" })

	public @ResponseBody JsonResponse<MotherCoilSlitModel> getDetailsByBatchCode(@RequestParam String batchCode) {
		logger.info("Method : getDetailsByBatchCode starts");
		JsonResponse<MotherCoilSlitModel> res = new JsonResponse<MotherCoilSlitModel>();
		try {
			res = restClient.getForObject(env.getProduction() + "getDetailsByBatchCode?id=" + batchCode,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getDetailsByBatchCode ends");
		return res;
	}

}