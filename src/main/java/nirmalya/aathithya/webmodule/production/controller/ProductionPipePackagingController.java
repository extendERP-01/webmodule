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
import nirmalya.aathithya.webmodule.production.model.MotherCoilSlitModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipePackagingModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeProductionModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPipePackagingController {
	Logger logger = LoggerFactory.getLogger(ProductionMotherCoilSlitController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * Get Mapping view pipe packaging page
	 */
	@GetMapping("/view-pipe-packaging")
	public String viewPipepackaging(Model model, HttpSession session) {

		logger.info("Method : viewPipepackaging   starts");

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

		logger.info("Method : viewPipepackaging   ends");

		return "production/view-pipe-packaging";
	}

	/*
	 * For view packaging throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-pipe-packaging-throughajax")
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

			JsonResponse<List<ProductionPipeProductionModel>> jsonResponse = new JsonResponse<List<ProductionPipeProductionModel>>();

			jsonResponse = restClient.postForObject(env.getProduction() + "viewMotherCoilForPackaging", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionPipeProductionModel> motherCoilSlit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionPipeProductionModel>>() {
					});

			String s = "";

			for (ProductionPipeProductionModel m : motherCoilSlit) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.gettMotherCoilBatch().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.gettMotherCoilThickness().getBytes());
				byte[] encodeId3 = Base64.getEncoder().encode(m.gettPipeSlitBatch().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>  ";
				s = s + "<a href='view-pipe-packaging-edit?batchid=" + new String(encodeId) + "&mthick="
						+ new String(encodeId2) + "&slitbatch=" + new String(encodeId3)
						+ "' ><button class=\"btn btn-info\">Add packaging Dtls" + "</button></a>";

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
	@PostMapping("/view-pipe-packaging-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalPipePAckaging(Model model,
			@RequestBody ProductionPipePackagingModel index, BindingResult result) {

		logger.info("Method :modalPipePAckaging starts");

		byte[] batchId = Base64.getDecoder().decode(index.gettMotherCoilBatch().getBytes());
		byte[] thickId = Base64.getDecoder().decode(index.gettMotherCoilThickness().getBytes());
		byte[] slitbatchId = Base64.getDecoder().decode(index.gettPipeSlitBatch().getBytes());

		String mBatchId = (new String(batchId));
		String mThickId = (new String(thickId));
		String slitBatchId = (new String(slitbatchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getProduction() + "viewMotherCoilById?mBatchId=" + mBatchId
					+ "&mThickId=" + mThickId + "&slitBatchId=" + slitBatchId, JsonResponse.class);

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
	@GetMapping("view-pipe-packaging-edit")
	public String editMotherCoil(Model model, @RequestParam("batchid") String mBatchId1,
			@RequestParam("mthick") String mThickId1, @RequestParam("slitbatch") String slitBatchId1,
			HttpSession session) {

		logger.info("Method : editMotherCoilSlit starts");
		byte[] encodeByte = Base64.getDecoder().decode(mBatchId1.getBytes());
		byte[] encodeByte1 = Base64.getDecoder().decode(mThickId1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(slitBatchId1.getBytes());

		String mBatchId = (new String(encodeByte));
		String mThickId = (new String(encodeByte1));
		String slitBatchId = (new String(encodeByte2));

		try {

			ProductionPipePackagingModel[] motherCoil = restClient
					.getForObject(env.getProduction() + "edit-packaging-ById?mBatchId=" + mBatchId + "&mThickId="
							+ mThickId + "&slitBatchId=" + slitBatchId, ProductionPipePackagingModel[].class);
			List<ProductionPipePackagingModel> mCoilList = Arrays.asList(motherCoil);
			if (!mCoilList.isEmpty()) {
				model.addAttribute("edit", "for Edit");
				model.addAttribute("mCoilList", mCoilList);
			}

			if (!mCoilList.isEmpty()) {
				// Drop Down Grade
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getthicknessEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade(),
							DropDownModel[].class);
					List<DropDownModel> thicknessList = Arrays.asList(MCoilGrade);

					model.addAttribute("thicknessList", thicknessList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!mCoilList.isEmpty()) {
				// Drop Down Grade
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getSlitWidthEdit?grade=" + mCoilList.get(0).gettMotherCoilGrade()
									+ "&thickness=" + mCoilList.get(0).gettMotherCoilThickness(),
							DropDownModel[].class);
					List<DropDownModel> slitWidthList = Arrays.asList(MCoilGrade);

					model.addAttribute("slitWidthList", slitWidthList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (!mCoilList.isEmpty()) {
				try {
					DropDownModel[] MCoilGrade = restClient.getForObject(
							env.getProduction() + "getPipeSizeit?grade=" + mCoilList.get(0).gettMotherCoilGrade()
									+ "&thickness=" + mCoilList.get(0).gettMotherCoilThickness(),
							DropDownModel[].class);
					List<DropDownModel> pipeSizeList = Arrays.asList(MCoilGrade);
					model.addAttribute("pipeSizeList", pipeSizeList);
				} catch (RestClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

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

		logger.info("Method : editMotherCoilSlit ends");
		return "production/add-pipe-packaging";

	}

	/**
	 * Web controller add packaging data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-pipe-packaging-add-ajax")
	public @ResponseBody JsonResponse<Object> addpackagingDetails(
			@RequestBody List<ProductionPipePackagingModel> packaging, HttpSession session) {

		logger.info("Method : addpackagingDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (ProductionPipePackagingModel r : packaging) {
				r.settPipeCreatedBy(userId);
				/*
				 * if (r.getBarcodeImageName() == null && r.getBarcodeImageName() == null) {
				 * String barcodeImageName = ""; String barcodeImageNo = generate12Random();
				 * long nowTime = new Date().getTime(); barcodeImageName = nowTime + ".png";
				 * r.setBarcodeImageName(barcodeImageName);
				 * r.setBarcodeImageNumber(barcodeImageNo); saveBarcode(barcodeImageName,
				 * barcodeImageNo); }
				 */

			}

			resp = restClient.postForObject(env.getProduction() + "addpackagingDetails", packaging, JsonResponse.class);

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
	@PostMapping("/view-pipe-packaging-add-form")
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
	@GetMapping("/view-pipe-packaging-add-form-page")
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
	@PostMapping("/view-pipe-packaging-details-modalView")
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
	@PostMapping("/view-pipe-packaging-draft-ajax")
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
	@PostMapping("/view-pipe-packaging-generate-token")
	public @ResponseBody JsonResponse<Object> geenatePackageToken(
			@RequestBody ProductionPipePackagingModel generateTaken, HttpSession session) {

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
	@GetMapping("/view-pipe-packaging-generate-token-form")
	public void generateToken(@RequestParam String batch, @RequestParam String thickness,
			@RequestParam String slitSubGroup, @RequestParam String pipeSize, @RequestParam String qty,
			@RequestParam String wt, @RequestParam String bcName, HttpServletResponse response) {
		logger.info("Method : generateToken starts");

		Map<String, Object> data = new HashMap<String, Object>();

		ProductionPipePackagingModel packageDetails = new ProductionPipePackagingModel();
		packageDetails.settMotherCoilBatch(batch);
		packageDetails.settMotherCoilThickness(thickness);
		packageDetails.settPipeSize(pipeSize);
		packageDetails.setPackagingQty(Double.parseDouble(qty));
		packageDetails.setPackagingWt(Double.parseDouble(wt));
		packageDetails.setSlitSubGroup(slitSubGroup);

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
	@GetMapping("/view-pipe-packaging-details")
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
	@GetMapping("/view-pipe-packaging-details-ajax")
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
						+ new String(encodeId) + ',' + new String(encodeId2) + ',' + new String(encodeId3)+ ',' + new String(encodeId4)
						+ ',' + new String(encodeId5)
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

	@GetMapping("/view-pipe-packaging-generate-token")
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
