package nirmalya.aathithya.webmodule.production.controller;

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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.production.model.AssignSlitPipeSizeModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "production")
public class AssignSlitPipeSizeController {

Logger logger = LoggerFactory.getLogger(AssignSlitPipeSizeController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/**
	 * View Default 'Gate-Pass Entry' page
	 *
	 */
	@GetMapping("/assign-slit-pipesize")
	public String defaultAssignSlitPipeSize(Model model, HttpSession session) {
		logger.info("Method : defaultAssignSlitPipeSize starts");
		
		try {
			DropDownModel[] dd = restClient.getForObject(env.getProduction()+"getSlitWithForAssignment", DropDownModel[].class);
			List<DropDownModel> slitWidthList = Arrays.asList(dd);
			model.addAttribute("slitWidthList", slitWidthList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : defaultAssignSlitPipeSize ends");
		return "production/assign-slit-pipesize";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-slit-pipesize-get-grade" })
	public @ResponseBody JsonResponse<DropDownModel> getGradeForAssignment(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGradeForAssignment starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getProduction() + "getGradeForAssignment?id=" + searchValue,
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

		logger.info("Method : getGradeForAssignment ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-slit-pipesize-get-thickness" })
	public @ResponseBody JsonResponse<DropDownModel> getThicknessForAssignment(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getThicknessForAssignment starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getProduction() + "getThicknessForAssignment", searchValue,
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

		logger.info("Method : getThicknessForAssignment ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-slit-pipesize-get-pipeSize" })
	public @ResponseBody JsonResponse<DropDownModel> getPipeSizeForAssignment(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getPipeSizeForAssignment starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.postForObject(env.getProduction() + "getPipeSizeForAssignment", searchValue,
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
		
		logger.info("Method : getPipeSizeForAssignment ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "assign-slit-pipesize-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveAssignedSlitPipeSize(@RequestBody List<AssignSlitPipeSizeModel> assignmnet, Model model,
			HttpSession session) {
		logger.info("Method : saveAssignedSlitPipeSize function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < assignmnet.size(); i++) {
				assignmnet.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getProduction() + "saveAssignedSlitPipeSize", assignmnet, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveAssignedSlitPipeSize function Ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/assign-slit-pipesize-view-through-ajax")
	public @ResponseBody DataTableResponse viewAssignedSlitPipeSizeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewAssignedSlitPipeSizeThroughAjax starts");

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

			JsonResponse<List<AssignSlitPipeSizeModel>> jsonResponse = new JsonResponse<List<AssignSlitPipeSizeModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "getAssignedSlitPipeSize", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssignSlitPipeSizeModel> assignment = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssignSlitPipeSizeModel>>() {
					});

			String s = "";

			for (AssignSlitPipeSizeModel m : assignment) {
				byte[] gId = Base64.getEncoder().encode(m.getGradeId().getBytes());
				byte[] tId = Base64.getEncoder().encode(m.getThicknessId().getBytes());
				byte[] pId = Base64.getEncoder().encode(m.getPipeSizeId().getBytes());
				
				s = s +"<a href='javascript:void(0)'" 
						+ "' onclick='deleteAssignedDataId(\"" + new String(gId) + "\",\"" + new String(tId) + "\",\"" + new String(pId) + "\"," + m.getSlitWidth() + ")' ><i class=\"fa fa-trash\" aria-hidden=\"true\" style='font-size:20px;'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignment);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAssignedSlitPipeSizeThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-slit-pipesize-delete" })
	public @ResponseBody JsonResponse<Object> deleteAssignedSlitPipe(Model model,
			@RequestBody AssignSlitPipeSizeModel searchValue, BindingResult result) {
		logger.info("Method : deleteAssignedSlitPipe starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] encodeByte1 = Base64.getDecoder().decode(searchValue.getGrade().getBytes());
		String grade = (new String(encodeByte1));
		searchValue.setGrade(grade);
		
		byte[] encodeByte2 = Base64.getDecoder().decode(searchValue.getThickness().getBytes());
		String thickness = (new String(encodeByte2));
		searchValue.setThickness(thickness);
		
		byte[] encodeByte3 = Base64.getDecoder().decode(searchValue.getPipeSize().getBytes());
		String pipeSize = (new String(encodeByte3));
		searchValue.setPipeSize(pipeSize);
		
		try {
			res = restClient.postForObject(env.getProduction() + "deleteAssignedSlitPipe", searchValue,
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

		logger.info("Method : deleteAssignedSlitPipe ends");
		return res;
	}
}
