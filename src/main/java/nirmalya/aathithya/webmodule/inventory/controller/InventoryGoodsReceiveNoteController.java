/**
 * WebController of InventoryGoodsReceiveNote
 */
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
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.inventory.model.AdditionalChargesModel;
import nirmalya.aathithya.webmodule.inventory.model.BatchModel;
import nirmalya.aathithya.webmodule.inventory.model.InspectGoodsReceiveModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryCustomerMailModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryPoDetailsForGrnModel;
import nirmalya.aathithya.webmodule.inventory.model.InventorySearchPurchaseOrderModel;
import nirmalya.aathithya.webmodule.inventory.model.SequenceModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryGoodsReceiveNoteController {

	Logger logger = LoggerFactory.getLogger(InventoryGoodsReceiveNoteController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	@Autowired
	MailService mailService;

	/*
	 * GetMApping for Adding new GoodsReceiveNote
	 *
	 */

	@SuppressWarnings("unused")
	@GetMapping("add-goods-receive-note")
	public String addGoodsReceive(Model model, HttpSession session) {

		logger.info("Method : addGoodsReceive starts");
		InventoryGoodsReceiveModel spa = (InventoryGoodsReceiveModel) session.getAttribute("sspa");

		/*
		 * dropDown for item Type add-items-getSubGroup-throughAjax
		 */
		try {

			String userId = (String) session.getAttribute("USER_ID");
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-get-godown?userId=" + userId, DropDownModel[].class);
			List<DropDownModel> godownList = Arrays.asList(dropDownModel);
			model.addAttribute("godownList", godownList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		InventoryGoodsReceiveModel invGoodsReceiveModel = new InventoryGoodsReceiveModel();
		InventoryGoodsReceiveModel receiveInventory = null;

		try {
			receiveInventory = (InventoryGoodsReceiveModel) session.getAttribute("receiveInventory");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");
		if (spa != null) {
			String ImgName = spa.getInvImg();
			String s = "";
			s = "<a class='example-image-link' href='/spaTable/" + ImgName + "' title='" + ImgName
					+ "' target= '_blank' >" + ImgName + "</a>";
			spa.setAction(s);
			model.addAttribute("invGoodsReceiveModel", invGoodsReceiveModel);
			session.setAttribute("sspa", null);
		} else {
			model.addAttribute("invGoodsReceiveModel", invGoodsReceiveModel);
		}
		logger.info("Method : addGoodsReceive ends");
		return "inventory/InvAddGoodsReceive";

	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-itemSubCategory-through-ajax" })
	public @ResponseBody JsonResponse<DropDownModel> getItemSubCategory(@RequestParam("itmCategory") String itmCategory,
			@RequestParam("gRNPurchaseOrderId") String gRNPurchaseOrderId) {
		logger.info("Method : getItemSubCategory starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-itemSubCategories?id=" + itmCategory
					+ "&gRNPurchaseOrderId=" + gRNPurchaseOrderId, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getItemSubCategory ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-getpurchaseOrderDetails" })
	public @ResponseBody JsonResponse<List<InventoryPoDetailsForGrnModel>> getpurchaseOrderDetails(
			@RequestParam("id") String gRNPurchaseOrderId) {
		logger.info("Method : getpurchaseOrderDetails starts");
		JsonResponse<List<InventoryPoDetailsForGrnModel>> res = new JsonResponse<List<InventoryPoDetailsForGrnModel>>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-purchase-order-details?id=" + gRNPurchaseOrderId,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			List<InventoryPoDetailsForGrnModel> form = mapper.convertValue(res.getBody(),
					new TypeReference<List<InventoryPoDetailsForGrnModel>>() {
					});
			for (InventoryPoDetailsForGrnModel m : form) {
				m.setOrderQuantity(m.getOrderQuantity() - m.getReceiveQuantity());
			}
			res.setBody(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {

			res.setMessage("success");
		}
		logger.info("Method : getpurchaseOrderDetails ends");
		return res;
	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-itemName-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemName(@RequestParam("itmSubCategory") String itmSubCategory,
			@RequestParam("gRNPurchaseOrderId") String gRNPurchaseOrderId) {
		logger.info("Method : getItemName starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-items-Name?id=" + itmSubCategory
					+ "&gRNPurchaseOrderId=" + gRNPurchaseOrderId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {

			res.setMessage("success");
		}
		logger.info("Method : getItemName ends");

		return res;

	}

	/**
	 * get ItemSubcateroy by the onChange of category selected in addItem form
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-quantity-through-ajax" })
	public @ResponseBody JsonResponse<Object> getQuantity(@RequestParam("itemName") String itemName,
			@RequestParam("gRNPurchaseOrderId") String gRNPurchaseOrderId) {
		logger.info("Method : getQuantity starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-quantity?id=" + itemName + "&gRNPurchaseOrderId=" + gRNPurchaseOrderId,
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
		logger.info("Method : getQuantity ends");
		return res;

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-goods-receive-note-get-itemCat-through-ajax" })
	public @ResponseBody JsonResponse<Object> getItemCategory1(Model model, @RequestBody String pOrder,
			BindingResult result) {
		logger.info("Method : getItemCategory starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-items-categories?id=" + pOrder,
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

	// for getting vendor name on change of purchase order
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-goods-receive-note-get-vendor-through-ajax" })
	public @ResponseBody JsonResponse<Object> getvendorName(Model model, @RequestBody String vendorName,
			BindingResult result) {
		logger.info("Method : getVendorName starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getVendor?id=" + vendorName, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getVendorName ends");
		return res;
	}

	@PostMapping(value = { "/add-goods-receive-note-upload-file" })
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
	 * List view of table by PurchaseOrder Onchange
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-goods-receive-note-list-view-goodsreceive" })
	public @ResponseBody JsonResponse<Object> listviewGoodsReceive1(Model model, @RequestBody String purchaseOrder,
			BindingResult result) {
		logger.info("Method : listviewGoodsReceive starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "list-goods-receives?id=" + purchaseOrder,
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
		logger.info("Method : listviewGoodsReceive ends");
		return res;
	}

	/*
	 * post Mapping for add goodsReceivenote
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-goods-receive-note", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveGoodsReceive(
			@RequestBody List<InventoryGoodsReceiveModel> invGoodsReceiveModel, Model model, HttpSession session) {
		logger.info("Method : saveGoodsReceive function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String fileFormat = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			invGoodsReceiveModel.get(0).setCreatedBy(userId);
			MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					fileFormat = fileType[1].replaceAll("vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xls");
					contentName = nowTime + ".xls";
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					fileFormat = fileType[1].replaceAll("vnd.openxmlformats-officedocument.wordprocessingml.document",
							"docx");
					contentName = nowTime + ".docx";
				}
				if (fileType[1].contentEquals("vnd.ms-excel")) {
					fileFormat = fileType[1].replaceAll("vnd.ms-excel", "xls");
					contentName = nowTime + ".xls";
				}
				invGoodsReceiveModel.get(0).setInvImg(contentName);

				res = restClient.postForObject(env.getInventoryUrl() + "rest-adds-goods-receives", invGoodsReceiveModel,
						JsonResponse.class);

				if ((res.getCode() == null || res.getCode() == "")
						&& (res.getMessage() == null || res.getMessage() == "")) {
					Path path = Paths.get(env.getFileUploadInventory() + contentName);
					if (fileType[1].contentEquals("pdf")) {
						Files.write(path, bytes);
					}
					if (fileFormat.contentEquals("xls")) {

						Files.write(path, bytes);

					}
					if (fileType[1].contentEquals("docx")) {

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
							Path pathThumb = Paths.get(env.getFileUploadInventory() + "thumb\\" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				String contentName = (String) session.getAttribute("quotationPFileEdit");
				invGoodsReceiveModel.get(0).setInvImg(contentName);
				res = restClient.postForObject(env.getInventoryUrl() + "rest-adds-goods-receives", invGoodsReceiveModel,
						JsonResponse.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("Method : saveGoodsReceive ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-goods-receive-note-save-batch", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveBatchDetails(@RequestBody List<BatchModel> batch, Model model,
			HttpSession session) {
		logger.info("Method : saveBatchDetails function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (BatchModel m : batch) {
				m.setCreatedBy(userId);
			}
			System.out.println(batch);
			res = restClient.postForObject(env.getInventoryUrl() + "saveBatchDetails", batch, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : saveBatchDetails ends");
		return res;
	}

	/*
	 * 
	 * GetMApping For Listing goods receive note
	 * 
	 * 
	 */

	@GetMapping(value = { "view-goods-receive-note" })
	public String viewGoodsReceivenote1(Model model) {
		logger.info("Method : viewGoodsReceivenote starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoices-numbers",
					DropDownModel[].class);
			List<DropDownModel> invoiceNumber = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoiceNumber);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<Object> goodreceive = new JsonResponse<Object>();
		model.addAttribute("goodreceive", goodreceive);
		logger.info("Method : viewGoodsReceivenote ends");
		return "inventory/viewGoodsReceiveNote";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-goods-receive-note-throughAjax" })
	public @ResponseBody DataTableResponse viewGoodsReceiveThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, HttpSession session) {
		logger.info("Method : viewGoodsReceiveThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

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
			tableRequest.setUserId(userId);
			JsonResponse<List<InventoryGoodsReceiveModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-alls-goodsreceives", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryGoodsReceiveModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryGoodsReceiveModel>>() {
					});
			String s = "";

			for (InventoryGoodsReceiveModel m : form) {

				byte[] pId = Base64.getEncoder().encode(m.getgRNInvoiceId().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getgRNInvoiceId() + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;<a href='edit-goodsreceive-note?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> ";
				s = s + "&nbsp;<a href='javascript:void(0)' title='Delete' onclick='DeleteGoods(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> &nbsp;";
				s = s + "<a href='javascript:void(0)' title='Mail' onclick='sendMail(\"" + new String(pId)
						+ "\")'><i class='fa fa-envelope'></i></a>";

				if (m.getInspectStatus().contentEquals("0")) {
					s = s + " &nbsp;<a title='Add Batch' href='view-goods-receive-note-add-batch?id=" + new String(pId)
							+ "' ><i class='fa fa-plus'></i></a> ";
				} else if (m.getInspectStatus().contentEquals("1")) {
					s = s + " &nbsp;<a title='Add Batch' href='view-goods-receive-note-add-batch?id=" + new String(pId)
							+ "' ><i class='fa fa-plus'></i></a> ";
					s = s + " &nbsp;<a title='Inspect' href='view-goods-receive-note-inspect?id=" + new String(pId)
							+ "' ><i class='fa fa-info-circle'></i></a> ";
				} else if (m.getInspectStatus().contentEquals("2")) {
					s = s + " &nbsp;<a title='Inspect' href='view-goods-receive-note-inspect?id=" + new String(pId)
							+ "' ><i class='fa fa-info-circle'></i></a> ";
				}

				m.setgRNInvoiceId("<a href='javascript:void' onclick='pdfCreate(\"" + m.getgRNInvoiceId() + "\")'>"
						+ m.getgRNInvoiceId() + "</a>");
				if (m.getgRNPurchaseOrderId() != null && m.getgRNPurchaseOrderId() != "") {
					m.setgRNPurchaseOrderId("<a href='javascript:void' onclick='pdfCreatePurchase(\""
							+ m.getgRNPurchaseOrderId() + "\")'>" + m.getgRNPurchaseOrderId() + "</a>");
				} else {
					m.setgRNPurchaseOrderId("N/A");
				}

				if (m.getgRnInvoiceActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("InActive");
				}
				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;<a title='forward' href='javascript:void(0)' onclick='forwardInvoice(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a>";
					} else {
						s = s + " &nbsp;<a title='resubmit' href='javascript:void(0)' onclick='rejectInvoice(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a>";
					}
					s = s + " &nbsp;<a title='reject' href='javascript:void(0)' onclick='rejectInvoice(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> ";
					s = s + " &nbsp;<a title='return' href='javascript:void(0)' onclick='rejectInvoice(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a>";
				}

				m.setAction(s);

				s = "";

				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewGoodsReceiveThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * method to delete goods receive
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-goodsreceive-note" })
	public @ResponseBody JsonResponse<Object> deleteGoodsReceiveNote(@RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : deleteGoodsReceiveNote Starts");

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
			resp = restClient.getForObject(
					env.getInventoryUrl() + "delete-goods-receives-notes?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteGoodsReceiveNote ends");
		return resp;

	}

	/*
	 * 
	 * 
	 * GetMapping for Edit GoodsReceive note
	 * 
	 * 
	 * 
	 */

	@SuppressWarnings("unused")
	@GetMapping(value = { "edit-goodsreceive-note" })
	public String editGoodsReceiveNote(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editGoodsReceiveNote starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveModel>();
		List<DropDownModel> vendor = new ArrayList<DropDownModel>();
		try {
			InventoryGoodsReceiveModel[] invGoodsReceiveModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-receives-byId?id=" + id, InventoryGoodsReceiveModel[].class);

			List<InventoryGoodsReceiveModel> invGRN = Arrays.asList(invGoodsReceiveModel);

			try {
				AdditionalChargesModel[] dd = restClient.getForObject(
						env.getInventoryUrl() + "editAdditionalCharges?id=" + invGRN.get(0).getgRNInvoiceId(),
						AdditionalChargesModel[].class);
				List<AdditionalChargesModel> addChargesList = Arrays.asList(dd);

				model.addAttribute("addChargesList", addChargesList);
				model.addAttribute("addChargesListSize", addChargesList.size());

			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (invGRN.get(0).getgRNPurchaseOrderId() != null || invGRN.get(0).getgRNPurchaseOrderId() != "") {
				try {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "editVendor?id=" + invGRN.get(0).getgRNPurchaseOrderId(),
							DropDownModel[].class);
					List<DropDownModel> vendorList = Arrays.asList(dropDownModel);

					vendor.addAll(vendorList);
					model.addAttribute("vendorList", vendor);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * dropDown value for purchase order number
				 */
				try {
					DropDownModel[] dropDownModel = restClient
							.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
					List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
					model.addAttribute("purchase", purchaseOrder);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				/*
				 * dropDown for item Type add-items-getSubGroup-throughAjax
				 */
				try {
					String userId = (String) session.getAttribute("USER_ID");
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "rest-get-godown?userId=" + userId, DropDownModel[].class);
					List<DropDownModel> godownList = Arrays.asList(dropDownModel);
					model.addAttribute("godownList", godownList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			try {

				int i = 1;
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				List<Object> qtyLists = new ArrayList<Object>();
				List<Object> pendingLists = new ArrayList<Object>();
				List<Object> priceLists = new ArrayList<Object>();

				for (InventoryGoodsReceiveModel m : invGRN) {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "get-subs-categories?id=" + m.getItmCategory(),
							DropDownModel[].class);
					List<DropDownModel> subcat = Arrays.asList(dropDownModel);

					subCatLists.add(subcat);

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getInventoryUrl() + "get-item-Id?id=" + m.getItmSubCategory(), DropDownModel[].class);
					List<DropDownModel> itemS = Arrays.asList(dropDownModel1);

					itemLists.add(itemS);// get itemlist

					// dropdown for quantity in edit form

					DropDownModel[] dropDownModel2 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-quantity?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> item = Arrays.asList(dropDownModel2);
					qtyLists.add(item);

					// drpdown for pending quantity
					DropDownModel[] dropDownModel3 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-pending?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> pending = Arrays.asList(dropDownModel3);
					pendingLists.add(pending);

					// drpdown for price
					DropDownModel[] dropDownModel4 = restClient.getForObject(
							env.getInventoryUrl() + "editPrice?id=" + m.getgRnInvoiceItmName(), DropDownModel[].class);

					List<DropDownModel> prc = Arrays.asList(dropDownModel4);

					priceLists.add(prc);

				}

				model.addAttribute("subCatlists", subCatLists);
				model.addAttribute("itemLists", itemLists);
				model.addAttribute("qtyLists", qtyLists);
				model.addAttribute("pendingLists", pendingLists);
				model.addAttribute("priceLists", priceLists);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			try {

				InventoryGoodsReceiveModel[] purchasedetails = restClient
						.getForObject(
								env.getInventoryUrl() + "lists-purchases-orders?id=" + invGRN.get(0).getPorder()
										+ "&invId=" + invGRN.get(0).getgRNInvoiceId(),
								InventoryGoodsReceiveModel[].class);
				List<InventoryGoodsReceiveModel> purchaseOrder = Arrays.asList(purchasedetails);

				model.addAttribute("purchase1", purchaseOrder);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			model.addAttribute("invGoods", invGRN.get(0).getgRNInvoiceId());

			session.setAttribute("message", "");

			String ImgName = invGRN.get(0).getInvImg();
			if (ImgName != null && ImgName != "") {
				String s = "";
				if (ImgName.endsWith(".xls") || ImgName.endsWith(".docx")) {
					s = "/document/excel/" + ImgName;
					invGRN.get(0).setAction(s);
				} else {
					s = "/document/invImg/" + ImgName;
					invGRN.get(0).setAction(s);
				}
				session.setAttribute("quotationPFileEdit", invGRN.get(0).getInvImg());

			}
			model.addAttribute("purchase", invGRN.get(0).getgRNPurchaseOrderId());
			model.addAttribute("invGoodsReceiveModel", invGRN);
			if (vendor.size() == 0) {
				model.addAttribute("vendorList", vendor);
			} else {
				model.addAttribute("vendorList", null);
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGoodsReceiveNote ends");
		return "inventory/InvAddGoodsReceive";

	}

	@SuppressWarnings("unused")
	@GetMapping(value = { "view-goods-receive-note-inspect" })
	public String inspectGoodsReceiveNote(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : inspectGoodsReceiveNote starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveModel>();
		List<DropDownModel> vendor = new ArrayList<DropDownModel>();
		try {
			InventoryGoodsReceiveModel[] invGoodsReceiveModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-receives-byId?id=" + id, InventoryGoodsReceiveModel[].class);

			List<InventoryGoodsReceiveModel> invGRN = Arrays.asList(invGoodsReceiveModel);
			if (invGRN.get(0).getgRNPurchaseOrderId() != null || invGRN.get(0).getgRNPurchaseOrderId() != "") {
				try {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "editVendor?id=" + invGRN.get(0).getgRNPurchaseOrderId(),
							DropDownModel[].class);
					List<DropDownModel> vendorList = Arrays.asList(dropDownModel);

					vendor.addAll(vendorList);
					model.addAttribute("vendorList", vendor);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				/*
				 * dropDown value for purchase order number
				 */
				try {
					DropDownModel[] dropDownModel = restClient
							.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
					List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
					model.addAttribute("purchase", purchaseOrder);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				/*
				 * dropDown for item Type add-items-getSubGroup-throughAjax
				 */
				try {
					String userId = (String) session.getAttribute("USER_ID");
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "rest-get-godown?userId=" + userId, DropDownModel[].class);
					List<DropDownModel> godownList = Arrays.asList(dropDownModel);
					model.addAttribute("godownList", godownList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			try {

				int i = 1;
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				List<Object> qtyLists = new ArrayList<Object>();
				List<Object> pendingLists = new ArrayList<Object>();
				List<Object> priceLists = new ArrayList<Object>();

				for (InventoryGoodsReceiveModel m : invGRN) {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "get-subs-categories?id=" + m.getItmCategory(),
							DropDownModel[].class);
					List<DropDownModel> subcat = Arrays.asList(dropDownModel);

					subCatLists.add(subcat);

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getInventoryUrl() + "get-item-Id?id=" + m.getItmSubCategory(), DropDownModel[].class);
					List<DropDownModel> itemS = Arrays.asList(dropDownModel1);

					itemLists.add(itemS);// get itemlist

					// dropdown for quantity in edit form

					DropDownModel[] dropDownModel2 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-quantity?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> item = Arrays.asList(dropDownModel2);
					qtyLists.add(item);

					// drpdown for pending quantity
					DropDownModel[] dropDownModel3 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-pending?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> pending = Arrays.asList(dropDownModel3);
					pendingLists.add(pending);

					// drpdown for price
					DropDownModel[] dropDownModel4 = restClient.getForObject(
							env.getInventoryUrl() + "editPrice?id=" + m.getgRnInvoiceItmName(), DropDownModel[].class);
					List<DropDownModel> prc = Arrays.asList(dropDownModel4);
					priceLists.add(prc);

				}

				model.addAttribute("subCatlists", subCatLists);
				model.addAttribute("itemLists", itemLists);
				model.addAttribute("qtyLists", qtyLists);
				model.addAttribute("pendingLists", pendingLists);
				model.addAttribute("priceLists", priceLists);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			model.addAttribute("invGoods", invGRN.get(0).getgRNInvoiceId());

			session.setAttribute("message", "");
			
			model.addAttribute("purchase", invGRN.get(0).getgRNPurchaseOrderId());
			model.addAttribute("invGoodsReceiveModel", invGRN);
			if (vendor.size() == 0) {
				model.addAttribute("vendorList", vendor);
			} else {
				model.addAttribute("vendorList", null);
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : inspectGoodsReceiveNote ends");
		return "inventory/inspectGoodsReceive";

	}

	/**
	 * View selected GoodsReceive in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-goods-receive-note-modal" })
	public @ResponseBody JsonResponse<Object> modelviewGoodsReceive(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewGoodsReceive starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "gets-goods-receives-modals?id=" + index,
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
		logger.info("Method : modelviewGoodsReceive ends");
		return res;
	}

	@GetMapping("/view-goods-receive-note-generate-report")
	public String generateGoodsReceiveReport(Model model, HttpSession session) {

		logger.info("Method : generateGoodsReceiveReport starts");

		InventoryGoodsReceiveModel invGoodsReceiveModel = new InventoryGoodsReceiveModel();
		InventoryGoodsReceiveModel invGoodsReceiveSession = (InventoryGoodsReceiveModel) session.getAttribute("sgood");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (invGoodsReceiveSession != null) {
				model.addAttribute("invGoodsReceiveModel", invGoodsReceiveSession);
				session.setAttribute("sgood", null);
			} else {
				model.addAttribute("invGoodsReceiveModel", invGoodsReceiveModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoices-numbers",
					DropDownModel[].class);
			List<DropDownModel> invoiceNumber = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoiceNumber);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : generateGoodsReceiveReport ends");
		return "inventory/ReportGoodsreceive";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-goods-receive-note-getGuestAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGuestList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPOrderByAutosearch1?id=" + searchValue,
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

		logger.info("Method : getGuestList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-good-receive-note-getsprchs-number" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseNoSuggestion(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getPurchaseNoSuggestion starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchseOrderBySuggestion?id=" + searchValue,
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

		logger.info("Method : getPurchaseNoSuggestion ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-good-receive-note-getpurchase-number-autosuggest" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseNoSuggest(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getPurchaseNoSuggest starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPurchseOrderBySuggests?id=" + searchValue,
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

		logger.info("Method : getPurchaseNoSuggest ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-good-receive-note-getinvoice-number-autosuggest" })
	public @ResponseBody JsonResponse<DropDownModel> getInvoiceNoSuggest(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getInvoiceNoSuggest starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getInvoiceBySuggests?id=" + searchValue,
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

		logger.info("Method : getInvoiceNoSuggest ends");
		return res;
	}

	// for getting pending quantity

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-Pendingquantity-through-ajax" })
	public @ResponseBody JsonResponse<Object> getPendingQty(@RequestParam("itemName") String itemName,
			@RequestParam("gRNPurchaseOrderId") String gRNPurchaseOrderId) {
		logger.info("Method : getPendingQty starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPendingQuantity?id=" + itemName
					+ "&gRNPurchaseOrderId=" + gRNPurchaseOrderId, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getPendingQty ends");
		return res;

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-calculated-price-items" })
	public @ResponseBody JsonResponse<Object> getPriceCalculate(@RequestParam("item") String itemName,
			@RequestParam("pOrder") String pOrder) {
		logger.info("Method : getPriceCalculate starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "getPriceCalculate?id=" + itemName + "&pOrder=" + pOrder,
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
		logger.info("Method : getPriceCalculate ends");
		return res;

	}

	/**
	 * Web Controller - Get Item List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-goods-receive-note-get-item" })
	public @ResponseBody JsonResponse<InventorySearchPurchaseOrderModel> getItemAutoSearchList(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<InventorySearchPurchaseOrderModel> res = new JsonResponse<InventorySearchPurchaseOrderModel>();

		// String a[] = searchValue.split(",");
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getItemListByAutoSearchWithPO?id="
					+ searchValue.getKey() + "&po=" + searchValue.getName(), JsonResponse.class);
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-goods-receive-note-get-gst" })
	public @ResponseBody JsonResponse<InventorySearchPurchaseOrderModel> getGSTAutoSerach(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getGSTAutoSerach starts");

		JsonResponse<InventorySearchPurchaseOrderModel> res = new JsonResponse<InventorySearchPurchaseOrderModel>();
		System.out.println(env.getInventoryUrl() + "getGSTAutoSerach?id=" + searchValue);
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGSTAutoSerach?id=" + searchValue,
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

		logger.info("Method : getGSTAutoSerach ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Listing Goods Receive pending for apporve by approver
	 * 
	 * 
	 */
	@GetMapping(value = { "view-goods-receive-approval" })
	public String viewRequisitionApproval(Model model) {
		logger.info("Method : viewRequisitionApproval starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoices-numbers",
					DropDownModel[].class);
			List<DropDownModel> invoiceNumber = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoiceNumber);
			System.out.println("invoiceNumber: " + invoiceNumber);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
			System.out.println("purchaseOrder: " + purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<Object> goodreceive = new JsonResponse<Object>();
		model.addAttribute("goodreceive", goodreceive);
		logger.info("Method : viewRequisitionApproval ends");
		return "inventory/approveGoodsReceive";
	}

	/*
	 * view Item throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-goods-receive-approval-throughAjax" })
	public @ResponseBody DataTableResponse viewGoodsReceiveApprovalListThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, @RequestParam String param2,
			@RequestParam String param3, @RequestParam String param4, HttpSession session) {
		logger.info("Method : viewGoodsReceiveApprovalListThroughAjax starts");
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
			tableRequest.setParam4(param4);
			tableRequest.setUserId(UserId);
			JsonResponse<List<InventoryGoodsReceiveModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-all-goods-receive-approve",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryGoodsReceiveModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryGoodsReceiveModel>>() {
					});
			String s = "";
			String n = "";
			for (InventoryGoodsReceiveModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getgRNInvoiceId().getBytes());

				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getgRNInvoiceId() + "\")'><i class='fa fa-search search'></i></a>";
				n = "";

				n = "<a href='javascript:void' onclick='viewDocument(\"" + m.getInvImg() + "\")'>View File</a>";
				m.setgRNInvoiceId("<a href='javascript:void' onclick='pdfCreate(\"" + m.getgRNInvoiceId() + "\")'>"
						+ m.getgRNInvoiceId() + "</a>");
				if (m.getgRNPurchaseOrderId() != null && m.getgRNPurchaseOrderId() != "") {
					m.setgRNPurchaseOrderId("<a href='javascript:void' onclick='pdfCreatePurchase(\""
							+ m.getgRNPurchaseOrderId() + "\")'>" + m.getgRNPurchaseOrderId() + "</a>");
				} else {
					m.setgRNPurchaseOrderId("N/A");
				}
				m.setInvImg(n);
				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardInvoice(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectInvoice(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectInvoice(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectInvoice(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				}

				m.setAction(s);
				s = "";

				if (m.getgRnInvoiceActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewGoodsReceiveApprovalListThroughAjax ends");
		return response;
	}

	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-goods-receive-approval-forward-action" })
	public @ResponseBody JsonResponse<Object> saveGoodsReceiveApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : saveGoodsReceiveApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getInventoryUrl() + "save-goods-receive-approval-action?id=" + id + "&createdBy=" + userId,
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
		System.out.println("message==" + resp.getMessage());
		logger.info("Method : saveGoodsReceiveApprovalAction ends");
		return resp;
	}

	/*
	 * Reject Requisition
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-goods-receive-approval-reject-action" })
	public @ResponseBody JsonResponse<Object> saveRequisitionRejectAction(Model model,
			@RequestBody InventoryGoodsReceiveModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : saveGoodsReceiveRejectAction starts");
		System.out.println(reqobject);
		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getgRNInvoiceId());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;

		reqobject.setCreatedBy(userId);
		reqobject.setgRNInvoiceId(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getInventoryUrl() + "save-goods-receive-reject-action", reqobject,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : saveGoodsReceiveRejectAction ends");
		return res;
	}

	/**
	 * View selected GoodsReceive in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-goods-receive-approval-model" })
	public @ResponseBody JsonResponse<Object> modelviewGoodsReceiveApprove(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewGoodsReceiveApprove starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "gets-goods-receives-modals?id=" + index,
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
		logger.info("Method : modelviewGoodsReceiveApprove ends");
		return res;

	}

	/**
	 * View selected GoodsReceive in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "edit-goodsreceive-note-send-mail" })
	public @ResponseBody JsonResponse<Object> sendMail(Model model, @RequestBody String id, BindingResult result) {
		logger.info("Method : modelviewGoodsReceive starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		List<InventoryCustomerMailModel> form = null;
		byte[] encodeByte = Base64.getDecoder().decode(id);
		String reqstnId = (new String(encodeByte));
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-details-for-mail?id=" + reqstnId,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			form = mapper.convertValue(res.getBody(), new TypeReference<List<InventoryCustomerMailModel>>() {
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (form != null) {
			try {
				String myEmail = form.get(0).getCustMail();
				String orderNo = form.get(0).getPoNumber();
				String vendorName = form.get(0).getCustomerName();
				StringBuilder sb = new StringBuilder();
				sb.append("Dear " + vendorName + ",");
				sb.append("\n");
				sb.append("We have  Received  Items On Purchase Order - " + orderNo + " having GRN - "
						+ form.get(0).getGrnNo());
				sb.append("\n");
				sb.append(" At Our Store " + form.get(0).getStore() + ",  ");
				sb.append("\n");
				sb.append(" Address " + form.get(0).getReceivedAt() + ",");
				sb.append("\n");
				mailService.sendEmail(myEmail, "From Go-Cool", sb.toString());

			} catch (Exception ex) {
				logger.error(ex.getMessage());
				res.setMessage(ex.getMessage());
			}
		} else {
			res.setMessage("Something Went Wrong.");
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modelviewGoodsReceive ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-goods-receive-note-add-inspect-data" })
	public String adddGoodsReceiveInspectData(Model model, @RequestParam String PO, @RequestParam String GRN,
			@RequestParam String ITEM, @RequestParam String ITEMNAME) {
		logger.info("Method : adddGoodsReceiveInspectData starts");

		byte[] encodePOByte = Base64.getDecoder().decode(PO);
		String poId = (new String(encodePOByte));

		byte[] encodeGRNByte = Base64.getDecoder().decode(GRN);
		String grnId = (new String(encodeGRNByte));

		byte[] encodeITEMByte = Base64.getDecoder().decode(ITEM);
		String itemId = (new String(encodeITEMByte));

		byte[] encodeITEMNAMEByte = Base64.getDecoder().decode(ITEMNAME);
		String itemName = (new String(encodeITEMNAMEByte));

		model.addAttribute("PO", poId);
		model.addAttribute("GRN", grnId);
		model.addAttribute("ITEMID", itemId);
		model.addAttribute("ITEMNAME", itemName);
		
		BatchModel index = new BatchModel();
		
		index.setItemId(itemId);
		index.setGrn(grnId);
		
		JsonResponse<List<BatchModel>> res = new JsonResponse<List<BatchModel>>();

		List<BatchModel> batchDtls = new ArrayList<BatchModel>();

		try {
			res = restClient.postForObject(env.getInventoryUrl() + "editViewBatchDetails", index, JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			batchDtls = mapper.convertValue(res.getBody(), new TypeReference<List<BatchModel>>() {
			});
			
			for(BatchModel m : batchDtls) {
				SequenceModel[] dropDownModel = restClient.getForObject(
						env.getInventoryUrl() + "restGetSequenceNumber?id=" + m.getBatchNo() + "&grn=" + m.getGrn() + "&item=" + m.getItemId(), SequenceModel[].class);
				List<SequenceModel> sequenceList = Arrays.asList(dropDownModel);
				
				m.setSequenceList(sequenceList);
			}
			
			model.addAttribute("batchDtls", batchDtls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : adddGoodsReceiveInspectData ends");
		return "inventory/adddGoodsReceiveInspectData";
	}

	@GetMapping(value = { "view-goods-receive-note-generate-barcode" })
	public String generateBarcode(Model model, @RequestParam String id) {
		logger.info("Method : generateBarcode starts");

//		byte[] encodePOByte = Base64.getDecoder().decode(PO);
//		String poId = (new String(encodePOByte));
//		
		byte[] encodeGRNByte = Base64.getDecoder().decode(id);
		String grnId = (new String(encodeGRNByte));
//		
//		byte[] encodeITEMByte = Base64.getDecoder().decode(ITEM);
//		String itemId = (new String(encodeITEMByte));
//		
//		byte[] encodeITEMNAMEByte = Base64.getDecoder().decode(ITEMNAME);
//		String itemName = (new String(encodeITEMNAMEByte));
//		
//		model.addAttribute("PO", poId);
		model.addAttribute("GRN", grnId);
//		model.addAttribute("ITEMID", itemId);
//		model.addAttribute("ITEMNAME", itemName);

		logger.info("Method : generateBarcode ends");
		return "inventory/generateBarcodeOfItem";
	}

	@SuppressWarnings({ "unused" })
	@GetMapping(value = { "view-goods-receive-note-add-batch" })
	public String addBatchDetailsDefault(Model model, @RequestParam String id, HttpSession session) {
		logger.info("Method : addBatchDetailsDefault starts");

		byte[] encodePOByte = Base64.getDecoder().decode(id);
		String index = (new String(encodePOByte));

		List<DropDownModel> vendor = new ArrayList<DropDownModel>();
		try {
			InventoryGoodsReceiveModel[] invGoodsReceiveModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-receives-byId?id=" + index, InventoryGoodsReceiveModel[].class);

			List<InventoryGoodsReceiveModel> invGRN = Arrays.asList(invGoodsReceiveModel);
			if (invGRN.get(0).getgRNPurchaseOrderId() != null || invGRN.get(0).getgRNPurchaseOrderId() != "") {
				try {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "editVendor?id=" + invGRN.get(0).getgRNPurchaseOrderId(),
							DropDownModel[].class);
					List<DropDownModel> vendorList = Arrays.asList(dropDownModel);

					vendor.addAll(vendorList);
					model.addAttribute("vendorList", vendor);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				try {
					DropDownModel[] dropDownModel = restClient
							.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
					List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
					model.addAttribute("purchase", purchaseOrder);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				try {
					String userId = (String) session.getAttribute("USER_ID");
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "rest-get-godown?userId=" + userId, DropDownModel[].class);
					List<DropDownModel> godownList = Arrays.asList(dropDownModel);
					model.addAttribute("godownList", godownList);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
			try {

				int i = 1;
				List<Object> subCatLists = new ArrayList<Object>();
				List<Object> itemLists = new ArrayList<Object>();
				List<Object> qtyLists = new ArrayList<Object>();
				List<Object> pendingLists = new ArrayList<Object>();
				List<Object> priceLists = new ArrayList<Object>();

				for (InventoryGoodsReceiveModel m : invGRN) {
					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getInventoryUrl() + "get-subs-categories?id=" + m.getItmCategory(),
							DropDownModel[].class);
					List<DropDownModel> subcat = Arrays.asList(dropDownModel);
					subCatLists.add(subcat);

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getInventoryUrl() + "get-item-Id?id=" + m.getItmSubCategory(), DropDownModel[].class);
					List<DropDownModel> itemS = Arrays.asList(dropDownModel1);

					itemLists.add(itemS);// get itemlist

					DropDownModel[] dropDownModel2 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-quantity?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> item = Arrays.asList(dropDownModel2);
					qtyLists.add(item);

					DropDownModel[] dropDownModel3 = restClient.getForObject(
							env.getInventoryUrl() + "get-edit-pending?id=" + m.getgRnInvoiceItmName()
									+ "&gRNPurchaseOrderId=" + m.getPorder() + "&gRNInvoiceId=" + m.getgRNInvoiceId(),
							DropDownModel[].class);
					List<DropDownModel> pending = Arrays.asList(dropDownModel3);
					pendingLists.add(pending);

					DropDownModel[] dropDownModel4 = restClient.getForObject(
							env.getInventoryUrl() + "editPrice?id=" + m.getgRnInvoiceItmName(), DropDownModel[].class);

					List<DropDownModel> prc = Arrays.asList(dropDownModel4);
					priceLists.add(prc);

				}

				model.addAttribute("subCatlists", subCatLists);
				model.addAttribute("itemLists", itemLists);
				model.addAttribute("qtyLists", qtyLists);
				model.addAttribute("pendingLists", pendingLists);
				model.addAttribute("priceLists", priceLists);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			model.addAttribute("invGoods", invGRN.get(0).getgRNInvoiceId());

			session.setAttribute("message", "");

			model.addAttribute("purchase", invGRN.get(0).getgRNPurchaseOrderId());
			model.addAttribute("invGoodsReceiveModel", invGRN);
			if (vendor.size() == 0) {
				model.addAttribute("vendorList", vendor);
			} else {
				model.addAttribute("vendorList", null);
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addBatchDetailsDefault ends");
		return "inventory/deafault-item-details-for-batch";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-goods-receive-note-batch-edit-view" })
	public @ResponseBody JsonResponse<List<BatchModel>> editViewOfBatchDetails(Model model,
			@RequestBody BatchModel index, BindingResult result) {
		logger.info("Method : editViewOfBatchDetails starts");

		JsonResponse<List<BatchModel>> res = new JsonResponse<List<BatchModel>>();

		List<BatchModel> batchDtls = new ArrayList<BatchModel>();

		try {
			res = restClient.postForObject(env.getInventoryUrl() + "editViewBatchDetails", index, JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			batchDtls = mapper.convertValue(res.getBody(), new TypeReference<List<BatchModel>>() {
			});
			
			for(BatchModel m : batchDtls) {
				SequenceModel[] dropDownModel = restClient.getForObject(
						env.getInventoryUrl() + "restGetSequenceNumber?id=" + m.getBatchNo() + "&grn=" + m.getGrn() + "&item=" + m.getItemId(), SequenceModel[].class);
				List<SequenceModel> sequenceList = Arrays.asList(dropDownModel);
				
				m.setSequenceList(sequenceList);
			}
			
			res.setBody(batchDtls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : editViewOfBatchDetails ends");
		return res;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-goods-receive-note-add-inspect-data-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveReturnDetails(@RequestBody List<InspectGoodsReceiveModel> batch, Model model,
			HttpSession session) {
		logger.info("Method : saveBatchDetails function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (InspectGoodsReceiveModel m : batch) {
				m.setCreatedBy(userId);
				if(m.getStatus().contains("0")) {
					m.setInspectStatus("2");
				}else{
					m.setInspectStatus("1");
				}
			}
			res = restClient.postForObject(env.getInventoryUrl() + "saveInspectDetails", batch, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : saveBatchDetails ends");
		return res;
	}

}
