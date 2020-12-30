/**
 * web Controller for property hotel

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
import nirmalya.aathithya.webmodule.property.model.PropertyHotelModel;

/**
 * @author Nirmalya Labs
 *
 */

@Controller
@RequestMapping(value = { "property" })

public class PropertyHotelController {
	Logger logger = LoggerFactory.getLogger(PropertyHotelController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GEt Mapping for Add hotel form view page
	 */
	@GetMapping("/add-hotel")
	public String addPropertyHotel(Model model, HttpSession session) {
		logger.info("Method : addPropertyHotel starts");
		PropertyHotelModel hotel = new PropertyHotelModel();

		PropertyHotelModel form = (PropertyHotelModel) session.getAttribute("shotel");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");
		if (form != null) {
			String ImgName = form.getHotelLogo();
			String s = "";
			s = "<a class='example-image-link' href='/image/seating/" + ImgName + "' title='" + ImgName
					+ "' data-lightbox='" + ImgName + "'>" + ImgName + "</a>";
			form.setAction(s);
			model.addAttribute("hotel", form);
			session.setAttribute("shotel", null);
		} else {
			model.addAttribute("hotel", hotel);
		}
		/*
		 * drop down data
		 */
		try {
			DropDownModel[] state = restClient.getForObject(env.getPropertyUrl() + "getStateName",
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(state);
			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addPropertyHotel end");
		return "property/addHotelForm";
	}

	/*
	 * for drop down district list by state
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-hotel-getdistrict" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getdistrict TypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "getDistName?state=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getdistrict TypeList ends");
		return res;
	}

	/*
	 * post Mapping for submit data
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-hotel")
	public String postHotel(@ModelAttribute PropertyHotelModel hotel, Model model, HttpSession session) {
		logger.info("Method : postHotel starts");
		// System.out.println("property form : "+property);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String imageName = null;
		try {
			byte[] bytes = (byte[]) session.getAttribute("logoFile");
			if (bytes != null) {

				long nowTime = new Date().getTime();
				imageName = nowTime + ".png";
				hotel.setHotelLogo(imageName);
			} else {
				imageName = (String) session.getAttribute("logoImageNameForEdit");

				hotel.setHotelLogo(imageName);
			}
			String userId = (String) session.getAttribute("USER_ID");
			hotel.setHotelCreatedBy(userId);
			resp = restClient.postForObject(env.getPropertyUrl() + "addHotel", hotel, JsonResponse.class);

			if (resp.getCode().contains("Data Saved Successfully")) {

				Path path = Paths.get(env.getFileUploadHotel() + imageName);
				if (bytes != null) {
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

						// ByteArrayOutputStream out = new
						// ByteArrayOutputStream();
						byte[] thumb = buffer.toByteArray();
						Path pathThumb = Paths.get(env.getFileUploadHotel() + "thumb\\" + imageName);
						Files.write(pathThumb, thumb);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("shotel", hotel);
			return "redirect:/property/add-hotel";
		}

		session.removeAttribute("logoFile");
		logger.info("Method : addNewLogo controller function 'post-mapping' ends");
		logger.info("Method : postHotel end");
		return "redirect:/property/view-hotel";
	}

	@PostMapping("/uploadhotelFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("logoFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}
	/*
	 * GEt Mapping for view hotel form
	 */

	@GetMapping("/view-hotel")
	public String viewHotel(Model model) {
		logger.info("Method : viewHotel starts");

		logger.info("Method : viewHotel end");
		return "property/listHotelForm";

	}
	/*
	 * GEt Mapping for viewHotelThroughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-hotel-throughajax")
	public @ResponseBody DataTableResponse viewHotelThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewHotelThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<PropertyHotelModel>> jsonResponse = new JsonResponse<List<PropertyHotelModel>>();
			jsonResponse = restClient.postForObject(env.getPropertyUrl() + "getAllHotel", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<PropertyHotelModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PropertyHotelModel>>() {
					});

			String s = "";
			for (PropertyHotelModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getHotelId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-hotel?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href='javascript:void(0)'\"+ \"' onclick='deleteItem(  \""
						+ new String(pId) + "\")' ><i class=\"fa fa-trash\"></i></a> ";

				m.setAction(s);
				s = "";
				s = "<a class='example-image-link' href='/image/seating/" + m.getHotelLogo() + "' title='"
						+ m.getHotelLogo() + "' data-lightbox='" + m.getHotelLogo() + "'>"
						+ "<img src='/image/seating/thumb/" + m.getHotelLogo() + "'/>" + "</a>";
				m.setHotelLogo(s);

				if (m.getHotelStatus()) {
					m.setHotelstatusName("Active");
				} else {
					m.setHotelstatusName("Inactive");
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
		logger.info("Method : viewHotelThroughAjax end");
		return response;
	}

	/*
	 * post Mapping for viewInModelDataHotel
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-hotel-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		try {
			res = restClient.getForObject(
					env.getPropertyUrl() + "getHotelById?id=" + id + "&Action=" + "ModelViewHotel", JsonResponse.class);
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

	/*
	 * GEt Mapping for delete hotel
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-hotel-delete-hotel")
	public @ResponseBody JsonResponse<Object> deleteHotel(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteHotel starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));

		try {
			String createdBy = (String) session.getAttribute("USER_ID");
			resp = restClient.getForObject(
					env.getPropertyUrl() + "deleteHotelById?id=" + id1 + "&createdBy=" + createdBy, JsonResponse.class);

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
		logger.info("Method : deleteHotel ends");

		return resp;
	}

	/*
	 * GEt Mapping for edit hotel
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-hotel")
	public String editHotel(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : edit Hotel starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PropertyHotelModel hotel = new PropertyHotelModel();
		JsonResponse<PropertyHotelModel> jsonResponse = new JsonResponse<PropertyHotelModel>();

		try {
			jsonResponse = restClient.getForObject(env.getPropertyUrl() + "getHotelById?id=" + id + "&Action=editHotel",
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

		hotel = mapper.convertValue(jsonResponse.getBody(), PropertyHotelModel.class);
		session.setAttribute("message", "");
		String ImgName = hotel.getHotelLogo();
		if (ImgName != null || ImgName != "") {
			String s = "";
			s = "/image/seating/" + ImgName;
			hotel.setAction(s);
			session.setAttribute("logoImageNameForEdit", hotel.getHotelLogo());
		}
		model.addAttribute("hotel", hotel);
		try {
			DropDownModel[] state = restClient.getForObject(env.getPropertyUrl() + "getStateName",
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(state);
			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String state = hotel.getState();
		try {
			DropDownModel[] dist = restClient.getForObject(env.getPropertyUrl() + "getdistName?state=" + state,
					DropDownModel[].class);
			List<DropDownModel> distList = Arrays.asList(dist);
			model.addAttribute("distList", distList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editHotel end");
		return "property/addHotelForm";
	}

}
