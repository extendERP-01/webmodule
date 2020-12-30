package nirmalya.aathithya.webmodule.sales.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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
import nirmalya.aathithya.webmodule.sales.model.CustomerGSTDataModel;
import nirmalya.aathithya.webmodule.sales.model.QuotationMasterModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderDisCountDetailsModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderModel;
import nirmalya.aathithya.webmodule.sales.model.SalesItemModel;

@Controller
@RequestMapping(value = "sales")
public class SalesAddSalesOrderController {

	Logger logger = LoggerFactory.getLogger(SalesAddSalesOrderController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Sale Order Master' page
	 *
	 */
	@GetMapping("/add-sale-order")
	public String defaultSaleOrder(Model model, HttpSession session) {

		logger.info("Method : defaultSaleOrder starts");

		SaleOrderModel sales = new SaleOrderModel();
		SaleOrderModel salesSession = (SaleOrderModel) session.getAttribute("salesSession");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}
			/**
			 * Get DropDown Value Store List
			 *
			 */
			try {
				String userId = (String) session.getAttribute("USER_ID");
				DropDownModel[] payMode = restClient
						.getForObject(env.getSalesUrl() + "restGetStoreList?userId=" + userId, DropDownModel[].class);
				List<DropDownModel> storeList = Arrays.asList(payMode);

				model.addAttribute("storeList", storeList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			session.setAttribute("message", "");
			if (salesSession != null) {
				model.addAttribute("sales", salesSession);
				session.setAttribute("salesSession", null);
			} else {
				model.addAttribute("sales", sales);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultSaleOrder ends");
		return "sales/add-sale-order";
	}

	/**
	 * Web Controller - Get Customer List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-sale-order-getCustomerAutocompleteList" })
	public @ResponseBody JsonResponse<CustomerGSTDataModel> getCustomerList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getCustomerList starts");

		JsonResponse<CustomerGSTDataModel> res = new JsonResponse<CustomerGSTDataModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleCustomerList?id=" + searchValue,
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

		logger.info("Method : getCustomerList ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-sale-order-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getItemAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getItemListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemAutoSearchList ends");
		return res;
	}

	/**
	 * Web Controller - Get Serve Type By Item
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-sale-order-get-servetype" })
	public @ResponseBody JsonResponse<SalesItemModel> getServetTypeByItem(@RequestParam String id,
			@RequestParam String custId) {
		logger.info("Method : getServetTypeByItem starts");

		JsonResponse<SalesItemModel> res = new JsonResponse<SalesItemModel>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getServetTypeByItemAndCust?id=" + id + "&custId=" + custId,
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

		logger.info("Method : getServetTypeByItem ends");
		return res;
	}

	/**
	 * Web Controller - Get discount details
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-sale-order-get-discount-details" })
	public @ResponseBody JsonResponse<SaleOrderDisCountDetailsModel> getDiscountDetails(@RequestParam String id) {
		logger.info("Method : getDiscountDetails starts");

		JsonResponse<SaleOrderDisCountDetailsModel> res = new JsonResponse<SaleOrderDisCountDetailsModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getDiscountDetails?id="+id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getDiscountDetails ends");
		return res;
	}

	/**
	 * Web Controller - Add Quotation Master
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-sale-order-save-order" )
	public @ResponseBody JsonResponse<Object> addNewSaleOrder(@RequestBody List<SaleOrderModel> quotation, Model model,
			HttpSession session) {

		logger.info("Method : addNewSaleOrder function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Double subTotal = 0.0;
			Double grandTotal = 0.0;
			Double itemCGST = 0.0;
			Double totalCGST = 0.0;
			Double itemIGST = 0.0;
			Double totalIGST = 0.0;
			Double cess = 0.0;
			Double totalCess = 0.0;
			for (int i = 0; i < quotation.size(); i++) {
				quotation.get(i).setCreatedBy(userId);
				Double stotal = quotation.get(i).getLineTotal();
				subTotal = subTotal + stotal;
				quotation.get(i).setSubTotal(subTotal);
				Double gstRate = quotation.get(i).getItemGST();
				Double gstRateHalf = gstRate / 2;
				Double lineAmt = quotation.get(i).getLineTotal();
				itemCGST = (lineAmt * gstRateHalf) / 100;
				totalCGST = totalCGST + itemCGST;
				quotation.get(i).setItemCGST(itemCGST);
				quotation.get(i).setItemSGST(itemCGST);
				quotation.get(i).setqCGST(totalCGST);
				quotation.get(i).setqSGST(totalCGST);
				itemIGST = (lineAmt * gstRate) / 100;
				cess = (itemIGST * quotation.get(i).getItemCess())/100;
				quotation.get(i).setItemIGST(itemIGST);
				totalCess = totalCess + cess;
				quotation.get(i).setTotalCess(totalCess);
				totalIGST = totalIGST + itemIGST;
				quotation.get(i).setqIGST(totalIGST);
				Boolean taxType = quotation.get(0).getTaxType();

				if (taxType) {
					totalCGST = 0.0;
					quotation.get(i).setqCGST(totalCGST);
					quotation.get(i).setqSGST(totalCGST);
					grandTotal = subTotal + totalIGST + totalCess;
				} else {
					totalIGST = 0.0;
					quotation.get(i).setqIGST(totalIGST);
					grandTotal = subTotal + totalCGST + totalCGST + totalCess;
				}

				Double gTotal = (double) Math.round(grandTotal);

				quotation.get(i).setGrandTotal(gTotal);
			}
			res = restClient.postForObject(env.getSalesUrl() + "addNewSaleOrder", quotation, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addNewSaleOrder function Ends");
		return res;
	}

	/**
	 * Default 'View Quotation Master' page
	 *
	 */
	@GetMapping("/view-sale-order")
	public String viewSaleOrder(Model model, HttpSession session) {

		logger.info("Method : viewSaleOrder starts");

		logger.info("Method : viewSaleOrder ends");
		return "sales/view-sale-order";
	}

	/**
	 * Web Controller - Get Quotation List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-order-getSaleOrderList" })
	public @ResponseBody JsonResponse<DropDownModel> getSaleOrderList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSaleOrderList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleOrderListByAuotSearch?id=" + searchValue,
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

		logger.info("Method : getSaleOrderList ends");
		return res;
	}

	/**
	 * Web Controller - Get Quotation
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-sale-order-through-ajax")
	public @ResponseBody DataTableResponse viewQuotationThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewQuotationThroughAjax starts");

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

			JsonResponse<List<SaleOrderModel>> jsonResponse = new JsonResponse<List<SaleOrderModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSaleOrder", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SaleOrderModel> quote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SaleOrderModel>>() {
					});

			String s = "";

			for (SaleOrderModel m : quote) {
				if (m.getSalesOrder() != null) {
					byte[] pId = Base64.getEncoder().encode(m.getSalesOrder().getBytes());
					byte[] qId = Base64.getEncoder().encode(m.getQuationId().getBytes());
					if (m.getqStatus()) {
						m.setStatus("Active");
					} else {
						m.setStatus("Inactive");
					}
					m.setSalesOrder("<a href='javascript:void' onclick='pdfCreate(\"" + new String(qId) + "\")'>"
							+ m.getSalesOrder() + "</a>");
					System.out.println("tauas value " + m.getApproveActive());
					if (m.getApproveActive()) {
						m.setApproveStatus("Complete");
						s = s + " <a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + new String(qId)
								+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
						s = s + "&nbsp;&nbsp;<a href='generate-sales-order?id=" + new String(qId)
								+ "'><i class='fa fa-files-o' title='Sales Order' style=\"font-size:24px\"></i></a>";
						m.setAction(s);
						s = "";
					} else {
						m.setApproveStatus("Pending");
						s = s + "<a href='view-sale-order-edit?id=" + new String(pId)
								+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
								+ " <a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + new String(pId)
								+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";

						s = s + "&nbsp;&nbsp;<a href='generate-sales-order?id=" + new String(qId)
								+ "'><i class='fa fa-files-o' title='Sales Order' style=\"font-size:24px\"></i></a>";

						m.setAction(s);
						s = "";
					}

				}

				response.setRecordsTotal(jsonResponse.getTotal());
				response.setRecordsFiltered(jsonResponse.getTotal());
				response.setDraw(Integer.parseInt(draw));
				response.setData(quote);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewQuotationThroughAjax ends");
		return response;

	}

	/**
	 * Web Controller - Get Quotation For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-order-modal" })
	public @ResponseBody JsonResponse<Object> modalQuotation(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalQuotation starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleOrderById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalQuotation ends");
		return res;
	}

	/**
	 * Web Controller - Edit Quotation Master
	 *
	 */
	@GetMapping("view-sale-order-edit")
	public String editQuotation(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editQuotation starts");

		/**
		 * get DropDown value for Serve Type
		 *
		 */
		try {
			DropDownModel[] serveType = restClient.getForObject(env.getSalesUrl() + "restGetServeType",
					DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(serveType);

			model.addAttribute("serveTypeList", serveTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute("USER_ID");
			DropDownModel[] payMode = restClient.getForObject(env.getSalesUrl() + "restGetStoreList?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {

			SaleOrderModel[] quotation = restClient.getForObject(env.getSalesUrl() + "editSaleOrderById?id=" + id,
					SaleOrderModel[].class);
			List<SaleOrderModel> quotationList = Arrays.asList(quotation);

			Stream<SaleOrderModel> notNullObjs = quotationList.stream().filter(obj -> obj.getDiscount() != null);

			Double discountSum = notNullObjs.mapToDouble(SaleOrderModel::getDiscount).sum();

			model.addAttribute("discountSum", discountSum);
			model.addAttribute("saleOrderId", quotationList.get(0).getSalesOrder());
			model.addAttribute("quotation", quotationList);
			System.out.println("quotationList " + quotationList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editQuotation starts");
		return "sales/add-sale-order";
	}

	/**
	 * Web Controller - Get POS NUMBER By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sale-order-posAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> posAutoComplete(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesOrderList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getPOSNumber?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSalesOrderList ends");
		return res;
	}

	/**
	 * Web Controller - Add Purchase Order
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/generate-sales-order1" })
	public String addPurchaseOrder(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method : addPurchaseOrder starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<QuotationMasterModel>> jsonresponse = new JsonResponse<List<QuotationMasterModel>>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getSaleOrderById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<QuotationMasterModel> quotation = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<QuotationMasterModel>>() {
				});

		/**
		 * for (QuotationMasterModel m : quotation) {
		 * 
		 * }
		 */

		/**
		 * Get DropDown Value For Payment Mode For Advance Payment
		 *
		 */
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getSalesUrl() + "restGetAdvPayMode",
					DropDownModel[].class);
			List<DropDownModel> payModeList = Arrays.asList(payMode);

			model.addAttribute("payModeList", payModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * Get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", "/document/hotel/" + logo + "");
		quotation.get(0).setCurDate(curDate);

		model.addAttribute("quotation", quotation);
		System.out.println(quotation);
		logger.info("Method : addPurchaseOrder ends");
		return "sales/generate-sales-order";
	}

	
	/**
	 * ONCHANGE MENU ITEM PRICE DATA
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("add-sale-order-getItemPriceByCust")
	public @ResponseBody JsonResponse<List<DropDownModel>> getPriceDetailsByCust(@RequestParam String menuItemId,
			@RequestParam String custId) {
		logger.info("Method : getPriceDetailsByCust  starts");
		JsonResponse<Object> response = new JsonResponse<Object>();
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(
					env.getRestaurantUrl() + "getPriceDetailsByCust?menuItemId=" + menuItemId + "&custId=" + custId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {
			res.setCode(response.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}
		logger.info("Method :  getPriceDetailsByCust  ends");
		return res;
	}
	
}
