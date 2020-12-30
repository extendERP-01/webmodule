package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMethod;
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGrnPaymentDetails;
import nirmalya.aathithya.webmodule.inventory.model.PaymentVoucherModel;

@Controller
@RequestMapping(value = "inventory")
public class InventoryPaymentVoucherController {
	Logger logger = LoggerFactory.getLogger(InventoryPaymentVoucherController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add add payment voucher
	 */
	@GetMapping("/add-payment-voucher")
	public String addPaymentVoucher(Model model, HttpSession session) {

		logger.info("Method : add-payment-voucher starts");

		PaymentVoucherModel paymentVoucherModel = new PaymentVoucherModel();
		PaymentVoucherModel sessionpaymentVoucherModel = (PaymentVoucherModel) session
				.getAttribute("sessionpaymentVoucherModel");

		String message = (String) session.getAttribute("message");

		try {
			DropDownModel[] payMode = restClient.getForObject(env.getInventoryUrl() + "getPayMode",
					DropDownModel[].class);
			List<DropDownModel> PayModeList = Arrays.asList(payMode);

			model.addAttribute("PayModeList", PayModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] bankName = restClient.getForObject(env.getInventoryUrl() + "getBankNames",
					DropDownModel[].class);
			List<DropDownModel> bankNameList = Arrays.asList(bankName);
			model.addAttribute("bankNameList", bankNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionpaymentVoucherModel != null) {
			model.addAttribute("paymentVoucherModel", sessionpaymentVoucherModel);
			session.setAttribute("sessionpaymentVoucherModel", null);
		} else {
			model.addAttribute("paymentVoucherModel", paymentVoucherModel);
		}

		logger.info("Method : add-payment-voucher  ends");

		return "inventory/paymentVoucher";
	}

	/*
	 * post mapping for add other service used
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-payment-voucher-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> paymentOfMultipleGRN(
			@RequestBody List<PaymentVoucherModel> paymentVoucherModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : paymentOfMultipleGRN function starts");
		try {
			Double bAmount = 0.0;
			Double subTotal = 0.0;
			Double subTotalNew = 0.0;
			Double igst = 0.0;
			Double igstNew = 0.0;
			Double cgst = 0.0;
			Double cgstNew = 0.0;
			Double tdsAmount = 0.0;
			Double tdsAmountNew = 0.0;
			Double cessAmt = 0.0;
			Double cessAmtNew = 0.0;
			Double totalAmount = 0.0;
			for (PaymentVoucherModel a : paymentVoucherModel) {
				a.setCreatedBy(a.getVendorName());
				totalAmount = a.getTotal();
				subTotal = a.getSubTotal();
				igst = a.getIgst();
				cgst = a.getCgst();
				tdsAmount = a.getTdsAmount();
				cessAmt = a.getCessAmt();
				if(a.getPaymentType()) {
					bAmount = a.getTotal();
				} else {
					bAmount = a.getPartialAmt();
				}
				subTotalNew = (bAmount * subTotal) / totalAmount;
				igstNew = (bAmount * igst) / totalAmount;
				cgstNew = (bAmount * cgst) / totalAmount;
				tdsAmountNew = (bAmount * tdsAmount) / totalAmount;
				cessAmtNew = (bAmount * cessAmt) / totalAmount;
				
				a.setSubTotal(subTotalNew);
				a.setIgst(igstNew);
				a.setCgst(cgstNew);
				a.setSgst(cgstNew);
				a.setTdsAmount(tdsAmountNew);
				a.setCessAmt(cessAmtNew);
			}

			res = restClient.postForObject(env.getInventoryUrl() + "restAddPaymentVoucher", paymentVoucherModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : paymentOfMultipleGRN function Ends");
		return res;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-getPOAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGuestList starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPOrderByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestList ends");
		return response;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-getVendorAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGuestList starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getVendorByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestList ends");
		return response;
	}

	/**
	 * get grn list for schedule payment voucher
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-get-gsn-list" })
	public @ResponseBody JsonResponse<List<PaymentVoucherModel>> getGRNList(Model model,
			@RequestBody PaymentVoucherModel paymentVoucherModel, HttpServletRequest request) {

		logger.info("Method : add-payment-voucher-get-gsn-list starts");

		String poNo = paymentVoucherModel.getPoNumber();
		String vendorName = paymentVoucherModel.getVendorName();
		String fromDate = paymentVoucherModel.getFromDate();
		String toDate = paymentVoucherModel.getToDate();
		DataTableRequest tableRequest = new DataTableRequest();
		JsonResponse<List<PaymentVoucherModel>> resp = new JsonResponse<List<PaymentVoucherModel>>();

		tableRequest.setParam1(poNo);
		tableRequest.setParam2(vendorName);
		tableRequest.setParam3(fromDate);
		tableRequest.setParam4(toDate);
		try {

			resp = restClient.postForObject(env.getInventoryUrl() + "getGRNList", tableRequest, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<PaymentVoucherModel> voucherModel = mapper.convertValue(resp.getBody(),
				new TypeReference<List<PaymentVoucherModel>>() {
				});

		for (PaymentVoucherModel a : voucherModel) {

			byte[] pId = Base64.getEncoder().encode(a.getGrnNo().getBytes());
			a.setAction1(" <a data-toggle='modal' title='view' href='javascript:void' onclick='viewInModel(\""
					+ new String(pId) + "\")'>" + a.getGrnNo() + "</a>");

		}

		resp.setBody(voucherModel);
		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :add-payment-voucher-get-gsn-list ends");

		return resp;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-getPaymentValues" })
	public @ResponseBody JsonResponse<PaymentVoucherModel> getpaymentValues(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getpaymentValues starts");

		JsonResponse<PaymentVoucherModel> response = new JsonResponse<PaymentVoucherModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPaymentValues?id=" + searchValue,
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
		logger.info("Method : getpaymentValues ends");
		return response;
	}

	/*
	 * for grn return details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-getReturnValues" })
	public @ResponseBody JsonResponse<PaymentVoucherModel> getReturnValues(Model model, @RequestBody String grnNo,
			BindingResult result) {
		logger.info("Method : getReturnValues starts");

		JsonResponse<PaymentVoucherModel> response = new JsonResponse<PaymentVoucherModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getReturnValues?id=" + grnNo,
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

		logger.info("Method : getReturnValues ends");
		return response;
	}

	/*
	 * for drop down branch names by bank id
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-branchList" })
	public @ResponseBody JsonResponse<DropDownModel> getbranchList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getbranchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getBranchList?id=" + index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getbranchList ends");
		return res;
	}

	/*
	 * for drop down account list by branch name
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-acNoList" })
	public @ResponseBody JsonResponse<DropDownModel> getAccountNoList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAccountNoList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getAccountNoList?id=" + index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAccountNoList ends");
		return res;
	}

	/**
	 * for account name by account number
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-acName" })
	public @ResponseBody JsonResponse<DropDownModel> getAccountNameList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAccountNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getAccountNameList?id=" + index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAccountNameList ends");
		return res;
	}

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/view-payment-voucher")
	public String viewPaymentVoucher(Model model, HttpSession session) {

		logger.info("Method : viewPaymentVoucher starts");

		logger.info("Method : viewPaymentVoucher ends");
		return "inventory/view-payment-voucher";
	}

	/**
	 * View all payment voucher through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-payment-voucher-ajax")
	public @ResponseBody DataTableResponse viewPaymentVoucherThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewPaymentVoucherThroughAjax starts");

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

			JsonResponse<List<PaymentVoucherModel>> jsonResponse = new JsonResponse<List<PaymentVoucherModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllPaymentVoucher", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PaymentVoucherModel> table = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PaymentVoucherModel>>() {
					});

			String s = "";

			for (PaymentVoucherModel m : table) {
				byte[] pId = Base64.getEncoder().encode(m.getPaymentVoucher().getBytes());

				s = "";
				s = s + "<a href='javascript:void' onclick='viewReturnDetails(\"" + new String(pId)
						+ "\")'><i class='fa fa-search search'></i></a>";

				m.setRefno("<button type=\"button\" class=\"btn btn-info\" onclick='viewModalOfPaymentDetails(\""
						+ m.getGrnNo() + "\")'> View </Button>");

				m.setPaymentVoucher("<a href='javascript:void'onclick='confirmPrint(\"" + new String(pId) + "\")'>"
						+ m.getPaymentVoucher() + "</a>");
				if (!m.getPaymentStatus()) {
					s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='addPaymentVoucher(\"" + new String(pId)
							+ "\")'><i class='fa fa-money' aria-hidden='true'></i></a>";
					/*
					 * if(!m.getPaymentType()) { s = s +
					 * "<a href='javascript:void' onclick='viewModalOfPartialPaymentDetails(\"" +
					 * m.getGrnNo() +
					 * "\")'><i class='fa fa-university' aria-hidden='true' style=\"font-size:24px\"></i></a>"
					 * ; }
					 */
				} else {

					s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewModalOfPartialPaymentDetailsPfd(\""
							+ new String(pId) + "\")'><i class='fa fa-download' aria-hidden='true' ></i></a>";
				}

				m.setAction(s);

				s = "";
				double total = (m.getSubTotal() + ((2 * m.getCgst()) + (m.getIgst()))) - (m.getTdsAmount());
				// total = total - (total * m.getTdsRate() /100 );
				m.setTotal(total);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(table);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewPaymentVoucherThroughAjax ends");
		return response;
	}

	/*
	 * for get return details by payment voucher
	 */
	@GetMapping("/view-payment-voucher-view")
	public String viewReturnData(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method in web: view-payment-voucher-view starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		try {

			PaymentVoucherModel[] paymentVoucher = restClient
					.getForObject(env.getInventoryUrl() + "getReturnData?id=" + id, PaymentVoucherModel[].class);

			List<PaymentVoucherModel> PaymentVoucherModel = Arrays.asList(paymentVoucher);
			if (!PaymentVoucherModel.isEmpty()) {
//				double totalSgst = 0;
//				double totalIgst = 0;
//				double total = 0;
//				if (PaymentVoucherModel.get(0).getGoodsReturnNote() != null) {
//					totalSgst = PaymentVoucherModel.get(0).getSgst() - PaymentVoucherModel.get(0).getReturnSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst() - PaymentVoucherModel.get(0).getReturnIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal() - PaymentVoucherModel.get(0).getReturnTotal();
//				} else {
//					totalSgst = PaymentVoucherModel.get(0).getSgst();
//					totalIgst = PaymentVoucherModel.get(0).getIgst();
//					total = PaymentVoucherModel.get(0).getGrnTotal();
//				}
//				double tdsAmount = (PaymentVoucherModel.get(0).getSubTotal() * 10) / 100;
//				PaymentVoucherModel.get(0).setTdsAmount(tdsAmount);
//				PaymentVoucherModel.get(0).setSgst(totalSgst);
//				PaymentVoucherModel.get(0).setCgst(totalSgst);
//				PaymentVoucherModel.get(0).setIgst(totalIgst);
//				total = PaymentVoucherModel.get(0).getSubTotal() + tdsAmount - totalIgst - (2 * totalSgst);
//				PaymentVoucherModel.get(0).setTotal(total);

				/**
				 * for vendor details
				 */
				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getVenderDetails?id=" + PaymentVoucherModel.get(0).getVendorId(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> vendorList = Arrays.asList(UserDetails);

					model.addAttribute("vendorList", vendorList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}

				try {
					PaymentVoucherModel[] UserDetails = restClient.getForObject(
							env.getInventoryUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getGrnNo(),
							PaymentVoucherModel[].class);
					List<PaymentVoucherModel> hotelList = Arrays.asList(UserDetails);

					model.addAttribute("hotelList", hotelList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			model.addAttribute("PaymentVoucherModel", PaymentVoucherModel);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getInventoryUrl() + "restLogoImage-PaymentVoucher?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			// data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String variable = env.getBaseUrlPath();
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", variable + "document/hotel/" + logo + "");
		logger.info("Method in web: view-payment-voucher-view ends");

		return "inventory/inventory-payment-voucher-confirm";
	}

	/*
	 * for get return details by payment voucher in view payment voucher page
	 */
	@GetMapping("/view-payment-voucher-view-modal")
	public String viewReturnDataModalView(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method in web: viewReturnDataModalView starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		try {

			PaymentVoucherModel[] paymentVoucher = restClient
					.getForObject(env.getInventoryUrl() + "getReturnData?id=" + id, PaymentVoucherModel[].class);

			List<PaymentVoucherModel> PaymentVoucherModel = Arrays.asList(paymentVoucher);
			model.addAttribute("PaymentVoucherModel", PaymentVoucherModel);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method in web: viewReturnDataModalView ends");

		return "inventory/inventory-payment-voucher-modalview";
	}

	/*
	 * get mapping for view accountant schedule page
	 */
	@GetMapping("/view-accountant-pay-schedule")
	public String viewPaymentVoucherAccountant(Model model, HttpSession session) {

		logger.info("Method : view-accountant-pay-schedule starts");

		logger.info("Method : view-accountant-pay-schedule ends");
		return "inventory/view-accountant-pay-schedule";
	}

	/**
	 * View all accountant pay schedule through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-accountant-pay-schedule-ajax")
	public @ResponseBody DataTableResponse viewPaymentVoucherAccountantThroughAjax(Model model,
			HttpServletRequest request) {

		logger.info("Method : viewPaymentVoucherAccountantThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));

			JsonResponse<List<PaymentVoucherModel>> jsonResponse = new JsonResponse<List<PaymentVoucherModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllPaymentVoucherAccountantUnpaid",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PaymentVoucherModel> table = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PaymentVoucherModel>>() {
					});

			String s = "";

			for (PaymentVoucherModel m : table) {
				byte[] pId = Base64.getEncoder().encode(m.getGrnNo().getBytes());
				s = s + "<a data-toggle='modal' title='view' href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' ></i></a>";

				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(table);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewPaymentVoucherAccountantThroughAjax ends");
		return response;
	}

	/*
	 * for adding schedule date from the schedule page
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-accountant-pay-schedule-add-schedule-date-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addScheduleDate(
			@RequestBody List<PaymentVoucherModel> paymentVoucherModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addScheduleDate function starts");

		try {
			res = restClient.postForObject(env.getInventoryUrl() + "getEditScheduleDate", paymentVoucherModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addScheduleDate function Ends");
		return res;
	}

	/*
	 * get mapping for view accountant schedule approve page
	 */
	@GetMapping("/view-accountant-pay-schedule-aprove")
	public String viewPaymentVoucherAccountantAprove(Model model, HttpSession session) {

		logger.info("Method :viewPaymentVoucherAccountantAprove starts");

		logger.info("Method : viewPaymentVoucherAccountantAprove ends");
		return "inventory/view-accountant-pay-schedule-aprove";
	}

	/**
	 * View all accountant pay schedule approve through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-accountant-pay-schedule-aprove-ajax")
	public @ResponseBody DataTableResponse viewPaymentVoucherAccountantAproveThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3, @RequestParam String param4) {

		logger.info("Method : viewPaymentVoucherAccountantAproveThroughAjax starts");

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

			JsonResponse<List<PaymentVoucherModel>> jsonResponse = new JsonResponse<List<PaymentVoucherModel>>();

			jsonResponse = restClient.postForObject(
					env.getInventoryUrl() + "getAllPaymentVoucherAccountantUnpaidAprove", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PaymentVoucherModel> table = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PaymentVoucherModel>>() {
					});

			String s = "";

			for (PaymentVoucherModel m : table) {
				byte[] pId = Base64.getEncoder().encode(m.getGrnNo().getBytes());
				if (!m.getAproveStaus()) {

					s = "";
					s = s + "<input type=\"checkbox\" id=\"grnCheck\" value=\"" + m.getGrnNo()
							+ "\" name=\"checkBoxName\"  class=\"checkboxClass\" />";

					m.setAction(s);

				} else {
					s = s + "<i class=\"fa fa-thumbs-up\" title='Approved'></i>";
				}
				s = s + " &nbsp; &nbsp; <a data-toggle='modal' title='view' href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(table);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewPaymentVoucherAccountantAproveThroughAjax ends");
		return response;
	}

	/*
	 * add schedule approved date for schedule approve page
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-accountant-pay-schedule-aprove-status-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addScheduleApproved(
			@RequestBody List<PaymentVoucherModel> paymentVoucherModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addScheduleDate function starts");

		try {
			res = restClient.postForObject(env.getInventoryUrl() + "getApproveScheduleDate", paymentVoucherModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addScheduleDate function Ends");
		return res;
	}

	/**
	 * get grn list for add payment voucher
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-get-grn-list-payment" })
	public @ResponseBody JsonResponse<List<PaymentVoucherModel>> getGnsListPayment(Model model,
			@RequestBody PaymentVoucherModel paymentVoucherModel, HttpServletRequest request) {

		logger.info("Method : getGnsListPayment starts");

		String poNo = paymentVoucherModel.getPoNumber();
		String vendorName = paymentVoucherModel.getVendorName();
		String fromDate = paymentVoucherModel.getFromDate();
		String toDate = paymentVoucherModel.getToDate();
		DataTableRequest tableRequest = new DataTableRequest();
		JsonResponse<List<PaymentVoucherModel>> resp = new JsonResponse<List<PaymentVoucherModel>>();

		tableRequest.setParam1(poNo);
		tableRequest.setParam2(vendorName);
		tableRequest.setParam3(fromDate);
		tableRequest.setParam4(toDate);
		try {

			resp = restClient.postForObject(env.getInventoryUrl() + "getGRNListPayment", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<PaymentVoucherModel> voucherModel = mapper.convertValue(resp.getBody(),
				new TypeReference<List<PaymentVoucherModel>>() {
				});

		for (PaymentVoucherModel a : voucherModel) {
//			double totalTax = 0;
//			double subTotal = a.getSubTotal();
//			double discount = a.getDiscount();
//			if (a.getTaxType()) {
//
//				totalTax = a.getIgst();
//			} else {
//				totalTax = a.getCgst() + a.getSgst();
//			}
//			double baseAmount = subTotal - discount;
//			double taxableAmount = baseAmount - (baseAmount * 0.1);
//			double totalAmount = taxableAmount + totalTax;
//			a.setTotal(totalAmount);
//			a.setTaxableAmount(taxableAmount);
			byte[] pId = Base64.getEncoder().encode(a.getGrnNo().getBytes());
			a.setAction1(" <a data-toggle='modal' title='view' href='javascript:void' onclick='viewInModel(\""
					+ new String(pId) + "\")'>" + a.getGrnNo() + "</a>");

		}
		resp.setBody(voucherModel);
		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :getGnsListPayment ends");

		return resp;
	}

	/**
	 * Modal View of accountant schedule page
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-modal" })
	public @ResponseBody JsonResponse<Object> modalAccountantSchedule(@RequestParam String id) {

		logger.info("Method : modalAccountantSchedule starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(encodeByte));

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGrnById?id=" + id1, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalAccountantSchedule ends");
		return res;
	}

	/**
	 * Modal View of schedule approved
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-aprove-modal" })
	public @ResponseBody JsonResponse<Object> modalaccountantApprove(@RequestParam String id) {

		logger.info("Method : modalaccountantApprove starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(encodeByte));

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGrnById?id=" + id1, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalaccountantApprove ends");
		return res;
	}

	/**
	 * Modal View of payment voucher
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-modal" })
	public @ResponseBody JsonResponse<Object> modalaPaymentVoucher(@RequestParam String id) {

		logger.info("Method : modalaPaymentVoucher starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(encodeByte));

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGrnById?id=" + id1, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalaPaymentVoucher ends");
		return res;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-getPOAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestListAccountantSchedule(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGuestListAccountantSchedule starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPOrderByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestListAccountantSchedule ends");
		return response;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-getVendorAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListAccountant(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGuestList starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getVendorByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestList ends");
		return response;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule approve
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-aprove-getPOAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestListAccountantScheduleApprove(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGuestListAccountantScheduleApprove starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPOrderByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestListAccountantScheduleApprove ends");
		return response;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule approve
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-accountant-pay-schedule-aprove-getVendorAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListAccountantApprove(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getVendorListAccountantApprove starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getVendorByAutosearch?id=" + searchValue,
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

		logger.info("Method : getVendorListAccountantApprove ends");
		return response;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule approve
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payment-voucher-getPOAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestListViewPaymentVoucher(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGuestListViewPaymentVoucher starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPOrderByAutosearch?id=" + searchValue,
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

		logger.info("Method : getGuestListViewPaymentVoucher ends");
		return response;
	}

	/*
	 * for purchase order auto complete for view-accountant-pay-schedule approve
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payment-voucher-getVendorAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorListViewPaymentVoucher(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getVendorListViewPaymentVoucher starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getVendorByAutosearch?id=" + searchValue,
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

		logger.info("Method : getVendorListViewPaymentVoucher ends");
		return response;
	}

	/**
	 * Modal View of schedule approved
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payment-voucher-reference-modalview" })
	public @ResponseBody JsonResponse<Object> modalViewOfReference(@RequestParam String id) {

		logger.info("Method : modalViewOfReference starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPaymentVoucherById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalViewOfReference ends");
		return res;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-payment-voucher-getPartialPaymentDetails" })
	public @ResponseBody JsonResponse<InventoryGrnPaymentDetails> getPartialPaymentDetails(Model model,
			@RequestBody String grnNo, BindingResult result) {
		logger.info("Method : getPartialPaymentDetails starts");

		JsonResponse<InventoryGrnPaymentDetails> response = new JsonResponse<InventoryGrnPaymentDetails>();
		try {
			response = restClient.getForObject(env.getInventoryUrl() + "getPartialPaymentDetails?id=" + grnNo,
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

		logger.info("Method : getPartialPaymentDetails ends");
		return response;
	}

	/*
	 * for purchase order auto complete
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/add-payment-voucher-getCreditDetails" })
	 * public @ResponseBody JsonResponse<InventoryGrnPaymentDetails>
	 * getCreditDetails(Model model,
	 * 
	 * @RequestBody String grnNo, BindingResult result) {
	 * logger.info("Method : getCreditDetails starts");
	 * 
	 * JsonResponse<InventoryGrnPaymentDetails> response = new
	 * JsonResponse<InventoryGrnPaymentDetails>(); try { response =
	 * restClient.getForObject(env.getInventoryUrl() + "getCreditDetails?id=" +
	 * grnNo, JsonResponse.class); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * if (response.getMessage() != null) {
	 * System.out.println("if block getmsg() not false : " + response.getMessage());
	 * response.setCode(response.getMessage()); response.setMessage("Unsuccess"); }
	 * else { response.setMessage("success"); }
	 * 
	 * logger.info("Method : getCreditDetails ends"); return response; }
	 */

	/**
	 * Modal View of all last payment details
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payment-voucher-all-payment-details" })
	public @ResponseBody JsonResponse<Object> modalViewOfLastTransation(@RequestParam String id) {

		logger.info("Method : modalViewOfLastTransation starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPartialPaymentDetails?id=" + id1,
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

		logger.info("Method : modalViewOfLastTransation ends");
		return res;
	}

}
