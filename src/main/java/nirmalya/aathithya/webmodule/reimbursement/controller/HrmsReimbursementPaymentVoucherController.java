package nirmalya.aathithya.webmodule.reimbursement.controller;

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
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsEmployeeCompanyDetailsModel;
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsReimbursementModel;
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsReimbursementPaymentModal;

@Controller
@RequestMapping(value = "reimbursement")
public class HrmsReimbursementPaymentVoucherController {

	Logger logger = LoggerFactory.getLogger(HrmsAddReimbursementController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add employee view page
	 */
	@GetMapping("/add-reimbursement-payment-voucher")
	public String addReimbursementePayment(Model model, HttpSession session) {

		logger.info("Method : addReimbursementePayment  starts");

		HrmsReimbursementPaymentModal reimbursementPayment = new HrmsReimbursementPaymentModal();
		HrmsReimbursementPaymentModal sessionReimbursementPayment = (HrmsReimbursementPaymentModal) session
				.getAttribute("sessionReimbursementPayment");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionReimbursementPayment != null) {
			model.addAttribute("reimbursementPayment", sessionReimbursementPayment);
			session.setAttribute("sessionReimbursementPayment", null);
		} else {
			model.addAttribute("reimbursementPayment", reimbursementPayment);
		}
		/*
		 * for viewing drop down list of traveling reimbursement
		 */
		try {
			DropDownModel[] travelReq = restClient.getForObject(env.getReimbursementUrl() + "getReqListPayment",
					DropDownModel[].class);
			List<DropDownModel> travelReqList = Arrays.asList(travelReq);
			model.addAttribute("travelReqList", travelReqList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list of employee
		 */
		try {
			DropDownModel[] empl = restClient.getForObject(env.getReimbursementUrl() + "getEmpListPayment",
					DropDownModel[].class);
			List<DropDownModel> employeeList = Arrays.asList(empl);
			model.addAttribute("employeeList", employeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] payMode = restClient.getForObject(env.getReimbursementUrl() + "getPayMode",
					DropDownModel[].class);
			List<DropDownModel> PayModeList = Arrays.asList(payMode);

			model.addAttribute("PayModeList", PayModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] bankName = restClient.getForObject(env.getReimbursementUrl() + "getBankNames",
					DropDownModel[].class);
			List<DropDownModel> bankNameList = Arrays.asList(bankName);
			model.addAttribute("bankNameList", bankNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addReimbursementePayment  ends");

		return "reimbursement/add-reimbursement-payment";
	}

	/**
	 * get grn list for add payment voucher
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-payment-voucher-get-reim-list" })
	public @ResponseBody JsonResponse<List<HrmsReimbursementPaymentModal>> getReimbursementListPayment(Model model,
			@RequestBody HrmsReimbursementPaymentModal hrmsReimbursementPaymentModal, HttpServletRequest request) {

		logger.info("Method : getReimbursementListPayment starts");

		String employeeId = hrmsReimbursementPaymentModal.getEmployeeId();
		String reimId = hrmsReimbursementPaymentModal.getReimbNo();
		String fromDate = hrmsReimbursementPaymentModal.getFromDate();
		String toDate = hrmsReimbursementPaymentModal.getToDate();
		DataTableRequest tableRequest = new DataTableRequest();
		JsonResponse<List<HrmsReimbursementPaymentModal>> resp = new JsonResponse<List<HrmsReimbursementPaymentModal>>();

		tableRequest.setParam1(employeeId);
		tableRequest.setParam2(reimId);
		tableRequest.setParam3(fromDate);
		tableRequest.setParam4(toDate);
		try {

			resp = restClient.postForObject(env.getReimbursementUrl() + "getReimbursementListPayment", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<HrmsReimbursementPaymentModal> voucherModel = mapper.convertValue(resp.getBody(),
				new TypeReference<List<HrmsReimbursementPaymentModal>>() {
				});

		for (HrmsReimbursementPaymentModal a : voucherModel) {
			double totalAmountTobePaid = 0;
			totalAmountTobePaid = a.getTotalAmonut() - a.getAdvanceTaken();
			a.setAmountTobePaid(totalAmountTobePaid);
			byte[] pId = Base64.getEncoder().encode(a.getReimbNo().getBytes());
			a.setAction(" <a data-toggle='modal' title='view' href='javascript:void' onclick='viewInModel(\""
					+ new String(pId) + "\")'>" + a.getReimbNo() + "</a>");

		}
		resp.setBody(voucherModel);
		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :getReimbursementListPayment ends");

		return resp;
	}

	/*
	 * for drop down branch names by bank id
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-payment-voucher-branchList" })
	public @ResponseBody JsonResponse<DropDownModel> getbranchList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getbranchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getBranchList?id=" + index, JsonResponse.class);

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
	@PostMapping(value = { "/add-reimbursement-payment-voucher-acNoList" })
	public @ResponseBody JsonResponse<DropDownModel> getAccountNoList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAccountNoList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getAccountNoList?id=" + index,
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

		logger.info("Method : getAccountNoList ends");
		return res;
	}

	/**
	 * for account name by account number
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-payment-voucher-acName" })
	public @ResponseBody JsonResponse<DropDownModel> getAccountNameList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getAccountNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getAccountNameList?id=" + index,
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

		logger.info("Method : getAccountNameList ends");
		return res;
	}

	/*
	 * post mapping for add other service used
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-reimbursement-payment-voucher-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addReimbursementVoucher(
			@RequestBody List<HrmsReimbursementPaymentModal> paymentVoucherModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addReimbursementVoucher function starts");
		try {
System.out.println(paymentVoucherModel);
			res = restClient.postForObject(env.getReimbursementUrl() + "addReimbursementVoucher", paymentVoucherModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addReimbursementVoucher function Ends");
		return res;
	}

	/*
	 * for get return details by payment voucher
	 */
	@GetMapping("/view-reimbursement-payment-voucher-confirm-view")
	public String viewReturnData(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method in web: view-payment-voucher-view starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		try {

			HrmsReimbursementPaymentModal[] paymentVoucher = restClient.getForObject(
					env.getReimbursementUrl() + "getReimbPayData?id=" + id, HrmsReimbursementPaymentModal[].class);

			List<HrmsReimbursementPaymentModal> PaymentVoucherModel = Arrays.asList(paymentVoucher);

			if (!PaymentVoucherModel.isEmpty()) {

				/**
				 * for vendor details
				 */
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl() + "getVenderDetails?id="
									+ PaymentVoucherModel.get(0).getEmployeeId(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> vendorList = Arrays.asList(UserDetails);

					model.addAttribute("vendorList", vendorList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}

				/**
				 * for hotel details
				 */
				try {
					HrmsEmployeeCompanyDetailsModel[] UserDetails = restClient.getForObject(
							env.getReimbursementUrl() + "getHotelDetails?id=" + PaymentVoucherModel.get(0).getAccNo(),
							HrmsEmployeeCompanyDetailsModel[].class);
					List<HrmsEmployeeCompanyDetailsModel> hotelList = Arrays.asList(UserDetails);

					model.addAttribute("hotelList", hotelList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			model.addAttribute("PaymentVoucherModel", PaymentVoucherModel);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getReimbursementUrl() + "restLogoImage-PaymentVoucher?logoType=" + "header-Logo",
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

		return "reimbursement/reimbursement-payment-confirm";
	}

	/*
	 * get mapping for view payment voucher
	 */
	@GetMapping("/view-reimbursement-payment-voucher")
	public String viewReimbursementPaymentVoucher(Model model, HttpSession session) {

		logger.info("Method : viewReimbursementPaymentVoucher starts");

		/*
		 * for viewing drop down list of traveling reimbursement
		 */
		try {
			DropDownModel[] travelReq = restClient.getForObject(env.getReimbursementUrl() + "getReqListPayment",
					DropDownModel[].class);
			List<DropDownModel> travelReqList = Arrays.asList(travelReq);
			model.addAttribute("travelReqList", travelReqList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list of employee
		 */
		try {
			DropDownModel[] empl = restClient.getForObject(env.getReimbursementUrl() + "getEmpListPayment",
					DropDownModel[].class);
			List<DropDownModel> employeeList = Arrays.asList(empl);
			model.addAttribute("employeeList", employeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewReimbursementPaymentVoucher ends");
		return "reimbursement/view-reimbursement-payment-voucher";
	}

	/**
	 * View all payment voucher through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-payment-voucher-ajax")
	public @ResponseBody DataTableResponse viewReimbursementPaymentVoucherThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3, @RequestParam String param4) {

		logger.info("Method : viewReimbursementPaymentVoucherThroughAjax starts");
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

			JsonResponse<List<HrmsReimbursementPaymentModal>> jsonResponse = new JsonResponse<List<HrmsReimbursementPaymentModal>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getAllReimbursementPaymentVoucher",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsReimbursementPaymentModal> table = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsReimbursementPaymentModal>>() {
					});

			String s = "";

			for (HrmsReimbursementPaymentModal m : table) {
				byte[] pId = Base64.getEncoder().encode(m.getVoucherNo().getBytes());

				s = "";
				s = s + "<a href='javascript:void' onclick='viewModal(\"" + new String(pId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";

				m.setReferenceNo("<button type=\"button\" class=\"btn btn-info\" style=\"font-size:20px\" onclick='viewModalOfPaymentDetails(\""
						+ new String(pId) + "\")'> View </Button>");

				s = s + "<a href='javascript:void' onclick='viewModalOfPartialPaymentDetailsPfd(\"" + new String(pId)
						+ "\")'><i class='fa fa-download' style=\"font-size:20px\" aria-hidden='true' style=\"font-size:24px\"></i></a>";

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

		logger.info("Method : viewReimbursementPaymentVoucherThroughAjax ends");
		return response;
	}

	/*
	 * for purchase order auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-reimbursement-payment-voucher-modal" })
	public @ResponseBody JsonResponse<HrmsReimbursementPaymentModal> getPartialPaymentDetails(Model model,
			@RequestBody String grnNo, BindingResult result) {
		logger.info("Method : getPartialPaymentDetails starts");
		byte[] decodeId = Base64.getDecoder().decode(grnNo.getBytes());

		String id = (new String(decodeId));
		JsonResponse<HrmsReimbursementPaymentModal> response = new JsonResponse<HrmsReimbursementPaymentModal>();
		try {
			response = restClient.getForObject(env.getReimbursementUrl() + "getReimbursementModal?id=" + id,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + response.getMessage());
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}

		logger.info("Method : getPartialPaymentDetails ends");
		return response;
	}

	/*
	 * For Modal other employee education
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-payment-voucher-modalView" })
	public @ResponseBody JsonResponse<List<HrmsReimbursementModel>> modalAssignmentEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmsReimbursementModel>> response = new JsonResponse<List<HrmsReimbursementModel>>();
		try {
			response = restClient.getForObject(env.getReimbursementUrl() + "getReimbusementByIdModal?reimId=" + id,
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
		System.out.println("++++++++++++++++++++++++++++"+response);
		logger.info("Method : modalAssignmentEdu  ends ");
		return response;
	}
}
