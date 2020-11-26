/**
 * 
 */
package nirmalya.aathithya.webmodule.sales.controller;

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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.SalesCustomerModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = { "sales/" })
public class SalesCustomerController {
	Logger logger = LoggerFactory.getLogger(SalesCustomerController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new customer Details
	 *
	 */
	@GetMapping(value = { "add-customer" })
	public String addCustomer(Model model, HttpSession session) {
		logger.info("Method : addCustomer starts");
		SalesCustomerModel salesCustomerModel = new SalesCustomerModel();
		SalesCustomerModel sCust = (SalesCustomerModel) session.getAttribute("salescustomer");
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

			String cId = sCust.getCustomerCountry();

			try {
				DropDownModel[] dropDownModel = restTemplate.getForObject(
						environmentVaribles.getSalesUrl() + "rest-get-country-list-master", DropDownModel[].class);
				List<DropDownModel> countryList = Arrays.asList(dropDownModel);
				model.addAttribute("countryList", countryList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}

			System.out.println("countryid :" + cId);
			sCust.setCustomerCountry(null);
		}

		session.setAttribute("message", "");

		/*
		 * 
		 * dropDown for country
		 *
		 *
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "rest-get-country-list-master", DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		session.setAttribute("message", "");

		if (sCust != null) {
			model.addAttribute("salesCustomerModel", sCust);
		} else {
			model.addAttribute("salesCustomerModel", salesCustomerModel);
		}
		logger.info("Method : addCustomer ends");
		return "sales/add-customer";
	}

	/*
	 * post Mapping for add Customer
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-customer" })
	public String addNewCustomer(@ModelAttribute SalesCustomerModel salesCustomerModel, Model model,
			HttpSession session) {
		logger.info("Method : addNewCustomer starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			salesCustomerModel.setCustomerCreatedBy(session.getAttribute("USER_ID").toString());
			jsonResponse = restTemplate.postForObject(environmentVaribles.getSalesUrl() + "rest-addnew-customer",
					salesCustomerModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("salescustomer", salesCustomerModel);
			return "redirect:add-customer";
		}
		logger.info("Method : addNewCustomer ends");
		return "redirect:view-customer";
	}

	/*
	 * 
	 * dropDown For State
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("add-customer-get-state-list-master")
	public @ResponseBody JsonResponse<DropDownModel> getStateList(Model model, @RequestBody String stateList,
			BindingResult result) {
		logger.info("Method : getStateList starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getSalesUrl() + "get-state-list-master?id=" + stateList,
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
		logger.info("Method : getStateList ends");
		return res;
	}

	/*
	 * 
	 * dropDown For District
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("add-customer-get-district-list-master")
	public @ResponseBody JsonResponse<DropDownModel> getDistrictList(Model model, @RequestBody String districtList,
			BindingResult result) {
		logger.info("Method : getDistrictList starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "get-district-list-master?id=" + districtList,
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
		logger.info("Method : getDistrictList ends");
		return res;
	}

	/*
	 * 
	 * GetMApping For Listing Customer
	 * 
	 * 
	 */
	@GetMapping(value = { "view-customer" })
	public String viewCustomer(Model model) {
		logger.info("Method : viewCustomer starts");

		/*
		 * dropDown of stateName for search param2
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getSalesUrl() + "param-stateName", DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown of stateName for search param
		 */
		try {
			DropDownModel[] dropDownModel2 = restTemplate
					.getForObject(environmentVaribles.getSalesUrl() + "param-districtName", DropDownModel[].class);
			List<DropDownModel> districtList = Arrays.asList(dropDownModel2);
			model.addAttribute("districtList", districtList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		JsonResponse<Object> customer = new JsonResponse<Object>();
		model.addAttribute("customer", customer);
		logger.info("Method : viewCustomer ends");
		return "sales/view-customer";
	}

	/*
	 * view Through Ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-customer-throughAjax" })
	public @ResponseBody DataTableResponse viewCustomer(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewCustomer(through ajax) starts");
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
			tableRequest.setParam3(param4);
			tableRequest.setParam3(param5);
			JsonResponse<List<SalesCustomerModel>> jsonResponse = new JsonResponse<List<SalesCustomerModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getSalesUrl() + "get-all-customer",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<SalesCustomerModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesCustomerModel>>() {
					});
			String s = "";
			for (SalesCustomerModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getCustomerId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-customer?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deleteCustomer(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getCustomerActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewCustomer(through ajax) ends");
		return response;
	}

	/**
	 * View selected Customer in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-customer-model" })
	public @ResponseBody JsonResponse<SalesCustomerModel> modelView(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelView starts");

		JsonResponse<SalesCustomerModel> resp = new JsonResponse<SalesCustomerModel>();

		try {
			resp = restTemplate.getForObject(environmentVaribles.getSalesUrl() + "view-modal?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method : modelView ends");
		System.out.println("Response :" + resp);
		return resp;
	}

	/*
	 * 
	 * GetMapping for delete Customer
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-customer" })
	public @ResponseBody JsonResponse<Object> deleteCustomer(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteCustomer starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String createdBy = "u0001";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "delete-customer?id=" + id + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteCustomer ends");
		System.out.println("RESPonse: " + resp);
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMApping for Edit Customer
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-customer" })
	public String editCustomer(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editCustomer starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		SalesCustomerModel salesCustomerModel = new SalesCustomerModel();
		JsonResponse<SalesCustomerModel> jsonResponse = new JsonResponse<SalesCustomerModel>();

		try {

			jsonResponse = restTemplate.getForObject(environmentVaribles.getSalesUrl() + "edit-Customer?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		/*
		 * 
		 * dropDown for country
		 *
		 *
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(
					environmentVaribles.getSalesUrl() + "rest-get-country-list-master", DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		salesCustomerModel = mapper.convertValue(jsonResponse.getBody(), SalesCustomerModel.class);

		/*
		 * 
		 * dropDown for State
		 * 
		 * 
		 */

		try {
			DropDownModel[] state = restTemplate.getForObject(environmentVaribles.getSalesUrl()
					+ "rest-get-state-list-master?id=" + salesCustomerModel.getCustomerCountry(),
					DropDownModel[].class);
			List<DropDownModel> StateList = Arrays.asList(state);
			model.addAttribute("stateList", StateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * 
		 * dropDown for district
		 *
		 *
		 */
		try {
			DropDownModel[] district = restTemplate.getForObject(environmentVaribles.getSalesUrl()
					+ "rest-get-district-list-master?id=" + salesCustomerModel.getCustomerState(),
					DropDownModel[].class);
			List<DropDownModel> DistrictList = Arrays.asList(district);
			model.addAttribute("districtList", DistrictList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		session.setAttribute("message", "");
		model.addAttribute("salesCustomerModel", salesCustomerModel);

		logger.info("Method : editCustomer ends");
		return "sales/add-customer";
	}

}