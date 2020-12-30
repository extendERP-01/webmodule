//package nirmalya.aathithya.webmodule.asset.controller;
//
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import nirmalya.aathithya.webmodule.asset.model.AssetFuelConsumptionModel;
//import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
//import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
//import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
//import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
//import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
//import nirmalya.aathithya.webmodule.sales.model.PreviousDataModel;
//
///**
// * @author Nirmalya Labs
// *
// */
//@Controller
//@RequestMapping(value = "asset")
//public class AssetFuelConsumptionController {
//	Logger logger = LoggerFactory.getLogger(AssetFuelConsumptionController.class);
//
//	@Autowired
//	RestTemplate restClient;
//
//	@Autowired
//	EnvironmentVaribles env;
//
//	/**
//	 * Default 'add fuel consumption' page
//	 *
//	 */
//
//	@GetMapping("/add-fuel-consumption")
//	public String addFuelConsumption(Model model, HttpSession session) {
//
//		logger.info("Method : addFuelConsumption starts");
//
//		AssetFuelConsumptionModel assetFuelConsumptionModel = new AssetFuelConsumptionModel();
//		AssetFuelConsumptionModel sessionAssetFuelConsumptionModel = (AssetFuelConsumptionModel) session
//				.getAttribute("sessionAssetFuelConsumptionModel");
//
//		String message = (String) session.getAttribute("message");
//
//		if (message != null && message != "") {
//			model.addAttribute("message", message);
//		}
//
//		session.setAttribute("message", "");
//
//		if (sessionAssetFuelConsumptionModel != null) {
//			model.addAttribute("assetFuelConsumptionModel", sessionAssetFuelConsumptionModel);
//			session.setAttribute("sessionAssetFuelConsumptionModel", null);
//		} else {
//			model.addAttribute("assetFuelConsumptionModel", assetFuelConsumptionModel);
//		}
//		/*
//		 * for viewing drop down fuel list
//		 */
//		try {
//			DropDownModel[] fuel = restClient.getForObject(env.getAssetUrl()+ "getFuel", DropDownModel[].class);
//			List<DropDownModel> FuelList = Arrays.asList(fuel);
//
//			model.addAttribute("FuelList", FuelList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] store = restClient.getForObject(env.getAssetUrl()+ "getStoreForFuelConsmption", DropDownModel[].class);
//			List<DropDownModel> storeList = Arrays.asList(store);
//			
//			model.addAttribute("storeList", storeList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		/*
//		 * for viewing drop down vehicle list
//		 */
//		try {
//			DropDownModel[] rmc = restClient.getForObject(env.getAssetUrl()+ "getVeichelList", DropDownModel[].class);
//			List<DropDownModel> veichleList = Arrays.asList(rmc);
//
//			model.addAttribute("veichleList", veichleList);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addFuelConsumption ends");
//		return "asset/add-fuel-consumption";
//	}
//
//	/*
//	 * delivery challan getPodetails
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-fuel-consumption-driverDetailsVechileOnchange" })
//	public @ResponseBody JsonResponse<DropDownModel> driverDetailsVechileOnchange(Model model,
//			@RequestBody String vechileNo, BindingResult result) {
//		logger.info("Method : driverDetailsVechileOnchange starts");
//
//		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
//
//		try {
//			res = restClient.getForObject(env.getAssetUrl() + "driverDetailsVechileChange?vechileNo=" + vechileNo,
//					JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (res.getMessage() != null) {
//
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//
//		logger.info("Method : driverDetailsVechileOnchange  ends");
//		return res;
//	}
//
//	/*
//	 * delivery challan getPodetails
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-fuel-consumption-getDriverDetails" })
//	public @ResponseBody JsonResponse<DropDownModel> getDriverDetails(Model model, @RequestBody String index,
//			BindingResult result) {
//		logger.info("Method : getDriverDetails starts");
//
//		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
//
//		try {
//			res = restClient.getForObject(env.getAssetUrl() + "getDriverDetailsAutoSearch?id=" + index, JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (res.getMessage() != null) {
//
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//
//		logger.info("Method : getDriverDetails  ends");
//		return res;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-fuel-consumption-get-diesel-rate" })
//	public @ResponseBody JsonResponse<DropDownModel> getDieselRate(Model model, @RequestBody String index,
//			BindingResult result) {
//		logger.info("Method : getDieselRate starts");
//		
//		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
//		
//		try {
//			//commented by subharam
//			//res = restClient.getForObject(env.getAssetUrl() + "getDieselRate", JsonResponse.class);
//			res = restClient.getForObject(env.getAssetUrl() + "getDieselRate?id=" + index, JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (res.getMessage() != null) {
//			
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		
//		logger.info("Method : getDieselRate  ends");
//		return res;
//	}
//
//	/**
//	 * Default 'View AssetFuelConsumptionModel Master' page
//	 *
//	 */
//	@GetMapping("/view-fuel-consumption")
//	public String tableMaster(Model model, HttpSession session) {
//
//		logger.info("Method : tableMaster starts");
//
//		/*
//		 * for viewing drop down vehicle list
//		 */
//		try {
//			DropDownModel[] rmc = restClient.getForObject(env.getAssetUrl()+ "getVeichelList", DropDownModel[].class);
//			List<DropDownModel> veichleList = Arrays.asList(rmc);
//
//			model.addAttribute("veichleList", veichleList);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] store = restClient.getForObject(env.getAssetUrl()+ "getStoreForFuelConsmption", DropDownModel[].class);
//			List<DropDownModel> storeList = Arrays.asList(store);
//			
//			model.addAttribute("storeList", storeList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//	  
//		logger.info("Method : tableMaster ends");
//		return "asset/view-fuel-consumption";
//	}
//
//	/**
//	 * Web controller add new AssetFuelConsumptionModel data
//	 *
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping("/add-fuel-consumption")
//	public String addDeliverChallan(@ModelAttribute AssetFuelConsumptionModel table, Model model, HttpSession session) {
//
//		logger.info("Method : addDeliverChallan starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//
//		try {
//			String userId = (String) session.getAttribute("USER_ID");
//
//			table.setCreatedBy(userId);
//			resp = restClient.postForObject(env.getAssetUrl() + "restAddFuelConsumption", table, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != "" && resp.getMessage() != null) {
//
//			session.setAttribute("message", resp.getMessage());
//			session.setAttribute("sessionAssetFuelConsumptionModel", table);
//			return "redirect:/asset/add-fuel-consumption";
//		}
//		logger.info("Method : addDeliverChallan ends");
//
//		return "redirect:/asset/view-fuel-consumption";
//	}
//
//	/**
//	 * View all data through AJAX
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-fuel-consumption-through-ajax")
//	public @ResponseBody DataTableResponse viewTableThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2) {
//
//		logger.info("Method : viewTableThroughAjax starts");
//
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//
//			JsonResponse<List<AssetFuelConsumptionModel>> jsonResponse = new JsonResponse<List<AssetFuelConsumptionModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getAllFuelConsumption", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<AssetFuelConsumptionModel> AssetFuelConsumptionModelMaster = mapper
//					.convertValue(jsonResponse.getBody(), new TypeReference<List<AssetFuelConsumptionModel>>() {
//					});
//
//			String s = "";
//
//			for (AssetFuelConsumptionModel m : AssetFuelConsumptionModelMaster) {
//				byte[] pId = Base64.getEncoder().encode(m.getConsumptionId().getBytes());
//				
//				if (m.getIsTankFuel()) {
//					m.setTankFullName("Yes");
//				} else {
//					m.setTankFullName("No");
//				}
//
//				s = "";
//				s = s + "<a href='view-fuel-consumption-edit?id=" + new String(pId)
//						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;" + "<a href='javascript:void(0)'"
//						+ "' onclick='DeleteItem(\"" + new String(pId)
//						+ "\")' ><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
//						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
//						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;";
//
//			 
//				m.setAction(s);
//
//				s = "";
//
//			}
//
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(AssetFuelConsumptionModelMaster);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : viewTableThroughAjax ends");
//		return response;
//	}
//
//	/*
//	 * For Modal other employee Language
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/view-fuel-consumption-modal" })
//	public @ResponseBody JsonResponse<AssetFuelConsumptionModel> modalDeliveryChallan(Model model,
//			@RequestBody String index, BindingResult result) {
//
//		logger.info("Method :modalDeliveryChallan starts");
//
//		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());
//
//		String id = (new String(decodeId));
//
//		JsonResponse<AssetFuelConsumptionModel> response = new JsonResponse<AssetFuelConsumptionModel>();
//		try {
//			response = restClient.getForObject(env.getAssetUrl() + "getfuelConsumptionModalById?id=" + id,
//					JsonResponse.class);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (response.getMessage() != null) {
//			response.setCode(response.getMessage());
//			response.setMessage("Unsuccess");
//		} else {
//			response.setMessage("success");
//		}
//		logger.info("Method : modalDeliveryChallan  ends ");
//		return response;
//	}
//
//	/**
//	 * Web controller edit AssetFuelConsumptionModel data
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-fuel-consumption-edit")
//	public String editTable(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
//
//		logger.info("Method : editAssetFuelConsumptionModel starts");
//
//		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
//		String id = (new String(encodeByte));
//
//		AssetFuelConsumptionModel assetFuelConsumptionModel = new AssetFuelConsumptionModel();
//		JsonResponse<AssetFuelConsumptionModel> jsonResponse = new JsonResponse<AssetFuelConsumptionModel>();
//
//		try {
//
//			jsonResponse = restClient.getForObject(env.getAssetUrl() + "getfuelConsumptionById?id=" + id,
//					JsonResponse.class);
//
//		} catch (RestClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		String message = (String) session.getAttribute("message");
//
//		if (message != null && message != "") {
//			model.addAttribute("message", message);
//		}
//
//		session.setAttribute("message", "");
//
//		/*
//		 * for viewing drop down rmc grade list
//		 */
//		try {
//			DropDownModel[] rmc = restClient.getForObject(env.getAssetUrl() + "getVeichelList", DropDownModel[].class);
//			List<DropDownModel> veichleList = Arrays.asList(rmc);
//
//			model.addAttribute("veichleList", veichleList);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] store = restClient.getForObject(env.getAssetUrl()+ "getStoreForFuelConsmption", DropDownModel[].class);
//			List<DropDownModel> storeList = Arrays.asList(store);
//			
//			model.addAttribute("storeList", storeList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		/*
//		 * for viewing drop down fuel list
//		 */
//		try {
//			DropDownModel[] fuel = restClient.getForObject(env.getAssetUrl()+ "getFuel", DropDownModel[].class);
//			List<DropDownModel> FuelList = Arrays.asList(fuel);
//
//			model.addAttribute("FuelList", FuelList);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		ObjectMapper mapper = new ObjectMapper();
//		assetFuelConsumptionModel = mapper.convertValue(jsonResponse.getBody(), AssetFuelConsumptionModel.class);
//		model.addAttribute("assetFuelConsumptionModel", assetFuelConsumptionModel);
//
//		logger.info("Method : editAssetFuelConsumptionModel ends");
//		return "asset/add-fuel-consumption";
//	}
//
//	/**
//	 * Delete AssetFuelConsumptionModel
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-fuel-consumption-delete")
//	public @ResponseBody JsonResponse<Object> deleteTable(Model model, @RequestParam String id, HttpSession session) {
//
//		logger.info("Method : deleteAssetFuelConsumptionModel starts");
//
//		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
//		String index = (new String(encodeByte));
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//
//		String createdBy = "Dj";
//		try {
//			resp = restClient.getForObject(
//					env.getAssetUrl() + "deleteAssetFuelConsumptionModelById?id=" + index + "&createdBy=" + createdBy,
//					JsonResponse.class);
//
//		} catch (RestClientException e) {
//
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			System.out.println("if block getmsg() not false : " + resp.getMessage());
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : deleteAssetFuelConsumptionModel ends");
//
//		return resp;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-fuel-consumption-get-previous-data" })
//	public @ResponseBody JsonResponse<PreviousDataModel> getPreviousKM(Model model, @RequestBody String searchValue,
//			BindingResult result) {
//		logger.info("Method : getPreviousKM starts");
//
//		JsonResponse<PreviousDataModel> res = new JsonResponse<PreviousDataModel>();
//
//		try {
//			res = restClient.getForObject(env.getAssetUrl() + "getPreviousDataFuel?id=" + searchValue, JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (res.getMessage() != null) {
//
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//
//		logger.info("Method : getPreviousKM ends");
//		return res;
//	}
//	
//	/*
//	 * Added for getting slipno datewise by subharam
//	 * */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-fuel-consumption-get-slipno" })
//	public @ResponseBody JsonResponse<DropDownModel> getRefSlipNo(Model model, @RequestBody String index,
//			BindingResult result) {
//		logger.info("Method : getRefSlipNo starts");
//		
//		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
//		
//		try {
//			res = restClient.getForObject(env.getAssetUrl() + "getRefSlipNo?id=" + index,
//					JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if (res.getMessage() != null) {
//			
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		
//		logger.info("Method : getRefSlipNo  ends");
//		return res;
//	}
//	
//}
