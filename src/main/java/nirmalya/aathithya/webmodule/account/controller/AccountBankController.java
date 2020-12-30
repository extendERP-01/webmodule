/**
 *  Nirmalya Labs
 */
package nirmalya.aathithya.webmodule.account.controller;

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

import nirmalya.aathithya.webmodule.account.model.AccountBankModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account/")
public class AccountBankController {

	Logger logger = LoggerFactory.getLogger(AccountBankController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Add Bank
	 */
	@GetMapping("add-bank")
	public String addBank(Model model, HttpSession session) {
		logger.info("Method : AccountBankController addBank starts");
		model.addAttribute("addbank", new AccountBankModel());
		AccountBankModel bankForm = (AccountBankModel) session.getAttribute("bankservice");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (bankForm != null) {
			model.addAttribute("addbank", bankForm);
		} else {
			model.addAttribute("addbank", new AccountBankModel());
		}
		logger.info("Method : AccountBankController addBank ends");
		return "account/AddBank";
	}

	/**
	 * View Bank
	 * 
	 */

	@GetMapping("view-bank")
	public String viewBank(Model model, HttpSession session) {

		logger.info("Method : AccountBankController viewBank starts");
		logger.info("Method : AccountBankController viewBank end");
		return "account/ViewBank";
	}

	/**
	 * Submit Add Bank Details
	 */

	@SuppressWarnings("rawtypes")
	@PostMapping("add-bank")
	public String submitAddBank(@ModelAttribute AccountBankModel addBankDetails, Model model, HttpSession session) {
		logger.info("Method : AccountBankController submitAddBank starts");
		JsonResponse resp = new JsonResponse();
		addBankDetails.setCreatedBy("u0001");
		try {
			resp = restClient.postForObject(env.getAccountUrl() + "rest-submit-bank-details", addBankDetails,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("bankservice", addBankDetails);
			return "redirect:/account/add-bank";
		}
		session.setAttribute("bankservice", null);
		logger.info("Method : AccountBankController submitAddBank end");
		return "redirect:/account/view-bank";
	}

	/**
	 * View Bank Details Ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-through-ajax")
	public @ResponseBody DataTableResponse viewBankThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : AccountBankController viewBankThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			// tableRequest.setParam2(param2);

			JsonResponse<List<AccountBankModel>> jsonResponse = new JsonResponse<List<AccountBankModel>>();

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "rest-view-bank-details-through-ajax",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountBankModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountBankModel>>() {
					});
			String s = "";

			for (AccountBankModel m : form) {

				/**
				 * ENCODE STARTS
				 *
				 */

				byte[] pId = Base64.getEncoder().encode(m.getBank().getBytes());

				/**
				 * ENCODE ENDS
				 *
				 */
				s = "";
				s = s + "&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "&nbsp;&nbsp;<a href='edit-account-bank-details?id1=" + new String(pId)
						+ "' ><i class='fa fa-edit edit'></i></a>";

				s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='DeleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash trash'></i></a>";

				m.setAction(s);
				s = "";

				if (m.getBankActive()) {
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
		logger.info("Method : AccountBankController viewBankThroughAjax end");
		return response;
	}

	/**
	 * Delete Bank Record
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-bank-delete")
	public @ResponseBody JsonResponse<Object> deleteBankDeatils(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method : AccountBankController deleteBankDeatils starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));

		// System.out.println("response delete "+id);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getAccountUrl() + "rest-account-bank-delete-details?id=" + index,
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

		logger.info("Method : AccountBankController deleteBankDeatils ends");
		return resp;
	}

	/**
	 * Edit Account Bank Details
	 */

	@SuppressWarnings("unchecked")

	@GetMapping("edit-account-bank-details")
	public String editBankDetails(Model model, @RequestParam String id1, HttpSession session) {
		logger.info("Method : AccountBankController editBankDetails starts");
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
		model.addAttribute("addbank", new AccountBankModel());
		AccountBankModel editbank = new AccountBankModel();
		JsonResponse<AccountBankModel> jsonResponse = new JsonResponse<AccountBankModel>();

		try {
			jsonResponse = restClient.getForObject(env.getAccountUrl() + "rest-edit-account-bank-details?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		editbank = mapper.convertValue(jsonResponse.getBody(), AccountBankModel.class);

		session.setAttribute("message", "");

		model.addAttribute("addbank", editbank);

		logger.info("Method : AccountBankController editBankDetails end");
		return "account/AddBank";
	}

	/**
	 * Modal Account Bank Details
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-bank-modal" })
	public @ResponseBody JsonResponse<Object> modalBankDetails(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : AccountBankController modalBankDetails starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAccountUrl() + "rest-edit-account-bank-details?id=" + index,
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
		logger.info("Method : AccountBankController modalBankDetails end");
		return res;
	}

}
