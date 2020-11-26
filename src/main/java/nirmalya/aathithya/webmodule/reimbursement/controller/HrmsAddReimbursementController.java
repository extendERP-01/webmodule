package nirmalya.aathithya.webmodule.reimbursement.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsReimbursementModel;
import nirmalya.aathithya.webmodule.reimbursement.model.HrmsTravelingRequisituionModel;

@Controller
@RequestMapping(value = "reimbursement")
public class HrmsAddReimbursementController {

	Logger logger = LoggerFactory.getLogger(HrmsAddReimbursementController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * GetMapping for Add reimbursement view page
	 */
	@GetMapping("/add-reimbursement")
	public String addReimbursemente(Model model, HttpSession session) {

		logger.info("Method : addReimbursemente  starts");

		String userId = "";
		HrmsReimbursementModel reimbursement = new HrmsReimbursementModel();
		HrmsReimbursementModel sessionReimbursement = (HrmsReimbursementModel) session
				.getAttribute("sessionReimbursement");

		String message = (String) session.getAttribute("message");
		userId = (String) session.getAttribute("USER_ID");
		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionReimbursement != null) {
			model.addAttribute("reimbursement", sessionReimbursement);
			session.setAttribute("sessionReimbursement", null);
		} else {
			model.addAttribute("reimbursement", reimbursement);
		}
		/*
		 * for viewing drop down list of traveling reimbursement
		 */
		try {
			DropDownModel[] travelReq = restClient.getForObject(env.getReimbursementUrl() + "getReqList?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> travelReqList = Arrays.asList(travelReq);
			model.addAttribute("travelReqList", travelReqList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getReimbTypeList",
					DropDownModel[].class);
			List<DropDownModel> reimbTypeList = Arrays.asList(reimType);
			model.addAttribute("reimbTypeList", reimbTypeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addReimbursemente  ends");

		return "reimbursement/add-reimbursement-form";
	}

	/*
	 * post mapping for add reimbursement
	 */

	@SuppressWarnings({ "restriction", "unchecked" })
	@RequestMapping(value = "/add-reimbursement-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addReimbursemetThroughAjax(
			@RequestBody List<HrmsReimbursementModel> hrmsReimbursementModal, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addReimbursemetThroughAjax function starts");
		int imagecount = 0;
		for (HrmsReimbursementModel a : hrmsReimbursementModal) {

			if (a.getFileName() != null && a.getFileName() != "") {
				String delimiters = "\\.";
				String[] x = a.getFileName().split(delimiters);

				for (String s1 : a.getRembFile()) {

					String[] se = s1.split("base64,");
					/*
					 * if (se[0].contains("pdf")) { try { byte[] bytes = new
					 * sun.misc.BASE64Decoder().decodeBuffer(se[1]); String pdfName =
					 * saveAllPdf(bytes); a.setFileName(pdfName); } catch (Exception ex) {
					 * ex.printStackTrace(); } } else { try { byte[] bytes = new
					 * sun.misc.BASE64Decoder().decodeBuffer(se[1]); String imageName =
					 * saveAllImage(bytes); a.setFileName(imageName); } catch (Exception e) {
					 * e.printStackTrace(); } }
					 */

				}

				try {
					a.setEmpId((String) session.getAttribute("USER_ID"));
					a.setCreatedBy((String) session.getAttribute("USER_ID"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					List<String> images = (List<String>) session.getAttribute("imageNameFromDnForEdit");

					if (!images.isEmpty()) {
						a.setFileName(images.get(imagecount));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					a.setEmpId((String) session.getAttribute("USER_ID"));
					a.setCreatedBy((String) session.getAttribute("USER_ID"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			imagecount = imagecount + 1;
		}

		try {
			res = restClient.postForObject(env.getReimbursementUrl() + "restAddReimbursement", hrmsReimbursementModal,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addReimbursemetThroughAjax  Ends");
		return res;
	}
	/*
	 * for save image in the folder and return image name
	 */

	public String saveAllImage(byte[] imageBytes) {
		logger.info("Method : saveAllImage starts");

		String imageName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				imageName = nowTime + ".png";
			}

			Path path = Paths.get(env.getFileUploadReimbursementUrl() + imageName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);

				ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
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
					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					ImageIO.write(imageBuff, "png", buffer);

					byte[] thumb = buffer.toByteArray();

					Path pathThumb = Paths.get(env.getFileUploadReimbursementUrl() + "thumb/" + imageName);
					Files.write(pathThumb, thumb);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllImage ends");
		return imageName;
	}

	/*
	 * for save all pdf in folder and return pdf name
	 */

	public String saveAllPdf(byte[] imageBytes) {
		logger.info("Method : saveAllPdf starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".pdf";
			}

			Path path = Paths.get(env.getFileUploadReimbursementUrl() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllPdf ends");
		return pdfName;
	}

	/*
	 * for traveling reimbursement details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-requisition-onchange" })
	public @ResponseBody JsonResponse<HrmsTravelingRequisituionModel> getRreimbursement(Model model,
			@RequestBody String travelReq, BindingResult result) {
		logger.info("Method :getRreimbursement starts");

		JsonResponse<HrmsTravelingRequisituionModel> res = new JsonResponse<HrmsTravelingRequisituionModel>();

		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getRequisitionDtls?travelReq=" + travelReq,
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

		logger.info("Method : getRreimbursement starts  ends");
		return res;
	}

	/*
	 * Get Mapping view reimbursement page
	 */
	@GetMapping("/view-reimbursement")
	public String viewReimbursement(Model model, HttpSession session) {

		logger.info("Method : viewReimbursement starts");

		/*
		 * for viewing drop down employee list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getReimbursementUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewReimbursement ends");

		return "reimbursement/view-reimbursement-form";
	}

	/*
	 * For view reimbursement through Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-ThroughAjax")
	public @ResponseBody DataTableResponse viewReimbursementThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {

		logger.info("Method : viewReimbursementThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		String userId = "";

		try {

			userId = (String) session.getAttribute("USER_ID");

			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setUserId(userId);

			JsonResponse<List<HrmsReimbursementModel>> jsonResponse = new JsonResponse<List<HrmsReimbursementModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getReimbursementDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsReimbursementModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsReimbursementModel>>() {
					});

			String s = "";
			for (HrmsReimbursementModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getRembId().getBytes());
				if (m.getReqStatus() == 0) {
					s = s + "<a href='view-reimbursement-edit?rembId=" + new String(encodeId)
							+ "' title='Edit'><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>";

				}
				s = s + "&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				s = s + " &nbsp; <a data-toggle='modal' title='View'  href='javascript:void' onclick='viewHistoryInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-history' aria-hidden='true' style=\"font-size:20px\"></i></a>";
				m.setAction(s);
				s = "";
				if (m.getReqStatus() == 3)
					m.setStatusName("Returned");
				else if (m.getReqStatus() == 1)
					m.setStatusName("Approved");
				else if (m.getReqStatus() == 2)
					m.setStatusName("Rejected");
				else
					m.setStatusName("Open");

				System.out.println("###" + m.getReimbType());
				if (m.getReimbType().contentEquals("2")) {
					m.setReimbName("Other");
				}

				else {
					m.setReimbName("Travel");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewReimbursementThroughAjax ends");
		System.out.println("@@@@" + response);

		return response;
	}

	/*
	 * For Modal view of reimbursement
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-reimbursement-modalView" })
	public @ResponseBody JsonResponse<List<HrmsReimbursementModel>> modalAssignmentEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmsReimbursementModel>> response = new JsonResponse<List<HrmsReimbursementModel>>();
		try {
			response = restClient.getForObject(env.getReimbursementUrl() + "getReimbusementById?reimId=" + id,
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
		logger.info("Method : modalAssignmentEdu  ends ");
		return response;
	}

	/*
	 * for Edit reimbursement
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-edit")
	public String editReimbursement(Model model, @RequestParam("rembId") String encodeId, HttpSession session) {

		logger.info("Method :editReimbursement starts");
		List<HrmsReimbursementModel> reimbursement = null ;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String userId = "";
		userId = (String) session.getAttribute("USER_ID");
		String id = (new String(decodeId));
		JsonResponse<List<HrmsReimbursementModel>> response = new JsonResponse<List<HrmsReimbursementModel>>();
		try {
			response = restClient.getForObject(env.getReimbursementUrl() + "getReimbusementById?reimId=" + id,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			  reimbursement = mapper.convertValue(response.getBody(),
					new TypeReference<List<HrmsReimbursementModel>>() {
					});
			if (reimbursement != null) {
				reimbursement.get(0).setEditId("edit");

				try {
					DropDownModel[] policy = restClient.getForObject(
							env.getReimbursementUrl() + "getPolicyList?reimbType=" + reimbursement.get(0).getRembType(),
							DropDownModel[].class);
					List<DropDownModel> policyList = Arrays.asList(policy);

					model.addAttribute("policyList", policyList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			List<String> l = new ArrayList<String>();
			for (HrmsReimbursementModel a : reimbursement) {

				String image = a.getFileName();

				l.add(image);
				String variable = env.getBaseUrlPath();
				String extension[] = image.split("\\.");
				if (extension[1] == "pdf") {
					a.setAction(variable + "document/reimbursement/" + image + "target='_balnk'");
				} else {
					a.setAction(variable + "document/reimbursement/" + image + "");
				}

			}

			/*
			 * for viewing drop down list of traveling reimbursement
			 */
			try {
				DropDownModel[] travelReq = restClient
						.getForObject(env.getReimbursementUrl() + "getReqList?id=" + userId, DropDownModel[].class);
				List<DropDownModel> travelReqList = Arrays.asList(travelReq);
				model.addAttribute("travelReqList", travelReqList);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * for viewing drop down list of reimbursement type
			 */
			try {
				DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getReimbTypeList",
						DropDownModel[].class);
				List<DropDownModel> reimbTypeList = Arrays.asList(reimType);
				model.addAttribute("reimbTypeList", reimbTypeList);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			session.setAttribute("imageNameFromDnForEdit", l);
			model.addAttribute("reimbursement", reimbursement);
			System.out.println("@@@@@@" + reimbursement);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editReimbursement ends");
		 
			if (reimbursement.get(0).getReimbType().contentEquals("2")) {
				return "reimbursement/add-reimbursement-other";
			}
			else {
				return "reimbursement/add-reimbursement-form";
			}
	}

	/*
	 * For Modal traveling reimbursement View
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-reimbursement-approve-stage-modalView" })
	public @ResponseBody JsonResponse<List<HrmsReimbursementModel>> modaltravelingAprove(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<HrmsReimbursementModel>> response = new JsonResponse<List<HrmsReimbursementModel>>();
		try {
			response = restClient.getForObject(env.getReimbursementUrl() + "getReimbusementById?reimId=" + id,
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
		logger.info("Method : modalAssignmentEdu  ends ");
		return response;
	}

	/*
	 * Get Mapping view traveling reimbursement approval process
	 */
	@GetMapping("/view-reimbursement-approve-stage")
	public String viewReimbursementApprove(Model model, HttpSession session) {

		logger.info("Method : viewReimbursementApprove starts");

		logger.info("Method : viewReimbursementApprove ends");

		return "reimbursement/view-reimbursement-approve";
	}

	/*
	 * For view reimbursement approval process dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-reimbursement-approve-stage-ThroughAjax")
	public @ResponseBody DataTableResponse viewReimbursementApproveajax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {
		logger.info("Method : viewReimbursementApproveajax statrs");

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
			tableRequest.setUserId(UserId);

			JsonResponse<List<HrmsReimbursementModel>> jsonResponse = new JsonResponse<List<HrmsReimbursementModel>>();

			jsonResponse = restClient.postForObject(env.getReimbursementUrl() + "getTravelingreimbursementDetailsFirst",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsReimbursementModel> travelingreimbursement = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsReimbursementModel>>() {
					});

			String s = "";

			for (HrmsReimbursementModel m : travelingreimbursement) {
				byte[] pId = Base64.getEncoder().encode(m.getRembId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";

				if ((m.getCurrentStageNo() == m.getApproversStageNo()) && (m.getReqStatus() != 1)) {
					if (m.getReqStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardreimbursement(\""
								+ new String(pId) + "\")'><i class='fa fa-forward' style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectreimbursement(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send' style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectreimbursement(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectreimbursement(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'style=\"font-size:20px\"></i></a> &nbsp;&nbsp; ";
				}
				m.setAction(s);
				s = "";

				if (m.getReqStatus() == 3)
					m.setStatusName("Returned");
				else if (m.getReqStatus() == 1)
					m.setStatusName("Approved");
				else if (m.getReqStatus() == 2)
					m.setStatusName("Rejected");
				else
					m.setStatusName("Open");
				
				if (m.getReimbType().contentEquals("2")) {
					m.setReimbName("Other");
				}

				else {
					m.setReimbName("Travel");
				}
				

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(travelingreimbursement);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewReimbursementApproveajax Theme ends");

		return response;
	}

	/*
	 * Forward reimbursement to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "save-reimbursement-approval-action" })
	public @ResponseBody JsonResponse<Object> savereimbursementApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : savereimbursementApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getReimbursementUrl() + "save-reimbursement-approval-action?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : savereimbursementApprovalAction ends");
		return resp;
	}
	/*
	 * Reject reimbursement
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "save-reimbursement-reject-action" })
	public @ResponseBody JsonResponse<Object> savereimbursementRejectAction(Model model,
			@RequestBody HrmsReimbursementModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : savereimbursementRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getRembId());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		reqobject.setCreatedBy(userId);
		reqobject.setReqId(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "save-reimbursement-reject-action?id=" + reqstnId
					+ "&createdBy=" + userId + "&desc=" + reqobject.getReturnDesc() + "&rejectType="
					+ reqobject.getRejectionType(), JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : savereimbursementRejectAction ends");
		return res;
	}

	/*
	 * For Modal traveling reimbursement View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-reimbursement-history" })
	public @ResponseBody JsonResponse<List<Object>> viewHistoryInModel(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modaltravelingAprove starts");

		JsonResponse<List<Object>> res = new JsonResponse<List<Object>>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getReimbursementUrl() + "getTravelingreimbursementHistoryById?id=" + id,
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
		logger.info("Method :modaltravelingAprove ends");
		return res;
	}

	/*
	 * for validating the expense amount
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-requisition-valid-policy" })
	public @ResponseBody JsonResponse<DropDownModel> getValidPolicy(Model model, @RequestBody String policyId,
			BindingResult result, HttpSession session) {
		logger.info("Method :getValidPolicy starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String empId = "";
		try {
			empId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			res = restClient.getForObject(
					env.getReimbursementUrl() + "getValidPolicy?policyId=" + policyId + "&empId=" + empId,
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

		logger.info("Method : getValidPolicy starts  ends");
		return res;
	}

	/*
	 * for get policy from reimbursement type
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-reimbursement-requisition-get-policy" })
	public @ResponseBody JsonResponse<DropDownModel> getPolicyReimbOnchange(Model model, @RequestBody String reimType,
			BindingResult result, HttpSession session) {
		logger.info("Method :getPolicyReimbOnchange starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String empId = "";
		try {
			empId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			res = restClient.getForObject(
					env.getReimbursementUrl() + "getPolicyReimbOnchange?reimType=" + reimType + "&empId=" + empId,
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

		logger.info("Method : getPolicyReimbOnchange starts  ends");
		return res;
	}

	/*
	 * GetMapping for Add other reimbursement view page
	 */
	@GetMapping("/add-reimbursement-other")
	public String addOtherReimbursemente(Model model, HttpSession session) {

		logger.info("Method : addOtherReimbursemente  starts");

		String userId = "";
		HrmsReimbursementModel reimbursement = new HrmsReimbursementModel();
		HrmsReimbursementModel sessionReimbursement = (HrmsReimbursementModel) session
				.getAttribute("sessionReimbursement");

		String message = (String) session.getAttribute("message");
		userId = (String) session.getAttribute("USER_ID");
		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionReimbursement != null) {
			model.addAttribute("reimbursement", sessionReimbursement);
			session.setAttribute("sessionReimbursement", null);
		} else {
			model.addAttribute("reimbursement", reimbursement);
		}

		/*
		 * for viewing drop down list of traveling reimbursement
		 */
		try {
			DropDownModel[] travelReq = restClient.getForObject(env.getReimbursementUrl() + "getReqList?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> travelReqList = Arrays.asList(travelReq);
			model.addAttribute("travelReqList", travelReqList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list of reimbursement type
		 */
		try {
			DropDownModel[] reimType = restClient.getForObject(env.getReimbursementUrl() + "getReimbTypeList",
					DropDownModel[].class);
			List<DropDownModel> reimbTypeList = Arrays.asList(reimType);
			model.addAttribute("reimbTypeList", reimbTypeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addOtherReimbursemente  ends");

		return "reimbursement/add-reimbursement-other";
	}

	/*
	 * post mapping for add reimbursement
	 */

	@SuppressWarnings({ "restriction", "unchecked" })
	@RequestMapping(value = "/add-reimbursement-other-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addOtherReimbursemetThroughAjax(
			@RequestBody List<HrmsReimbursementModel> hrmsReimbursementModal, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addReimbursemetThroughAjax function starts");
		int imagecount = 0;
		for (HrmsReimbursementModel a : hrmsReimbursementModal) {

			if (a.getFileName() != null && a.getFileName() != "") {
				String delimiters = "\\.";
				String[] x = a.getFileName().split(delimiters);

				for (String s1 : a.getRembFile()) {

					String[] se = s1.split("base64,");
					/*
					 * if (se[0].contains("pdf")) { try { byte[] bytes = new
					 * sun.misc.BASE64Decoder().decodeBuffer(se[1]); String pdfName =
					 * saveAllPdf(bytes); a.setFileName(pdfName); } catch (Exception ex) {
					 * ex.printStackTrace(); } } else { try { byte[] bytes = new
					 * sun.misc.BASE64Decoder().decodeBuffer(se[1]); String imageName =
					 * saveAllImage(bytes); a.setFileName(imageName); } catch (Exception e) {
					 * e.printStackTrace(); } }
					 */

				}

				try {
					a.setEmpId((String) session.getAttribute("USER_ID"));
					a.setCreatedBy((String) session.getAttribute("USER_ID"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					List<String> images = (List<String>) session.getAttribute("imageNameFromDnForEdit");

					if (!images.isEmpty()) {
						a.setFileName(images.get(imagecount));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					a.setEmpId((String) session.getAttribute("USER_ID"));
					a.setCreatedBy((String) session.getAttribute("USER_ID"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			imagecount = imagecount + 1;
		}
		try {
			res = restClient.postForObject(env.getReimbursementUrl() + "restAddOtherReimbursement",
					hrmsReimbursementModal, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addReimbursemetThroughAjax  Ends");
		return res;
	}

}
