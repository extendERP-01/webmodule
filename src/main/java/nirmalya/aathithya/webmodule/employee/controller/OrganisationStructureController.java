package nirmalya.aathithya.webmodule.employee.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
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

import nirmalya.aathithya.webmodule.account.model.AccountBankAccountModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.OrganisationStructureModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "employee/")
public class OrganisationStructureController {
	Logger logger = LoggerFactory.getLogger(OrganisationStructureController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-parent", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addParent(@ModelAttribute OrganisationStructureModel dataForParent, Model model,
			HttpSession session) {
		JsonResponse<Object> response = new JsonResponse<Object>();
		logger.info("Method : addParent function starts");
		try {
			response = restClient.postForObject(env.getEmployeeUrl() + "restAddParent", dataForParent,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = response.getMessage();

		if (message != null && message != "") {

		} else {
			response.setMessage("Success");
		}

		System.out.println("response data" + response);
		logger.info("Method : addParent function Ends");
		return response;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-child", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addChild(@ModelAttribute OrganisationStructureModel dataForChild, Model model,
			HttpSession session) {
		JsonResponse<Object> response = new JsonResponse<Object>();
		logger.info("Method : addChild function starts");
		// System.out.println("posted addUserRole data" + checkBoxData);
		try {

			// for(UserRoleModel i:checkBoxData) {
			// i.setCreatedBy("User001");
			//
			// }
			response = restClient.postForObject(env.getEmployeeUrl() + "restAddChild", dataForChild, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = response.getMessage();

		if (message != null && message != "") {

		} else {
			response.setMessage("Success");
		}

		System.out.println("response data" + response);
		logger.info("Method : addChild function Ends");
		return response;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	@GetMapping("view-organizational-structure")
	public String viewAccountTree(Model model, HttpSession session) {

		logger.info("Method : viewAccountTree  starts");

		JsonResponse<List<OrganisationStructureModel>> respTblact = new JsonResponse<List<OrganisationStructureModel>>();
		List<OrganisationStructureModel> treeData = new ArrayList<OrganisationStructureModel>();

		try {
			respTblact = restClient.getForObject(
					env.getEmployeeUrl() + "getAccountTreeDetails?Action=" + "getAccountTreeDetails",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForTblact = respTblact.getMessage();

		if (messageForTblact != null || messageForTblact != "") {
			model.addAttribute("message", messageForTblact);
		}

		ObjectMapper mapper4 = new ObjectMapper();

		treeData = mapper4.convertValue(respTblact.getBody(), new TypeReference<List<OrganisationStructureModel>>() {
		});
		model.addAttribute("treeData1", treeData);

		logger.info("Method : viewAccountTree  end");
		return "employee/ViewAccountTree";
	}

	/**
	 * Add Bank Account
	 */
	@GetMapping("add-bank-account")
	public String addBankAccount(Model model, HttpSession session) {
		logger.info("Method : AccountBankAccountController addBankAccount starts");
		model.addAttribute("addbankaccount", new AccountBankAccountModel());
		AccountBankAccountModel bankAccountForm = (AccountBankAccountModel) session.getAttribute("bankaccountservice");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (bankAccountForm != null) {
			model.addAttribute("addbankaccount", bankAccountForm);
		} else {
			model.addAttribute("addbankaccount", new AccountBankAccountModel());
		}

		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BankAccount = restClient.getForObject(env.getEmployeeUrl() + "rest-bank-account-list",
					DropDownModel[].class);
			List<DropDownModel> BankAccountList = Arrays.asList(BankAccount);

			model.addAttribute("bankList", BankAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN BRANCH
		 *
		 */
		try {
			DropDownModel[] BankBranchAccount = restClient
					.getForObject(env.getEmployeeUrl() + "rest-branch-account-list", DropDownModel[].class);
			List<DropDownModel> BranchAccountList = Arrays.asList(BankBranchAccount);

			model.addAttribute("branchList", BranchAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : AccountBankAccountController addBankAccount ends");
		return "employee/AddBankAccount";
	}

	/**
	 * View Bank
	 * 
	 */

	@GetMapping("view-bank-account")
	public String viewBankAccount(Model model, HttpSession session) {

		logger.info("Method : AccountBankAccountController viewBankAccount starts");

		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BankAccount = restClient.getForObject(env.getEmployeeUrl() + "rest-bank-account-list",
					DropDownModel[].class);
			List<DropDownModel> BankAccountList = Arrays.asList(BankAccount);

			model.addAttribute("bankList", BankAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN BRANCH
		 *
		 */
		try {
			DropDownModel[] BankBranchAccount = restClient
					.getForObject(env.getEmployeeUrl() + "rest-branch-account-list", DropDownModel[].class);
			List<DropDownModel> BranchAccountList = Arrays.asList(BankBranchAccount);

			model.addAttribute("branchList", BranchAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : AccountBankAccountController viewBankAccount end");
		return "employee/ViewBankAccount";
	}

	/**
	 * Submit Add Bank Account Details
	 */

	@SuppressWarnings("rawtypes")
	@PostMapping("add-bank-account")
	public String submitAddBankAccount(@ModelAttribute AccountBankAccountModel addBankAccountDetails, Model model,
			HttpSession session) {
		logger.info("Method : AccountBankAccountController submitAddBankAccount starts");
		JsonResponse resp = new JsonResponse();
		addBankAccountDetails.setCreatedBy("u0001");
		try {
			resp = restClient.postForObject(env.getEmployeeUrl() + "rest-submit-bank-account-details",
					addBankAccountDetails, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("bankaccountservice", addBankAccountDetails);
			return "redirect:/account/add-bank-account";
		}
		session.setAttribute("bankaccountservice", null);
		logger.info("Method : AccountBankAccountController submitAddBankAccount end");
		return "redirect:/employee/view-bank-account";
	}

	/**
	 * View Bank Account Details Ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-account-through-ajax")
	public @ResponseBody DataTableResponse viewBankAccountThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : AccountBankAccountController viewBankAccountThroughAjax starts");

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

			JsonResponse<List<AccountBankAccountModel>> jsonResponse = new JsonResponse<List<AccountBankAccountModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "rest-view-bank-account-details-through-ajax",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountBankAccountModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountBankAccountModel>>() {
					});
			String s = "";

			for (AccountBankAccountModel m : form) {

				/**
				 * ENCODE STARTS
				 *
				 */

				byte[] pId = Base64.getEncoder().encode(m.getBranch().getBytes());
				byte[] pId1 = Base64.getEncoder().encode(m.getAccountNumber().getBytes());

				/**
				 * ENCODE ENDS
				 *
				 */
				s = "";
				s = s + "&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ m.getBranch() + ',' + m.getAccountNumber() + "\")'><i class='fa fa-search search' ></i></a>";
				s = s + "&nbsp;&nbsp;<a href='edit-account-bank-account-details?id1=" + new String(pId) + "&id2="
						+ new String(pId1) + "' ><i class='fa fa-edit edit'></i></a>";

				s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='DeleteItem(\"" + new String(pId) + ','
						+ new String(pId1) + "\")'><i class='fa fa-trash trash'></i></a>";

				m.setAction(s);
				s = "";

				if (m.getAccountActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : AccountBankAccountController viewBankAccountThroughAjax end");
		return response;
	}

	/**
	 * Delete Bank Account Record
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-account-delete")
	public @ResponseBody JsonResponse<Object> deleteBankAccountDeatils(Model model, @RequestParam String id,
			@RequestParam String id1, HttpSession session) {

		logger.info("Method : AccountBankAccountController deleteBankAccountDeatils starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(id1.getBytes());
		String br = (new String(encodeByte));
		String ac = (new String(encodeByte1));

		System.out.println("response delete " + br);
		System.out.println("response delete " + ac);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "rest-account-bank-account-delete-details?br=" + br + "&ac=" + ac,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println("response delete in web controller " + resp);
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : AccountBankAccountController deleteBankAccountDeatils ends");
		return resp;
	}

	/**
	 * Edit Account Bank Account Details
	 */

	@SuppressWarnings("unchecked")

	@GetMapping("edit-account-bank-account-details")
	public String editBankBranchDetails(Model model, @RequestParam String id1, @RequestParam String id2,
			HttpSession session) {
		logger.info("Method : AccountBankAccountController editBankAccountDetails starts");
		/**
		 * DECODE STARTS
		 *
		 */
		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());

		byte[] encodeByte1 = Base64.getDecoder().decode(id2.getBytes());

		String br = (new String(encodeByte));
		String ac = (new String(encodeByte1));

		// String id = (new String(encodeByte));
		/**
		 * DECODE ENDS
		 *
		 */
		model.addAttribute("addbankaccount", new AccountBankAccountModel());
		AccountBankAccountModel editbankaccount = new AccountBankAccountModel();
		JsonResponse<AccountBankAccountModel> jsonResponse = new JsonResponse<AccountBankAccountModel>();

		try {
			jsonResponse = restClient.getForObject(
					env.getEmployeeUrl() + "rest-edit-account-bank-account-details?br=" + br + "&ac=" + ac,
					JsonResponse.class);

		} catch (RestClientException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		editbankaccount = mapper.convertValue(jsonResponse.getBody(), AccountBankAccountModel.class);

		session.setAttribute("message", "");

		model.addAttribute("addbankaccount", editbankaccount);

		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BankAccount = restClient.getForObject(env.getEmployeeUrl() + "rest-bank-account-list",
					DropDownModel[].class);
			List<DropDownModel> BankAccountList = Arrays.asList(BankAccount);

			model.addAttribute("bankList", BankAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN BRANCH
		 *
		 */
		try {
			DropDownModel[] BankBranchAccount = restClient
					.getForObject(env.getEmployeeUrl() + "rest-branch-account-list", DropDownModel[].class);
			List<DropDownModel> BranchAccountList = Arrays.asList(BankBranchAccount);

			model.addAttribute("branchList", BranchAccountList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : AccountBankAccountController editBankAccountDetails end");
		return "employee/AddBankAccount";
	}

	/**
	 * Modal Account Bank Account Details
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-bank-account-modal" })
	public @ResponseBody JsonResponse<Object> modalBankAccountDetails(Model model,
			@RequestBody AccountBankAccountModel obj, BindingResult result) {
		logger.info("Method : AccountBankAccountController modalBankAccountDetails starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-edit-account-bank-account-details?br="
					+ obj.getBranch() + "&ac=" + obj.getAccountNumber(), JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : AccountBankAccountController modalBankAccountDetails end");
		return res;
	}

	/**
	 * DROP DOWN DATA FOR BANK TO BRANCH NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-bank-branch-bank-onchange-branch-list" })
	public @ResponseBody JsonResponse<DropDownModel> bankOnchangeBranchList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :AccountBankBranchController bankOnchangeBranchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "rest-account-bank-branch-bank-onchange-branch-list?proCat=" + index,
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

		logger.info("Method : AccountBankBranchController bankOnchangeBranchList ends");
		return res;
	}
}
