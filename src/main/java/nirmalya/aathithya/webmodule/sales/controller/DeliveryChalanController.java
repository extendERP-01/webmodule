package nirmalya.aathithya.webmodule.sales.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import nirmalya.aathithya.webmodule.sales.model.DeliveryChalanModel;
import nirmalya.aathithya.webmodule.sales.model.DeliveryChallanInvoiceModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderPoDetailsModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class DeliveryChalanController {
	Logger logger = LoggerFactory.getLogger(DeliveryChalanController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Default 'Delivery challan' page
	 *
	 */

	@GetMapping("/delivery-challan-add")
	public String addDeliverChallan(Model model, HttpSession session) {

		logger.info("Method : addDeliverChallan starts");

		DeliveryChalanModel deliveryChalanModel = new DeliveryChalanModel();
		DeliveryChalanModel sessionDeliveryChalanModel = (DeliveryChalanModel) session
				.getAttribute("sessionDeliveryChalanModel");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sessionDeliveryChalanModel != null) {
			model.addAttribute("deliveryChalanModel", sessionDeliveryChalanModel);
			session.setAttribute("sessionDeliveryChalanModel", null);
		} else {
			model.addAttribute("deliveryChalanModel", deliveryChalanModel);
		}

		/*
		 * for viewing drop down sales order list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getSaleOrder", DropDownModel[].class);
			List<DropDownModel> saleOrderList = Arrays.asList(cost);

			model.addAttribute("saleOrderList", saleOrderList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for viewing drop down plant list
		 */
		try {
			DropDownModel[] plant = restClient.getForObject(env.getSalesUrl() + "getPlantList", DropDownModel[].class);
			List<DropDownModel> batchPlantList = Arrays.asList(plant);

			model.addAttribute("batchPlantList", batchPlantList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down cement brand list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getCementList", DropDownModel[].class);
			List<DropDownModel> cementList = Arrays.asList(cost);

			model.addAttribute("cementList", cementList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down pump list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getpumpList", DropDownModel[].class);
			List<DropDownModel> pumpList = Arrays.asList(cost);

			model.addAttribute("pumpList", pumpList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down rmc grade list
		 */
		try {
			DropDownModel[] rmc = restClient.getForObject(env.getSalesUrl() + "getpRmcGradeList",
					DropDownModel[].class);
			List<DropDownModel> rmcGradeList = Arrays.asList(rmc);

			model.addAttribute("rmcGradeList", rmcGradeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down rmc grade list
		 */
		try {
			DropDownModel[] rmc = restClient.getForObject(env.getSalesUrl() + "getVeichelList", DropDownModel[].class);
			List<DropDownModel> veichleList = Arrays.asList(rmc);

			model.addAttribute("veichleList", veichleList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addDeliverChallan ends");

		return "sales/delivery-challan";
	}

	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/delivery-challan-add-getPodetails" })
	public @ResponseBody JsonResponse<SaleOrderPoDetailsModel> getPODetails(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getPODetails starts");

		JsonResponse<SaleOrderPoDetailsModel> res = new JsonResponse<SaleOrderPoDetailsModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getPoDetails?soId=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getPODetails  ends");
		return res;
	}
	
	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/delivery-challan-add-vechileChange-driverDetails" })
	public @ResponseBody JsonResponse<DropDownModel> driverDetailsVechileOnchange(Model model, @RequestBody String vechileNo,
			BindingResult result) {
		logger.info("Method : driverDetailsVechileOnchange starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "driverDetailsVechileOnchange?vechileNo=" + vechileNo, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : driverDetailsVechileOnchange  ends");
		return res;
	}

	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/delivery-challan-add-getDriverDetails" })
	public @ResponseBody JsonResponse<SaleOrderPoDetailsModel> getDriverDetails(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getDriverDetails starts");

		JsonResponse<SaleOrderPoDetailsModel> res = new JsonResponse<SaleOrderPoDetailsModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getDriverDetails?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getDriverDetails  ends");
		return res;
	}

	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/delivery-challan-add-tripDetails" })
	public @ResponseBody JsonResponse<SaleOrderPoDetailsModel> getTripDetails(@RequestParam String rmcGrade,
			@RequestParam String saleOrder) {
		logger.info("Method : getTripDetails starts");

		JsonResponse<SaleOrderPoDetailsModel> res = new JsonResponse<SaleOrderPoDetailsModel>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getTripDetails?rmcGrade=" + rmcGrade + "&saleOrder=" + saleOrder,
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

		logger.info("Method : getTripDetails  ends");
		return res;
	}
	
	
	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/delivery-challan-add-cementDetails" })
	public @ResponseBody JsonResponse<SaleOrderPoDetailsModel> getCementDetails(@RequestParam String customer,
			@RequestParam String cementBrand) {
		logger.info("Method : getCementDetails starts");

		JsonResponse<SaleOrderPoDetailsModel> res = new JsonResponse<SaleOrderPoDetailsModel>();
 
		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getCementDetails?customer=" + customer + "&cementBrand=" + cementBrand,
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

		logger.info("Method : getCementDetails  ends");
		return res;
	}
	

	/**
	 * Default 'View deliveryChalanModel Master' page
	 *
	 */
	@GetMapping("/list-deliveryChalan")
	public String tableMaster(Model model, HttpSession session) {

		logger.info("Method : tableMaster starts");

		JsonResponse<Object> table = new JsonResponse<Object>();
		model.addAttribute("table", table);

		/*
		 * for viewing drop down sales order list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getSaleOrder", DropDownModel[].class);
			List<DropDownModel> saleOrderList = Arrays.asList(cost);

			model.addAttribute("saleOrderList", saleOrderList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : tableMaster ends");
		return "sales/list_delivery_challan";
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delivery-challan-add")
	public String addDeliverChallan(@ModelAttribute DeliveryChalanModel table, Model model, HttpSession session) {

		logger.info("Method : addDeliverChallan starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			String userId = (String) session.getAttribute("USER_ID");
			table.setSand2Id("itm00029");
			table.setChip310mmId("itm00030");
			table.setChip420mmId("itm00031");
			table.setAddMixId("itm00034");
			table.setFlyAshId("itm00032");
			table.setWaterId("itm00033");
			table.setSlurryPowderId("itm00045");

			table.setCreatedBy(userId);
			resp = restClient.postForObject(env.getSalesUrl() + "restAddDeliveryChallan", table, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionDeliveryChalanModel", table);
			return "redirect:/sales/delivery-challan-add";
		}
		logger.info("Method : addDeliverChallan ends");

		return "redirect:/sales/list-deliveryChalan";
	}

	/**
	 * View all data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/list-deliveryChalan-through-ajax")
	public @ResponseBody DataTableResponse viewTableThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewTableThroughAjax starts");

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

			JsonResponse<List<DeliveryChalanModel>> jsonResponse = new JsonResponse<List<DeliveryChalanModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getAllDeliveryChallan", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<DeliveryChalanModel> deliveryChalanModelMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DeliveryChalanModel>>() {
					});

			String s = "";

			for (DeliveryChalanModel m : deliveryChalanModelMaster) {
				byte[] pId = Base64.getEncoder().encode(m.getChallanNo().getBytes());

				s = "";
				s = s + "<a href='list-deliveryChalan-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;" + "<a href='javascript:void(0)'"
						+ "' onclick='DeleteItem(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;"

						+ "<a href='javascript:void(0)'" + "' onclick='createPdf(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-download\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; ";
				
				
				m.setInvoiceNumber("<a target='_blank' href=/download/delivery-challan-invoice-pdf?id=" + new
						   String(pId) + ">" + m.getInvoiceNumber() + "</a>");
				
				/*
				 * if (!m.getInvoiceStatus()) { s = s +
				 * "<a href=/sales/list-deliveryChalan-generate-invoice?id=" + new String(pId) +
				 * " > <button type=\"button\" class=\"btn btn-primary\" >Invoice</button></a>&nbsp;&nbsp; "
				 * ; m.setInvoiceNumber("N/A"); } else {
				 * m.setInvoiceNumber("<a href=/download/delivery-challan-invoice-pdf?id=" + new
				 * String(pId) + "> " + m.getInvoiceNumber() + "</a>&nbsp;&nbsp; "); }
				 */
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(deliveryChalanModelMaster);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewTableThroughAjax ends");
		return response;
	}

	/*
	 * For Modal delivery challan
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/list-deliveryChalan-modal" })
	public @ResponseBody JsonResponse<DeliveryChalanModel> modalDeliveryChallan(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalDeliveryChallan starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<DeliveryChalanModel> response = new JsonResponse<DeliveryChalanModel>();
		try {
			response = restClient.getForObject(env.getSalesUrl() + "getdeliveryChalanModalById?id=" + id,
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
		logger.info("Method : modalDeliveryChallan  ends ");
		return response;
	}

	/**
	 * Web controller edit deliveryChalanModel data
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/list-deliveryChalan-edit")
	public String editTable(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editdeliveryChalanModel starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		DeliveryChalanModel deliveryChalanModel = new DeliveryChalanModel();
		JsonResponse<DeliveryChalanModel> jsonResponse = new JsonResponse<DeliveryChalanModel>();

		try {

			jsonResponse = restClient.getForObject(env.getSalesUrl() + "getdeliveryChalanById?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		/*
		 * for viewing drop down sales order
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getSaleOrder", DropDownModel[].class);
			List<DropDownModel> saleOrderList = Arrays.asList(cost);

			model.addAttribute("saleOrderList", saleOrderList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for viewing drop down plant list
		 */
		try {
			DropDownModel[] plant = restClient.getForObject(env.getSalesUrl() + "getPlantList", DropDownModel[].class);
			List<DropDownModel> batchPlantList = Arrays.asList(plant);

			model.addAttribute("batchPlantList", batchPlantList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down cement list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getCementList", DropDownModel[].class);
			List<DropDownModel> cementList = Arrays.asList(cost);

			model.addAttribute("cementList", cementList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down pump list
		 */
		try {
			DropDownModel[] cost = restClient.getForObject(env.getSalesUrl() + "getpumpList", DropDownModel[].class);
			List<DropDownModel> pumpList = Arrays.asList(cost);

			model.addAttribute("pumpList", pumpList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for viewing drop down rmc grade list
		 */
		try {
			DropDownModel[] rmc = restClient.getForObject(env.getSalesUrl() + "getpRmcGradeList",
					DropDownModel[].class);
			List<DropDownModel> rmcGradeList = Arrays.asList(rmc);

			model.addAttribute("rmcGradeList", rmcGradeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for viewing drop down rmc grade list
		 */
		try {
			DropDownModel[] rmc = restClient.getForObject(env.getSalesUrl() + "getVeichelList", DropDownModel[].class);
			List<DropDownModel> veichleList = Arrays.asList(rmc);

			model.addAttribute("veichleList", veichleList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();
		deliveryChalanModel = mapper.convertValue(jsonResponse.getBody(), DeliveryChalanModel.class);
		deliveryChalanModel.setCementId(deliveryChalanModel.getCementBrand());
		model.addAttribute("deliveryChalanModel", deliveryChalanModel);

		logger.info("Method : editdeliveryChalanModel ends");
		return "sales/delivery-challan";
	}

	/**
	 * Delete deliveryChalanModel
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/list-deliveryChalan-delete")
	public @ResponseBody JsonResponse<Object> deleteTable(Model model, @RequestParam String id, HttpSession session) {

		logger.info("Method : deletedeliveryChalanModel starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String createdBy = "Dj";
		try {
			resp = restClient.getForObject(
					env.getSalesUrl() + "deleteDeliveryChalanModelById?id=" + index + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deletedeliveryChalanModel ends");

		return resp;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/list-deliveryChalan-update-invoice-data" })
	public @ResponseBody JsonResponse<DropDownModel> addInvoiceDiscount(@RequestParam("id") String id,
			@RequestParam("discount") String discount, @RequestParam("taxType") boolean taxType,
			@RequestParam("taxRate") String taxRate) {
		logger.info("Method : addInvoiceDiscount starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {

			res = restClient.getForObject(env.getSalesUrl() + "addInvoiceDiscount?id=" + id + "&discount=" + discount
					+ "&taxType=" + taxType + "&taxRate=" + taxRate, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {

			res.setMessage("success");
		}

		logger.info("Method : addInvoiceDiscount ends");
		return res;
	}

	/*
	 * for add discount page in invoice
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/list-deliveryChalan-generate-invoice" })
	public String addGenerateInvoice(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method :Add prices starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id1 = (new String(decodeId));
		JsonResponse<List<DeliveryChallanInvoiceModel>> jsonresponse = new JsonResponse<List<DeliveryChallanInvoiceModel>>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getInvoiceData?id=" + id1, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<DeliveryChallanInvoiceModel> deliveryChallanList = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<DeliveryChallanInvoiceModel>>() {
				});
		for (DeliveryChallanInvoiceModel a : deliveryChallanList) {
			a.setSubTotal(a.getPrice() * a.getQuantity());
		}

		model.addAttribute("deliveryChallanInvoiceModel", deliveryChallanList);

		return "sales/invoice-delivery-challan";
	}

	/*
	 * for add discount page in invoice
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/list-deliveryChalan-generate-preview" })
	public String generateInvoicePrintPreView(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method :generateInvoicePrintPreView  starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id1 = (new String(decodeId));
		JsonResponse<List<DeliveryChallanInvoiceModel>> jsonresponse = new JsonResponse<List<DeliveryChallanInvoiceModel>>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getInvoiceDataPreview?id=" + id1,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<DeliveryChallanInvoiceModel> deliveryChallanList = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<DeliveryChallanInvoiceModel>>() {
				});
		for (DeliveryChallanInvoiceModel a : deliveryChallanList) {
			a.setTotalPrice(a.getPrice() * a.getQuantity());
		}

		model.addAttribute("deliveryChallanInvoiceModel", deliveryChallanList);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);

		model.addAttribute("currentDate", currentDate);
		logger.info("generateInvoicePrintPreView ends");
		return "sales/delivery-challan-invoice-confirm";
	}

}
