package nirmalya.aathithya.webmodule.recruitment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.recruitment.model.QuestionTypeModel;

@Controller
@RequestMapping(value = "recruitment")
public class QuestionTypeController {

	Logger logger = LoggerFactory.getLogger(QuestionTypeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	
	@GetMapping("add-questiontype")
	public String addQuestiontype(Model model, HttpSession session) {

		logger.info("Method : addQuestiontype starts");

		QuestionTypeModel queAssign = new QuestionTypeModel();
		QuestionTypeModel sessionqueAssign = (QuestionTypeModel) session
				.getAttribute("queAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionqueAssign != null) {
			model.addAttribute("queAssign", sessionqueAssign);
			session.setAttribute("queAssign", null);
		} else {
			model.addAttribute("queAssign", queAssign);
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Specific = restClient.getForObject(env.getRecruitment() + "getSpecificList",
					DropDownModel[].class);
			List<DropDownModel> SpecificList = Arrays.asList(Specific);
			model.addAttribute("SpecificList", SpecificList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Question = restClient.getForObject(env.getRecruitment() + "getQuestionList",
					DropDownModel[].class);
			List<DropDownModel> QuestionList = Arrays.asList(Question);
			model.addAttribute("QuestionList", QuestionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	logger.info("Method : addQuestiontype ends");
	return "recruitment/add-questiontype";

	}
	
	/*
	 * post mapping 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-questiontype-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addQuestion(
			@RequestBody List<QuestionTypeModel> QuestionTypeModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addQuestion function starts");
System.out.println(QuestionTypeModel);
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (QuestionTypeModel r : QuestionTypeModel) {

				r.setCreatedBy(userId);
				

			}

			res = restClient.postForObject(env.getRecruitment() + "restQuestionType", QuestionTypeModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addQuestion function Ends");
		return res;
	}

	/*
	 * Get Mapping view 
	 */
	@GetMapping("view-questiontype")
	public String viewQuestionType(Model model, HttpSession session) {

		logger.info("Method : viewQuestionType starts");

		try {
			DropDownModel[] Specific = restClient.getForObject(env.getRecruitment() + "getSpecificList",
					DropDownModel[].class);
			List<DropDownModel> SpecificList = Arrays.asList(Specific);
			model.addAttribute("SpecificList", SpecificList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Question = restClient.getForObject(env.getRecruitment() + "getQuestionList",
					DropDownModel[].class);
			List<DropDownModel> QuestionList = Arrays.asList(Question);
			model.addAttribute("QuestionList", QuestionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		logger.info("Method : viewQuestionType ends");

		return "recruitment/view-questiontype";
	}
	/*
	 * For view for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-questiontype-through-ajax")
	public @ResponseBody DataTableResponse viewQuestiondependent(Model model, HttpServletRequest request,@RequestParam String param1) {

		logger.info("Method : viewQuestiondependent statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<QuestionTypeModel>> jsonResponse = new JsonResponse<List<QuestionTypeModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getAssignQueDetails",tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<QuestionTypeModel> assignQue = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<QuestionTypeModel>>() {
					});
System.out.println("assignQue  " + assignQue);
			String s = "";

			for (QuestionTypeModel m : assignQue) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getSpeTypeId().getBytes());

				s = s + "<a href='view-questiontype-edit?speTypeId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				 

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignQue);

		} catch (Exception e) { 
			e.printStackTrace();
		}

		logger.info("Method : viewQuestiondependent ends");

		return response;
	}

	
	/*
	 * for Edit assign 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-questiontype-edit")
	public String editQuedependent(Model model, @RequestParam("speTypeId") String encodeId, HttpSession session) {

		logger.info("Method :editQuedependent starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<QuestionTypeModel>> response = new JsonResponse<List<QuestionTypeModel>>();
		try {
   System.out.println(id);
			response = restClient.getForObject(
					env.getRecruitment() + "getQueDepndById?speTypeId=" + id, JsonResponse.class);

			
			ObjectMapper mapper = new ObjectMapper();

			List<QuestionTypeModel> dpndQue = mapper.convertValue(response.getBody(),
					new TypeReference<List<QuestionTypeModel>>() {
					});
			if (dpndQue != null) {
				dpndQue.get(0).setEditId("edit");
			}
		System.out.println(dpndQue);
			model.addAttribute("queAssign", dpndQue);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Specific = restClient.getForObject(env.getRecruitment() + "getSpecificList",
					DropDownModel[].class);
			List<DropDownModel> SpecificList = Arrays.asList(Specific);
			model.addAttribute("SpecificList", SpecificList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Question = restClient.getForObject(env.getRecruitment() + "getQuestionList",
					DropDownModel[].class);
			List<DropDownModel> QuestionList = Arrays.asList(Question);
			model.addAttribute("QuestionList", QuestionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : editQuedependent ends");

		return "recruitment/add-questiontype";
	}
	
	/*
	 * For Modal certification 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-questiontype-modalView" })
	public @ResponseBody JsonResponse<List<QuestionTypeModel>> modalQuestionEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalQuestionEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<QuestionTypeModel>> response = new JsonResponse<List<QuestionTypeModel>>();
		try {
			response = restClient.getForObject(env.getRecruitment() + "getQueDepndById?speTypeId=" + id,
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
		logger.info("Method : modalQuestionEdu  ends ");
		return response;
	}
	
	

}



