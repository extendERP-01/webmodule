
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.recruitment.model.TrainingCreationModel;
import nirmalya.aathithya.webmodule.recruitment.model.TrainingCreationModel;
import nirmalya.aathithya.webmodule.recruitment.model.TrainingCreationModel;

@Controller

@RequestMapping(value = "recruitment")
public class TrainingCreationController {
	Logger logger = LoggerFactory.getLogger(TrainingCreationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-training-creation")
	public String addTrainingCreation(Model model, HttpSession session) {

		logger.info("Method : addTrainingCreation starts");

		/**
		 * JObCreation MASTER MODEL
		 *
		 */
		TrainingCreationModel training = new TrainingCreationModel();
		try {
			TrainingCreationModel trainingSession = (TrainingCreationModel) session.getAttribute("training");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (trainingSession != null) {
				model.addAttribute("training", trainingSession);
				session.setAttribute("training", null);
			} else {
				model.addAttribute("training", training);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addTrainingCreation ends");

		return "recruitment/add-training-creation";
	}

	/**
	 * Add Training creation
	 */

	@SuppressWarnings("unchecked")

	@PostMapping("add-training-creation")
	public String addState(@ModelAttribute TrainingCreationModel training, Model model, HttpSession session) {

		logger.info("Method : addState starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			training.setCreatedBy(userId);
			resp = restClient.postForObject(env.getRecruitment() + "restAddTraining", training, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sstate", training);

			return "redirect:/master/add-training-master";
		}

		logger.info("Method : addState ends");
		return "redirect:/recruitment/view-training-creation";
	}

	/**
	 * Default 'View Training Master' page
	 *
	 */

	@GetMapping("/view-training-creation")
	public String viewTrainingCreation(Model model, HttpSession session) {

		logger.info("Method : viewTrainingCreation starts");

		logger.info("Method : viewTrainingCreation ends");

		return "recruitment/view-training-creation";
	}
	/* Web Controller - View Training Details Through AJAX */

	@SuppressWarnings("unchecked")

	@GetMapping("/view-training-creation-through-ajax")
	public @ResponseBody DataTableResponse viewTrainingThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewTrainingThroughAjax starts");

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
			JsonResponse<List<TrainingCreationModel>> jsonResponse = new JsonResponse<List<TrainingCreationModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getTraining", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<TrainingCreationModel> training = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<TrainingCreationModel>>() {
					});

			for (TrainingCreationModel m : training) {
				String s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getTrainingId().getBytes());
				s = s + "<a href='view-training-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" title='Edit' style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteTraining(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" title='Delete' aria-hidden=\"true\" style=\"font-size:20px\"></i></a>"

						+ "<a href='javascript:void(0)' onclick='viewInModal(\"" + new String(encodeId)
						+ "\")' ><i class=\"fa fa-search search\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";

				m.setAction(s);
				String s1 = "Active";
				m.setActiveStatus(s1);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(training);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewTrainingThroughAjax ends");

		return response;

	}

	/**
	 * Web Controller - Delete training
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-training-master-delete")
	public @ResponseBody JsonResponse<Object> deleteTraining(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method :training creation starts");
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
					env.getRecruitment() + "training-creation-delete?id=" + index + "&createdBy=" + userId,
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
		logger.info("Method :training creation ends");

		return resp;
	}

	/**
	 * View selected Training in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-training-modal" })
	public @ResponseBody JsonResponse<TrainingCreationModel> viewInModal(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalviewOfTraining starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<TrainingCreationModel> res = new JsonResponse<TrainingCreationModel>();

		try {
			res = restClient.postForObject(env.getRecruitment() + "view-training-modal-index", id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modalviewOfTraining ends");
		return res;
	}

	/*
	 * for Edit trainning Details
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-training-edit")
	public String editJobDetails(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editJobDetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<TrainingCreationModel> res = new JsonResponse<TrainingCreationModel>();
		TrainingCreationModel trainingForm = new TrainingCreationModel();

		try {
			res = restClient.getForObject(env.getRecruitment() + "edit-training-details?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		trainingForm = mapper.convertValue(res.getBody(), TrainingCreationModel.class);
		session.setAttribute("message", "");
		model.addAttribute("training", trainingForm);
		model.addAttribute("trainingId", trainingForm.getTrainingId());

		logger.info("Method : editjobDetails ends");
		return "/recruitment/add-training-creation";

	}

}
