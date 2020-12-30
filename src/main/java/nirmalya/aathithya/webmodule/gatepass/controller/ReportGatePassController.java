package nirmalya.aathithya.webmodule.gatepass.controller;

import java.util.Arrays;
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
import nirmalya.aathithya.webmodule.gatepass.model.GatePassEntryModel;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassOutModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "gatepass")
public class ReportGatePassController {

	Logger logger = LoggerFactory.getLogger(ReportGatePassController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Maintenance Report' page
	 *
	 */
	@GetMapping("/gatepass-entry-report")
	public String generateMaintenanceReport(Model model, HttpSession session) {
		logger.info("Method : generateMaintenanceReport starts");
		
		logger.info("Method : generateMaintenanceReport ends");
		return "gatepass/generate-gatepass-entry-report";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-entry-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewSaleReport(HttpSession session,HttpServletRequest request, Model model,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<GatePassEntryModel>> jsonResponse = new JsonResponse<List<GatePassEntryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getEntryReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassEntryModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassEntryModel>>() {
				});
		
		for(GatePassEntryModel m : gatePass) {
			if(m.getWeightType()==1) {
				m.setAction("Container Wise");
			} else if(m.getWeightType()==2) {
				m.setAction("Unit Wise");
			} else {
				m.setAction("N/A");
			}
			
			if(m.getpOrder()==null || m.getpOrder()=="") {
				m.setpOrder("N/A");
			}
			
			if(m.getCustomer()==null || m.getCustomer()=="") {
				m.setCustomer("N/A");
			}
			
			if(m.getVendor()==null || m.getVendor()=="") {
				m.setVendor("N/A");
			}
		}
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(gatePass);
		
		return response;
	}
	
	/**
	 * View Default 'Maintenance Report' page
	 *
	 */
	@GetMapping("/gatepass-out-summary-report")
	public String generateGetPassOutSummaryReport(Model model, HttpSession session) {
		logger.info("Method : generateGetPassOutSummaryReport starts");
		try {
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl()+"getStoreForGatePass", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { DropDownModel[] driverId =
		 * restClient.getForObject(env.getGatepassUrl()+"getDriverIdForGatePass",
		 * DropDownModel[].class); List<DropDownModel> driverList =
		 * Arrays.asList(driverId); model.addAttribute("driverList", driverList); }
		 * catch(Exception e) { e.printStackTrace(); }
		 */
		logger.info("Method : generateGetPassOutSummaryReport ends");
		return "gatepass/generate-gatepass-out-summary-report";
	}
	/**
	 * Web Controller - Get driver List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-summary-report-autocomplete-driver" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassOutAutoCompleteDriverReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassOutAutoCompleteDriverReport starts");
		System.out.println(searchValue);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassOutAutoCompleteDriverReport?id=" + searchValue,
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

		logger.info("Method : getGetPassOutAutoCompleteDriverReport ends");
		return res;
	}
	/**
	 * Web Controller - Get Item List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-summary-report-autocomplete-Item" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassOutAutoCompleteItemReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassOutAutoCompleteItemReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassOutAutoCompleteItemReport?id=" + searchValue,
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

		logger.info("Method : getGetPassOutAutoCompleteItemReport ends");
		return res;
	}
	/**
	 * Web Controller - Get Item List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-summary-report-autocomplete-vehicle-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassOutAutoCompleteVehicleNoReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassOutAutoCompleteVehicleNoReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassOutAutoCompleteVehicleNoReport?id=" + searchValue,
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

		logger.info("Method : getGetPassOutAutoCompleteVehicleNoReport ends");
		return res;
	}
	/**
	 * Web Controller - Get gate pass no List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-summary-report-autocomplete-gatepass-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassOutNOAutoCompleteReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassOutNOAutoCompleteReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassOutNOAutoCompleteReport?id=" + searchValue,
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

		logger.info("Method : getGetPassOutNOAutoCompleteReport ends");
		return res;
	}
	/**
	 * Web Controller - Get gate pass no List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-summary-report-autocomplete-rst-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassOutAutoCompleteRSTNoReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassAutoCompleteRSTNoReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		System.out.println(searchValue);
		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassOutAutoCompleteRSTNoReport?id=" + searchValue,
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

		logger.info("Method : getGetPassAutoCompleteRSTNoReport ends");
		return res;
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-out-summary-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewGatePassOutSummaryReport(HttpSession session,HttpServletRequest request, Model model,@RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param4") String param4,
			@RequestParam("param5") String param5, @RequestParam("param6") String param6,@RequestParam("param7") String param7,@RequestParam("param8") String param8) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<GatePassOutModel>> jsonResponse = new JsonResponse<List<GatePassOutModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);
		tableRequest.setParam7(param7);
		tableRequest.setParam8(param8);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getPassOutSummaryReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassOutModel> gatePassOut = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassOutModel>>() {
				});
		

		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(gatePassOut);
		
		return response;
	}
	
	/**
	 * Web Controller - Get driver List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-summary-report-autocomplete-driver" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassAutoCompleteDriverReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassAutoCompleteDriverReport starts");
		System.out.println(searchValue);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassAutoCompleteDriverReport?id=" + searchValue,
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

		logger.info("Method : getGetPassAutoCompleteDriverReport ends");
		return res;
	}
	/**
	 * Web Controller - Get Item List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-summary-report-autocomplete-Item" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassAutoCompleteItemReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassAutoCompleteItemReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassAutoCompleteItemReport?id=" + searchValue,
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

		logger.info("Method : getGetPassAutoCompleteItemReport ends");
		return res;
	}
	/**
	 * Web Controller - Get Item List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-summary-report-autocomplete-vehicle-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassAutoCompleteVehicleNoReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassAutoCompleteVehicleNoReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassAutoCompleteVehicleNoReport?id=" + searchValue,
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

		logger.info("Method : getGetPassAutoCompleteVehicleNoReport ends");
		return res;
	}
	/**
	 * Web Controller - Get gate pass no List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-summary-report-autocomplete-gatepass-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassNOAutoCompleteReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassNOAutoCompleteReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassNOAutoCompleteReport?id=" + searchValue,
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

		logger.info("Method : getGetPassNOAutoCompleteReport ends");
		return res;
	}
	/**
	 * Web Controller - Get gate pass no List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-summary-report-autocomplete-rst-no" })
	public @ResponseBody JsonResponse<DropDownModel> getGetPassAutoCompleteRSTNoReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGetPassAutoCompleteRSTNoReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		System.out.println(searchValue);
		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGetPassAutoCompleteRSTNoReport?id=" + searchValue,
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

		logger.info("Method : getGetPassAutoCompleteRSTNoReport ends");
		return res;
	}
	
	/**
	 * View Default 'Maintenance Report' page
	 *
	 */
	@GetMapping("/gatepass-entry-summary-report")
	public String generateGetPassSummaryReport(Model model, HttpSession session) {
		logger.info("Method : generateGetPassSummaryReport starts");
		try {
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl()+"getStoreForGatePass", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { DropDownModel[] driverId =
		 * restClient.getForObject(env.getGatepassUrl()+"getDriverIdForGatePass",
		 * DropDownModel[].class); List<DropDownModel> driverList =
		 * Arrays.asList(driverId); model.addAttribute("driverList", driverList); }
		 * catch(Exception e) { e.printStackTrace(); }
		 */
		logger.info("Method : generateGetPassSummaryReport ends");
		return "gatepass/generate-gatepass-entry-summary-report";
		
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-entry-summary-report-preview-through-ajax" })
	public @ResponseBody DataTableResponse previewGatePassEntrySummaryReport(HttpSession session,HttpServletRequest request, Model model,@RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, @RequestParam("param4") String param4,
			@RequestParam("param5") String param5, @RequestParam("param6") String param6,@RequestParam("param7") String param7,@RequestParam("param8") String param8) {


		String UserId = (String) session.getAttribute("USER_ID");
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");

		JsonResponse<List<GatePassEntryModel>> jsonResponse = new JsonResponse<List<GatePassEntryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);
		tableRequest.setParam7(param7);
		tableRequest.setParam8(param8);
		tableRequest.setUserId(UserId);
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getPassEntrySummaryReportForPreview", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassEntryModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassEntryModel>>() {
				});
		

		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(gatePass);
		
		return response;
	}

}
