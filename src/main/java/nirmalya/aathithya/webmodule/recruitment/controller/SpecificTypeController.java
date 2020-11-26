package nirmalya.aathithya.webmodule.recruitment.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.recruitment.model.SpecificTypeModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Base64;

@Controller
@RequestMapping(value = "recruitment")
public class SpecificTypeController {
	Logger logger = LoggerFactory.getLogger(SpecificTypeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/add-specifictype")
	public String defaultStudentDetails(Model model, HttpSession session) {

		logger.info("Method : defaultSpecificType starts");
		SpecificTypeModel specific = new SpecificTypeModel();
		try {
			SpecificTypeModel specificSession = (SpecificTypeModel) session.getAttribute("sspecific");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (specificSession != null) {
				model.addAttribute("specific", specificSession);
				session.setAttribute("sspecific", null);
			} else {
				model.addAttribute("specific", specific);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : defaultSpecificType ends");
		return "recruitment/add-specifictype";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("add-specifictype")
	public String addSpecific(@ModelAttribute SpecificTypeModel specific, Model model, HttpSession session) {

		logger.info("Method : addSpecifictype starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			specific.setSpecificCreatedBy(userId);
			resp = restClient.postForObject(env.getRecruitment() + "restAddSpecific", specific, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sspecific", specific);
			return "redirect:/recruitment/add-specifictype";
		}

		logger.info("Method : addSpecifictype ends");

		return "redirect:/recruitment/view-specifictype";
	}

	/**
	 * Default 'View Specific Type' page
	 *
	 */
	@GetMapping("/view-specifictype")
	public String viewSpecific(Model model, HttpSession session) {

		logger.info("Method : viewSpecifictype starts");

		logger.info("Method : viewSpecifictype ends");
		return "recruitment/view-specifictype";
	}

	/**
	 * Web Controller - View Specific Details Through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-specifictype-through-ajax")
	public @ResponseBody DataTableResponse viewSpecificTypeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewSpecificTypeThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<SpecificTypeModel>> jsonResponse = new JsonResponse<List<SpecificTypeModel>>();

			jsonResponse = restClient.getForObject(env.getRecruitment() + "getSpecific", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SpecificTypeModel> specific = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SpecificTypeModel>>() {
					});

			String s = "";

			for (SpecificTypeModel m : specific) {
				byte[] pId = Base64.getEncoder().encode(m.getSpecificId().getBytes());

				s = s + "<a href='edit-specifictype?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)' onclick='deleteSpecific(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";

				m.setAction(s);
				s = "";
				if(m.getSpecificActive()) {
					m.setStatusName("Active");
				}else {
					m.setStatusName("InActive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(specific);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSpecificTypeThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller - Edit Program
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("edit-specifictype")
	public String editProgram(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editSpecific starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		SpecificTypeModel specific = new SpecificTypeModel();
		JsonResponse<SpecificTypeModel> jsonResponse = new JsonResponse<SpecificTypeModel>();

		try {

			jsonResponse = restClient.getForObject(env.getRecruitment() + "getSpecificById?id=" + id,
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

		specific = mapper.convertValue(jsonResponse.getBody(), SpecificTypeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("specific", specific);

		logger.info("Method : editSpecific ends");

		return "recruitment/add-specifictype";
	}

	/**
	 * Web Controller - Delete Program
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-specifictype-delete")
	public @ResponseBody JsonResponse<Object> deleteSpecific(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method : deleteSpecific starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resp = restClient.getForObject(
					env.getRecruitment() + "deleteSpecificById?id=" + index + "&createdBy=" + userId,
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
		logger.info("Method : deleteSpecific ends");

		return resp;
	}
}
