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
import nirmalya.aathithya.webmodule.recruitment.model.ShiftMasterModel;

@Controller
@RequestMapping(value = "recruitment")
public class ShiftMasterController {
	Logger logger = LoggerFactory.getLogger(ShiftMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/add-shift-master")
	public String addShiftMaster(Model model, HttpSession session) {

		logger.info("Method : addShiftMaster starts");

		try {
			DropDownModel[] ShiftName = restClient.getForObject(env.getRecruitment() + "dropDownshift",
					DropDownModel[].class);
			List<DropDownModel> shiftNameList = Arrays.asList(ShiftName);

			model.addAttribute("shiftNameList", shiftNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ShiftMasterModel shift = new ShiftMasterModel();
		try {
			ShiftMasterModel shiftregion = (ShiftMasterModel) session.getAttribute("sshift");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (shiftregion != null) {
				model.addAttribute("shift", shiftregion);
				session.setAttribute("sshift", null);
			} else {
				model.addAttribute("shift", shift);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addShiftMaster ends");

		return "recruitment/add-shift-master";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-shift-master")
	public String addShiftMaster(@ModelAttribute ShiftMasterModel shift, Model model, HttpSession session) {

		logger.info("Method : addShiftMaster strats");

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		shift.setCreatedBy(userId);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.postForObject(env.getRecruitment() + "restAddShift", shift, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sshift", shift);
			return "redirect:/recruitment/add-shift-master";
		}

		logger.info("Method : addShiftMaster ends");
		System.out.println("hello");
		return "recruitment/view-shift-master";
	}

	@GetMapping("/view-shift-master")
	public String viewShiftMaster(Model model, HttpSession session) {

		logger.info("Method : viewShiftMaster starts");

		try {
			DropDownModel[] ShiftName = restClient.getForObject(env.getRecruitment() + "dropDownshift",
					DropDownModel[].class);
			List<DropDownModel> shiftNameList = Arrays.asList(ShiftName);

			model.addAttribute("shiftNameList", shiftNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewShiftMaster ends");

		return "recruitment/view-shift-master";
	}

//view sshift through Ajax
	@SuppressWarnings("unchecked")
	@GetMapping("/view-shift-master-through-ajax")
	public @ResponseBody DataTableResponse viewStructThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method ; viewShiftThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<ShiftMasterModel>> jsonResponse = new JsonResponse<List<ShiftMasterModel>>();

			jsonResponse = restClient.postForObject(env.getRecruitment() + "getshiftd", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ShiftMasterModel> addshif = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ShiftMasterModel>>() {
					});
			String s = "";
			for (ShiftMasterModel m : addshif) {
				byte[] pId = Base64.getEncoder().encode(m.getShiftId().getBytes());

				s = s + "<a href='edit-shift-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)' onclick='deleteShift(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";
				s += "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewshifModal(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-search '  style='font-size:20px; color:#0095c6;'></i></a>";

				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(addshif);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(response);
		logger.info("Method ; viewShiftThroughAjax ends");

		return response;
	}
	// Edit

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-shift-master")
	public String editShift(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editShift starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		ShiftMasterModel shift = new ShiftMasterModel();
		JsonResponse<ShiftMasterModel> jsonResponse = new JsonResponse<ShiftMasterModel>();

		try {
			DropDownModel[] ShiftName = restClient.getForObject(env.getRecruitment() + "dropDownshift",
					DropDownModel[].class);
			List<DropDownModel> shiftNameList = Arrays.asList(ShiftName);

			model.addAttribute("shiftNameList", shiftNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {

			jsonResponse = restClient.getForObject(env.getRecruitment() + "getShifteditById?id=" + id,
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

		shift = mapper.convertValue(jsonResponse.getBody(), ShiftMasterModel.class);
		session.setAttribute("message", "");

		System.out.println(shift + "#########");

		model.addAttribute("shift", shift);

		logger.info("Method : editShift ends");
		return "recruitment/add-shift-master";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-shift-delete")
	public @ResponseBody JsonResponse<Object> deleteShift(Model model, @RequestParam String id, HttpSession session) {
		logger.info("Method : deleteShift starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		System.out.println(index);

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String createdBy = "";

		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			resp = restClient.getForObject(env.getRecruitment() + "deleteShift?id=" + index + "&createdBy=" + createdBy,
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

		logger.info("Method :  deleteShift ends");
		return resp;

	}

	/*
	 * For Modal
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-shift-modalView" })
	public @ResponseBody JsonResponse<List<ShiftMasterModel>> modalshift(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalShift starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<ShiftMasterModel>> response = new JsonResponse<List<ShiftMasterModel>>();
		try {
			response = restClient.getForObject(env.getRecruitment() + "getshiftmodalById?shiftId=" + id,
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
		logger.info("Method : modalShift  ends ");
		return response;
	}

}
