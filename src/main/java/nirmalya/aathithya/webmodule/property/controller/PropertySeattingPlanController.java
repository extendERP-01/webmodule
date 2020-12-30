/**
 * web Controller for Seatting plan
 */
package nirmalya.aathithya.webmodule.property.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.property.model.PropertySeattingPlanModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class PropertySeattingPlanController {
	public static String uploadDirectory = System.getProperty("user.dir") + "uploads";

	Logger logger = LoggerFactory.getLogger(PropertySeattingPlanController.class);

	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add sitting plan view page
	 */
	@GetMapping("/add-seatingplan")
	public String addPropertyTheme(Model model, HttpSession session) {

		logger.info("Method : addPropertyTheme starts");

		PropertySeattingPlanModel seattingPlan = new PropertySeattingPlanModel();
		PropertySeattingPlanModel thm = (PropertySeattingPlanModel) session.getAttribute("seattingPlan");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (thm != null) {
			model.addAttribute("seattingPlan", thm);
			session.setAttribute("seattingPlan", null);
		} else {
			model.addAttribute("seattingPlan", seattingPlan);
		}
		if (thm != null) {
			String ImgName = thm.getPlanlogo();
			String s = "";
			s = "<a class='example-image-link' href='/document/"+ImgName+"' title='"+ImgName+"' data-lightbox='"+ImgName+"'>"+ImgName+"</a>";
			thm.setAction(s);
			model.addAttribute("seattingPlan", thm);
			session.setAttribute("seattingPlan", null);
			} else {
			model.addAttribute("seattingPlan", seattingPlan);
			}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addPropertyTheme end");
		return "property/AddSeatingPlan";
	}

	/*
	 * post mapping for adding new sitting
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-seatingplan")
	public String addSetting(@ModelAttribute PropertySeattingPlanModel seattingPlan, Model model,
			HttpSession session) {

		logger.info("Method : addSetting starts");

		JsonResponse<Object> response = new JsonResponse<Object>();
		try {
			byte[] bytes = (byte[]) session.getAttribute("menuItemsMasterFile");
			long nowTime = new Date().getTime();
			String imageName = nowTime + ".png";
			seattingPlan.setPlanlogo(imageName);
			seattingPlan.setCreatedBy("Dj");
			response = restClient.postForObject(env.getPropertyUrl() + "restAddSitting", seattingPlan,
					JsonResponse.class);

			if ((response.getCode() == null || response.getCode() == "")
					&& (response.getMessage() == null || response.getMessage() == "")) {
				Path path = Paths.get(env.getFileUploadProperty() + imageName);
				Files.write(path, bytes);

				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				Integer height = 50;
				Integer width = 50;

				try {
					BufferedImage img = ImageIO.read(in);
					if (height == 0) {
						height = (width * img.getHeight()) / img.getWidth();
					}
					if (width == 0) {
						width = (height * img.getWidth()) / img.getHeight();
					}

					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
					BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					ImageIO.write(imageBuff, "jpg", buffer);

					byte[] thumb = buffer.toByteArray();
					Path pathThumb = Paths.get(env.getFileUploadProperty() + "thumb\\" + imageName);
					Files.write(pathThumb, thumb);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != "") {
			session.setAttribute("message", response.getMessage());
			session.setAttribute("seattingPlan", seattingPlan);
			return "redirect:/property/add-seatingplan";
		}
		
		try {
			TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

		logger.info("Method : addSetting ends");
		return "redirect:/property/view-seatingplan";
	}

	/*
	 * Get Mapping View sitting plan page
	 */
	@GetMapping("/view-seatingplan")
	public String getAlllistSitting(Model model, HttpSession session) {
		logger.info("Method : getAlllistSitting starts");
		JsonResponse<Object> plan = new JsonResponse<Object>();
		model.addAttribute("plan", plan);
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : getAlllistSitting ends");
		return "property/ListSeatingPlan";
	}

	/*
	 * 
	 * Get Mapping for Ajax call from list view page
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-seatingplan-ThroughAjax")
	public @ResponseBody DataTableResponse viewSettingsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewSettingsThroughAjax starts");
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

			JsonResponse<List<PropertySeattingPlanModel>> jsonResponse = new JsonResponse<List<PropertySeattingPlanModel>>();

			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllSitting", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertySeattingPlanModel> listSeattingPlan = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertySeattingPlanModel>>() {
					});

			String s = "";

			for (PropertySeattingPlanModel m : listSeattingPlan) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getSeatingPlan().getBytes());
				s = s + "<a href='edit-Seating?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteSeatingPlan(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);

				s = "";
				s = "<a class='example-image-link' href='/document/" + m.getPlanlogo() + "' title='"
						+ m.getPlanlogo() + "' data-lightbox='" + m.getPlanlogo() + "'>"
						+ "<img src='/document/thumb/" + m.getPlanlogo() + "'/>" + "</a>";
				m.setPlanlogo(s);

				s = "";
				if (m.getPlanActive() == true) {
					m.setStausName("Active");
				} else {
					m.setStausName("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(listSeattingPlan);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewSettingsThroughAjax ends");
		return response;
	}

	/*
	 * GetMapping for edit of sitting data
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-Seating")
	public String editSetting(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editSetting Starts");

		PropertySeattingPlanModel seattingPlan = new PropertySeattingPlanModel();
		JsonResponse<PropertySeattingPlanModel> jsonResponse = new JsonResponse<PropertySeattingPlanModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getSittingById?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] propertyName = restClient.getForObject(env.getPropertyUrl() + "getPropertyName",
					DropDownModel[].class);
			List<DropDownModel> propertyNameList = Arrays.asList(propertyName);
			model.addAttribute("propertyNameList", propertyNameList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		seattingPlan = mapper.convertValue(jsonResponse.getBody(), PropertySeattingPlanModel.class);
		session.setAttribute("message", "");
		String ImgName = seattingPlan.getPlanlogo();
		if (ImgName != null || ImgName != "") {
			String s = "";
			s = "/document/sitting/" + ImgName;
			seattingPlan.setAction(s);
		}
		model.addAttribute("seattingPlan", seattingPlan);

		logger.info("Method : editSeating ends");
		return "property/AddSeatingPlan";
	}

	/*
	 * For Delete Sitting
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-seatingplan-delete-Seating")
	public @ResponseBody JsonResponse<Object> deleteSetting(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteSetting Starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "Dj";
		try {
			resp = restClient.getForObject(
					env.getPropertyUrl() + "deleteSittingById?id=" + id1 + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteSetting  ends");
		return resp;
	}

	/*
	 * Modal Setting View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-seatingplan-modalViewSetting" })
	public @ResponseBody JsonResponse<Object> getOneModalSetting(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getOneModalSetting Starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getSittingById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getOneModalSetting ends");
		return res;
	}

	@PostMapping("/uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("menuItemsMasterFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

}
