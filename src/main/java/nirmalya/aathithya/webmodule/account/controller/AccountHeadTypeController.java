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

import nirmalya.aathithya.webmodule.account.model.AccountHeadTypeModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "account")
public class AccountHeadTypeController {

	Logger logger = LoggerFactory.getLogger(AccountHeadTypeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for add account head
	 */
	@GetMapping("/add-account-head-type")
	public String addAccountHead(Model model, HttpSession session) {

		logger.info("Method : addAccountHead starts");

		AccountHeadTypeModel accountHeadTypeModel = new AccountHeadTypeModel();
		AccountHeadTypeModel sessionAccountHeadTypeModel = (AccountHeadTypeModel) session
				.getAttribute("sessionAccountHeadTypeModel");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionAccountHeadTypeModel != null) {
			model.addAttribute("accountHeadTypeModel", sessionAccountHeadTypeModel);
			session.setAttribute("sessionAccountHeadTypeModel", null);
		} else {
			model.addAttribute("accountHeadTypeModel", accountHeadTypeModel);
		}

		logger.info("Method : addAccountHead ends");

		return "account/add-account-head-type";
	}

	/*
	 * Post Mapping for adding new account head
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-account-head-type")
	public String addAccountType(@ModelAttribute AccountHeadTypeModel accountHeadTypeModel, Model model,
			HttpSession session) {

		logger.info("Method : addAccountType starts");
		accountHeadTypeModel.setCreatedBy("DJ");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getAccountUrl() + "restAddAccountHead", accountHeadTypeModel,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionAccountHeadTypeModel", accountHeadTypeModel);
			return "redirect:/account/add-account-head-type";
		}
		logger.info("Method : addAccountType ends");

		return "redirect:/account/view-account-head-type";
	}

	/*
	 * Get Mapping View account head
	 */
	@GetMapping("/view-account-head-type")
	public String listAccountHead(Model model, HttpSession session) {

		logger.info("Method : listAccountHead starts");

		logger.info("Method : listAccountHead ends");

		return "account/list-account-head-type";
	}

	/*
	 * For view account head for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-head-type-ThroughAjax")
	public @ResponseBody DataTableResponse viewAccountHeadThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewThemeThrowAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<AccountHeadTypeModel>> jsonResponse = new JsonResponse<List<AccountHeadTypeModel>>();

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllAccountHead", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountHeadTypeModel> accountHeadTypeModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountHeadTypeModel>>() {
					});

			String s = "";

			for (AccountHeadTypeModel m : accountHeadTypeModel) {

				byte[] encodeId = Base64.getEncoder().encode(m.getAccountHeadTypeId().getBytes());
				s = s + "<a href='view-account-head-type-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteAccountHead(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(accountHeadTypeModel);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method :viewAccountHeadThroughAjax  Theme ends");

		return response;
	}

	/*
	 * for Edit account head
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-head-type-edit")
	public String editAccountType(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editAccountType starts");

		AccountHeadTypeModel accountHeadTypeModel = new AccountHeadTypeModel();
		JsonResponse<AccountHeadTypeModel> jsonResponse = new JsonResponse<AccountHeadTypeModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getAccountUrl() + "getAccountById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		accountHeadTypeModel = mapper.convertValue(jsonResponse.getBody(), AccountHeadTypeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("accountHeadTypeModel", accountHeadTypeModel);

		logger.info("Method : editAccountType ends");

		return "account/add-account-head-type";
	}

	/*
	 * For Delete account head
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-head-type-delete")
	public @ResponseBody JsonResponse<Object> deleteAccountHead(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteAccountHead ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "DJ";
		try {
			resp = restClient.getForObject(
					env.getAccountUrl() + "deleteAccountById?id=" + id1 + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteAccountHead ends");
		return resp;
	}

	/*
	 * For Modal view of account head type
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-account-head-type-modalView" })
	public @ResponseBody JsonResponse<Object> modalAccountHead(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalAccountHead ends");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getAccountUrl() + "getAccountById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalAccountHead ends");

		return res;
	}

}
