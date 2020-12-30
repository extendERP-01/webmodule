package nirmalya.aathithya.webmodule.production.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

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
import nirmalya.aathithya.webmodule.common.utils.ResponseConstands;
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.model.ProductionGradePlanningModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPlanningModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "production")
public class ProductionPlanningController {
	Logger logger = LoggerFactory.getLogger(ProductionPlanningController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'add production planning ' page
	 *
	 */

	@GetMapping("/add-production-planning")
	public String addProductionPlanning(Model model, HttpSession session) {

		logger.info("Method : addProductionPlanning starts");

		ProductionPlanningModel productiionPlanning = new ProductionPlanningModel();
		ProductionPlanningModel sessionProductiionPlanning = (ProductionPlanningModel) session
				.getAttribute("sessionProductiionPlanning");

		String message = (String) session.getAttribute(ResponseConstands.MESSAGE);

		if (message != null && !message.isEmpty()) {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sessionProductiionPlanning != null) {
			model.addAttribute("productionPlanning", sessionProductiionPlanning);
			session.setAttribute("sessionDeliveryChalanModel", null);
		} else {
			model.addAttribute("productionPlanning", productiionPlanning);
		}

		/*
		 * for viewing drop down sales order list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getProduction() + "getPlanDetails",
					DropDownModel[].class);
			List<DropDownModel> saleOrderList = Arrays.asList(cost);

			model.addAttribute("saleOrderList", saleOrderList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing production list
		 */
		try {
			ProductionPlanningModel[] production = restClient.getForObject(env.getProduction() + "getProductionDetails",
					ProductionPlanningModel[].class);
			List<ProductionPlanningModel> productionList = Arrays.asList(production);
			double count = 1;
			for (ProductionPlanningModel a : productionList) {
				a.setCount(count);
				count = count + 1;
			}
			if (!productionList.isEmpty()) {
				model.addAttribute("productionList", productionList);
			} else {
				model.addAttribute("productionList", null);
			}

		} catch (RestClientException e) {
			e.printStackTrace();
		}

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

		logger.info("Method : addProductionPlanning ends");

		return "production/add-production-planning";
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-production-planning-ajax")
	public @ResponseBody JsonResponse<Object> addProductionPlanning(
			@RequestBody List<ProductionPlanningModel> productionPlanningModelList, HttpSession session) {

		logger.info("Method : addProductionPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute(StringConstands.USERID);

			productionPlanningModelList.forEach(l -> l.setCreatedBy(userId));
			resp = restClient.postForObject(env.getProduction() + "restAddPlanning", productionPlanningModelList,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();
		if (message != null && !message.isEmpty()) {
			resp.setMessage(ResponseConstands.UNSUCCESS);
			resp.setCode(message);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : addProductionPlanning function Ends");
		return resp;
	}

	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-production-planning-get-batch" })
	public @ResponseBody JsonResponse<ProductionPlanningModel> getProductionPlannaingBatch(
			@RequestParam String batchQty, @RequestParam String prodQty) {
		logger.info("Method : getProductionPlannaingBatch starts");

		JsonResponse<ProductionPlanningModel> res = new JsonResponse<ProductionPlanningModel>();

		try {
			res = restClient.getForObject(
					env.getProduction() + "getProductionPlannaingBatch?batchQty=" + batchQty + "&prodQty=" + prodQty,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getProductionPlannaingBatch  ends");
		return res;
	}

	/*
	 * get production details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-production-planning-getProduction-details" })
	public @ResponseBody JsonResponse<ProductionPlanningModel> getProductionPlannaingDetails(
			@RequestParam String fromDate, @RequestParam String plantId, HttpSession session) {
		logger.info("Method : getProductionPlannaingDetails starts");

		JsonResponse<ProductionPlanningModel> res = new JsonResponse<ProductionPlanningModel>();

		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			res = restClient.getForObject(env.getProduction() + "getProductionPlannaingDetails?fromDate=" + fromDate
					+ "&userId=" + userId + "&storeId=" + plantId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getProductionPlannaingDetails  ends");
		return res;
	}

	/*
	 * Get Mapping view employee education
	 */
	@GetMapping("/view-production-planning")
	public String viewProductionPlanning(Model model, HttpSession session) {

		logger.info("Method : viewProductionPlanning starts");

		logger.info("Method : viewProductionPlanning ends");

		return "production/view-production-planning";
	}

	/*
	 * For view employee education for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-planning-throughajax")
	public @ResponseBody DataTableResponse viewProductionPlanning(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewProductionPlanning statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam1(param2);
			JsonResponse<List<ProductionPlanningModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getProduction() + "getPlanningDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPlanningModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPlanningModel>>() {
					});

			for (ProductionPlanningModel m : assignEdu) {

				if (Boolean.TRUE.equals(m.getTotalCompleteStatus())) {
					m.setProductionStatusName("Cont.");
				} else {
					m.setProductionStatusName("Completed");
				}
				if (m.getApproveStatus() == 1) {
					m.setApproveStatusName("Approved");
				} else if (m.getApproveStatus() == 2) {
					m.setApproveStatusName("Rejected");
				} else if (m.getApproveStatus() == 3) {
					m.setApproveStatusName("Returned");
				} else if (m.getApproveStatus() == 4) {
					m.setApproveStatusName("Resubmited");
				} else {
					m.setApproveStatusName("Open");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewProductionPlanning ends");

		return response;
	}

	/*
	 * for Edit assign Edu
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-planning-edit")
	public String editPlanningPlan(Model model, @RequestParam("pId") String encodeId,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, HttpSession session) {

		logger.info("Method :editPlanningPlan starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<ProductionPlanningModel>> response = new JsonResponse<List<ProductionPlanningModel>>();
		try {

			response = restClient.getForObject(
					env.getProduction() + "getPlanningById?pId=" + id + "&fromDate=" + fromDate + "&toDate=" + toDate,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPlanningModel> planningList = mapper.convertValue(response.getBody(),
					new TypeReference<List<ProductionPlanningModel>>() {
					});
			model.addAttribute("planningList", planningList);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : editPlanningPlan ends");

		return "production/add-production-planning";
	}

	/*
	 * for Edit assign Edu
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-planning-details-by-grade")
	public String getPlanningPlanByGrade(Model model, @RequestParam("pId") String encodeId,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {

		logger.info("Method :getPlanningPlanByGrade starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<ProductionPlanningModel>> response = new JsonResponse<List<ProductionPlanningModel>>();
		try {

			response = restClient.getForObject(
					env.getProduction() + "getPlanningById?pId=" + id + "&fromDate=" + fromDate + "&toDate=" + toDate,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPlanningModel> planningList = mapper.convertValue(response.getBody(),
					new TypeReference<List<ProductionPlanningModel>>() {
					});
			model.addAttribute("planningList", planningList);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : getPlanningPlanByGrade ends");

		return "production/view-planning-details-grade-view-page";
	}

	/*
	 * for get details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-planning-grade")
	public String getPlanningPlanByGradeDetails(Model model, @RequestParam String grade, @RequestParam String pId,
			@RequestParam String fromDate, @RequestParam String toDate) {

		logger.info("Method :getPlanningPlanByGradeDetails starts");

		JsonResponse<List<ProductionGradePlanningModel>> response = new JsonResponse<List<ProductionGradePlanningModel>>();
		try {

			response = restClient.getForObject(env.getProduction() + "getPlanningByIdAndGrade?pId=" + pId + "&grade="
					+ grade + "&fromDate=" + fromDate + "&toDate=" + toDate, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionGradePlanningModel> planningList = mapper.convertValue(response.getBody(),
					new TypeReference<List<ProductionGradePlanningModel>>() {
					});

			Stream<ProductionGradePlanningModel> notNullObjs = planningList.stream()
					.filter(obj -> obj.getSales() != null);

			Double salesSum = notNullObjs.mapToDouble(ProductionGradePlanningModel::getSales).sum();

			double totalSum = 0;
			for (ProductionGradePlanningModel a : planningList) {
				if (a.getFp() != null && a.getSlit() != null && a.getMcoil() != null && a.getWip() != null) {
					totalSum = totalSum + a.getFp() + a.getSlit() + a.getMcoil() + a.getWip();
				}

			}

			for (ProductionGradePlanningModel a : planningList) {
				double total = a.getFp() + a.getSlit() + a.getMcoil() + a.getWip();
				a.setTotal(total);
				a.setSockPercentage((total / totalSum) * 100);
				a.setRatio((a.getSales() / salesSum) * 100);
			}
			ProductionGradePlanningModel pgm = new ProductionGradePlanningModel();

			/*
			 * Supplier<Stream<ProductionGradePlanningModel>> notNullObjsSupplier =
			 * (Supplier<Stream<ProductionGradePlanningModel>>)
			 * planningList.stream().filter(obj -> obj != null);
			 */

			Stream<ProductionGradePlanningModel> notNullObjsRatio = planningList.stream()
					.filter(obj -> obj.getRatio() != null);
			pgm.setRatio(notNullObjsRatio.mapToDouble(ProductionGradePlanningModel::getRatio).sum());

			Stream<ProductionGradePlanningModel> notNullObjsFp = planningList.stream()
					.filter(obj -> obj.getFp() != null);
			pgm.setFp(notNullObjsFp.mapToDouble(ProductionGradePlanningModel::getFp).sum());

			Stream<ProductionGradePlanningModel> notNullObjswip = planningList.stream()
					.filter(obj -> obj.getWip() != null);
			pgm.setWip(notNullObjswip.mapToDouble(ProductionGradePlanningModel::getWip).sum());

			Stream<ProductionGradePlanningModel> notNullObjsSlit = planningList.stream()
					.filter(obj -> obj.getSlit() != null);
			pgm.setSlit(notNullObjsSlit.mapToDouble(ProductionGradePlanningModel::getSlit).sum());

			Stream<ProductionGradePlanningModel> notNullObjsMother = planningList.stream()
					.filter(obj -> obj.getMcoil() != null);
			pgm.setMcoil(notNullObjsMother.mapToDouble(ProductionGradePlanningModel::getMcoil).sum());

			Stream<ProductionGradePlanningModel> notNullObjsTotal = planningList.stream()
					.filter(obj -> obj.getTotal() != null);
			pgm.setTotal(notNullObjsTotal.mapToDouble(ProductionGradePlanningModel::getTotal).sum());

			Stream<ProductionGradePlanningModel> notNullObjsStock = planningList.stream()
					.filter(obj -> obj.getSockPercentage() != null);
			pgm.setSockPercentage(notNullObjsStock.mapToDouble(ProductionGradePlanningModel::getSockPercentage).sum());
			pgm.setSize("Total");
			pgm.setSales(salesSum);
			planningList.add(pgm);
			model.addAttribute("planningList", planningList);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : getPlanningPlanByGradeDetails ends");

		return "production/view-planning-by-grade";
	}

	/*
	 * Get Mapping production planning approval process
	 */
	@GetMapping("/view-production-planning-approve-stage")
	public String viewplanningApprove(Model model, HttpSession session) {

		logger.info("Method : viewplanningApprove starts");

		logger.info("Method : viewplanningApprove ends");

		return "production/view-production-planning-approve";
	}

	/*
	 * For view planning approval process dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-production-planning-approve-stage-throughajax")
	public @ResponseBody DataTableResponse viewplanningApprove(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {
		logger.info("Method : viewplanningApprove statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {

			String userId = (String) session.getAttribute(StringConstands.USERID);

			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setUserId(userId);

			JsonResponse<List<ProductionPlanningModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getProduction() + "getProductionPlanningApprovalData",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPlanningModel> travelingplanning = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPlanningModel>>() {
					});

			String s = "";

			for (ProductionPlanningModel m : travelingplanning) {
				byte[] pId = Base64.getEncoder().encode(m.getPpId().getBytes());

				s = "";
				if ((m.getCurrentStageNo() == m.getApproverStageNo().byteValue()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardplanning(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectplanning(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectplanning(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectplanning(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				} else if ((m.getCurrentStageNo() != m.getApproverStageNo().byteValue())
						&& (m.getApproveStatus() != 1)) {
					s = s + "Proceed to Next Step";
				} else if (m.getApproveStatus() == 1 && Boolean.FALSE.equals(m.getProductionStatus())
						&& (m.getApproveStatus() == 1)) {
					s = s + "Approved";
				}
				m.setAction(s);
				if (m.getTotalCompleteStatus()) {
					m.setProductionStatusName("Closed");
				} else {
					m.setProductionStatusName("Open");
				}

				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(travelingplanning);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewplanningApprove Theme ends");
		return response;
	}

	/*
	 * Forward planning to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-planning-approval-action" })
	public @ResponseBody JsonResponse<Object> saveplanningApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : saveplanningApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute(StringConstands.USERID);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getProduction() + "save-planning-approval-action?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && !resp.getMessage().isEmpty()) {
			resp.setCode(resp.getMessage());
			resp.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : saveplanningApprovalAction ends");
		return resp;
	}
	/*
	 * Reject planning
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-planning-reject-action" })
	public @ResponseBody JsonResponse<Object> saveplanningRejectAction(Model model,
			@RequestBody ProductionPlanningModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : saveplanningRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getPpId());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute(StringConstands.USERID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		reqobject.setCreatedBy(userId);
		reqobject.setPpId(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(
					env.getProduction() + "save-planning-reject-action?id=" + reqstnId + "&createdBy=" + userId
							+ "&desc=" + reqobject.getReturnDesc() + "&rejectType=" + reqobject.getReturnType(),
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && !res.getMessage().isEmpty()) {
			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
			res.setCode(res.getMessage());
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : saveplanningRejectAction ends");
		return res;
	}

	/*
	 * get raw item details details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-production-planning-get-raw-details" })
	public @ResponseBody JsonResponse<ProductionPlanningModel> getRawMaterialDetails(@RequestBody String index) {
		logger.info("Method : getRawMaterialDetails starts");

		JsonResponse<ProductionPlanningModel> res = new JsonResponse<ProductionPlanningModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "getRawMaterialDetails?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			res.setMessage(ResponseConstands.SUCCESS);
		}

		logger.info("Method : getRawMaterialDetails  ends");
		return res;
	}

	/**
	 * Web controller add raw material data
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-production-planning-add-raw-details")
	public @ResponseBody JsonResponse<Object> addRawMaterialDetails(@RequestParam String batchCode,
			@RequestParam String batchQuantity, @RequestParam String storeId, @RequestParam String fromDate,
			HttpSession session) {

		logger.info("Method : addRawMaterialDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			resp = restClient
					.getForObject(
							env.getProduction() + "addRawMaterialDetails?batchCode=" + batchCode + "&batchQuantity="
									+ batchQuantity + "&storeId=" + storeId + "&fromDate=" + fromDate,
							JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && !message.isEmpty()) {
			resp.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : addRawMaterialDetails function Ends");
		return resp;
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-production-planning-save-raw-data")
	public @ResponseBody JsonResponse<Object> saveRawData(
			@RequestBody List<ProductionPlanningModel> productionPlanningModelList) {

		logger.info("Method : saveRawData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.postForObject(env.getProduction() + "saveRawData", productionPlanningModelList,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && !message.isEmpty()) {
			resp.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : saveRawData function Ends");
		return resp;
	}

	/*
	 * For Modal pipe polishing
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-production-planning-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalAssignmentDepnd(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalAssignmentDepnd starts");

		byte[] slitbatchId = Base64.getDecoder().decode(index.getBytes());

		String slitBatchId = (new String(slitbatchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getProduction() + "viewMotherCoilById?slitBatchId=" + slitBatchId,
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

}
