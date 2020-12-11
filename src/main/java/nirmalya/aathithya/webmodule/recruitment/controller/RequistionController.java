package nirmalya.aathithya.webmodule.recruitment.controller;

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
import nirmalya.aathithya.webmodule.recruitment.model.AddRequistionModel;

@Controller
@RequestMapping(value = "recruitment")
public class RequistionController {
	Logger logger = LoggerFactory.getLogger(RequistionController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("add-requistion")
	public String addRequistion(Model model, HttpSession session) {

		logger.info("Method : addRequistion starts");

		AddRequistionModel requistion = new AddRequistionModel();
		AddRequistionModel form = (AddRequistionModel) session.getAttribute("sRequistion");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		session.setAttribute("message", "");
		if (form != null) {
			model.addAttribute("requistion", form);
			session.setAttribute("sRequistion", null);

		} else {
			model.addAttribute("requistion", requistion);
		}

		try {
			DropDownModel[] jobCode = restClient.getForObject(env.getRecruitment() + "getjobCodeList",
					DropDownModel[].class);
			List<DropDownModel> jobCodeList = Arrays.asList(jobCode);
			model.addAttribute("jobCodeList", jobCodeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentForDepa",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] hiringManager = restClient.getForObject(env.getRecruitment() + "getHiringManagerForMang",
					DropDownModel[].class);
			List<DropDownModel> hiringManagerList = Arrays.asList(hiringManager);
			model.addAttribute("hiringManagerList", hiringManagerList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addRequistion ends");

		return "recruitment/add-requistion";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-requistion-get-jobtitle" })
	public @ResponseBody JsonResponse<Object> getJobtiteldop(Model model, @RequestBody String jobtitle,
			BindingResult result) {
		logger.info("Method : getjobtitle starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getRecruitment() + "getJobTitleForJobTitle?id=" + jobtitle,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getjobtitle ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-requistion-details")
	public String addRequisition(@ModelAttribute AddRequistionModel struct, Model model, HttpSession session) {

		logger.info("Method : addRequisition strats");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getRecruitment() + "restAddRequistion", struct, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sRequistion", struct);
			return "redirect:/recruitment/add-requistion";
		}

		logger.info("Method : addRequisition ends");

		return "redirect:/recruitment/view-requistion";
	}

	@GetMapping("/view-requistion")
	public String viewRequistion(Model model, HttpSession session) {

		logger.info("Method : viewRequistion starts");
		try {
			DropDownModel[] jobCode = restClient.getForObject(env.getRecruitment() + "getjobCodeList",
					DropDownModel[].class);
			List<DropDownModel> jobCodeList = Arrays.asList(jobCode);
			model.addAttribute("jobCodeList", jobCodeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentForDepa",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewRequistion ends");

		return "recruitment/view-requistion";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-requistion-through-ajax")
	public @ResponseBody DataTableResponse viewStructThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method ; viewStructThroughAjax starts");

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

			JsonResponse<List<AddRequistionModel>> jsonResponse = new JsonResponse<List<AddRequistionModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getRequistionview", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AddRequistionModel> addreq = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AddRequistionModel>>() {
					});

			String s = "";
			for (AddRequistionModel m : addreq) {
				byte[] pId = Base64.getEncoder().encode(m.getRequistionId().getBytes());

				s = s + "<a href='view-requistion-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)' onclick='deleterequistion(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp";
				s += "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewModal(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-search '  style='font-size:20px; color:#0095c6;'></i></a>";

				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(addreq);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method ; viewStructThroughAjax ends");

		return response;
	}
	// Edit

	@SuppressWarnings("unchecked")
	@GetMapping("/view-requistion-edit")
	public String editrequistion(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : edit requistion starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		AddRequistionModel requi = new AddRequistionModel();
		JsonResponse<AddRequistionModel> jsonResponse = new JsonResponse<AddRequistionModel>();

		try {
			DropDownModel[] jobCode = restClient.getForObject(env.getRecruitment() + "getjobCodeList",
					DropDownModel[].class);
			List<DropDownModel> jobCodeList = Arrays.asList(jobCode);
			model.addAttribute("jobCodeList", jobCodeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * try { DropDownModel[] jobTitle = restClient.getForObject(env.getRecruitment()
		 * + "getJobTitleForJobTitle", DropDownModel[].class); List<DropDownModel>
		 * jobTitleList = Arrays.asList(jobTitle); model.addAttribute("jobTitleList",
		 * jobTitleList); } catch (RestClientException e) { e.printStackTrace(); }
		 */

		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentForDepa",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] hiringManager = restClient.getForObject(env.getRecruitment() + "getHiringManagerForMang",
					DropDownModel[].class);
			List<DropDownModel> hiringManagerList = Arrays.asList(hiringManager);
			model.addAttribute("hiringManagerList", hiringManagerList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {

			jsonResponse = restClient.getForObject(env.getRecruitment() + "getrequiById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		requi = mapper.convertValue(jsonResponse.getBody(), AddRequistionModel.class);
		session.setAttribute("message", "");

		model.addAttribute("requistion", requi);

		logger.info("Method :edit requistion end");

		return "recruitment/add-requistion";

	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-requistion-delete")
	public @ResponseBody JsonResponse<Object> deleterequistion(Model model, @RequestParam String id,
			HttpSession session) {
		logger.info("Method : delectRequistion starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String createdBy = "";

		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resp = restClient.getForObject(
					env.getRecruitment() + "deleteRequistion?id=" + index + "&createdBy=" + createdBy,
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

		logger.info("Method :  delectRequistion ends");
		return resp;
	}

	/*
	 * For Modal certification
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-requistion-modalView" })
	public @ResponseBody JsonResponse<List<AddRequistionModel>> modalRequistion(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalRequistion starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<AddRequistionModel>> response = new JsonResponse<List<AddRequistionModel>>();
		try {
			response = restClient.getForObject(env.getRecruitment() + "getreqmodalById?reqId=" + id,
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
		logger.info("Method : modalRequistion  ends ");
		return response;
	}

}