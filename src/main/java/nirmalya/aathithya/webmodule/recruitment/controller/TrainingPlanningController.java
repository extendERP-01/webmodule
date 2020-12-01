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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import nirmalya.aathithya.webmodule.recruitment.model.AddTrainingPlanning; 


@Controller
@RequestMapping(value = "recruitment")
public class TrainingPlanningController {
	Logger logger = LoggerFactory.getLogger(TrainingPlanningController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping("/add-training-planning")
	public String addTrainingPlanning(Model model, HttpSession session) {

		logger.info("Method : addTrainingPlanning starts");
		
		try {
			DropDownModel[] tranningType = restClient.getForObject(env.getRecruitment() + "dropDowntranningType",
					DropDownModel[].class);
			List<DropDownModel> tranningTypeList = Arrays.asList(tranningType);

			model.addAttribute("tranningTypeList", tranningTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		 
		AddTrainingPlanning planning = new AddTrainingPlanning();
		try {
			AddTrainingPlanning planningregion = (AddTrainingPlanning) session.getAttribute("splanning");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (planningregion != null) {
				model.addAttribute("planning", planningregion);
				session.setAttribute("splanning", null);
			} else {
				model.addAttribute("planning", planning);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		logger.info("Method : addTrainingPlanning ends");

		return "recruitment/add-training-planning";
	}
	
	/*
	 * post mapping 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-training-planning-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addplanning(
			@RequestBody List<AddTrainingPlanning> addTrainingPlanning, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addTrainingPlanning function starts");


		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (AddTrainingPlanning r : addTrainingPlanning) {

				r.setCreatedBy(userId);
				

			}
		

			res = restClient.postForObject(env.getRecruitment() + "restaddplan", addTrainingPlanning,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addTrainingPlanning function Ends");
		return res;
	}
	
	@GetMapping("/view-training-planning")
	public String viewTrainingPlanning(Model model, HttpSession session) {

		logger.info("Method : viewTrainingPlanning starts");
		
		logger.info("Method : viewTrainingPlanning ends");

		return "recruitment/view-training-planning";
	}
	//view sshift through Ajax
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trainning-planning-through-ajax")
	public @ResponseBody DataTableResponse viewtrainningplanningthroughajax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method ; viewTrainingPlanning throughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			
			  tableRequest.setStart(Integer.parseInt(start));
			  tableRequest.setLength(Integer.parseInt(length));
			  tableRequest.setParam1(param1);
		
	
			JsonResponse<List<AddTrainingPlanning>> jsonResponse = new JsonResponse<List<AddTrainingPlanning>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "gettrainningplanning", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AddTrainingPlanning> addtrainning = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AddTrainingPlanning>>() {
					});
			String s = "";
			
			

				for (AddTrainingPlanning m : addtrainning) {
					byte[] pId = Base64.getEncoder().encode(m.getTrainningId().getBytes());

					s = s + "<a href='view-edit-planning?trainningId=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)' onclick='deletePlanning(\"" + new String(pId) 
							+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";
					  
					
					if(m.getStatus().equals(true)) {
						m.setStatusName("Active");
					}
					if(m.getStatus().equals(false)) {
						m.setStatusName("In Active");
					}					
					
				m.setAction(s);;
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(addtrainning);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method ; viewTrainingPlanning throughAjax ends");

		return response;
	}

  

//Delete trainning Planning
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trainning-planning-delete")
	public @ResponseBody JsonResponse<Object> deletePlanning(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : delete TrainingPlanning starts");
		byte[] encodeByte=Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String createdBy ="";
		try {
			createdBy = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getRecruitment() + "deletetrainningbyId?id=" + index + "&createdBy=" + createdBy,
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
		logger.info("Method : delete TrainingPlanning ends");
		
		return resp;
	}

	/*
	 * for Edit assign
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-edit-planning")
	public String editplanning(Model model, @RequestParam("trainningId") String encodeId, HttpSession session) {

		logger.info("Method :edit trainningplanning starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<AddTrainingPlanning>> response = new JsonResponse<List<AddTrainingPlanning>>();
		
		
		

		try {
			DropDownModel[] tranningType = restClient.getForObject(env.getRecruitment() + "dropDowntranningType",
					DropDownModel[].class);
			List<DropDownModel> tranningTypeList = Arrays.asList(tranningType);

			model.addAttribute("tranningTypeList", tranningTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		try {
		
			response = restClient.getForObject(env.getRecruitment() + "geteditplanning?trainningId=" + id,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AddTrainingPlanning> planning = mapper.convertValue(response.getBody(),
					new TypeReference<List<AddTrainingPlanning>>() {
					});
			
			
			if (planning != null) {
				planning.get(0).setEditId("edit");
			}
			
			model.addAttribute("planning", planning);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		logger.info("Method : edit trainningplanning ends");

		return "recruitment/add-training-planning";
	}
	
	
}
