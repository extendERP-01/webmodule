package nirmalya.aathithya.webmodule.account.controller;

import java.text.DecimalFormat;
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

import nirmalya.aathithya.webmodule.account.model.ContraVoucherModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")
public class ContraVoucherController {

	Logger logger = LoggerFactory.getLogger(ContraVoucherController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Contra Voucher' page
	 *
	 */
	@GetMapping("/add-contra-voucher-master")
	public String contraVoucher(Model model, HttpSession session) {

		logger.info("Method : contraVoucher starts");

		ContraVoucherModel contraVoucher = new ContraVoucherModel();
		try {
			ContraVoucherModel contraVoucherSession = (ContraVoucherModel) session.getAttribute("sContra");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (contraVoucherSession != null) {
				model.addAttribute("contraVoucher", contraVoucherSession);
				session.setAttribute("sContra", null);
			} else {
				model.addAttribute("contraVoucher", contraVoucher);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Contra Voucher Type
		 *
		 */

		try {
			DropDownModel[] cvType = restClient.getForObject(env.getAccountUrl() + "restGetCVType",
					DropDownModel[].class);
			List<DropDownModel> cvTypeList = Arrays.asList(cvType);

			model.addAttribute("cvTypeList", cvTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "restGetCostCenterList",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Bank Name
		 *
		 *//*
			 * 
			 * try { ContraVoucherModel[] bank = restClient.getForObject(env.getAccountUrl()
			 * + "restGetAccountAndBankList", ContraVoucherModel[].class);
			 * List<ContraVoucherModel> bankList = Arrays.asList(bank);
			 * 
			 * model.addAttribute("bankList", bankList);
			 * 
			 * } catch (RestClientException e) { e.printStackTrace(); }
			 */
		/**
		 * get DropDown value for Account Head Type
		 *
		 */

		try {
			DropDownModel[] accountHead = restClient.getForObject(
					env.getAccountUrl() + "restGetAccountHead?type=" + "cashInHand", DropDownModel[].class);
			List<DropDownModel> accountHeadCashList = Arrays.asList(accountHead);

			model.addAttribute("accountHeadCashList", accountHeadCashList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] accountHeadPettyCash = restClient.getForObject(
					env.getAccountUrl() + "restGetAccountHead?type=" + "pettyCash", DropDownModel[].class);
			List<DropDownModel> accountHeadPettyCashList = Arrays.asList(accountHeadPettyCash);

			model.addAttribute("accountHeadPettyCashList", accountHeadPettyCashList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : contraVoucher ends");
		return "account/add-contra-voucher-master";
	}

	/*
	 * post Mapping for Get Issue Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-contra-voucher-master-restGetAccountAndBankList" })
	public @ResponseBody JsonResponse<ContraVoucherModel> getIssueAutocompleteList(Model model, @RequestBody String searchValue, BindingResult result) {
		logger.info("Method : restGetAccountAndBankList starts");
		
		JsonResponse<ContraVoucherModel> res = new JsonResponse<ContraVoucherModel>();
		try {
			res = restClient.getForObject(env.getAccountUrl()+ "restGetAccountAndBankList?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : restGetAccountAndBankList ends");
		return res;
	}
	/*
	 * post Mapping for Get Issue Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-contra-voucher-master-toAccountAndBankList" })
	public @ResponseBody JsonResponse<ContraVoucherModel> toAccountAndBankList(Model model, @RequestBody String searchValue, BindingResult result) {
		logger.info("Method : toAccountAndBankList starts");
		
		JsonResponse<ContraVoucherModel> res = new JsonResponse<ContraVoucherModel>();
		try {
			res = restClient.getForObject(env.getAccountUrl()+ "toAccountAndBankList?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : toAccountAndBankList ends");
		return res;
	}
	/**
	 * Web Controller - Get Branch Name
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-contra-voucher-master-get-branch" })
	public @ResponseBody JsonResponse<Object> getBranchName(Model model, @RequestBody String bank,
			BindingResult result) {
		logger.info("Method : getBranchName starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "restGetBranchName?id=" + bank, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getBranchName ends");
		return res;
	}

	/**
	 * Web Controller - Get Account
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-contra-voucher-master-get-account" })
	public @ResponseBody JsonResponse<Object> getAccount(Model model, @RequestBody String branch,
			BindingResult result) {
		logger.info("Method : getAccount starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "restGetAccountNo?id=" + branch, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getAccount ends");
		return res;
	}

	/**
	 * Web Controller - Add New Contra Voucher
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-contra-voucher-master-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addContraVoucher(@RequestBody ContraVoucherModel contraVoucher,
			Model model, HttpSession session) {
		logger.info("Method : addContraVoucher starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		contraVoucher.setCreatedBy("u0010");
		try {
			res = restClient.postForObject(env.getAccountUrl() + "restAddContraVoucher", contraVoucher,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addContraVoucher ends");
		return res;
	}

	/**
	 * Default 'View Contra Voucher Master' page
	 *
	 */
	@GetMapping("/view-contra-voucher-master")
	public String viewContraVoucher(Model model, HttpSession session) {

		logger.info("Method : viewContraVoucher starts");

		/**
		 * get DropDown value for Contra Voucher Type
		 *
		 */

		try {
			DropDownModel[] cvType = restClient.getForObject(env.getAccountUrl() + "restGetCVType",
					DropDownModel[].class);
			List<DropDownModel> cvTypeList = Arrays.asList(cvType);

			model.addAttribute("cvTypeList", cvTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "restGetCostCenterList",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewContraVoucher ends");
		return "account/view-contra-voucher-master";
	}

	/**
	 * Web Controller - View Contra Voucher Details
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-contra-voucher-master-through-ajax")
	public @ResponseBody DataTableResponse viewContraVoucherThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {

		logger.info("Method : viewContraVoucherThroughAjax starts");

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

			JsonResponse<List<ContraVoucherModel>> jsonResponse = new JsonResponse<List<ContraVoucherModel>>();

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getContraVoucher", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ContraVoucherModel> contraVoucher = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ContraVoucherModel>>() {
					});

			String s = "";
			
			for (ContraVoucherModel m : contraVoucher) {
				byte[] pId = Base64.getEncoder().encode(m.getContraVoucherId().getBytes());

				s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);			
			      DecimalFormat format = new DecimalFormat("0.00"); 
			      m.setAmount(format.format(m.getCvAmount()));
				
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(contraVoucher);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewContraVoucherThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller - Modal View of CONTRA VOUCHER
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-contra-voucher-master-modal" })
	public @ResponseBody JsonResponse<Object> modalContraVoucher(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalContraVoucher starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "getContraVoucherById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalContraVoucher ends");
		return res;
	}

	/**
	 * Web Controller - CONTRA VOUCHER REPORT
	 *
	 */
	@GetMapping("view-contra-voucher-master-generate-report")
	public String generateCVReport(Model model, HttpSession session) {

		logger.info("Method : generateCVReport starts");

		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restClient.getForObject(env.getAccountUrl() + "restGetCostCenterList",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : generateCVReport ends");
		return "account/report-contra-voucher-master";
	}
}
