package nirmalya.aathithya.webmodule.gatepass.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import nirmalya.aathithya.webmodule.gatepass.model.GatePassEntryModel;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassItemModel;
import nirmalya.aathithya.webmodule.sales.model.QuotationMasterModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "gatepass")
public class GatePassEntryController {

	Logger logger = LoggerFactory.getLogger(GatePassEntryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	private static final String USER_Id = "USER_ID";

	/**
	 * View Default 'Gate-Pass Entry' page
	 *
	 */
	@GetMapping("/gatepass-entry")
	public String gatePassEntry(Model model, HttpSession session) {
		logger.info("Method : gatePassEntry add starts");

		try {
			String userId = (String) session.getAttribute(USER_Id);
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl() + "getStoreForGatePassEntry?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : gatePassEntry ends");
		return "gatepass/gatepass-entry";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-get-purchase-order" })
	public @ResponseBody JsonResponse<DropDownModel> getPurchaseOrderAutoSearchList( 
			@RequestParam String value, @RequestParam String store) {
		logger.info("Method : getPurchaseOrderAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getPOAutoSearchForGatePassEntry?id=" + value +"&store="+store,
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

		logger.info("Method : getPurchaseOrderAutoSearchList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-get-towhom-given" })
	public @ResponseBody JsonResponse<DropDownModel> getCustomerAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getCustomerAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getCustomerForGatePassEntry?id=" + searchValue,
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

		logger.info("Method : getCustomerAutoSearchList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getItemAutoSearchList(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getItemAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getGatepassUrl() + "getItemForGatePassEntry", searchValue,
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

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-get-servetype" })
	public @ResponseBody JsonResponse<GatePassItemModel> getServeType(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getServeType starts");

		JsonResponse<GatePassItemModel> res = new JsonResponse<GatePassItemModel>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getServeTypeForGatePassEntry?id=" + id,
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

		logger.info("Method : getServeType ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/gatepass-entry-get-item-dtls-by-po" })
	public @ResponseBody JsonResponse<GatePassItemModel> getItemDetailsByPOrder(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getItemDetailsByPOrder starts");

		JsonResponse<GatePassItemModel> res = new JsonResponse<GatePassItemModel>();
		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getItemDetailsByPOrder?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getItemDetailsByPOrder ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "gatepass-entry-save" )
	public @ResponseBody JsonResponse<Object> newGatePassEntrySave(@RequestBody List<GatePassEntryModel> gatePass,
			Model model, HttpSession session) {
		logger.info("Method : newGatePassEntrySave function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < gatePass.size(); i++) {
				gatePass.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getGatepassUrl() + "newGatePassEntrySave", gatePass, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : newGatePassEntrySave function Ends");
		return res;
	}

	/**
	 * Default 'View Gate Pass' page
	 *
	 */
	@GetMapping("/view-gatepass-entry")
	public String viewGatePassEntry(Model model, HttpSession session) {
		logger.info("Method : viewGatePassEntry starts");

		try {
			String userId = (String) session.getAttribute(USER_Id);
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl() + "getStoreForGatePassEntry?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		logger.info("Method : viewGatePassEntry ends");
		return "gatepass/view-gatepass-entry";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-gatepass-entry-through-ajax")
	public @ResponseBody DataTableResponse viewGatePassThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5 ,@RequestParam String param6) {
		logger.info("Method : viewGatePassThroughAjax starts");

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
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);

			JsonResponse<List<GatePassEntryModel>> jsonResponse ;

			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getGatePassEntryDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<GatePassEntryModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<GatePassEntryModel>>() {
					});

			String s = "";

			for (GatePassEntryModel m : gatePass) {
				byte[] pId = Base64.getEncoder().encode(m.getGatePass().getBytes());
				if (m.getPassType() == 1) {
					m.setPassTypeName("Gate In");
				} else if (m.getPassType() == 2) {
					m.setPassTypeName("Gate Out");
				}
				if (m.getExitTime().contentEquals("") || m.getExitTime() == null) {
					m.setExitTime("- -");
				}

				m.setGatePass("<a href='javascript:void' onclick='pdfGatePass(\"" + new String(pId) + "\")'>"
						+ m.getGatePass() + "</a>");

				if (Boolean.TRUE.equals(m.getGrnStatus())) {
					s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					m.setAction(s);
					s = "";
				} else {
					s = s + "<a href='edit-gatepass-entry?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
							+ " <a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + new String(pId)
							+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					m.setAction(s);
					s = "";
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(gatePass);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewGatePassThroughAjax ends");
		return response;
	}

	@GetMapping("edit-gatepass-entry")
	public String editGatePassEntry(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editGatePassEntry starts");

		try {
			String userId = (String) session.getAttribute(USER_Id);
			DropDownModel[] dd = restClient.getForObject(env.getGatepassUrl() + "getStoreForGatePassEntry?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {

			GatePassEntryModel[] gatePass = restClient.getForObject(env.getGatepassUrl() + "editGatePassEntry?id=" + id,
					GatePassEntryModel[].class);
			List<GatePassEntryModel> gatePassList = Arrays.asList(gatePass);

			try {
				GatePassItemModel[] item = restClient.getForObject(
						env.getGatepassUrl() + "editGatePassEntryPODtls?id=" + gatePassList.get(0).getpOrder(),
						GatePassItemModel[].class);
				List<GatePassItemModel> itemList = Arrays.asList(item);

				model.addAttribute("itemList", itemList);
			} catch (RestClientException e) {

				e.printStackTrace();
			}
			System.out.println(gatePassList);
			model.addAttribute("gatePassId", gatePassList.get(0).getGatePass());
			model.addAttribute("gatePass", gatePassList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGatePassEntry starts");
		return "gatepass/gatePass-entry";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-gatepass-entry-modal" })
	public @ResponseBody JsonResponse<Object> modalGatePassEntry(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGatePassEntry starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getGatepassUrl() + "getGatePassEntryById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalGatePassEntry ends");
		return res;
	}

	/**
	 * 
	 * PDF FOR GATE PASS
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-gatepass-entry-pdf")
	public void generateGatePassEntryPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<List<GatePassEntryModel>> jsonResponse = new JsonResponse<List<GatePassEntryModel>>();
		try {
			jsonResponse = restClient.getForObject(env.getGatepassUrl() + "getGatePassEntryById?id=" + id,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassEntryModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassEntryModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
	 
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(
					env.getGatepassUrl() + "restLogoImage-GatePass?logoType=" + "header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		/**
		 * get Hotel List
		 *
		 */
		try {
			QuotationMasterModel[] hotel = restClient.getForObject(
					env.getGatepassUrl() + "restGetStoreDetails?id=" + gatePass.get(0).getStore(),
					QuotationMasterModel[].class);
			List<QuotationMasterModel> hotelList = Arrays.asList(hotel);
			data.put("hotelList", hotelList);
			 
System.out.println("hotelList " + hotelList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		data.put("currdate", curDate);
		String logo = logoList.get(0).getName();

		data.put("logoImage", env.getBaseUrlPath() + "document/hotel/" + logo + "");
		data.put("gatePass", gatePass);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=Gatepass.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("gatepass/gatepass-entry-pdf", data);
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
}
