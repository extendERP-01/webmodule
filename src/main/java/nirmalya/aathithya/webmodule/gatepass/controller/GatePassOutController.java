package nirmalya.aathithya.webmodule.gatepass.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassItemModel;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassOutModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "gatepass")
public class GatePassOutController {

	Logger logger = LoggerFactory.getLogger(GatePassOutController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 * View Default 'Gate-Pass out' page
	 *
	 */
	@GetMapping("/gatepass-out")
	public String defaultGatePassOut(Model model, HttpSession session) {
		logger.info("Method : defaultGatePassOut starts");

		try {
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl() + "getStoreForGatePassEntry",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultGatePassOut ends");
		return "gatepass/gatepass-out";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-customer" })
	public @ResponseBody JsonResponse<DropDownModel> getCustomerForGateOut(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getCustomerForGateOut starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getCustomerForGateOut?id=" + searchValue,
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

		logger.info("Method : getCustomerForGateOut ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-challan" })
	public @ResponseBody JsonResponse<DropDownModel> getChallanForGateOut(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getChallanForGateOut starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getChallanForGateOut?id=" + searchValue,
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
		
		logger.info("Method : getChallanForGateOut ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-challan-details" })
	public @ResponseBody JsonResponse<GatePassOutModel> getChallanDetails(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getChallanDetails starts");
		
		JsonResponse<GatePassOutModel> res = new JsonResponse<GatePassOutModel>();
		
		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getChallanDetails?id=" + searchValue,
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
		
		logger.info("Method : getChallanDetails ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-item-category" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategoryAutoSearch(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemCategoryAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getItemCategoryAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemCategoryAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-item-subcategory" })
	public @ResponseBody JsonResponse<DropDownModel> getItemSubCatAutoSearch(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getGatepassUrl() + "getItemSubCatAutoSearch", searchValue,
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

		logger.info("Method : getItemSubCatAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getItemAutoSearch(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getGatepassUrl() + "getItemAutoSearch", searchValue,
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

		logger.info("Method : getItemAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-out-get-servetype" })
	public @ResponseBody JsonResponse<GatePassItemModel> getServeType(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getServeType starts");

		JsonResponse<GatePassItemModel> res = new JsonResponse<GatePassItemModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getServeTypeForGatePass?id=" + id,
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

		logger.info("Method : getServeType ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "gatepass-out-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addGatePassOut(@RequestBody List<GatePassOutModel> gatePass, Model model,
			HttpSession session) {
		logger.info("Method : addGatePassOut function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
		byte[] bytes;
		String imageName = null;
		try {
			bytes = inputFile.getBytes();
			String[] fileType = inputFile.getContentType().split("/");
			imageName = saveAllImage(bytes,fileType[1]);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			for (int i = 0; i < gatePass.size(); i++) {
				gatePass.get(i).setCreatedBy(userId);
				gatePass.get(i).setFileUpload(imageName);
			}
			res = restClient.postForObject(env.getGatepassUrl() + "newGatePassOut", gatePass, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addGatePassOut function Ends");
		return res;
	}
	
	/**
	 * Default 'View Gate Pass' page
	 *
	 */
	@GetMapping("/view-gatepass-out")
	public String viewGatePassOut(Model model, HttpSession session) {
		logger.info("Method : viewGatePassOut starts");

		logger.info("Method : viewGatePassOut ends");
		return "gatepass/view-gatepass-out";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-gatepass-out-through-ajax")
	public @ResponseBody DataTableResponse viewGatePassOutThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewGatePassOutThroughAjax starts");

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
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			JsonResponse<List<GatePassOutModel>> jsonResponse = new JsonResponse<List<GatePassOutModel>>();

			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getGatePassOutDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<GatePassOutModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<GatePassOutModel>>() {
					});

			String s = "";

			for (GatePassOutModel m : gatePass) {
				byte[] pId = Base64.getEncoder().encode(m.getGatePassOut().getBytes());
				if (m.getPassType() == 1) {
					m.setPassTypeName("Gate In");
				} else if (m.getPassType() == 2) {
					m.setPassTypeName("Gate Out");
				}
				
				if(m.getDelChallan()==null || m.getDelChallan()=="") {
					m.setDelChallan("N/A");
				}
				
				m.setGatePassOut("<a href='javascript:void' onclick='pdfGatePass(\"" + new String(pId) + "\")'>"+m.getGatePassOut()+"</a>");

				s = s + "<a href='view-gatepass-out-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
						+ " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(gatePass);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewGatePassOutThroughAjax ends");
		return response;
	}
	
	@GetMapping("view-gatepass-out-edit")
	public String editGatePassOut(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editGatePassOut starts");

		try {
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl() + "getStoreForGatePass",
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {

			GatePassOutModel[] gatePass = restClient.getForObject(env.getGatepassUrl() + "editGatePassOut?id=" + id,
					GatePassOutModel[].class);
			List<GatePassOutModel> gatePassList = Arrays.asList(gatePass);

			model.addAttribute("gatePassIdOut", gatePassList.get(0).getGatePassOut());
			model.addAttribute("gatePass", gatePassList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGatePassOut starts");
		return "gatepass/gatepass-out";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-gatepass-out-modal" })
	public @ResponseBody JsonResponse<Object> modalGatePassOut(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGatePassOut starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "modalGatePassOut?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalGatePassOut ends");
		return res;
	}
	
	/**
	 * 
	 * PDF FOR GATE PASS
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-gatepass-out-pdf")
	public void generateGatePassOutPdf(HttpServletResponse response,Model model,@RequestParam("id") String encodeId ){
		
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes()); 
		String  id = (new String(encodeByte));
		
		JsonResponse<List<GatePassOutModel>> jsonResponse = new JsonResponse<List<GatePassOutModel>>();
		try {
			jsonResponse = restClient.getForObject(env.getGatepassUrl() + "modalGatePassOut?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<GatePassOutModel> gatePass = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<GatePassOutModel>>() { });
		
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		for(GatePassOutModel m : gatePass) {
			}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getGatepassUrl() + "restLogoImage-GatePass?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		
		data.put("currdate",curDate);
		String logo = logoList.get(0).getName();
		
	    data.put("logoImage",env.getBaseUrlPath()+"document/hotel/"+logo+"");
		data.put("gatePass", gatePass);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=Gatepass.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("gatepass/gatepass-out-pdf", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@PostMapping("/gatepass-out-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("quotationPFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}
	
	public String saveAllImage(byte[] imageBytes, String ext) {
		logger.info("Method : saveAllImage starts");
		
		String imageName = null;
		
		try {
			
			if(imageBytes!=null) {
//				String[] fileType = inputFile.getContentType().split("/");
				long nowTime = new Date().getTime();
				if(ext.contentEquals("jpeg")) {
					imageName = nowTime+".jpg";
				} else {
					imageName = nowTime+"."+ext;
				}
				
			}

			Path path = Paths.get(env.getFileUploadGatePassUrl() + imageName);
			if(imageBytes !=null) {
				Files.write(path, imageBytes);
				
				ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
				Integer height = 50;
				Integer width = 50;
				
				try{
					BufferedImage img = ImageIO.read(in);
					if(height == 0){
						height = (width * img.getHeight())/img.getWidth();
					}
					if(width == 0){
						width = (height * img.getWidth())/img.getHeight();
					}
					
					BufferedImage outputImage = new BufferedImage(width,
							height, img.getType());
			 
			        Graphics2D g2d = outputImage.createGraphics();
			        g2d.drawImage(img, 0, 0, width, height, null);
			        g2d.dispose();
			        String outputImagePath = env.getFileUploadGatePassUrl()+"thumb/"+imageName;
			        ImageIO.write(outputImage, ext, new File(outputImagePath));

				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : saveAllImage ends");
		return imageName;
	}
}
