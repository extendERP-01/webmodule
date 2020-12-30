
/**
 * web Controller for property floor
 */
package nirmalya.aathithya.webmodule.property.controller;

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
import nirmalya.aathithya.webmodule.property.model.PropertyFloorModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = "property")
public class PropertyFloorController {
	Logger logger = LoggerFactory.getLogger(PropertyFloorController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add property floor view page
	 */

	@GetMapping("/add-property-floor")
	public String addPropertyFloor(Model model, HttpSession session) {
		logger.info("Method : addPropertyFloor starts");
		PropertyFloorModel floor = new PropertyFloorModel();
		PropertyFloorModel form = (PropertyFloorModel) session.getAttribute("sfloor");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("floor", form);
			session.setAttribute("sfloor", null);

		} else {
			model.addAttribute("floor", floor);
		}
		logger.info("Method : addPropertyFloor end");
		return "property/addPropertyFloor";
	}

	/*
	 * post mapping for adding new floor
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-property-floor")
	public String postFloor(@ModelAttribute PropertyFloorModel floor, Model model, HttpSession session) {
		logger.info("Method : PostFloor starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String UserId=null;
		try {
			UserId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			floor.setpFloorCreatedBy(UserId);
			resp = restClient.postForObject(env.getPropertyUrl() + "addFloor", floor, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sfloor", floor);
			return "redirect:/property/add-property-floor";
		}
		session.setAttribute("sfloor", null);
		logger.info("Method : postFloor end");
		return "redirect:/property/view-property-floor";
	}
	/*
	 * get mapping for view property floor
	 */

	@GetMapping("/view-property-floor")
	public String viewFloor(Model model) {
		logger.info("Method : viewFloor starts");

		JsonResponse<Object> floor = new JsonResponse<Object>();
		model.addAttribute("floor", floor);

		logger.info("Method : viewFloor end");
		return "property/listPropertyFloor";

	}
	/*
	 * get mapping for viewFloorThroughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-floor-throughajax")
	public @ResponseBody DataTableResponse viewFloorThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewFloorThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<PropertyFloorModel>> jsonResponse = new JsonResponse<List<PropertyFloorModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllfloor", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyFloorModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyFloorModel>>() {
					});

			String s = "";
			for (PropertyFloorModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getFloorId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-property-floor?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href= 'javascript:void(0)' "
						+ "' onclick='deleteFloor(\"" + new String(pId) + "\")' ><i class=\"fa fa-trash\"></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getFloorActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewFloorThroughAjax end");
		return response;
	}

	/*
	 * get mapping for delete floor
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-property-floor-delete")
	public @ResponseBody JsonResponse<Object> deleteFloor(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteFloor starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		String createdBy = null;
		try {
			createdBy = (String) session.getAttribute("USER_ID");
			System.out.println(createdBy);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			resp = restClient.getForObject(
					env.getPropertyUrl() + "deleteFloorById?id=" + id1 + "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteFloor ends");
		return resp;
	}

	/*
	 * get mapping for edit property floor
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-property-floor")
	public String editFloor(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editFloor starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PropertyFloorModel floor = new PropertyFloorModel();
		JsonResponse<PropertyFloorModel> jsonResponse = new JsonResponse<PropertyFloorModel>();

		try {
			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getFloorById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		floor = mapper.convertValue(jsonResponse.getBody(), PropertyFloorModel.class);
		session.setAttribute("message", "");

		model.addAttribute("floor", floor);

		logger.info("Method : editFloor end");
		return "property/addPropertyFloor";
	}

	/*
	 * get mapping for viewInModelData property floor
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-property-floor-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		/* List<Floor> res = new ArrayList<Floor>(); */
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getFloorById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modelView end");
		return res;
	}

}