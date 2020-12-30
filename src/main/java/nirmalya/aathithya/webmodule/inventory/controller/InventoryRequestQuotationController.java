package nirmalya.aathithya.webmodule.inventory.controller;

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
import nirmalya.aathithya.webmodule.inventory.model.InventoryReqDetailsModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryRequestQuotationController {

	Logger logger = LoggerFactory.getLogger(InventoryRequestQuotationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * Add Request For Quotation
	 * 
	 */
	@GetMapping("add-rfq")
	public String addRequestForQuotation(Model model, HttpSession session) {
		logger.info("Method : addRequestForQuotation starts");

		InventoryReqDetailsModel reqQuotation = new InventoryReqDetailsModel();
		
		try {
			String message = (String) session.getAttribute("message");
			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");
			model.addAttribute("reqQuotation", reqQuotation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(
					env.getInventoryUrl() + "rest-get-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addRequestForQuotation ends");
		return "inventory/addRequestForQuotaion";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-rfq")
	public String viewReqQuotation(Model model, HttpSession session) {

		logger.info("Method : viewReqQuotation starts");

		InventoryReqDetailsModel changeApprove = new InventoryReqDetailsModel();
		try {
			// leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("changeApprove", changeApprove);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start Vendor Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> VendorList = new ArrayList<DropDownModel>();
		try {
			respTblMstr1 = restClient.getForObject(env.getInventoryUrl() + "getVendorsList?Action=" + "getVendorsList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr1 = respTblMstr1.getMessage();

		if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
			model.addAttribute("message", messageForTblMstr1);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		VendorList = mapper1.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("VendorList", VendorList);
		// End Vendor Type Drop Down List

		// Start Vendor Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr3 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> catList = new ArrayList<DropDownModel>();
		try {
			respTblMstr3 = restClient.getForObject(env.getInventoryUrl() + "getCatList?Action=" + "getCatList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr3 = respTblMstr3.getMessage();

		if (messageForTblMstr3 != null || messageForTblMstr3 != "") {
			model.addAttribute("message", messageForTblMstr3);
		}

		ObjectMapper mapper3 = new ObjectMapper();

		catList = mapper3.convertValue(respTblMstr3.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("lCatDate", catList);
		// End Vendor Type Drop Down List

		// Start Vendor Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr2 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> RFQList = new ArrayList<DropDownModel>();
		try {
			respTblMstr2 = restClient.getForObject(env.getInventoryUrl() + "getRFQNameList?Action=" + "getRFQNameList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr2 = respTblMstr2.getMessage();

		if (messageForTblMstr2 != null || messageForTblMstr2 != "") {
			model.addAttribute("message", messageForTblMstr2);
		}

		ObjectMapper mapper2 = new ObjectMapper();

		RFQList = mapper2.convertValue(respTblMstr2.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("lRFQData", RFQList);
		// End RFQ NAME Drop Down List

		logger.info("Method : viewReqQuotation ends");

		return "inventory/viewReqQuotation";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/approve-rfq")
	public String approveRFQ(Model model, HttpSession session) {

		logger.info("Method : approveRFQ starts");

		InventoryReqDetailsModel changeApprove = new InventoryReqDetailsModel();
		try {
			// leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("changeApprove", changeApprove);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start Vendor Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr2 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> RFQList = new ArrayList<DropDownModel>();
		try {
			respTblMstr2 = restClient.getForObject(env.getInventoryUrl() + "getRFQNameList?Action=" + "getRFQNameList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr2 = respTblMstr2.getMessage();

		if (messageForTblMstr2 != null || messageForTblMstr2 != "") {
			model.addAttribute("message", messageForTblMstr2);
		}

		ObjectMapper mapper2 = new ObjectMapper();

		RFQList = mapper2.convertValue(respTblMstr2.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("lRFQData", RFQList);
		// End RFQ NAME Drop Down List

		logger.info("Method : approveRFQ ends");

		return "inventory/approveRFQ";
	}

	/**
	 * InVentory Approve Status CHANGE
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("approve-rfq-ids")
	public @ResponseBody JsonResponse<Object> ApproveRFQStatus(Model model,
			@RequestParam("checkedRFQId") String checkedRFQId, HttpSession session) {

		logger.info("Method : WEBMODULE  ApproveRFQStatus starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		// foodOrderData.get(0).setCreatedBy("u0010");
		try {
			resp = restClient.getForObject(env.getInventoryUrl() + "approveRFQStatus?checkedRFQId=" + checkedRFQId,
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
		logger.info("Method : WEBMODULE ApproveRFQStatus ends");
		return resp;
	}

	/**
	 * Web Controller - Get Vendors By Unchange
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-rfq-getVendorName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getVenList(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getVenList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getVenListbyCat?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getVenList ends");
		return res;
	}

	/*
	 *
	 * View all Request Quotation data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-rfq-through-ajax")
	public @ResponseBody DataTableResponse viewReqQuotThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewReqQuotThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			// System.out.println("param 1 is---------"+param1);

			JsonResponse<List<InventoryReqDetailsModel>> jsonResponse = new JsonResponse<List<InventoryReqDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getReqQuotData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryReqDetailsModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryReqDetailsModel>>() {
					});

			String s = "";
			String k = "";
			String p = "";

			for (InventoryReqDetailsModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getReqQuotId().getBytes());

				String venList = m.getRfqVendor();
				if (venList != null) {
					m.setRfqVendor(venList);
				} else {
					m.setRfqVendor("Not Assigned Yet");
				}
				
//				DropDownModel[] dd = restClient.getForObject(env.getInventoryUrl()+"getVendorListForAsign?id="+m.getPurOrderNo(), DropDownModel[].class);
//				List<DropDownModel> vendorList = Arrays.asList(dd);
//
//				m.setVendorList(vendorList);
				
				String extension = "";

				String name = "";
				name = m.getRfqBackground();
				extension = name.substring(name.lastIndexOf("."));
				
				String ext = ".pdf";
				if (extension.equals(ext)) {
					p = p + "<a target=\"_blank\" href='/document/invImg/" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png'/>" + "</a>";
					m.setRfqBackground(p);
					p = "";
				} else {
					k = "<a class='example-image-link' href='/document/invImg/" + m.getRfqBackground() + "' title='"
							+ m.getRfqBackground() + "' data-lightbox='" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png'/>" + "</a>";
					m.setRfqBackground(k);
					k = "";
				}
				
				if(m.getApprovedStatus()==0) {
					s = s + "<a href='edit-rfq?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;";
				}
				s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
					  + m.getReqQuotId() + "\")'><i class='fa fa-search search'></i></a>&nbsp;";
				Byte fStatusDb = 2;

				if (m.getApprovedStatus().equals(fStatusDb)) {
					s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\"" + new String(pId)
							+ ',' + m.getApprovedStatus() + ',' + new String(pId)
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\"></i></a>&nbsp";
					s = s + "<a href='javascript:void(0)'"
							+ "' onclick='gotovendorquot()' ><i class=\"fa fa-external-link\" aria-hidden=\"true\"></i></a>&nbsp";

				} else {
					if(m.getApprovedStatus()==1) {
						s = s + "<a data-toggle='modal' title='Approve'  "
								+ "href='javascript:void' onclick='viewInModelApprove(\"" + m.getReqQuotId()
								+ "\",\"" + m.getPurOrderNo() + "\")'><i class=\"fa fa-times-circle\" title=\"Pending for Approve\"></i></a>";
					}

				} /* + "\",\"" + m.getVendorList() */
				m.setAction(s);
				s = "";
				
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewReqQuotThroughAjax ends");
		return response;
	}

	/*
	 *
	 * View all Request Quotation data through AJAX for Approve RFQ
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/approve-rfq-through-ajax")
	public @ResponseBody DataTableResponse approveRFQThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : approveRFQThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			// System.out.println("param 1 is---------"+param1);

			JsonResponse<List<InventoryReqDetailsModel>> jsonResponse = new JsonResponse<List<InventoryReqDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getRFQApproveData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryReqDetailsModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryReqDetailsModel>>() {
					});

			String s = "";
			String p = "";
			String k = "";
			String y = "";

			for (InventoryReqDetailsModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getReqQuotId().getBytes());

				String extension = "";

				String name = "";
				name = m.getRfqBackground();
				extension = name.substring(name.lastIndexOf("."));
				// System.out.println("extention-----------------"+extension);
				String ext = ".pdf";
				if (extension.equals(ext)) {

					p = p + "<a target=\"_blank\" href='/document/invImg/" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png'  />" + "</a>";
					m.setRfqBackground(p);
					p = "";

				} else {

					k = "<a class='example-image-link' href='/document/invImg/" + m.getRfqBackground() + "' title='"
							+ m.getRfqBackground() + "' data-lightbox='" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png'/>" + "</a>";
					m.setRfqBackground(k);
					k = "";
				}

				Byte fStatusDb = 1;
				Byte fStatusDb1 = 2;
				if (m.getApprovedStatus().equals(fStatusDb) || m.getApprovedStatus().equals(fStatusDb1)) {
					s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ m.getReqQuotId() + "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;"
							+ "<img src='../assets/images/thumbup.png' height='15' width='15' style='margin-bottom:4px;' />"
							+ "</a>";
					s = s + "&nbsp;&nbsp;<a href='javascript:void(0)' title='Mail' onclick='sendMail(\""
							+ new String(pId) + "\")'><i class='fa fa-envelope'></i></a>";
					m.setAction(s);
					s = "";
				} else {

					s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ m.getReqQuotId() + "\")'><i class='fa fa-search search'></i></a>&nbsp;&nbsp;"
							+ "<input type='checkbox' name='checkbox' id=" + m.getReqQuotId() + "  value="
							+ m.getReqQuotId() + "  >";

					m.setAction(s);
					s = "";
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : ApproveRFQThroughAjax ends");
		return response;
	}

	/*
	 *
	 * View all Request Quotation data through AJAX for Staff
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-rfq-through-ajax-staff")
	public @ResponseBody DataTableResponse viewReqQuotThroughAjaxStaff(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewReqQuotThroughAjaxstaff starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<InventoryReqDetailsModel>> jsonResponse = new JsonResponse<List<InventoryReqDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getReqQuotDataStaff", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryReqDetailsModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryReqDetailsModel>>() {
					});

			String s = "";
			String p = "";
			String k = "";

			for (InventoryReqDetailsModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getReqQuotId().getBytes());

				String extension = "";

				String name = "";
				name = m.getRfqBackground();
				extension = name.substring(name.lastIndexOf("."));
				String ext = ".pdf";
				if (extension.equals(ext)) {

					p = p + "<a target=\"_blank\" href='/document/invImg/" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png' height='56' width='50' />" + "</a>";
					m.setRfqBackground(p);
					p = "";

				} else {

					k = "<a class='example-image-link' href='/document/invImg/" + m.getRfqBackground() + "' title='"
							+ m.getRfqBackground() + "' data-lightbox='" + m.getRfqBackground() + "'>"
							+ "<img src='../assets/images/doc-icon.png'/>" + "</a>";
					m.setRfqBackground(k);
					k = "";
				}

				// Byte fStatusDb=1;
				Byte fStatusDb1 = m.getApprovedStatus();

				if (fStatusDb1 >= 1) {

					s = s + "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						  + m.getReqQuotId() + "\")'><i class='fa fa-search search'></i></a>";

				} else {

					s = s + "<a href='edit-rfq?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;"
							+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ m.getReqQuotId() + "\")'><i class='fa fa-search search'></i></a>";

				}

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewReqQuotThroughAjaxstaff ends");
		return response;
	}

	/**
	 * EDIT Request Quotation
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/edit-rfq")
	public String editRFQ(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editRFQ starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(
					env.getInventoryUrl() + "rest-get-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		InventoryReqDetailsModel reqQuotation = new InventoryReqDetailsModel();
		JsonResponse<InventoryReqDetailsModel> jsonResponse = new JsonResponse<InventoryReqDetailsModel>();

		try {
			/// getDistrictById
			System.out.println("id for get edit the reocrd for request quotation------------------" + id);
			jsonResponse = restClient.getForObject(
					env.getInventoryUrl() + "getRFQById?id=" + id + "&Action=viewEditRFQ", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		reqQuotation = mapper.convertValue(jsonResponse.getBody(), InventoryReqDetailsModel.class);
		session.setAttribute("message", "");
		String ImgName = reqQuotation.getRfqBackground();
		if (ImgName != null || ImgName != "") {
			String s = "";
			s = "/document/invImg/" + ImgName;
			reqQuotation.setAction(s);
			session.setAttribute("imageNameFromDnForEdit", ImgName);
		}

		String extension = "";

		String name = "";
		name = reqQuotation.getRfqBackground();
		extension = name.substring(name.lastIndexOf("."));
		// System.out.println("extention in edit time-----------------"+extension);
		String ext = ".pdf";
		if (extension.equals(ext)) {
			model.addAttribute("extension", extension);
			model.addAttribute("pdfName", name);

		}

		model.addAttribute("reqQuotation", reqQuotation);

		logger.info("Method : editRFQ ends");
		return "inventory/addRequestForQuotaion";
	}

	/*
	 * 
	 * Modal View of Leave Period
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-rfq-model" })
	public @ResponseBody JsonResponse<Object> modelViewRFQ(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modelViewRFQ starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restClient.getForObject(env.getInventoryUrl() + "getRFQById?id=" + index + "&Action=" + "ModelView",
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
		logger.info("Method : modelViewRFQ ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-rfq-get-vendor-list-through-ajax" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getVendorListForAssign(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getVendorListForAssign starts");
		
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		
		try {
			res = restClient.getForObject(env.getInventoryUrl()+"getVendorListForAsign?id="+index,
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
		
		logger.info("Method : getVendorListForAssign ends");
		return res;
	}

	/**
	 * POST MAPPING SUBMIT VENDOR Approve
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("view-rfq-vendor-approve")
	public @ResponseBody JsonResponse<Object> submitVendors(@RequestBody InventoryReqDetailsModel changeApprove, Model model,
			HttpSession session) {
		logger.info("Method : WEBMODULE ChangeApprove submitUser starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			changeApprove.setCreatedBy(userId);
			String s = "";
			
			for (String m : changeApprove.getRfqVendors()) {
				s = s + m + ",";
			}

			if (s != "") {
				s = s.substring(0, s.length() - 1);
				changeApprove.setRfqVendor(s);
			}

			resp = restClient.postForObject(env.getInventoryUrl() + "restAddVendors", changeApprove,
					JsonResponse.class);

		} catch (RestClientException e) { 
			e.printStackTrace();
		}
		
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("Success");
		}

		logger.info("Method : WEBMODULE UserController submitUser end");
		return resp;
	}
//	@SuppressWarnings("rawtypes")
//	@PostMapping("view-rfq-vendor-approve")
//	public String submitVendors(@ModelAttribute InventoryReqDetailsModel changeApprove, Model model,
//			HttpSession session) {
//		logger.info("Method : WEBMODULE ChangeApprove submitUser starts");
//		JsonResponse resp = new JsonResponse();
//		
//		String userId = "";
//		
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			changeApprove.setCreatedBy(userId);
//			String s = "";
//			
//			System.out.println("RFQ VENDOR LIST====="+changeApprove.getRfqVendors());
//			
//			for (String m : changeApprove.getRfqVendors()) {
//				s = s + m + ",";
//			}
//			
//			if (s != "") {
//				s = s.substring(0, s.length() - 1);
//				changeApprove.setRfqVendor(s);
//			}
//			
//			resp = restClient.postForObject(env.getInventoryUrl() + "restAddVendors", changeApprove,
//					JsonResponse.class);
//			
//		} catch (RestClientException e) { // TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if (resp.getMessage() != "") {
//			session.setAttribute("message", resp.getMessage());
//			session.setAttribute("vendorservice", changeApprove);
//			return "redirect:/inventory/view-rfq";
//		}
//		session.setAttribute("userservice", null);
//		logger.info("Method : WEBMODULE UserController submitUser end");
//		return "redirect:/inventory/view-rfq";
//	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-rfq-staff")
	public String viewReqQuotationStaff(Model model, HttpSession session) {

		logger.info("Method : viewReqQuotationStaff starts");

		InventoryReqDetailsModel changeApprove = new InventoryReqDetailsModel();
		try {
			// leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("changeApprove", changeApprove);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start Leave Type Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> VendorList = new ArrayList<DropDownModel>();
		try {
			respTblMstr1 = restClient.getForObject(env.getInventoryUrl() + "getVendorsList?Action=" + "getVendorsList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr1 = respTblMstr1.getMessage();

		if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
			model.addAttribute("message", messageForTblMstr1);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		VendorList = mapper1.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("VendorList", VendorList);
		// End Leave Type Drop Down List

		// Start Vendor Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr2 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> RFQList = new ArrayList<DropDownModel>();
		try {
			respTblMstr2 = restClient.getForObject(env.getInventoryUrl() + "getRFQNameList?Action=" + "getRFQNameList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr2 = respTblMstr2.getMessage();

		if (messageForTblMstr2 != null || messageForTblMstr2 != "") {
			model.addAttribute("message", messageForTblMstr2);
		}

		ObjectMapper mapper2 = new ObjectMapper();

		RFQList = mapper2.convertValue(respTblMstr2.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("lRFQData", RFQList);
		// End RFQ NAME Drop Down List

		logger.info("Method : viewReqQuotationStaff ends");

		return "inventory/viewReqQuotationStaff";
	}

	/**
	 * Web Controller - Upload Menu Item Picture
	 *
	 */
	@PostMapping("/add-rfq-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("RFQIMAGE", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	/**
	 * Web Controller - Post Mapping Add rfq
	 *
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("add-rfq")
	public String addRFQ(@ModelAttribute InventoryReqDetailsModel reqQuotation, Model model, HttpSession session) {
		logger.info("Method : addRFQ controller function 'post-mapping' starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String imageName = null;
		String fileName = null;

		//////////////////// start////////////////////////////
		try {

			MultipartFile inputFile = (MultipartFile) session.getAttribute("RFQIMAGE");
			// System.out.println("InputFile---------------------"+inputFile.getOriginalFilename());
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];
				fileName = contentName;
				reqQuotation.setRfqBackground(fileName);

				res = restClient.postForObject(env.getInventoryUrl() + "restAddReqQuotation", reqQuotation,
						JsonResponse.class);
				if (res.getCode().contains("Data Saved Successfully")
						&& (res.getMessage() == null || res.getMessage() == "")) {
					Path path = Paths.get(env.getFileUploadInventory() + contentName);
					if (fileType[1].contentEquals("pdf")) {
						Files.write(path, bytes);
					} else {

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

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadInventory() + "thumb\\" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				imageName = (String) session.getAttribute("imageNameFromDnForEdit");
				reqQuotation.setRfqBackground(imageName);
				// fileName = null;
				// reqQuotation.setRfqBackground(fileName);
				res = restClient.postForObject(env.getInventoryUrl() + "restAddReqQuotation", reqQuotation,
						JsonResponse.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//////////////////// end/////////////////////////////

		if (res.getMessage() != "") {
			session.setAttribute("message", res.getMessage());
			session.setAttribute("reqQuotation", reqQuotation);
			return "redirect:/inventory/add-rfq";
		}
		session.removeAttribute("RFQIMAGE");
		logger.info("Method : addRFQ controller function 'post-mapping' ends");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/inventory/view-rfq";
	}

}
