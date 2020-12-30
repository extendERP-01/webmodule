package nirmalya.aathithya.webmodule.inventory.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.AllocateItemsToRackModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InspectedGRNItemController {

	Logger logger = LoggerFactory.getLogger(InspectedGRNItemController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping(value = { "view-inspected-grn" })
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
		return "inventory/viewInspectedGRNItem";
	}

	/*
	 * view throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-inspected-grn-throughAjax" })
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
				s = s + " &nbsp;&nbsp <a href='view-inspected-grn-allocate-rack?id=" + new String(pId)
						+ "' ><i class=\"fa fa-tasks\" aria-hidden=\"true\"></i></a> ";

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

	@SuppressWarnings("unused")
	@GetMapping(value = { "view-inspected-grn-allocate-rack" })
	public String allocateGRNItemToRack(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : allocateGRNItemToRack starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveModel>();
		List<DropDownModel> vendor = new ArrayList<DropDownModel>();
		List<DropDownModel> itemList = new ArrayList<DropDownModel>();
		List<DropDownModel> itemList2 = new ArrayList<DropDownModel>();
		
		try {
			InventoryGoodsReceiveModel[] invGoodsReceiveModel = restClient.getForObject(
					env.getInventoryUrl() + "edit-goods-receives-byId?id=" + id, InventoryGoodsReceiveModel[].class);

			List<InventoryGoodsReceiveModel> invGRN = Arrays.asList(invGoodsReceiveModel);
			for(InventoryGoodsReceiveModel a : invGRN) {
				 
				itemList.add(new DropDownModel(a.getgRnInvoiceItmName() ,a.getGodown()));
				itemList2.add(new DropDownModel(a.getgRnInvoiceItmName() ,a.getGodown()));
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
					e.printStackTrace();
				}

				try {
					DropDownModel[] dropDownModel = restClient.postForObject(
							env.getInventoryUrl() + "getSubInventoryByStorePost",itemList, DropDownModel[].class);
					List<DropDownModel> subInventoryList = Arrays.asList(dropDownModel);
					model.addAttribute("subInventoryList", subInventoryList);
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
				
				/*
				 * dropDown for item Type add-items-getSubGroup-throughAjax
				 */
				try { 
					DropDownModel[] dropDownModel = restClient.postForObject(
							env.getInventoryUrl() + "rest-get-ware-house-post", itemList2, DropDownModel[].class);
					List<DropDownModel> gwarehouseList = Arrays.asList(dropDownModel);
					model.addAttribute("gwarehouseList", gwarehouseList);
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
					s = "/document/image/" + ImgName;
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

		logger.info("Method : allocateGRNItemToRack ends");
		return "inventory/allocateGRNItemToRack";
	}

	/**
	 * get rack details by sub inventory
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-shelfs" })
	public @ResponseBody JsonResponse<AllocateItemsToRackModel> getRackList(@RequestParam("id") String id,
			@RequestParam String itemId) {
		logger.info("Method : getRackList starts");

		JsonResponse<AllocateItemsToRackModel> res = new JsonResponse<AllocateItemsToRackModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-shelfs-list?id=" + id + "&itemId=" + itemId,
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
		logger.info("Method : getRackList ends");
		return res;
	}

	/**
	 * get ware house details by sub inventory
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-goods-receive-note-get-ware-house" })
	public @ResponseBody JsonResponse<DropDownModel> getWareHouseList(@RequestParam("id") String id) {
		logger.info("Method : getWareHouseList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "get-ware-house?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getWareHouseList ends");
		return res;
	}

	/*
	 * post Mapping for add goodsReceivenote
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-inspected-grn-allocate-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveGoodsReceive(
			@RequestBody List<AllocateItemsToRackModel> allocateItemsToRackModelList, HttpSession session) {
		logger.info("Method : saveGoodsReceive function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			allocateItemsToRackModelList.forEach(s -> s.setCratedBy((String) session.getAttribute("USER_ID")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AllocateItemsToRackModel a : allocateItemsToRackModelList) {
			String[] s = a.getShelfName().split("_");
			if (s != null) {
				a.setShelfName(s[0]);
				String[] s2 = s[1].split("--");
				if (s2 != null) {
					a.setRackId(s2[0]);
					a.setInspQuantity(Double.parseDouble(s2[1]));
				}
			}
		}
		try {
			res = restClient.postForObject(env.getInventoryUrl() + "rest-adds-allocated-shelfs",
					allocateItemsToRackModelList, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!allocateItemsToRackModelList.isEmpty()) {

			List<AllocateItemsToRackModel> dataList = new ArrayList<AllocateItemsToRackModel>();
			List<DropDownModel> imageList = new ArrayList<DropDownModel>();
			Double quantity = allocateItemsToRackModelList.get(0).getQuantity();
			for (int i = 0; i <= quantity; i++) {
				AllocateItemsToRackModel model = new AllocateItemsToRackModel();
				DropDownModel dd = new DropDownModel();
				String barcodeImageName = "";
				String barcodeImageNo = generate12Random();
				long nowTime = new Date().getTime();
				barcodeImageName = nowTime + ".png";
				saveBarcode(barcodeImageName, barcodeImageNo);
				model.setPoNo(allocateItemsToRackModelList.get(0).getPoNo());
				model.setGrn(allocateItemsToRackModelList.get(0).getGrn());
				model.setItemId(allocateItemsToRackModelList.get(0).getItemId());
				model.setStoreId(allocateItemsToRackModelList.get(0).getStoreId());
				model.setWareHouseId(allocateItemsToRackModelList.get(0).getWareHouseId());
				model.setRackId(allocateItemsToRackModelList.get(0).getRackId());
				model.setShelfName(allocateItemsToRackModelList.get(0).getShelfName());
				model.setSubInventoryId(allocateItemsToRackModelList.get(0).getSubInventoryId());
				model.setBarcodeNo(barcodeImageNo);
				model.setBarCodeImageName(barcodeImageName);
				model.setCratedBy((String) session.getAttribute("USER_ID"));
				dataList.add(model);
				dd.setKey(barcodeImageNo);
				dd.setName(barcodeImageName);
				imageList.add(dd);
			}
			try {
				res = restClient.postForObject(env.getInventoryUrl() + "rest-adds-barcode-details", dataList,
						JsonResponse.class);
				session.setAttribute("imageList", imageList);

			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("imageList", "");
			}
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
	@PostMapping("/view-inspected-grn-allocate-generate-barcode")
	public @ResponseBody JsonResponse<Object> generateToken(
			@RequestBody AllocateItemsToRackModel allocateItemsToRackModelList, HttpSession session) {

		logger.info("Method : generateToken starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		List<AllocateItemsToRackModel> dataList = new ArrayList<AllocateItemsToRackModel>();
		List<DropDownModel> imageList = new ArrayList<DropDownModel>();
		if (allocateItemsToRackModelList != null) {
			String userId = "";
			try {
				userId = (String) session.getAttribute("USER_ID");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String[] s = allocateItemsToRackModelList.getShelfName().split("_");
			if (s != null) {
				allocateItemsToRackModelList.setShelfName(s[0]);
				String[] s2 = s[1].split("--");
				if (s2 != null) {
					allocateItemsToRackModelList.setRackId(s2[0]);
					allocateItemsToRackModelList.setInspQuantity(Double.parseDouble(s2[1]));
				}
			}

			Double quantity = allocateItemsToRackModelList.getQuantity();
			for (int i = 0; i <= quantity; i++) {
				AllocateItemsToRackModel model = new AllocateItemsToRackModel();
				DropDownModel dd = new DropDownModel();
				String barcodeImageName = "";
				String barcodeImageNo = generate12Random();
				long nowTime = new Date().getTime();
				barcodeImageName = nowTime + ".png";
				saveBarcode(barcodeImageName, barcodeImageNo);
				model.setPoNo(allocateItemsToRackModelList.getPoNo());
				model.setGrn(allocateItemsToRackModelList.getGrn());
				model.setItemId(allocateItemsToRackModelList.getItemId());
				model.setStoreId(allocateItemsToRackModelList.getStoreId());
				model.setWareHouseId(allocateItemsToRackModelList.getWareHouseId());
				model.setRackId(allocateItemsToRackModelList.getRackId());
				model.setShelfName(allocateItemsToRackModelList.getShelfName());
				model.setSubInventoryId(allocateItemsToRackModelList.getSubInventoryId());
				model.setBarcodeNo(barcodeImageNo);
				model.setBarCodeImageName(barcodeImageName);
				model.setCratedBy(userId);
				dataList.add(model);
				dd.setKey(barcodeImageNo);
				dd.setName(barcodeImageName);
				imageList.add(dd);
			}
			try {
				res = restClient.postForObject(env.getInventoryUrl() + "rest-adds-barcode-details", dataList,
						JsonResponse.class);
				session.setAttribute("imageList", imageList);

			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("imageList", "");
			}

		}
		/*
		 * String barcodeImageName = ""; String barcodeImageNo = generate12Random();
		 * long nowTime = new Date().getTime(); barcodeImageName = nowTime + ".png";
		 * saveBarcode(barcodeImageName, barcodeImageNo);
		 */

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

		logger.info("Method : generateToken function Ends");
		return res;
	}

	/*
	 * for bar code
	 */
	public BufferedImage generateEAN13BarcodeImage(String barcodeText) {

		logger.info("Method : generateEAN13BarcodeImage starts");
		EAN13Bean barcodeGenerator = new EAN13Bean();
		BitmapCanvasProvider canvas = new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);

		barcodeGenerator.generateBarcode(canvas, barcodeText);
		logger.info("Method : generateEAN13BarcodeImage starts");
		return canvas.getBufferedImage();
	}

// to save the bar code image in file
	public void saveBarcode(String imageName, String barcodeNo) {
		logger.info("Method : saveBarcode starts");

		Path path = Paths.get(env.getBarcodesUpload() + imageName);

		try {
			BufferedImage img = generateEAN13BarcodeImage(barcodeNo);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			Files.write(path, imageInByte);
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : saveBarcode starts");
	}

// for generating 12 digit random number
	public String generate12Random() {
		logger.info("Method : generate12Random starts");

		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(random.nextInt(9) + 1);
		for (int i = 0; i < 11; i++) {
			sb.append(random.nextInt(10));
		}
		logger.info("Method : generate12Random starts");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-inspected-grn-allocate-generate-barcode-download")
	public void generateRestaurantMenuItemsPdf(HttpServletResponse response, HttpSession session) {

		List<DropDownModel> dataList = new ArrayList<DropDownModel>();
		try {

			dataList = (List<DropDownModel>) session.getAttribute("imageList");

			session.removeAttribute("imageList");
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		for (DropDownModel a : dataList) {

			a.setName(variable + "document/barcode/" + a.getName());

		}
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("dataList", dataList);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/barcode_pdf", data);
			InputStream in = new FileInputStream(file);
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
	 * get rack details by sub inventory
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-inspected-grn-allocate-generate-barcode-reprint" })
	public @ResponseBody JsonResponse<DropDownModel> getReprintList(
			@RequestBody List<AllocateItemsToRackModel> allocateItemsToRackModelList, HttpSession session) {
		logger.info("Method : getRackList starts");
		List<DropDownModel> imageList = new ArrayList<DropDownModel>();
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		for (AllocateItemsToRackModel a : allocateItemsToRackModelList) {
			String[] s = a.getShelfName().split("_");
			if (s != null) {
				a.setShelfName(s[0]);
				String[] s2 = s[1].split("--");
				if (s2 != null) {
					a.setRackId(s2[0]);
					a.setInspQuantity(Double.parseDouble(s2[1]));
				}
			}
		}
		try {
			res = restClient.postForObject(env.getInventoryUrl() + "rest-get-barcode-details",
					allocateItemsToRackModelList, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			imageList = mapper.convertValue(res.getBody(), new TypeReference<List<DropDownModel>>() {
			});
			session.setAttribute("imageList", imageList);

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("imageList", "");
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		System.out.println(res);
		logger.info("Method : getRackList ends");
		return res;
	}

}
