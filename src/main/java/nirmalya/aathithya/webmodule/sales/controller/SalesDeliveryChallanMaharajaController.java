package nirmalya.aathithya.webmodule.sales.controller;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import nirmalya.aathithya.webmodule.sales.model.CustomerGSTDataModel;
import nirmalya.aathithya.webmodule.sales.model.RestsalesDeliveryChallanDetailsModel;
import nirmalya.aathithya.webmodule.sales.model.SalesDeliveryChallanModel;
import nirmalya.aathithya.webmodule.sales.model.salesDataSetPriceModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = { "sales/" })
public class SalesDeliveryChallanMaharajaController {
	Logger logger = LoggerFactory.getLogger(SalesDeliveryChallanMaharajaController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/**
	 *
	 * Add Delivery Challan For Sales
	 * 
	 */
	@GetMapping("add-delivery-challan")
	public String addDelivery(Model model, HttpSession session) {
		logger.info("Method : addDelivery starts");
		SalesDeliveryChallanModel salesDelivery = new SalesDeliveryChallanModel();
		try {
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("salesDelivery", salesDelivery);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for item Type add-items-getSubGroup
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-itemCategory",
					DropDownModel[].class);
			List<DropDownModel> itemGroupList = Arrays.asList(dropDownModel);
			model.addAttribute("itemGroupList", itemGroupList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * Get DropDown Value Store List
		 *
		 */
		try {
			String userId = (String) session.getAttribute("USER_ID");
			DropDownModel[] payMode = restClient.getForObject(env.getSalesUrl() + "restGetStoreList?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addDelivery ends");
		return "sales/addDeliveryChallan";
	}

	/**
	 *
	 * View Delivery Challan For Sales
	 * 
	 */
	@GetMapping("view-delivery-challan")
	public String viewDeliveryChallan(Model model, HttpSession session) {

		logger.info("Method : viewDeliveryChallan starts");

		SalesDeliveryChallanModel changeApprove = new SalesDeliveryChallanModel();
		try {
			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}
			session.setAttribute("message", "");

			model.addAttribute("changeApprove", changeApprove);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewDeliveryChallan ends");
		return "sales/viewDeliveryChallan";
	}

	/**
	 * Web Controller - Get Sales Order No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-delivery-challan-AutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSaleOrderNoList(@RequestParam String id,
			@RequestParam String storeId) {
		logger.info("Method : getSaleOrderNoList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(
					env.getSalesUrl() + "getSaleOrderLByAutoSearch?id=" + id + "&storeId=" + storeId,
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

		logger.info("Method : getSaleOrderNoList ends");
		return res;
	}

	/**
	 * Web Controller - Get Customer Name By Selected
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-getCustomer" })
	public @ResponseBody JsonResponse<CustomerGSTDataModel> getDelChallanCustomer(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getDelChallanCustomer starts");
		JsonResponse<CustomerGSTDataModel> res = new JsonResponse<CustomerGSTDataModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getCustomerBySaleid?id=" + searchValue,
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

		logger.info("Method : getDelChallanCustomer ends");
		return res;
	}

	/**
	 * item a/c to salesOrderNo
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-menu-item" })
	public @ResponseBody JsonResponse<salesDataSetPriceModel> getChallanItemList(Model model,
			@RequestBody List<salesDataSetPriceModel> params) {
		logger.info("Method :getChallanItemList starts");
		JsonResponse<salesDataSetPriceModel> res = new JsonResponse<salesDataSetPriceModel>();
		try {
			res = restClient.postForObject(env.getSalesUrl() + "getChallanItemList", params, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :getChallanItemList  ends");
		return res;
	}

	/**
	 * item a/c to salesOrderNo
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-delivery-challan-getSaleOrderDetails" })
	public @ResponseBody JsonResponse<RestsalesDeliveryChallanDetailsModel> getSaleOrderDetails(@RequestParam String id,
			@RequestParam String storeId) {
		logger.info("Method :getSaleOrderDetails starts");
		JsonResponse<RestsalesDeliveryChallanDetailsModel> res = new JsonResponse<RestsalesDeliveryChallanDetailsModel>();
		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleOrderDetails?id=" + id + "&storeId=" + storeId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :getSaleOrderDetails  ends");
		return res;
	}

	/**
	 * post Mapping for add-delivery-challan
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-delivery-challan")
	public @ResponseBody JsonResponse<Object> saveDelChallanDetls(
			@RequestBody List<SalesDeliveryChallanModel> salesDeliveryChallanModel, Model model, HttpSession session) {
		logger.info("Method : saveDelChallanDetls function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			double igst;
			double grandTotal = 0;
			double subTotal = 0;
			double remainBal = 0;
			double totalDiscount = 0;
			double lineTotal;
			double totalIgst = 0;
			double itemCess = 0;
			double totalCess = 0;

			for (SalesDeliveryChallanModel i : salesDeliveryChallanModel) {
				i.setCreatedBy(userId);
				lineTotal = i.getQuantity() * i.getUnitPrice() - i.getItemDiscount();
				igst = (lineTotal * i.getGstRate()) / 100;
				itemCess = (igst * i.getItemCess()) / 100;
				if (Boolean.TRUE.equals(i.getTaxType())) {
					i.setqIGST(igst);
					i.setqSGST((double) 0);
					i.setqCGST((double) 0);
				} else {
					i.setqSGST(igst / 2);
					i.setqCGST(igst / 2);
					i.setqIGST((double) 0);
				}
				i.setLineTotal(lineTotal);
				subTotal = subTotal + lineTotal;
				grandTotal = grandTotal + (lineTotal + igst + itemCess);
				totalDiscount = totalDiscount + i.getItemDiscount();
				remainBal = grandTotal - 0;
				totalIgst = totalIgst + igst;
				totalCess = totalCess + itemCess;
			}
			for (SalesDeliveryChallanModel m : salesDeliveryChallanModel) {
				m.setSubTotal(subTotal);
				m.setTotalCess(totalCess);
				m.setGrandTotal(grandTotal);
				m.setRemainAmnt(0.0);
				m.setTotalDiscount(totalDiscount);
				if(m.getTaxType()) {
					m.setTotalCgst(0.0);
					m.setTotalSgst(0.0);
					m.setTotalIgst(totalIgst);
				} else {
					m.setTotalCgst(totalIgst / 2);
					m.setTotalSgst(totalIgst / 2);
					m.setTotalIgst(0.0);
				}
			}

			res = restClient.postForObject(env.getSalesUrl() + "restAddDelChallan", salesDeliveryChallanModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && !message.isEmpty()) {
			res.setMessage("Faiiled");
		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveDelChallanDetls function Ends");
		return res;
	}

	/**
	 *
	 * View all DELIVERY CHALLAN SALES data through AJAX
	 *
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("/view-delivery-challan-through-ajax")
	public @ResponseBody DataTableResponse viewReqQuotThroAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param3, @RequestParam String param4,
			@RequestParam String param5) {
		logger.info("Method : viewReqQuotThroAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			JsonResponse<List<SalesDeliveryChallanModel>> jsonResponse = new JsonResponse<List<SalesDeliveryChallanModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getDelChallanDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SalesDeliveryChallanModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SalesDeliveryChallanModel>>() {
					});

			String s = "";
			String p = "";
			String k = "";

			for (SalesDeliveryChallanModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getDelChallanId().getBytes());

				Byte delStatus = 0;
				Byte delGeneratedInv = 2;
				Byte delStatusDB = m.getApproveStatus();
				if (delStatus.equals(delStatusDB)) {

					m.setApproveShowStatus("Pending");

					s = s + "<a data-toggle='modal' title='Approve'  "
							+ "href='javascript:void' onclick='viewInModelApprove(\"" + m.getDelChallanId()
							+ "\")'><i class=\"fa fa-times-circle\" title=\"Pending for Approve\" "
							+ "style=\"font-size:24px;color:#e30f0f\"></i></a>";

				} else {
					m.setApproveShowStatus("Delivered");
					if (delGeneratedInv.equals(delStatusDB)) {
						s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\""
								+ new String(pId) + ',' + m.getApproveStatus() + ',' + new String(pId)
								+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px;color:#090\"></i></a>&nbsp;&nbsp;";

						s = s + "<a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + m.getDelChallanId()
								+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";

						s = s + "<a  title='DownLoad Pdf'  " + "href='javascript:void' onclick='downloadPdf(\""
								+ new String(pId) + "\")'><i class='fa fa-download' style=\"font-size:24px\"></i></a>";
					} else {
						s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\""
								+ new String(pId) + ',' + m.getApproveStatus() + ',' + new String(pId)
								+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px;color:#090\"></i></a>&nbsp;&nbsp;";

						s = s + "<a data-toggle='modal' title='View'  "
								+ "href='javascript:void' onclick='viewInModel(\"" + m.getDelChallanId()
								+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";

						/*
						 * s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='invoiceCreate100(\""
						 * + new String(pId) +
						 * "\")'><i class='fa fa-files-o' title='Invoice' style=\"font-size:24px\"></i></a>&nbsp;&nbsp"
						 * ;
						 */
					}
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

		logger.info("Method : viewReqQuotThroAjax ends");
		return response;
	}

	/**
	 * Challan Salers ORDER STATUS CHANGE DATA to Delivery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-delivery-challan-change-status")
	public @ResponseBody JsonResponse<Object> changeDeliveryStatus(Model model, @RequestParam("id") String id,
			@RequestParam("status") Byte status, @RequestParam("createdBy") String createdBy, HttpSession session) {

		logger.info("Method : WEBMODULE ChangeDeliveryStatus starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getSalesUrl() + "restDeliveryStatus?id=" + id + "&status=" + status + "&createdBy=" + createdBy,
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
		logger.info("Method : WEBMODULE ChangeDeliveryStatus ends");
		return resp;
	}

	/**
	 * Web Controller - Upload Menu Image/PDF
	 *
	 */
	@PostMapping("/view-delivery-challan-uploadFile")
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
	 * Web Controller - Post Mapping Change Status
	 *
	 */
	@SuppressWarnings({ "unchecked" })
	@PostMapping("view-delivery-challan-approve")
	public String addRFQ(@ModelAttribute SalesDeliveryChallanModel reqQuotation, Model model, HttpSession session) {
		logger.info("Method :  ChangeDeliveryStatus function 'post-mapping' starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String imageName = null;
		String fileName = null;
		try {

			MultipartFile inputFile = (MultipartFile) session.getAttribute("RFQIMAGE");
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];
				fileName = contentName;
				reqQuotation.setImageName(fileName);

				res = restClient.postForObject(env.getSalesUrl() + "restAddDelStatus", reqQuotation,
						JsonResponse.class);
				if (res.getCode().contains("Data Saved Successfully")
						&& (res.getMessage() == null || res.getMessage().isEmpty())) {
					Path path = Paths.get(env.getFileUploadSales() + contentName);
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

							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadSales() + "thumb\\" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				imageName = (String) session.getAttribute("imageNameFromDnForEdit");
				reqQuotation.setImageName(imageName);
				res = restClient.postForObject(env.getSalesUrl() + "restAddReqQuotation", reqQuotation,
						JsonResponse.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != "") {
			session.setAttribute("message", res.getMessage());
			session.setAttribute("reqQuotation", reqQuotation);
			return "redirect:/sales/view-delivery-challan";
		}
		session.removeAttribute("RFQIMAGE");
		logger.info("Method : ChangeDeliveryStatus function 'post-mapping' ends");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			 logger.warn(e.getMessage());
			 Thread.currentThread().interrupt(); 
		}
		return "redirect:/sales/view-delivery-challan";
	}

	/**
	 * Web Controller - Get Challan No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-delivery-challan-AutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getChallanList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getChallanList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getChallanListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getChallanList ends");
		return res;
	}

	/**
	 * Web Controller - Get getAut Customer No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-delivery-challan-AutocompleteCus" })
	public @ResponseBody JsonResponse<DropDownModel> getAutCusList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAutCusList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getAutCusListBySearch?id=" + searchValue,
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

		logger.info("Method : getAutCusList ends");
		return res;
	}

	/**
	 *
	 * Add Sale Invoice
	 *
	 */
	@GetMapping("/view-delivery-challan-invoice-page")
	public String addInvoiceSaleChallan(Model model, @RequestParam("id") String index, HttpSession session) {

		logger.info("Method : addInvoiceSaleChallan starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			SalesDeliveryChallanModel[] invDetails = restClient
					.getForObject(env.getSalesUrl() + "getInvChallanDtls?id=" + id, SalesDeliveryChallanModel[].class);
			List<SalesDeliveryChallanModel> invChallanList = Arrays.asList(invDetails);
			model.addAttribute("taxType", invChallanList.get(0).getTaxType());

			String variable = env.getBaseUrlPath();
			String logo = "logo_grandcourtyard.png";
			model.addAttribute("logoImage", variable + "document/hotel/" + logo + "");

			model.addAttribute("invChallandetls", invChallanList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : addInvoiceSaleChallan ends");
		return "sales/salesInvoiceChallan";
	}

	/**
	 * post Mapping for add invoice for challan
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = "view-delivery-challan-invoice-page-save")
	public @ResponseBody JsonResponse<Object> saveSaleChallanInvDetls(
			@RequestBody List<SalesDeliveryChallanModel> SalesDeliveryChallanModel, Model model, HttpSession session) {
		logger.info("Method : saveSaleChallanInvDetls function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			for (SalesDeliveryChallanModel i : SalesDeliveryChallanModel) {
				i.setCreatedBy("abc001");
			}
			res = restClient.postForObject(env.getSalesUrl() + "restAddSorderInv", SalesDeliveryChallanModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();

		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveSaleChallanInvDetls function Ends");
		return res;
	}

	/**
	 * 
	 * Modal View of For delivery challan Id
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-delivery-challan-model" })
	public @ResponseBody JsonResponse<SalesDeliveryChallanModel> modelDeliveryChallan(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method : modelDeliveryChallan starts");
		JsonResponse<SalesDeliveryChallanModel> res = new JsonResponse<SalesDeliveryChallanModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getChallanDtlsById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelDeliveryChallan ends");
		return res;
	}

	/**
	 * PDF FOR Delivery Challan Delivery Sales
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-delivery-challan-pdf")
	public void deliveryChallanPdf(HttpServletResponse response, Model model, @RequestParam("id") String encodeId) {
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<List<SalesDeliveryChallanModel>> jsonResponse11 = new JsonResponse<List<SalesDeliveryChallanModel>>();

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			jsonResponse11 = restClient.getForObject(
					env.getSalesUrl() + "restDelChallan11?id=" + index + "&Action=" + "modalDelChallan11",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<SalesDeliveryChallanModel> dataset = mapper.convertValue(jsonResponse11.getBody(),
				new TypeReference<List<SalesDeliveryChallanModel>>() {
				});
		Double gstRate = dataset.get(0).getGstRate();

		String conCgstString = "";
		conCgstString = "CGST(" + gstRate / 2 + "%)";
		String conSgstString = "";
		conSgstString = "SGST(" + gstRate / 2 + "%)";
		String conIgstString = "";
		conIgstString = "IGST(" + gstRate + "%)";

		data.put("dataset", dataset);

		data.put("conCgstString", conCgstString);
		data.put("conSgstString", conSgstString);
		data.put("conIgstString", conIgstString);

		data.put("saleOrderNo", dataset.get(0).getSaleOrderNo());
		data.put("challanNo", dataset.get(0).getDelChallanId());
		data.put("purchaseOrderNo", dataset.get(0).getPurchaseOrderNo());
		data.put("invoiceNo", dataset.get(0).getInvoiceNo());
		data.put("customerName", dataset.get(0).getCustomerName());
		data.put("challanDate", dataset.get(0).getDelChallanDate());
		data.put("subtotal", dataset.get(0).getSubTotal());
		data.put("desc", dataset.get(0).getqNote());
		data.put("cgst", dataset.get(0).getqCGST());
		data.put("sgst", dataset.get(0).getqSGST());
		data.put("igst", dataset.get(0).getqIGST());
		data.put("grandTotal", dataset.get(0).getGrandTotal());

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=challanPdf.html");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("sales/challanPdf", data);
			FileInputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-getSubCategory-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getItemCategory(Model model, @RequestBody String itemCategory,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "rest-get-itemSubCategory-challan?id=" + itemCategory,
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
		logger.info("Method : getItemCategory ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-valid-qty-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> validQty(@RequestParam String menuItemCat,
			@RequestParam String menuItemSubCat, @RequestParam String menuItemId) {
		logger.info("Method : validQty starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "rest-get-validQty?menuItemCat=" + menuItemCat
					+ "&menuItemSubCat=" + menuItemSubCat + "&menuItemId=" + menuItemId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : validQty ends");
		return res;
	}

	
	/**
	 * Web Controller - Get Customer Name By Selected
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-get-delivery-from" })
	public @ResponseBody JsonResponse<DropDownModel> getDeliveryFrom(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getDeliveryFrom starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getDeliveryFrom?id=" + searchValue,
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

		logger.info("Method : getDeliveryFrom ends");
		return res;
	}
	
	
	/**
	 * Web Controller - Get Customer Name By Selected
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-delivery-challan-get-delivery-to" })
	public @ResponseBody JsonResponse<DropDownModel> getDeliveryTo(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getDeliveryTo starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getDeliveryTo?id=" + searchValue,
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

		logger.info("Method : getDeliveryTo ends");
		return res;
	}
	
	
}
