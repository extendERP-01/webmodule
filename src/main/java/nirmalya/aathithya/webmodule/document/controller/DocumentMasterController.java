/**
 * 
 */
package nirmalya.aathithya.webmodule.document.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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
import nirmalya.aathithya.webmodule.documentmodel.ApprovalHistoryModel;
import nirmalya.aathithya.webmodule.documentmodel.DocumentModel;
/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "document/" })
public class DocumentMasterController {
	Logger logger = LoggerFactory.getLogger(DocumentMasterController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new Category
	 *
	 */
	@GetMapping(value = { "add-document" })
	public String addDocument(Model model, HttpSession session) {
		logger.info("Method : addDocument starts");
		DocumentModel documentModel = new DocumentModel();
		DocumentModel spa = (DocumentModel) session.getAttribute("sspa");
		/*
		 * dropDown value for category Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getDocumentUrl() + "get-category", DropDownModel[].class);
			List<DropDownModel> catList = Arrays.asList(dropDownModel);
			model.addAttribute("catList", catList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");
		if (spa != null) {
			String ImgName = spa.getDocumentImage();
			String s = "";
			s = "<a class='example-image-link' href='/spaTable/" + ImgName + "' title='" + ImgName + "'>" + ImgName + "</a>";
			spa.setAction(s);
			model.addAttribute("documentModel", spa);
			session.setAttribute("sspa", null);
		} else {
			model.addAttribute("documentModel", documentModel);
		}
		logger.info("Method : addDocument ends");
		return "document/addDocument";
	}

	/*
	 * 
	 * 
	 * Post Mapping to upload file
	 * 
	 */
	@PostMapping(value = { "add-document-uploadfile" })
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
	/*
	 * post Mapping for add category
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-document" })
	public String addNewDocument(@ModelAttribute DocumentModel documentModel, Model model, HttpSession session) {
		logger.info("Method : addNewDocument starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String fileFormat="";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			documentModel.setCreatedBy(userId);
			MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];
				if( fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					 fileFormat	=fileType[1].replaceAll("vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xls");
					  contentName = nowTime + ".xls" ;
				}
				if( fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					 fileFormat	=fileType[1].replaceAll("vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
					  contentName = nowTime + ".docx" ;
				}
					if( fileType[1].contentEquals("vnd.ms-excel")) {
					 fileFormat	=fileType[1].replaceAll("vnd.ms-excel", "xls");
					  contentName = nowTime + ".xls" ;
				}
				documentModel.setDocumentImage(contentName);
				res = restTemplate.postForObject(environmentVaribles.getDocumentUrl() + "rest-addnew-document",
						documentModel, JsonResponse.class);
				

				if ((res.getCode() == null || res.getCode() == "")
						&& (res.getMessage() == null || res.getMessage() == "")) {
					Path path = Paths.get(environmentVaribles.getFileUploadDocumenttUrl() + contentName);
					if (fileType[1].contentEquals("pdf")) {
							Files.write(path, bytes);
					}
					if (fileFormat.contentEquals("xls")) {
						
						Files.write(path, bytes);
						
					}
					if (fileType[1].contentEquals("docx")) {
						
						Files.write(path, bytes);
					
					}else {
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
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(environmentVaribles.getFileUploadDocumenttUrl() + "thumb\\" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				res = restTemplate.postForObject(environmentVaribles.getDocumentUrl() + "rest-addnew-document",
						documentModel, JsonResponse.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != "") {
			session.setAttribute("message", res.getMessage());
			session.setAttribute("sspa", documentModel);
			return "redirect:/document/add-document";
		}

		session.removeAttribute("logoFile");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addNewLogo controller function 'post-mapping' ends");
		logger.info("Method : addNewDocument end");
		return "redirect:view-document";
	}

	/*
	 * 
	 * GetMApping For Listing Document
	 * 
	 * 
	 */
	@GetMapping(value = { "view-document" })
	public String viewDocument(Model model) {
		logger.info("Method : view Document starts");
		/*
		 * dropDown value for category Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getDocumentUrl() + "get-category", DropDownModel[].class);
			List<DropDownModel> catList = Arrays.asList(dropDownModel);
			model.addAttribute("catList", catList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : view Document ends");
		return "document/viewDocument";
	}

	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-document-throughAjax" })
	public @ResponseBody DataTableResponse viewDocument(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2,@RequestParam String param3) {
		logger.info("Method :view Document (through ajax) starts");
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
			JsonResponse<List<DocumentModel>> jsonResponse = new JsonResponse<List<DocumentModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getDocumentUrl() + "get-all-document",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<DocumentModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DocumentModel>>() {
					});
			String s = "";
			for (DocumentModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getDocument().getBytes());
				List<DropDownModel> subCatLists = new ArrayList<DropDownModel>();
				try {
					DropDownModel[] dropDownModel = restTemplate
							.getForObject(environmentVaribles.getDocumentUrl() + "get-files?id="+m.getDocument(), DropDownModel[].class);
					subCatLists = Arrays.asList(dropDownModel);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String x="";
				int inc =0;
				for (DropDownModel n : subCatLists) {
					x=x+"<a href='javascript:void' onclick='viewDocument(\"" +  n.getName()+ "\")'>version1."+ inc++ +"</a><br/>";
					
				if ((m.getApprovalStatus() == 2) || (m.getApprovalStatus() == 1)) {
					s = "";
					s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style='font-size:20px'></i></a>";

					s = s + "<a href='javascript:void(0)' onclick='deleteDocument(\"" + new String(pId)
							+ "\")'><i class='fa fa-trash' style='font-size:20px'></i></a> ";
					
					m.setAction(s);
				} else {
					s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style='font-size:20px'></i></a>";
					s = s + " &nbsp;&nbsp <a href='edit-document?id=" + new String(pId)
							+ "'  title='Edit'><i class='fa fa-edit' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
					s = s + "<a href='javascript:void(0)' onclick='deleteDocument(\"" + new String(pId)
							+ "\")'><i class='fa fa-trash' style='font-size:20px'></i></a> ";

				}
				}
				
				m.setDocumentImage(x);
				
				if(m.getApprovalStatus()!=1 && m.getApprovalStatus()!=null &&  m.getApprovalStatus()!=0) {
					s =s+ "<a data-toggle='modal' title='Response' data-target='#myModal' href='javascript:void(0)' onclick='viewComment(\""
							+ new String(pId) + "\")'><i class='fa fa-tasks' style='font-size:20px'>&nbsp;&nbsp;&nbsp;</i></a>";

				}
				
				if(m.getApproverStageNo()!=null) {
					s = s + "<a data-toggle='modal' title='Approved History' data-target='#myModalhistory' href='javascript:void(0)' onclick='viewHistory(\""
							+ new String(pId) + "\")'><i class='fa fa-book' aria-hidden='true' style='font-size:20px'></i></a>";
					}
					
					m.setAction(s);
				
				s = "";
				if (m.getApprovalStatus() == 3)
					m.setStatus("Re-check");
				else if (m.getApprovalStatus() == 1)
					m.setStatus("Approved");
				else if (m.getApprovalStatus() == 2)
					m.setStatus("Rejected");
				else
					m.setStatus("Pending");

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : view Document  (through ajax) ends");
		return response;
	}

	/**
	 * View selected Country in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-document-model" })
	public @ResponseBody JsonResponse<DocumentModel> modelView(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelView starts");

		JsonResponse<DocumentModel> resp = new JsonResponse<DocumentModel>();

		try {
			resp = restTemplate.getForObject(environmentVaribles.getDocumentUrl() + "get-document-byId?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method : modelView ends");
		return resp;
	}

	/**
	 * View selected Country in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-document-history" })
	public @ResponseBody JsonResponse<List<ApprovalHistoryModel>> modelViewOfHistory(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelViewOfHistory starts");

		JsonResponse<List<ApprovalHistoryModel>> resp = new JsonResponse<List<ApprovalHistoryModel>>();

		try {
			resp = restTemplate.getForObject(environmentVaribles.getDocumentUrl() + "get-documentHistory-byId?id=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method : modelViewOfHistory ends");
		return resp;
	}


	/*
	 * 
	 * GetMapping for delete document
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-document" })
	public @ResponseBody JsonResponse<Object> deleteDocument(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : delete Document starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = null;
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getDocumentUrl() + "delete-document?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : delete Document ends");
		return resp;
	}

	/*
	 * 
	 * 
	 * GetMapping for Edit Document
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-document" })
	public String editDocument(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editDocument starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		DocumentModel documentModel = new DocumentModel();
		JsonResponse<DocumentModel> jsonResponse = new JsonResponse<DocumentModel>();
		/*
		 * dropDown value for category Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getDocumentUrl() + "get-category", DropDownModel[].class);
			List<DropDownModel> catList = Arrays.asList(dropDownModel);
			model.addAttribute("catList", catList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {

			jsonResponse = restTemplate.getForObject(
					environmentVaribles.getDocumentUrl() + "get-document-byId?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		documentModel = mapper.convertValue(jsonResponse.getBody(), DocumentModel.class);
		session.setAttribute("message", "");

		String ImgName = documentModel.getDocumentImage();
		if (ImgName != null || ImgName != "") {
			String s = "";
			if(ImgName.endsWith(".xls")|| ImgName.endsWith(".docx")) {
				s = "/document/excel/" + ImgName;
				documentModel.setAction(s);
			}
			else {
			s = "/document/image/" + ImgName;
			documentModel.setAction(s);
			}
			session.setAttribute("logoImageNameForEdit", documentModel.getDocumentImage());
		}
		model.addAttribute("documentModel", documentModel);
		logger.info("Method : editDocument ends");
		return "document/addDocument";
	}

	/*
	 * 
	 * GetMApping For Listing Country
	 * 
	 * 
	 */
	@GetMapping(value = { "view-document-approval" })
	public String viewDocumentApproval(Model model) {
		logger.info("Method : view Document Approval starts");
		/*
		 * dropDown value for category Name
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate
					.getForObject(environmentVaribles.getDocumentUrl() + "get-category", DropDownModel[].class);
			List<DropDownModel> catList = Arrays.asList(dropDownModel);
			model.addAttribute("catList", catList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : view Document Approval ends");
		return "document/viewDocumentApproval";
	}

	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-document-approval-throughAjax" })
	public @ResponseBody DataTableResponse viewDocumentApproval(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2,@RequestParam String param3, HttpSession session) {
		logger.info("Method :view  Document Approval(through ajax) starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String UserId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setUserId(UserId);
			JsonResponse<List<DocumentModel>> jsonResponse = new JsonResponse<List<DocumentModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getDocumentUrl() + "get-all-document-approve",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<DocumentModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DocumentModel>>() {
					});
			
			String s = "";
			
			for (DocumentModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getDocument().getBytes());
				
				List<DropDownModel> subCatLists = new ArrayList<DropDownModel>();
				try {
					DropDownModel[] dropDownModel = restTemplate
							.getForObject(environmentVaribles.getDocumentUrl() + "get-files?id="+m.getDocument(), DropDownModel[].class);
					subCatLists = Arrays.asList(dropDownModel);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String x="";
				int inc =0;
				for (DropDownModel n : subCatLists) {
					x=x+"<a href='javascript:void' onclick='viewDocument(\"" +  n.getName()+ "\")'>version1."+ inc++ +"</a><br/>";
				
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style='font-size:20px'></i></a> &nbsp;&nbsp;";
				if(m.getCurrentStageNo()!=0) {
				s = s + "<a data-toggle='modal' title='Approved History' data-target='#myModalhistory' href='javascript:void(0)' onclick='viewHistory(\""
						+ new String(pId) + "\")'><i class='fa fa-book' aria-hidden='true' style='font-size:20px'></i></a>";
				}
				
				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApprovalStatus() != 1)) {
					if (m.getApprovalStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardDocument(\""
								+ new String(pId) + "\")'><i class='fa fa-forward' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectDocument(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectDocument(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='Re-check' href='javascript:void(0)' onclick='rejectDocument(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
				}
				}
				m.setDocumentImage(x);
				m.setAction(s);
				s = "";
				if (m.getApprovalStatus() == 3)
					m.setStatus("Re-check");
				else if (m.getApprovalStatus() == 1)
					m.setStatus("Approved");
				else if (m.getApprovalStatus() == 2)
					m.setStatus("Rejected");
				else
					m.setStatus("Open");
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : view  Document Approval(through ajax) (through ajax) ends");
		return response;
	}

	/*
	 * Forward Document to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-document-approval-action" })
	public @ResponseBody JsonResponse<Object> saveDocumentApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : saveDocumentApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(environmentVaribles.getDocumentUrl() + "save-document-approval-action?id="
					+ id + "&createdBy=" + userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : saveDocumentApprovalAction ends");
		return resp;
	}
	/*
	 * Reject Requisition
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-document-reject-action" })
	public @ResponseBody JsonResponse<Object> saveDocumentRejectAction(Model model,
			@RequestBody DocumentModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : saveDocumentRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getDocument());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;

		reqobject.setCreatedBy(userId);
		reqobject.setDocument(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(environmentVaribles.getDocumentUrl() + "save-document-reject-action",
					reqobject, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : saveDocumentRejectAction ends");
		return res;
	}
}