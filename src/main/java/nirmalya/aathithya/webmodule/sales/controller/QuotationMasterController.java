package nirmalya.aathithya.webmodule.sales.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date; 
import java.util.List; 
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.QuotationMasterModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderAdvancePayDtlsParentModel;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderAdvancePaymentModel;
import nirmalya.aathithya.webmodule.sales.model.SalesItemModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class QuotationMasterController {

	Logger logger = LoggerFactory.getLogger(QuotationMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Quotation Master' page
	 *
	 */
	@GetMapping("/add-quotation-master")
	public String defaultQuotationMaster(Model model, HttpSession session) {

		logger.info("Method : defaultQuotationMaster starts");

		QuotationMasterModel quotation = new QuotationMasterModel();
		QuotationMasterModel quotationSession = (QuotationMasterModel) session.getAttribute("squotation");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (quotationSession != null) {
				model.addAttribute("quotation", quotationSession);
				session.setAttribute("squotation", null);
			} else {
				model.addAttribute("quotation", quotation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultQuotationMaster ends");
		return "sales/add-quotation-master";
	}

	/**
	 * Web Controller - Get Customer List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-quotation-master-getCustomerAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getCustomerList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getCustomerList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getCustomerListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getCustomerList ends");
		return res;
	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-quotation-master-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getItemAutoSearchList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getItemListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemAutoSearchList ends");
		return res;
	}

	/**
	 * Web Controller - Get Serve Type By Item
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-quotation-master-get-servetype" })
	public @ResponseBody JsonResponse<SalesItemModel> getServetTypeByItem(Model model, @RequestBody String key,
			BindingResult result) {
		logger.info("Method : getServetTypeByItem starts");

		JsonResponse<SalesItemModel> res = new JsonResponse<SalesItemModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getServetTypeByItem?id=" + key, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getServetTypeByItem ends");
		return res;
	}

	/**
	 * Web Controller - Add Quotation Master
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-quotation-master-save-quotation", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addNewQuotation(@RequestBody List<QuotationMasterModel> quotation,
			Model model, HttpSession session) {

		logger.info("Method : addNewQuotation function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Double subTotal = 0.0;
			Double grandTotal = 0.0;
			Double itemCGST = 0.0;
			Double totalCGST = 0.0;
			Double itemIGST = 0.0;
			Double totalIGST = 0.0;
			for (int i = 0; i < quotation.size(); i++) {
				quotation.get(i).setCreatedBy(userId);
				Double stotal = quotation.get(i).getLineTotal();
				subTotal = subTotal + stotal;
				quotation.get(i).setSubTotal(subTotal);
				Double gstRate = quotation.get(i).getItemGST();
				Double gstRateHalf = gstRate / 2;
				Double lineAmt = quotation.get(i).getLineTotal();
				itemCGST = (lineAmt * gstRateHalf) / 100;
				totalCGST = totalCGST + itemCGST;
				quotation.get(i).setItemCGST(itemCGST);
				quotation.get(i).setItemSGST(itemCGST);
				quotation.get(i).setqCGST(totalCGST);
				quotation.get(i).setqSGST(totalCGST);
				itemIGST = (lineAmt * gstRate) / 100;
				quotation.get(i).setItemIGST(itemIGST);
				totalIGST = totalIGST + itemIGST;
				quotation.get(i).setqIGST(totalIGST);
				Boolean taxType = quotation.get(0).getTaxType();

				if (taxType) {
					grandTotal = subTotal + totalIGST;
				} else {
					grandTotal = subTotal + totalCGST + totalCGST;
				}

				Double gTotal = (double) Math.round(grandTotal);

				quotation.get(i).setGrandTotal(gTotal);
			}
			res = restClient.postForObject(env.getSalesUrl() + "addNewQuotation", quotation, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addNewQuotation function Ends");
		return res;
	}

	/**
	 * Default 'View Quotation Master' page
	 *
	 */
	@GetMapping("/view-quotation-master")
	public String viewQuotationMaster(Model model, HttpSession session) {

		logger.info("Method : viewQuotationMaster starts");

		logger.info("Method : viewQuotationMaster ends");
		return "sales/view-quotation-master";
	}

	/**
	 * Web Controller - Get Quotation List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-quotation-master-getQuotationList" })
	public @ResponseBody JsonResponse<DropDownModel> getQuotationList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getQuotationList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getQuotationListByAuotSearch?id=" + searchValue,
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

		logger.info("Method : getQuotationList ends");
		return res;
	}

	/**
	 * Web Controller - Get Quotation
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-quotation-master-through-ajax")
	public @ResponseBody DataTableResponse viewQuotationThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewQuotationThroughAjax starts");

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

			JsonResponse<List<QuotationMasterModel>> jsonResponse = new JsonResponse<List<QuotationMasterModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getQuotation", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<QuotationMasterModel> quote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<QuotationMasterModel>>() {
					});

			String s = "";

			for (QuotationMasterModel m : quote) {
				byte[] pId = Base64.getEncoder().encode(m.getQuotationId().getBytes());
				if (m.getqStatus()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
				m.setQuotationId("<a href='javascript:void' onclick='pdfCreate(\"" + new String(pId) + "\")'>"
						+ m.getQuotationId() + "</a>");
				if (m.getApproveActive()) {
					m.setApproveStatus("Complete");
					s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					s = s + "&nbsp;&nbsp;<a href='generate-sales-order?id=" + new String(pId)
							+ "'><i class='fa fa-files-o' title='Sales Order' style=\"font-size:24px\"></i></a>";
					m.setAction(s);
					s = "";
				} else {
					m.setApproveStatus("Pending");
					s = s + "<a href='edit-quotation-master?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
							+ " <a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + new String(pId)
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					s = s + "&nbsp;&nbsp;<a href='generate-sales-order?id=" + new String(pId)
							+ "'><i class='fa fa-files-o' title='Sales Order' style=\"font-size:24px\"></i></a>";
					m.setAction(s);
					s = "";
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(quote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewQuotationThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller - Get Quotation For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-quotation-master-modal" })
	public @ResponseBody JsonResponse<Object> modalQuotation(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalQuotation starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getQuotationById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalQuotation ends");
		return res;
	}

	/**
	 * Web Controller - Edit Quotation Master
	 *
	 */
	@GetMapping("edit-quotation-master")
	public String editQuotation(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editQuotation starts");

		/**
		 * get DropDown value for Serve Type
		 *
		 */
		try {
			DropDownModel[] serveType = restClient.getForObject(env.getSalesUrl() + "restGetServeType",
					DropDownModel[].class);
			List<DropDownModel> serveTypeList = Arrays.asList(serveType);

			model.addAttribute("serveTypeList", serveTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {

			QuotationMasterModel[] quotation = restClient.getForObject(env.getSalesUrl() + "editQuotationById?id=" + id,
					QuotationMasterModel[].class);
			List<QuotationMasterModel> quotationList = Arrays.asList(quotation);
			model.addAttribute("quotationId", quotationList.get(0).getQuotationId());
			model.addAttribute("quotation", quotationList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editQuotation starts");
		return "sales/add-quotation-master";
	}

	/**
	 * Web Controller - Add Purchase Order
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/generate-sales-order" })
	public String generateSaleOrder(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method : generateSaleOrder starts");
		String encodeId = encodedParam1;

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<SaleOrderAdvancePayDtlsParentModel> jsonresponse = new JsonResponse<SaleOrderAdvancePayDtlsParentModel>();
		try {
			jsonresponse = restClient.getForObject(env.getSalesUrl() + "getQuotationByIdForAdvancePayDtls?id=" + id, JsonResponse.class);
	 
		} catch (RestClientException e) { 
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		SaleOrderAdvancePayDtlsParentModel saleOrderAdvancePayDtlsParentModel = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<SaleOrderAdvancePayDtlsParentModel>() {
				}); 

		
		List<QuotationMasterModel> quotation  = saleOrderAdvancePayDtlsParentModel.getQuotationMasterModelList();
	    List<SaleOrderAdvancePaymentModel> saleOrderAdvancePaymentModelList = saleOrderAdvancePayDtlsParentModel.getSaleOrderAdvancePaymentModelList();
		
		/**
		 * Get DropDown Value For Payment Mode For Advance Payment
		 *
		 */
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getSalesUrl() + "restGetAdvPayMode",
					DropDownModel[].class);
			List<DropDownModel> payModeList = Arrays.asList(payMode);

			model.addAttribute("payModeList", payModeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/**
		 * Get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getSalesUrl() + "restLogoImage-Quotation?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String logo = logoList.get(0).getName();
		model.addAttribute("logoImage", "/document/hotel/" + logo + "");
		quotation.get(0).setCurDate(curDate);

		model.addAttribute("quotation", quotation);
		System.out.println("saleOrderAdvancePaymentModelList" + saleOrderAdvancePaymentModelList);
		if(saleOrderAdvancePaymentModelList.isEmpty()) {
			model.addAttribute("saleOrderAdvancePaymentModelList","");
		}else {
			model.addAttribute("saleOrderAdvancePaymentModelList", saleOrderAdvancePaymentModelList);
		}
		
	 
		logger.info("Method : generateSaleOrder ends");
		return "sales/generate-sales-order";
	}

	/**
	 * Web Controller - Upload Purchase Order Picture
	 *
	 */
	@PostMapping("/add-quotation-master-uploadFile")
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

	/**
	 * 
	 * Web Controller - Generate Sales Order
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	@PostMapping(value = { "view-quotation-master-generate-salesorder" })
	public @ResponseBody JsonResponse<Object> generateSalesOrder(Model model, @RequestBody QuotationMasterModel index,
			HttpSession session) {
		logger.info("Method : generateSalesOrder starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			index.setCreatedBy(userId);
			MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");

			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];
				index.setpOrderImage(contentName);

				index.setAdvAmount(index.getFirstPayAmount() + index.getSecondPayAmount());
				res = restClient.postForObject(env.getSalesUrl() + "generateSales", index, JsonResponse.class);

				 

				if ((res.getCode() == null || res.getCode() == "")
						&& (res.getMessage() == null || res.getMessage() == "")) {
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

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadSales() + "thumb\\" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				res = restClient.postForObject(env.getSalesUrl() + "generateSales", index, JsonResponse.class);
			}
			/**/
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : generateSalesOrder ends");
		return res;
	}

	/**
	 * Default 'View Sales Order' page
	 *
	 */
	@GetMapping("/view-sales-order")
	public String viewSalesOrder(Model model, HttpSession session) {

		logger.info("Method : viewSalesOrder starts");

		logger.info("Method : viewSalesOrder ends");
		return "sales/view-sales-order";
	}

	/**
	 * Web Controller - Get Quotation
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-sales-order-through-ajax")
	public @ResponseBody DataTableResponse viewSalesOrderThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {

		logger.info("Method : viewSalesOrderThroughAjax starts");

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

			JsonResponse<List<QuotationMasterModel>> jsonResponse = new JsonResponse<List<QuotationMasterModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSalesOrder", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<QuotationMasterModel> quote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<QuotationMasterModel>>() {
					});

			String s = "";

			for (QuotationMasterModel m : quote) {
				byte[] pId = Base64.getEncoder().encode(m.getQuotationId().getBytes());

				m.setSalesOrder("<a href='javascript:void' onclick='pdfCreateSalesOrder(\"" + new String(pId) + "\")'>"
						+ m.getSalesOrder() + "</a>");
				if (m.getApproveActive()) {
					m.setApproveStatus("Complete");
				} else {
					m.setApproveStatus("Pending");
				}

				if (m.getSalesOrderActive()) {
					m.setStatus("Complete");
				} else {
					m.setStatus("Pending");
				}

				if (m.getpOrderImage().contentEquals("null")) {
					m.setpOrderImage(m.getPurchaseOrder());
				} else {
					s = "<a href='/document/purchaseOrder/" + m.getpOrderImage() + "' title='" + m.getpOrderImage()
							+ "' target='_blank'>" + m.getPurchaseOrder() + "</a>";
					m.setpOrderImage(s);
					s = "";

				}
				s = "";

				s = s + " <a href='generate-sales-order?id=" + new String(pId)
						+ "'><i class='fa fa-external-link' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(quote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSalesOrderThroughAjax ends");
		return response;
	}

	/**
	 * Web Controller - Get Sales Order List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sales-order-salesOrderAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesOrderListAutoSearch(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getSalesOrderListAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesOrderAuotSearch?id=" + searchValue,
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

		logger.info("Method : getSalesOrderListAutoSearch ends");
		return res;
	}

	/**
	 * Web Controller - Get Purchase Order List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sales-order-pOrderAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getPOrderListAutoSearch(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getPOrderListAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getPurchaseOrderListAutoSearch?id=" + searchValue,
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

		logger.info("Method : getPOrderListAutoSearch ends");
		return res;
	}

	/**
	 * Web Controller - Sales Order Report
	 *
	 *//*
		 * @GetMapping("/view-sales-order-generate-report") public String
		 * salesOrderGenerateReport(Model model, HttpSession session) {
		 * 
		 * logger.info("Method : salesOrderGenerateReport starts");
		 * 
		 * 
		 * logger.info("Method : salesOrderGenerateReport ends"); return
		 * "sales/report-sales-order"; }
		 */
	/**
	 * Web Controller - Get POS NUMBER By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-quotation-master-posAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> posAutoComplete(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesOrderList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getPOSNumber?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSalesOrderList ends");
		return res;
	}

	/**
	 * Web Controller - Sales Order Report
	 *
	 */
	@GetMapping("/view-sales-order-generate-report")
	public String salesOrderGenerateReport(Model model, HttpSession session) {
		logger.info("Method : salesOrderGenerateReport starts");

		try {
			DropDownModel[] dd = restClient.getForObject(env.getSalesUrl() + "report-itemCategory",
					DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dd);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] ddm = restClient.getForObject(env.getSalesUrl() + "report-itemSubCategory",
					DropDownModel[].class);
			List<DropDownModel> subCategoryList = Arrays.asList(ddm);
			model.addAttribute("subCategoryList", subCategoryList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : salesOrderGenerateReport ends");
		return "sales/report-sales-order";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-sales-order-generate-report-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getItemAutoSearchListForReport(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchListForReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getItemListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getItemAutoSearchListForReport ends");
		return res;
	}
}
