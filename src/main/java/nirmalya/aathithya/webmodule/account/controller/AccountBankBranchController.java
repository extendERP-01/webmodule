/**
 * 
 */
package nirmalya.aathithya.webmodule.account.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.AccountBankBranchModel;
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
@RequestMapping(value = "account/")
public class AccountBankBranchController {
	
	Logger logger = LoggerFactory.getLogger(AccountBankBranchController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	
	/**
	 * Add Bank Branch
	 */
	@GetMapping("add-bank-branch")
	public String addBankBranch(Model model, HttpSession session) {
		logger.info("Method : AccountBankBranchController addBankBranch starts");
		model.addAttribute("addbankbranch", new AccountBankBranchModel());
		AccountBankBranchModel bankbranchForm = (AccountBankBranchModel) session.getAttribute("bankbranchservice");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (bankbranchForm != null) {
			model.addAttribute("addbankbranch", bankbranchForm);
		} else {
			model.addAttribute("addbankbranch", new AccountBankBranchModel());
		}
		
		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BranchBank = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-bank-list",
					DropDownModel[].class);
			List<DropDownModel> BankList = Arrays.asList(BranchBank);

			model.addAttribute("bankList", BankList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * DROPDOWN STATE
		 *
		 */
		try {
			DropDownModel[] BranchState = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-state-list",
					DropDownModel[].class);
			List<DropDownModel> StateList = Arrays.asList(BranchState);

			model.addAttribute("stateList", StateList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN DISTRICT
		 *
		 */
		try {
			DropDownModel[] BranchDist = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-district-list",
					DropDownModel[].class);
			List<DropDownModel> DistList = Arrays.asList(BranchDist);

			model.addAttribute("distList", DistList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : AccountBankBranchController addBankBranch ends");
		return "account/AddBankBranch";
	}

	/**
	 * View Bank Branch
	 * 
	 */

	@GetMapping("view-bank-branch")
	public String viewBankBranch(Model model, HttpSession session) {

		logger.info("Method : AccountBankBranchController viewBankBranch starts");
		
		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BranchBank = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-bank-list",
					DropDownModel[].class);
			List<DropDownModel> BankList = Arrays.asList(BranchBank);

			model.addAttribute("bankList", BankList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * DROPDOWN STATE
		 *
		 */
		try {
			DropDownModel[] BranchState = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-state-list",
					DropDownModel[].class);
			List<DropDownModel> StateList = Arrays.asList(BranchState);

			model.addAttribute("stateList", StateList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN DISTRICT
		 *
		 */
		try {
			DropDownModel[] BranchDist = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-district-list",
					DropDownModel[].class);
			List<DropDownModel> DistList = Arrays.asList(BranchDist);

			model.addAttribute("distList", DistList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : AccountBankBranchController viewBankBranch end");
		return "account/ViewBankBranch";
	}
	
	/**
	 * Submit Add Bank Branch Details
	 */

	@SuppressWarnings("rawtypes")
	@PostMapping("add-bank-branch")
	public String submitAddBankBranch(@ModelAttribute AccountBankBranchModel addBankBranchDetails, Model model, HttpSession session) {
		logger.info("Method : AccountBankBranchController submitAddBankBranch starts");
		JsonResponse resp = new JsonResponse();
		addBankBranchDetails.setCreatedBy("u0001");
		try {
			resp = restClient.postForObject(env.getAccountUrl() + "rest-submit-bank-branch-details", addBankBranchDetails,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("bankbranchservice", addBankBranchDetails);
			return "redirect:/account/add-bank-branch";
		}
		session.setAttribute("bankbranchservice", null);
		logger.info("Method : AccountBankBranchController submitAddBankBranch end");
		return "redirect:/account/view-bank-branch";
	}

	/**
	 * View Bank Branch Details Ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-branch-through-ajax")
	public @ResponseBody DataTableResponse viewBankBranchThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,
			@RequestParam String param2,
			@RequestParam String param3) {
		logger.info("Method : AccountBankBranchController viewBankBranchThroughAjax starts");

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
		    tableRequest.setParam2(param3);

			JsonResponse<List<AccountBankBranchModel>> jsonResponse = new JsonResponse<List<AccountBankBranchModel>>();

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "rest-view-bank-branch-details-through-ajax",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountBankBranchModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountBankBranchModel>>() {
					});
			String s = "";

			for (AccountBankBranchModel m : form) {

				/**
				 * ENCODE STARTS
				 *
				 */

				byte[] pId = Base64.getEncoder().encode(m.getBranch().getBytes());

				/**
				 * ENCODE ENDS
				 *
				 */
				s = "";
				s = s + "&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "&nbsp;&nbsp;<a href='edit-account-bank-branch-details?id1=" + new String(pId)
						+ "' ><i class='fa fa-edit edit'></i></a>";

				s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='DeleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash trash'></i></a>";

				m.setAction(s);
				s = "";

				if (m.getBranchActive()) {
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
		logger.info("Method : AccountBankBranchController viewBankBranchThroughAjax end");
		return response;
	}

	/**
	 * Delete Bank Branch Record
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-branch-delete")
	public @ResponseBody JsonResponse<Object> deleteBankBranchDeatils(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method : AccountBankBranchController deleteBankBranchDeatils starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));

		// System.out.println("response delete "+id);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getAccountUrl() + "rest-account-bank-branch-delete-details?id=" + index,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("response delete in web controller "+resp);
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : AccountBankBranchController deleteBankBranchDeatils ends");
		return resp;
	}

	/**
	 * Edit Account Bank Branch Details
	 */

	@SuppressWarnings("unchecked")

	@GetMapping("edit-account-bank-branch-details")
	public String editBankBranchDetails(Model model, @RequestParam String id1, HttpSession session) {
		logger.info("Method : AccountBankBranchController editBankBranchDetails starts");
		/**
		 * DECODE STARTS
		 *
		 */
		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());

		String id = (new String(encodeByte));
		/**
		 * DECODE ENDS
		 *
		 */
		model.addAttribute("addbank", new AccountBankBranchModel());
		AccountBankBranchModel editbankbranch = new AccountBankBranchModel();
		JsonResponse<AccountBankBranchModel> jsonResponse = new JsonResponse<AccountBankBranchModel>();

		try {
			jsonResponse = restClient.getForObject(env.getAccountUrl() + "rest-edit-account-bank-branch-details?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		editbankbranch = mapper.convertValue(jsonResponse.getBody(), AccountBankBranchModel.class);

		session.setAttribute("message", "");

		model.addAttribute("addbankbranch", editbankbranch);
		
		
		/**
		 * DROPDOWN BANK
		 *
		 */
		try {
			DropDownModel[] BranchBank = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-bank-list",
					DropDownModel[].class);
			List<DropDownModel> BankList = Arrays.asList(BranchBank);

			model.addAttribute("bankList", BankList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * DROPDOWN STATE
		 *
		 */
		try {
			DropDownModel[] BranchState = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-state-list",
					DropDownModel[].class);
			List<DropDownModel> StateList = Arrays.asList(BranchState);

			model.addAttribute("stateList", StateList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN DISTRICT
		 *
		 */
		try {
			DropDownModel[] BranchDist = restClient.getForObject(env.getAccountUrl() + "rest-account-branch-district-list",
					DropDownModel[].class);
			List<DropDownModel> DistList = Arrays.asList(BranchDist);

			model.addAttribute("distList", DistList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : AccountBankBranchController editBankBranchDetails end");
		return "account/AddBankBranch";
	}

	/**
	 * Modal Account Bank Branch Details
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-bank-branch-modal" })
	public @ResponseBody JsonResponse<Object> modalBankBranchDetails(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : AccountBankBranchController modalBankBranchDetails starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "rest-edit-account-bank-branch-details?id=" + index,
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
		logger.info("Method : AccountBankBranchController modalBankBranchDetails end");
		return res;
	}
	
	/**
	 * DROP DOWN DATA FOR STATE TO DISTRICT NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-bank-branch-state-onchange-district-list" })
	public @ResponseBody JsonResponse<DropDownModel> stateOnchangeDistrictList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :AccountBankBranchController stateOnchangeDistrictList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "rest-account-bank-branch-state-onchange-district-list?proCat=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : AccountBankBranchController stateOnchangeDistrictList ends");
		return res;
	}

}
