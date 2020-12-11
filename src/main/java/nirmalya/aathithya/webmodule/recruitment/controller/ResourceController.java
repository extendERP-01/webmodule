package nirmalya.aathithya.webmodule.recruitment.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
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

import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.recruitment.model.ResourceModel;

@Controller
@RequestMapping(value = "recruitment")
public class ResourceController {

	Logger logger = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("/view-resource-ThroughAjax")
	public @ResponseBody DataTableResponse viewResource(Model model, HttpServletRequest request) {

		logger.info("Method : viewResource web statrs");

		DataTableResponse response = new DataTableResponse();

		try {
			JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getResource", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ResourceModel> resource = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ResourceModel>>() {
					});

			String s = "";
			for (ResourceModel m : resource) {

				if (m.getAsgnStatus()) {
					m.setStatus("Vendor Allocated");
				} else {
					m.setStatus("Vendor Not Allocated");
				}

				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getRequisitionId().getBytes());

				s = s + "<a href='view-resource-add-vendor-details?id=" + new String(encodeId)
						+ "' ><button type='button' class='btn btn-primary'>Add Vendor</button></a>";
				m.setVendor(s);
				s = "";
				s = s + "<a href='view-requistion-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:16px\"></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setData(resource);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewResource web ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-resource-add-vendor-details")
	public String addVendor(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
		logger.info("Method : addVendor starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		DropDownModel[] vendorName = restTemplate.getForObject(env.getRecruitment() + "getVendor",
				DropDownModel[].class);
		List<DropDownModel> vendorList = Arrays.asList(vendorName);
		model.addAttribute("vendorList", vendorList);

		JsonResponse<ResourceModel> jsonResponse = new JsonResponse<ResourceModel>();

		try {
			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getDetails?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		ResourceModel vendor = mapper.convertValue(jsonResponse.getBody(), ResourceModel.class);
		session.setAttribute("message", "");
		
		model.addAttribute("vendor", vendor);
		logger.info("Method : addVendor ends");

		return "recruitment/view-resource-add-vendor";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("view-resource-vendor-ajax")
	public @ResponseBody JsonResponse<Object> setVendor(Model model, @RequestBody ResourceModel index,
			BindingResult result, HttpSession session) {
		logger.info("Method : setVendor starts");

	
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(env.getRecruitment() + "setVendor", index, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		
		logger.info("Method : setVendor ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-shortList-ThroughAjax")
	public @ResponseBody DataTableResponse viewShortList(Model model, HttpServletRequest request) {

		logger.info("Method : viewShortList web statrs");

		DataTableResponse response = new DataTableResponse();

		try {
			JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getShortList", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ResourceModel> resource = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ResourceModel>>() {
					});

			String s = "";
			for (ResourceModel m : resource) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getRequisitionId().getBytes());

				s = s + "<a href='view-shortList-candidate-details?id=" + new String(encodeId)
						+ "' ><button type='button' class='btn btn-danger'>Upload Resume</button></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setData(resource);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewShortList web ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-shortList-candidate-details")
	public String viewCandidate(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
		logger.info("Method : viewCandidate starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		

		DropDownModel[] vendorName = restTemplate.getForObject(env.getRecruitment() + "getVendorSelected?id=" + id,
				DropDownModel[].class);
		List<DropDownModel> vendorList = Arrays.asList(vendorName);
		
		model.addAttribute("vendorList", vendorList);

		JsonResponse<ResourceModel> jsonResponse = new JsonResponse<ResourceModel>();

		try {
			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getDetails?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		ResourceModel vendor = mapper.convertValue(jsonResponse.getBody(), ResourceModel.class);
		session.setAttribute("message", "");
		
		model.addAttribute("shortList", vendor);
		logger.info("Method : viewCandidate ends");

		return "recruitment/view-resource-add-shortlist";
	}

	// SAVE ALL FILE

	public String saveAllPdf(byte[] imageBytes) {
		logger.info("Method : saveAllPdf starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".pdf";
			}

			Path path = Paths.get(env.getFileUploadEmployee() + pdfName);
			
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllPdf ends");
		return pdfName;
	}

	public String saveAllDocx(byte[] imageBytes) {
		logger.info("Method : saveAllDocx starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".docx";
			}

			Path path = Paths.get(env.getFileUploadEmployee() + pdfName);
		
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDocx ends");
		return pdfName;
	}

	public String saveAllDoc(byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".doc";
			}

			Path path = Paths.get(env.getFileUploadEmployee() + pdfName);
			
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-shortList-cadidate-list", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addShortListcandidate(@RequestBody List<ResourceModel> shortList,
			Model model, HttpSession session) {
		logger.info("Method : addShortListcandidate function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		Boolean status = true;
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		int imagecount = 0;
		for (ResourceModel a : shortList) {

			a.setCreatedBy(userId);

			if (a.getDocument() != null && a.getDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getDocument().split(delimiters);

				if (x[1].matches("pdf")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = Base64.getDecoder().decode(s1);

							String pdfName = saveAllPdf(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("docx")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = Base64.getDecoder().decode(s1);
							String pdfName = saveAllDocx(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("doc")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = Base64.getDecoder().decode(s1);
							String pdfName = saveAllDoc(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					status = false;
				}
			}
			imagecount = imagecount + 1;
		}

		if (status) {
			try {
				
				res = restTemplate.postForObject(env.getRecruitment() + "addShortListCandidate", shortList,
						JsonResponse.class);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		} else {
			res.setMessage("Selected File Must be Pdf, doc or docs");
			res.setCode("Select Proper File");
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addShortListcandidate function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-resource-shortListDetails-ajax")
	public @ResponseBody DataTableResponse viewList(Model model, HttpServletRequest request) {

		logger.info("Method : viewShortList web statrs");

		DataTableResponse response = new DataTableResponse();

		try {
			JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getShortListCandidate",
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ResourceModel> resource = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ResourceModel>>() {
					});

			int i = 0;
			for (ResourceModel m : resource) {
				i++;
				String s = "";
				if (m.getStatus().equals("2")) {
					s = s + "<th id='status'><div><input type='radio' id='statusShort_" + i
							+ "' name='status1' value='1' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Short List</span></div>"
							+ "<div><input type='radio' id='statusHold_" + i
							+ "' name='status1' value='2' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' checked /><span class='chkboxtxt'>Hold</span></div>"
							+ "<div><input type='radio' id='statusReject_" + i
							+ "' name='status1' value='3' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Reject</span></div></th>";
				} else {
					s = s + "<th id='status'><div><input type='radio' id='statusShort_" + i
							+ "' name='status1' value='1' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Short List</span></div>"
							+ "<div><input type='radio' id='statusHold_" + i
							+ "' name='status1' value='2' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Hold</span></div>"
							+ "<div><input type='radio' id='statusReject_" + i
							+ "' name='status1' value='3' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Reject</span></div></th>";
				}

				String x = "";

				if (m.getStatus().equals("0") || m.getStatus().equals("2")) {
					x = x + "<button class='btn btn-info' id='" + m.getCandidateId() + "' onclick='scheduleInterview(\""
							+ m.getRequisitionId() + "\",\"" + m.getCandidateId()
							+ "\");' style='display:none;' >Schedule</button>";
				}
				if (m.getStatus().equals("1")) {
					x = x + "<button class='btn btn-danger' onclick='interviewScheduled(\"" + m.getRequisitionId()
							+ "\",\"" + m.getCandidateId() + "\");'>Scheduled</button></th>";
				}

				/*
				 * if (m.getStatus().equals("2")) { x = x +
				 * "<button class='btn btn-danger' id='" + m.getCandidateId() +
				 * "' style='display:block;' >Hold</button>"; }
				 */

				if (m.getStatus().equals("3")) {
					x = x + "<button class='btn btn-danger'>Rejected</button>";
				}

				String str = "<a href='/document/employee/" + m.getDocument()
						+ "' target='_blank'><img src=\"../assets/images/pdf_demo.png\" height=\"50\" width=\"35\"></a>";

				m.setAction(x);
				m.setDocument(str);
				if (m.getStatus().equals("0") || m.getStatus().equals("2")) {
					m.setStatus(s);
				} else if (m.getStatus().equals("1")) {
					m.setStatus("<th>Interview Scheduled</th>");
				} /*
					 * else if(m.getStatus().equals("2")) {
					 * m.setStatus("<th>Candidate In Hold</th>"); }
					 */ else if (m.getStatus().equals("3")) {
					m.setStatus("<th>Candidate Rejected</th>");
				}

			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setData(resource);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewShortList web ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-resource-modal")
	public @ResponseBody JsonResponse<List<ResourceModel>> viewModal(Model model, HttpSession session) {
		logger.info("Method : viewModal starts");

		JsonResponse<List<ResourceModel>> res = new JsonResponse<List<ResourceModel>>();

		res = restTemplate.getForObject(env.getRecruitment() + "getEmployee", JsonResponse.class);

		if (res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		} 
		logger.info("Method : viewModal ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-resource-modal-edit-details")
	public @ResponseBody JsonResponse<List<ResourceModel>> viewEditModal(Model model, HttpSession session,
			@RequestParam String id) {
		logger.info("Method : viewEditModal starts");

		JsonResponse<List<ResourceModel>> res = new JsonResponse<List<ResourceModel>>();

		res = restTemplate.getForObject(env.getRecruitment() + "getInterviewerDtls?id=" + id, JsonResponse.class);

		if (res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		 
		logger.info("Method : viewEditModal ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-resource-shortList-hold", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addHold(@RequestBody ResourceModel shortList, Model model,
			HttpSession session) {
		logger.info("Method : addHold function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(env.getRecruitment() + "addHold", shortList, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addHold function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-resource-shortList-interview-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addShortListInterview(@RequestBody ResourceModel shortList, Model model,
			HttpSession session) {
		logger.info("Method : addShortListInterview function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		shortList.setCreatedBy(userId);

		try {
			res = restTemplate.postForObject(env.getRecruitment() + "addShortListInterview", shortList,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addShortListInterview function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-resource-interview-feedback-ajax")
	public @ResponseBody DataTableResponse addFeedBack(Model model, HttpServletRequest request, HttpSession session) {
		logger.info("Method : viewCandidate starts");

		DataTableResponse response = new DataTableResponse();

		JsonResponse<List<ResourceModel>> jsonResp = new JsonResponse<List<ResourceModel>>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			jsonResp = restTemplate.getForObject(env.getRecruitment() + "getAllDetails?id=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<ResourceModel> candidate = mapper.convertValue(jsonResp.getBody(),
				new TypeReference<List<ResourceModel>>() {
				});

		String s = "";

		for (ResourceModel m : candidate) {
			s = "";
			byte[] encodeId = Base64.getEncoder().encode(m.getRequisitionId().getBytes());
			Integer encodeId1 = m.getCandidateId();

			s = s + "<a href='view-employee-edit-feedback?id=" + new String(encodeId) + "&index=" + encodeId1
					+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>";

			String str = "<a href='/document/employee/" + m.getDocument()
					+ "' target='_blank'><img src=\"../assets/images/pdf_demo.png\" height=\"50\" width=\"35\"></a>";

			if (m.getStatus().equals("1")) {
				m.setStatus("Hired");
				m.setAction("N/A");
			} else if (m.getStatus().equals("2")) {
				m.setStatus("Hold");
				m.setAction(s);
			} else if (m.getStatus().equals("3")) {
				m.setStatus("Rejected");
				m.setAction("N/A");
			} else {
				m.setStatus("Short Listed");
				m.setAction(s);
			}
			m.setDocument(str);
			s = "";

		}

		response.setRecordsTotal(jsonResp.getTotal());
		response.setRecordsFiltered(jsonResp.getTotal());

		response.setData(candidate);

		logger.info("Method : viewCandidate ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-edit-feedback")
	public String editEmployee(Model model, @RequestParam("id") String encodedIndex,
			@RequestParam("index") String encodedIndex1, HttpSession session) {
		logger.info("Method : editEmployee Feedback starts");

		// SPECIFIC NAME DROUP DOWN
		DropDownModel[] specificName = restTemplate.getForObject(env.getRecruitment() + "getSpecificNameList",
				DropDownModel[].class);
		List<DropDownModel> specificNameList = Arrays.asList(specificName);
	
		model.addAttribute("specificNameList", specificNameList);

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

		try {
			jsonResponse = restTemplate.getForObject(
					env.getRecruitment() + "editFeedbackDetails?id=" + id + "&index=" + encodedIndex1,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ResourceModel m = new ResourceModel();
		m.setCreatedBy(userId);

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		List<ResourceModel> feedback = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ResourceModel>>() {
				});

		session.setAttribute("message", "");
	

		for (int i = 0; i < feedback.size(); i++) {
			if (m.getCreatedBy().equals(feedback.get(i).getEmployeeId())) {
			
				model.addAttribute("employeeId", feedback.get(i).getEmployeeId());
				model.addAttribute("employeeName", feedback.get(i).getEmployeeName());
			}
		}

		model.addAttribute("feedback", feedback.get(0));

		logger.info("Method : editEmployee Feedback ends");

		return "recruitment/add-employee-feedback";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/add-employee-question")
	public @ResponseBody JsonResponse<List<DropDownModel>> getQuestion(Model model, @RequestParam("id") String id,
			HttpSession session) {

		logger.info("Method : getQuestion starts");

		JsonResponse<List<DropDownModel>> jsonResponse = new JsonResponse<List<DropDownModel>>();

		jsonResponse = restTemplate.getForObject(env.getRecruitment() + "getQuestion?id=" + id, JsonResponse.class);

		String message = jsonResponse.getMessage();

		if (message != null && message != "") {
			jsonResponse.setMessage("Unsuccess");
		} else {
			jsonResponse.setMessage("Success");
		}

		logger.info("Method : getQuestion starts");

		return jsonResponse;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-employee-feedback-ajax")
	public @ResponseBody JsonResponse<Object> addFeedback(Model model, @RequestBody List<ResourceModel> index,
			BindingResult result, HttpSession session) {

		logger.info("Method : addFeedback starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		index.get(0).setCreatedBy(userId);

		try {
			res = restTemplate.postForObject(env.getRecruitment() + "addFeedback", index, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addFeedback ends");

		return res;

	}

	@GetMapping("/view-generate-offer-letter")
	public String viewOfferLater(Model model, HttpSession session) {
		logger.info("Method : viewOfferLater starts");

		logger.info("Method : viewOfferLater ends");
		return "recruitment/view-generate-offer-letter";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-resource-add-offer-Letter-ajax")
	public @ResponseBody DataTableResponse addOfferLetter(Model model, HttpServletRequest request) {

		logger.info("Method : addOfferLetter web statrs");

		DataTableResponse response = new DataTableResponse();

		try {
			JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

			jsonResponse = restTemplate.getForObject(env.getRecruitment() + "addOfferLetter", JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ResourceModel> resource = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ResourceModel>>() {
					});
			int i = 0;
			for (ResourceModel m : resource) {
				i++;
				String s = "";
				if (m.getToTime().equals("0")) {
					s = s + "<th id='status'><div><input type='radio' id='statusShort_" + i
							+ "' name='status1' value='1' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Hire</span></div>"
							+ "<div><input type='radio' id='statusHold_" + i
							+ "' name='status1' value='2' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Hold</span></div>"
							+ "<div><input type='radio' id='statusReject_" + i
							+ "' name='status1' value='3' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Reject</span></div></th>";
				} else if (m.getToTime().equals("1")) {
					s = s + "<button class='btn btn-success'>Hired</button>";
				} else if (m.getToTime().equals("2")) {
					s = s + "<th id='status'><div><input type='radio' id='statusShort_" + i
							+ "' name='status1' value='1' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Hire</span></div>"
							+ "<div><input type='radio' id='statusHold_" + i
							+ "' name='status1' value='2' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' checked /><span class='chkboxtxt'>Hold</span></div>"
							+ "<div><input type='radio' id='statusReject_" + i
							+ "' name='status1' value='3' onclick='myFun(" + m.getCandidateId() + "," + i
							+ ")' /><span class='chkboxtxt'>Reject</span></div></th>";
				}
				m.setJobType(s);

				String str = "<a href='/document/employee/" + m.getDocument()
						+ "' target='_blank'><img src=\"../assets/images/pdf_demo.png\" height=\"50\" width=\"35\"></a>";

				s = "";
				if (m.getToTime().equals("1")) {
					s = s + "<button class='btn btn-info' id='offer_" + m.getCandidateId()
							+ "' onclick='addOfferLater(\"" + m.getRequisitionId() + "\",\"" + m.getCandidateId()
							+ "\");' style='display:block;' >Add Offer Later</button>";
				} else {
					s = s + "<button class='btn btn-info' id='offer_" + m.getCandidateId()
							+ "' onclick='addOfferLater(\"" + m.getRequisitionId() + "\",\"" + m.getCandidateId()
							+ "\");' style='display:none;' >Add Offer Later</button>";
				}

				m.setStatus(s);
				m.setDocument(str);
				String x = "";
				x = x + "<button class='btn btn-warning' onclick='View(\"" + m.getRequisitionId() + "\",\""
						+ m.getCandidateId() + "\");'>View</button></th>";
				m.setAction(x);
				
				/*
				 * String v = ""; v = v +
				 * "<a href='/recruitment/generate-appointment-letter'><i class='fa fa-files-o' title='Generate Appointment Letter' style=\"font-size:20px\">"
				 * +
				 * "</i></a>&nbsp;<a href=\"\"><i class='fa fa-download' title='Download' style=\"font-size:20px\"></i></a>"
				 * ; m.setVendor(v);
				 */
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setData(resource);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : addOfferLetter web ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-generate-offer-letter-status", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addFinalStatus(@RequestBody ResourceModel shortList, Model model,
			HttpSession session) {
		logger.info("Method : addFinalStatus function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(env.getRecruitment() + "addFinalStatus", shortList, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addFinalStatus function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-generate-offer-letter-view-details")
	public @ResponseBody JsonResponse<List<ResourceModel>> viewDetails(Model model, HttpSession session,
			@RequestParam("reqId") String reqId, @RequestParam String candId) {
		logger.info("Method : viewDetails starts");

		JsonResponse<List<ResourceModel>> res = new JsonResponse<List<ResourceModel>>();

		res = restTemplate.getForObject(env.getRecruitment() + "viewDetails?reqId=" + reqId + "&candId=" + candId,
				JsonResponse.class);

		if (res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		ObjectMapper mapper = new ObjectMapper();
		List<ResourceModel> data = mapper.convertValue(res.getBody(), new TypeReference<List<ResourceModel>>() {
		});

		data.forEach(s -> s.setEmployeeId(new String(Base64.getEncoder().encode(s.getEmployeeId().getBytes()))));
		res.setBody(data);
		logger.info("Method : viewDetails ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("view-generate-offer-letter-view-feedback")
	public String viewFeedBack(Model model, HttpSession session, @RequestParam String empId,
			@RequestParam String candId) {

		logger.info("Method : viewFeedBack starts");

		byte[] s = Base64.getDecoder().decode(empId.getBytes());
		String emplId = new String(s);

		// SPECIFIC NAME DROUP DOWN
		DropDownModel[] specificName = restTemplate.getForObject(env.getRecruitment() + "getSpecificNameList",
				DropDownModel[].class);
		List<DropDownModel> specificNameList = Arrays.asList(specificName);
		
		model.addAttribute("specificNameList", specificNameList);

		JsonResponse<List<ResourceModel>> jsonResponse = new JsonResponse<List<ResourceModel>>();

		try {
			jsonResponse = restTemplate.getForObject(
					env.getRecruitment() + "getFeedbackDetails?empId=" + emplId + "&candId=" + candId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		List<ResourceModel> feedback = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<ResourceModel>>() {
				});
		session.setAttribute("message", "");
		model.addAttribute("isEdit", feedback.get(0).getIsEdit());
		model.addAttribute("status", feedback.get(0).getStatus());
		model.addAttribute("toTime", feedback.get(0).getToTime());
		model.addAttribute("feedback", feedback);

		logger.info("Method : viewFeedBack ends");
		return "recruitment/add-employee-feedback";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-generate-offer-letter-add")
	public @ResponseBody JsonResponse<Object> addOfferLetter(Model model, @RequestParam("reqId") String encodedIndex1,
			@RequestParam("candId") String encodedIndex2, HttpSession session) {
		logger.info("Method : addOfferLetter starts");

		JsonResponse<ResourceModel> jsonResponse = new JsonResponse<ResourceModel>();
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			jsonResponse = restTemplate.getForObject(
					env.getRecruitment() + "generateOfferLetter?reqId=" + encodedIndex1 + "&candId=" + encodedIndex2,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		ResourceModel offerLetterDtls = mapper.convertValue(jsonResponse.getBody(), new TypeReference<ResourceModel>() {
		});

		session.setAttribute("message", "");

		session.setAttribute("offerLetterDtls", offerLetterDtls);

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addOfferLetter ends");

		return res;
	}

	@GetMapping("/view-generate-offer-letter-add-data")
	public String addOfferLetterData(Model model, HttpSession session) {
		logger.info("Method : addOfferLetterData starts");

		/*
		 * dropDown value for Nationality
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getEmployeeUrl() + "rest-get-nationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(dropDownModel);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Pay Grade
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getEmployeeUrl() + "rest-get-payGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(dropDownModel);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Job Title list
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getEmployeeUrl() + "rest-get-jobList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(dropDownModel);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Country list
		 */
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getEmployeeUrl() + "rest-get-countryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] genderListModel = restTemplate
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeGenderList", DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(genderListModel);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] maritalListModel = restTemplate
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeMaritalList", DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(maritalListModel);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Object offerLetterDtls = session.getAttribute("offerLetterDtls");

		model.addAttribute("offerLetter", offerLetterDtls);

		logger.info("Method : addOfferLetterData ends");
		return "employee/add-offer-letter";
	}

}
