package nirmalya.aathithya.webmodule.recruitment.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.recruitment.model.QuestionModel;

@Controller
@RequestMapping(value = "recruitment")
public class QuestionController {
	Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/add-question")
	public String defaultStudentDetails(Model model, HttpSession session) {

		logger.info("Method : defaultQuestionType starts");
		QuestionModel question = new QuestionModel();
		try {
			QuestionModel questionSession = (QuestionModel) session.getAttribute("squestion");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (questionSession != null) {
				model.addAttribute("question", questionSession);
				session.setAttribute("squestion", null);
			} else {
				model.addAttribute("question", question);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : defaultQuestionType ends");
		return "recruitment/add-question";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("add-question")
	public String addQuestion(@ModelAttribute QuestionModel question, Model model, HttpSession session) {

		logger.info("Method : addQuestion starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			question.setQuestionCreatedBy(userId);
			resp = restClient.postForObject(env.getRecruitment() + "restAddQuestion", question, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("squestion", question);
			return "redirect:/recruitment/add-question";
		}

		logger.info("Method : addQuestion ends");

		return "redirect:/recruitment/view-question";
	}
	
	/**
	 * Default 'View Question Type' page
	 *
	 */
	@GetMapping("/view-question")
	public String viewSpecific(Model model, HttpSession session) {
		
		logger.info("Method : viewQuestion starts");
		
		logger.info("Method : viewQuestion ends");
		return "recruitment/view-question";
	}
	/**
	 * Web Controller - View Specific Details Through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-question-through-ajax")
	public @ResponseBody DataTableResponse viewSpecificTypeThroughAjax(Model model, HttpServletRequest request,@RequestParam String param1) {
		
		logger.info("Method : viewQuestionThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			
			JsonResponse<List<QuestionModel>> jsonResponse = new JsonResponse<List<QuestionModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getQuestion",
					tableRequest,JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<QuestionModel> question = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<QuestionModel>>() {
					});

			String s = "";
			
			for (QuestionModel m : question) {
				byte[] pId = Base64.getEncoder().encode(m.getQuestionId().getBytes());
				
				s = s + "<a href='edit-question?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+"<a href='javascript:void(0)' onclick='deleteQuestion(\"" + new String(pId) 
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";
				
					
				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(question);
            
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewQuestionThroughAjax ends");
		return response;
	}
	/**
	 * Web Controller - Edit Program
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("edit-question")
	public String editProgram(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editQuestion starts");
		
		byte[] encodeByte=Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		System.out.println(id);
		QuestionModel question = new QuestionModel();
		JsonResponse<QuestionModel> jsonResponse = new JsonResponse<QuestionModel>();

		try {
			
			jsonResponse = restClient.getForObject(env.getRecruitment() + "getQuestionById?id=" + id,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		question = mapper.convertValue(jsonResponse.getBody(), QuestionModel.class);
		session.setAttribute("message", "");
		
		model.addAttribute("question", question);

		logger.info("Method : editQuestion ends");
		
		return "recruitment/add-question";
	}
	/**
	 * Web Controller - Delete Program
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-question-delete")
	public @ResponseBody JsonResponse<Object> deleteQuestion(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : deleteQuestion starts");
		byte[] encodeByte=Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId ="";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getRecruitment() + "deleteQuestionById?id=" + index +"&createdBy=" + userId, JsonResponse.class);

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
		logger.info("Method : deleteQuestion ends");
		
		return resp;
	}
}
