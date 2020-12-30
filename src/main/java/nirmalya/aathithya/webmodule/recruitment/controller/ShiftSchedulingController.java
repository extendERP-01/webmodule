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
import nirmalya.aathithya.webmodule.recruitment.model.ShiftSchedulingModel;

@Controller
@RequestMapping(value = "recruitment")
public class ShiftSchedulingController {
	Logger logger = LoggerFactory.getLogger(ShiftSchedulingController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "add-shift-scheduling" })
	public String addShiftScheduling(Model model, HttpSession session) {
		logger.info("Method : addItemRequisition function starts");

		ShiftSchedulingModel itemRequisitionModel = new ShiftSchedulingModel();
		ShiftSchedulingModel sessionitemRequisitionModel = (ShiftSchedulingModel) session
				.getAttribute("itemRequisitionModel");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionitemRequisitionModel != null) {
			model.addAttribute("itemRequisitionModel", sessionitemRequisitionModel);
			session.setAttribute("itemRequisitionModel", null);
		} else {
			model.addAttribute("itemRequisitionModel", itemRequisitionModel);
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Schedule = restClient.getForObject(env.getRecruitment() + "getScheduleList",
					DropDownModel[].class);
			List<DropDownModel> ScheduleList = Arrays.asList(Schedule);

			model.addAttribute("scheduleList", ScheduleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] Section = restClient.getForObject(env.getRecruitment() + "getSectionList",
					DropDownModel[].class);
			List<DropDownModel> SectionList = Arrays.asList(Section);
			model.addAttribute("sectionList", SectionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("departmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] sList = restClient.getForObject(env.getRecruitment() + "getShiftList",
					DropDownModel[].class);
			List<DropDownModel> shiftList = Arrays.asList(sList);
			model.addAttribute("shiftList", shiftList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] emList = restClient.getForObject(env.getRecruitment() + "getEmpList",
					DropDownModel[].class);
			List<DropDownModel> empList = Arrays.asList(emList);
			model.addAttribute("empList", empList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addItemRequisition ends");
		return "recruitment/add-shift-scheduling";

	}

	/*
	 * post mapping
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-shift-scheduling-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addShift(@RequestBody List<ShiftSchedulingModel> ShiftSchedulingModel,
			Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addShift function starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (ShiftSchedulingModel r : ShiftSchedulingModel) {

				r.setCreatedBy(userId);

			}

			res = restClient.postForObject(env.getRecruitment() + "restSchedule", ShiftSchedulingModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addShift function Ends");
		return res;
	}

	@GetMapping("/view-shift-scheduling")
	public String viewShiftScheduling(Model model, HttpSession session) {

		logger.info("Method : viewShiftScheduling starts");

		try {
			DropDownModel[] Schedule = restClient.getForObject(env.getRecruitment() + "getScheduleList",
					DropDownModel[].class);
			List<DropDownModel> ScheduleList = Arrays.asList(Schedule);

			model.addAttribute("scheduleList", ScheduleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] Section = restClient.getForObject(env.getRecruitment() + "getSectionList",
					DropDownModel[].class);
			List<DropDownModel> SectionList = Arrays.asList(Section);
			model.addAttribute("sectionList", SectionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("departmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] emList = restClient.getForObject(env.getRecruitment() + "getEmpList",
					DropDownModel[].class);
			List<DropDownModel> empList = Arrays.asList(emList);
			model.addAttribute("empList", empList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewShiftScheduling ends");

		return "recruitment/view-shift-scheduling";
	}

	/*
	 * For view for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-shift-scheduling-through-ajax")
	public @ResponseBody DataTableResponse viewShiftdependent(Model model, HttpServletRequest request,@RequestParam String param1,@RequestParam String param2,@RequestParam String param3) {

		logger.info("Method : viewShiftdependent statrs");

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


			JsonResponse<List<ShiftSchedulingModel>> jsonResponse = new JsonResponse<List<ShiftSchedulingModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getAssignShiftDetails",tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ShiftSchedulingModel> assignShift = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ShiftSchedulingModel>>() {
					});

			String s = "";

			for (ShiftSchedulingModel m : assignShift) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getsShiftId().getBytes());

				s = s + "<a href='view-shift-scheduling-edit?sShiftId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				
			}
			
		
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignShift);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewShiftdependent ends");

		return response;
	}

	/*
	 * for Edit assign
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-shift-scheduling-edit")
	public String editQuedependent(Model model, @RequestParam("sShiftId") String encodeId, HttpSession session) {

		logger.info("Method :editShiftdependent starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<ShiftSchedulingModel>> response = new JsonResponse<List<ShiftSchedulingModel>>();
		try {
  
			response = restClient.getForObject(
					env.getRecruitment() + "getShiftDepndById?sShiftId=" + id, JsonResponse.class);

			System.out.println("sShiftId");
			ObjectMapper mapper = new ObjectMapper();

			List<ShiftSchedulingModel> dpndShift = mapper.convertValue(response.getBody(),
					new TypeReference<List<ShiftSchedulingModel>>() {
					});
			if (dpndShift != null) {
				dpndShift.get(0).setEditId("edit");
			}
		System.out.println(dpndShift);
		System.out.println(dpndShift.get(0).getEditId());
			model.addAttribute(""
					+ ""
					+ "", dpndShift);
			model.addAttribute("itemReq", dpndShift.get(0).gettShiftId());
			model.addAttribute("itemRequisitionModel",dpndShift);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Schedule = restClient.getForObject(env.getRecruitment() + "getScheduleList",
					DropDownModel[].class);
			List<DropDownModel> ScheduleList = Arrays.asList(Schedule);
			
			model.addAttribute("scheduleList", ScheduleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			DropDownModel[] Section = restClient.getForObject(env.getRecruitment() + "getSectionList",
					DropDownModel[].class);
			List<DropDownModel> SectionList = Arrays.asList(Section);
			model.addAttribute("sectionList", SectionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] Department = restClient.getForObject(env.getRecruitment() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(Department);
			model.addAttribute("departmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			DropDownModel[] sList = restClient.getForObject(env.getRecruitment() + "getShiftList",
					DropDownModel[].class);
			List<DropDownModel> shiftList = Arrays.asList(sList);
			model.addAttribute("shiftList", shiftList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] emList = restClient.getForObject(env.getRecruitment() + "getEmpList",
					DropDownModel[].class);
			List<DropDownModel> empList = Arrays.asList(emList);
			model.addAttribute("empList", empList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		logger.info("Method : editShiftdependent ends");

		return "recruitment/add-shift-scheduling";
	
	}
	/*
	 * For Modal certification 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-shift-schedule-modalView" })
	public @ResponseBody JsonResponse<List<ShiftSchedulingModel>> modalShiftEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalShiftEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<ShiftSchedulingModel>> response = new JsonResponse<List<ShiftSchedulingModel>>();
		try {
			response = restClient.getForObject(env.getRecruitment() + "getShiftDepndById?sShiftId=" + id,
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
		logger.info("Method : modalShiftEdu  ends ");
		return response;
	}
	
	

}


