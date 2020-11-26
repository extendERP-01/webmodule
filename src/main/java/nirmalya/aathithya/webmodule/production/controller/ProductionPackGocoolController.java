package nirmalya.aathithya.webmodule.production.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitModel;
import nirmalya.aathithya.webmodule.production.model.ProductionGocoolPacakgeingModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipePackagingModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPackGocoolController {

	Logger logger = LoggerFactory.getLogger(ProductionPackGocoolController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * Get Mapping view pipe packaging page
	 */
	@GetMapping("/view-productions-packaging")
	public String viewPipepackaging(Model model, HttpSession session) {

		logger.info("Method : viewPipepackaging   starts");

		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] items = restClient.getForObject(env.getProduction() + "getProdItemList",
					DropDownModel[].class);
			List<DropDownModel> itemList = Arrays.asList(items);

			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		}

		logger.info("Method : viewPipepackaging   ends");

		return "production/view-production-packaging";
	}

	/*
	 * For view packaging throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-productions-packaging-throughajax")
	public @ResponseBody DataTableResponse viewPipepackaging(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewPipepackaging statrs");

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

			JsonResponse<List<ProductionGocoolPacakgeingModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getProduction() + "view-production-packaging", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionGocoolPacakgeingModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionGocoolPacakgeingModel>>() {
					});

			for (ProductionGocoolPacakgeingModel m : motherCoilSlit) {
				String s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getProdId().getBytes());

				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  ";
				if (m.getProductionStage() == 1) {
					s = s + "<a href='view-productions-packaging-edit?prodId=" + new String(encodeId)
							+ "' ><button class=\"styled\">Add packaging Dtls" + "</button></a>";
				} else {
					s = s + " <button class=\"styled\">packaging Dtls Added" + "</button> ";
				}

				m.setAction(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(motherCoilSlit);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewPipepackaging ends");

		return response;
	}

	/**
	 * modal view
	 * 
	 * @param model
	 * @param index
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-packaging-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalPipePAckaging(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalPipePAckaging starts");

		byte[] batchId = Base64.getDecoder().decode(index.getBytes());

		String prodId = (new String(batchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getProduction() + "viewPackagingById?prodId=" + prodId,
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
		logger.info("Method : modalPipePAckaging  ends ");
		return response;
	}

	/**
	 * add packaging details
	 * 
	 * @param model
	 * @param mBatchId1
	 * @param mThickId1
	 * @param slitBatchId1
	 * @param session
	 * @return
	 */
	@GetMapping("view-productions-packaging-edit")
	public String editMotherCoil(Model model, @RequestParam String prodId, HttpSession session) {

		logger.info("Method : editMPackaging starts");
		byte[] encodeByte = Base64.getDecoder().decode(prodId.getBytes());

		String mBatchId = (new String(encodeByte));

		try {

			ProductionGocoolPacakgeingModel[] motherCoil = restClient.getForObject(
					env.getProduction() + "edit-packagings-ById?prodId=" + mBatchId,
					ProductionGocoolPacakgeingModel[].class);
			List<ProductionGocoolPacakgeingModel> prodList = Arrays.asList(motherCoil);
			if (!prodList.isEmpty()) {
				model.addAttribute("edit", "for Edit");
				model.addAttribute("prodList", prodList);
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editMPackaging ends");
		return "production/add-pipe-packaging-form";

	}

	/**
	 * Web controller add packaging data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-packaging-add-ajax")
	public @ResponseBody JsonResponse<Object> addpackagingDetails(
			@RequestBody List<ProductionGocoolPacakgeingModel> packaging, HttpSession session) {

		logger.info("Method : addpackagingDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (ProductionGocoolPacakgeingModel r : packaging) {
				r.setCreatedBy(userId);
				/*
				 * if (r.getBarcodeImageName() == null && r.getBarcodeImageName() == null) {
				 * String barcodeImageName = ""; String barcodeImageNo = generate12Random();
				 * long nowTime = new Date().getTime(); barcodeImageName = nowTime + ".png";
				 * r.setBarcodeImageName(barcodeImageName);
				 * r.setBarcodeImageNumber(barcodeImageNo); saveBarcode(barcodeImageName,
				 * barcodeImageNo); }
				 */

			}

			resp = restClient.postForObject(env.getProduction() + "addpackagingDetail", packaging, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if ((resp.getCode() == null || resp.getCode() == "")
				&& (resp.getMessage() == null || resp.getMessage() == "")) {

			resp.setMessage("Success");

		} else {
			resp.setMessage("failed");
		}

		logger.info("Method : addpackagingDetails function Ends");
		return resp;
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@PostMapping("/view-productions-packaging-add-form")
	public @ResponseBody JsonResponse<Object> addpackagingPlanningForm(
			@RequestBody ProductionPipePackagingModel packageDetails, HttpSession session) {

		logger.info("Method : addpackagingPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		session.setAttribute("packageDetails", packageDetails);

		logger.info("Method : addpolishingPlanning function Ends");
		resp.setMessage("success");
		return resp;

	}

	/*
	 * GetMapping for Add language view page
	 */
	@GetMapping("/view-productions-packagin-add-form-page")
	public String addpagePackageing(Model model, HttpSession session) {

		logger.info("Method : addpagePackageing starts");

		ProductionPipePackagingModel packageDetails = new ProductionPipePackagingModel();
		ProductionPipePackagingModel sessionpackageDetails = (ProductionPipePackagingModel) session
				.getAttribute("packageDetails");

		String message = (String) session.getAttribute("message");

		try {
			ProductionPipePackagingModel[] packagingModel = restClient.getForObject(env.getProduction()
					+ "get-packaging-draft-details?mBatchId=" + sessionpackageDetails.gettMotherCoilBatch()
					+ "&mThickId=" + sessionpackageDetails.gettMotherCoilThickness() + "&slitBatchId="
					+ sessionpackageDetails.gettPipeSlitBatch() + "&slitWidth="
					+ sessionpackageDetails.gettPipeSlitWidth() + "&pipeSize=" + sessionpackageDetails.gettPipeSize()
					+ "&subBatch=" + sessionpackageDetails.getSlitSubGroup(), ProductionPipePackagingModel[].class);
			List<ProductionPipePackagingModel> pacakagingDetailsDetails = Arrays.asList(packagingModel);
			if (!pacakagingDetailsDetails.isEmpty()) {

				model.addAttribute("edit", "for edit");
			} else {

				model.addAttribute("edit", null);
			}
			model.addAttribute("packagingModel", pacakagingDetailsDetails);
			model.addAttribute("sessionpackageDetails", sessionpackageDetails);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		if (sessionpackageDetails != null) {
			model.addAttribute("packageDetails", sessionpackageDetails);
			session.setAttribute("sessionpackageDetails", null);
		} else {
			model.addAttribute("packageDetails", packageDetails);
		}

		logger.info("Method : addpagePackageing ends");

		return "production/add-pipe-packaging-form";
	}

	/*
	 * For Modal packaging details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-packaging-details-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalDetails(Model model, @RequestBody MotherCoilSlitModel index,
			BindingResult result) {

		logger.info("Method :modalDetails starts");

		byte[] batchId = Base64.getDecoder().decode(index.gettMotherCoilBatch().getBytes());
		byte[] thickId = Base64.getDecoder().decode(index.gettMotherCoilThickness().getBytes());
		byte[] slitbatchId = Base64.getDecoder().decode(index.gettPipeSlitBatch().getBytes());
		byte[] slitWidthId = Base64.getDecoder().decode(index.gettPipeSlitWidth().getBytes());
		byte[] pipeSizeId = Base64.getDecoder().decode(index.gettPipeSize().getBytes());

		String mBatchId = (new String(batchId));
		String mThickId = (new String(thickId));
		String slitBatchId = (new String(slitbatchId));
		String slitWidth = (new String(slitWidthId));
		String pipeSize = (new String(pipeSizeId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(
					env.getProduction() + "getPackagingDetailsModal?mBatchId=" + mBatchId + "&mThickId=" + mThickId
							+ "&slitBatchId=" + slitBatchId + "&slitWidth=" + slitWidth + "&pipeSize=" + pipeSize,
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
		logger.info("Method : modalDetails  ends ");
		return response;
	}

	/**
	 * Web controller add draft data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-packaging-draft-ajax")
	public @ResponseBody JsonResponse<Object> draftPackagingPlanning(
			@RequestBody List<ProductionPipePackagingModel> draftObj, HttpSession session) {

		logger.info("Method : draftPackagingPlanning starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (ProductionPipePackagingModel r : draftObj) {
				r.settPipeCreatedBy(userId);
				if (r.getBarcodeImageName().isEmpty()) {
					String barcodeImageName = "";
					String barcodeImageNo = generate12Random();
					long nowTime = new Date().getTime();
					barcodeImageName = nowTime + ".png";
					r.setBarcodeImageName(barcodeImageName);
					r.setBarcodeImageNumber(barcodeImageNo);
					saveBarcode(barcodeImageName, barcodeImageNo);
				}

			}

			resp = restClient.postForObject(env.getProduction() + "draftPackagingDetails", draftObj,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if ((resp.getCode() == null || resp.getCode() == "")
				&& (resp.getMessage() == null || resp.getMessage() == "")) {
			resp.setMessage("Success");
		} else {
			resp.setMessage("failed");
		}

		logger.info("Method : draftPackagingPlanning function Ends");
		return resp;
	}

	/**
	 * Web controller for generate Ticket
	 *
	 *
	 */
	@PostMapping("/view-productions-packaging-generate-token")
	public @ResponseBody JsonResponse<Object> geenatePackageToken(
			@RequestBody ProductionGocoolPacakgeingModel generateTaken, HttpSession session) {

		logger.info("Method : geenatePackageToken starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		session.setAttribute("generateTaken", generateTaken);
		logger.info("Method : geenatePackageToken function Ends");
		resp.setMessage("success");
		return resp;

	}

	/*
	 * GetMapping for Add language view page
	 */
	@GetMapping("/view-productions-packaging-generate-token-form")
	public void generateToken(@RequestParam String planId, @RequestParam String batchId,
			@RequestParam String prodItemName, @RequestParam String packagingQty, @RequestParam String packagingWt,
			@RequestParam String bcName, HttpServletResponse response) {
		logger.info("Method : generateToken starts");

		Map<String, Object> data = new HashMap<String, Object>();

		ProductionGocoolPacakgeingModel packageDetails = new ProductionGocoolPacakgeingModel();
		packageDetails.setPlanId(planId);
		packageDetails.setBatchId(batchId);
		packageDetails.setProdItemName(prodItemName);
		packageDetails.setPackagingQty(Double.parseDouble(packagingQty));
		packageDetails.setPackagingWt(Double.parseDouble(packagingWt));

		data.put("sessionpackageDetails", packageDetails);
		String variable = env.getBaseUrlPath();
		data.put("barcodeImg", variable + "document/challan/" + bcName + "");

		/*
		 * if (sessionpackageDetails != null) {
		 * 
		 * 
		 * session.setAttribute("sessionpackageDetails", null); } else {
		 * data.put("sessionpackageDetails", packageDetails); }
		 */

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=GnerateToken.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("production/generate-token-page", data);
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

		Path path = Paths.get(env.getChallan() + imageName);

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

	/*
	 * Get Mapping view pipe production page
	 */
	@GetMapping("/view-productions-packaging-details")
	public String viewPipePackaingDetails(Model model, HttpSession session) {

		logger.info("Method : viewPipePackaingDetails   starts");

		// Drop Down Grade
		try {
			DropDownModel[] MCoilGrade = restClient.getForObject(env.getProduction() + "getMotherCoilGrade",
					DropDownModel[].class);
			List<DropDownModel> GradeList = Arrays.asList(MCoilGrade);

			model.addAttribute("gradeList", GradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Drop Down mother coil batch
		try {
			DropDownModel[] MCoilBatchList = restClient.getForObject(env.getProduction() + "getMotherCoilBatchEdit",
					DropDownModel[].class);
			List<DropDownModel> MCoilBatchLists = Arrays.asList(MCoilBatchList);

			model.addAttribute("MCoilBatchLists", MCoilBatchLists);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewPipePackaingDetails   ends");

		return "production/pipe-packaging-details";
	}

	/*
	 * For view pipe production page throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-productions-packaging-details-ajax")
	public @ResponseBody DataTableResponse viewPipePackagingDetailscThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewPipePackagingDetailscThroughAjax statrs");

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

			JsonResponse<List<ProductionPipePackagingModel>> jsonResponse = new JsonResponse<List<ProductionPipePackagingModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "getPackagingDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipePackagingModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipePackagingModel>>() {
					});

			String s = "";

			for (ProductionPipePackagingModel m : motherCoilSlit) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.gettMotherCoilBatch().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.gettMotherCoilThickness().getBytes());
				byte[] encodeId3 = Base64.getEncoder().encode(m.gettPipeSlitBatch().getBytes());
				byte[] encodeId4 = Base64.getEncoder().encode(m.gettPipeSlitWidth().getBytes());
				byte[] encodeId5 = Base64.getEncoder().encode(m.gettPipeSize().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3) + ','
						+ new String(encodeId4) + ',' + new String(encodeId5)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  ";

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(motherCoilSlit);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPipePackagingDetailscThroughAjax ends");

		return response;
	}

	@GetMapping("/view-productions-packaging-generate-token")
	public @ResponseBody JsonResponse<ProductionPipePackagingModel> generateToken() {

		logger.info("Method : generateToken starts");

		JsonResponse<ProductionPipePackagingModel> resp = new JsonResponse<ProductionPipePackagingModel>();
		ProductionPipePackagingModel packageModel = new ProductionPipePackagingModel();
		String barcodeImageName = "";
		String barcodeImageNo = generate12Random();
		long nowTime = new Date().getTime();
		barcodeImageName = nowTime + ".png";
		saveBarcode(barcodeImageName, barcodeImageNo);
		packageModel.setBarcodeImageName(barcodeImageName);
		packageModel.setBarcodeImageNumber(barcodeImageNo);
		resp.setMessage("success");
		resp.setBody(packageModel);

		logger.info("Method : generateToken function Ends");

		return resp;

	}

}
