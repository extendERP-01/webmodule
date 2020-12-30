package nirmalya.aathithya.webmodule.gatepass.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassEntryModel;
import nirmalya.aathithya.webmodule.gatepass.model.GatePassOutModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "gatepass")
public class PdfGatePassController {

	Logger logger = LoggerFactory.getLogger(PdfGatePassController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-entry-report-download" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3) {

		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		
		JsonResponse<List<GatePassEntryModel>> jsonResponse = new JsonResponse<List<GatePassEntryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getEntryRprtPdf", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassEntryModel> gatePass = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassEntryModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
		for(GatePassEntryModel m : gatePass) {
			if(m.getWeightType()==1) {
				m.setAction("Container Wise");
			} else if(m.getWeightType()==2) {
				m.setAction("Unit Wise");
			} else {
				m.setAction("N/A");
			}
			
			if(m.getpOrder()==null || m.getpOrder()=="") {
				m.setpOrder("N/A");
			}
			
			if(m.getCustomer()==null || m.getCustomer()=="") {
				m.setCustomer("N/A");
			}
			
			if(m.getVendor()==null || m.getVendor()=="") {
				m.setVendor("N/A");
			}
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		/**List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Maintenance?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+""); */
	    data.put("fromDate", param2);
		data.put("toDate", param3);
		data.put("gatePass", gatePass);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=GatePassEntryReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("gatepass/entryReportPDF", data);
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
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-entry-summary-report-download" })
	public void generateGatePassEntrySummaryPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2,
			 @RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4,
			 @RequestParam("param5") String encodedParam5,@RequestParam("param6") String encodedParam6,
			 @RequestParam("param7") String encodedParam7, @RequestParam("param8") String encodedParam8, HttpSession ss)
			 {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		byte[] encodeByte7 = Base64.getDecoder().decode(encodedParam7.getBytes());
		byte[] encodeByte8 = Base64.getDecoder().decode(encodedParam8.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String param6 = (new String(encodeByte6));
		String param7 = (new String(encodeByte7));
		String param8 = (new String(encodeByte8));
		
		
		
		
		JsonResponse<List<GatePassEntryModel>> jsonResponse = new JsonResponse<List<GatePassEntryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam1(param1);		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);
		tableRequest.setParam7(param7);
		tableRequest.setParam8(param8);
		
		
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getPassEntrySummaryReportForPreviewPDF", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
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
		/**List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAssetUrl() + "restLogoImage-Maintenance?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String variable = env.getBaseUrlPath();
		
		String logo = logoList.get(0).getName();
	    data.put("logoImage",variable+"document/hotel/"+logo+""); */
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		String printedBy = (String) ss.getAttribute("USER_NAME");
		if (gatePass.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("gatePass", "");
		}

	   
		data.put("gatePass", gatePass);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=GatePassEntrySummaryReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("gatepass/entrySummaryReportPDF", data);
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
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "gatepass-out-summary-report-pdf-download" })
	public void generateGatePassOutSummaryPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2,
			 @RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4,
			 @RequestParam("param5") String encodedParam5,@RequestParam("param6") String encodedParam6,
			 @RequestParam("param7") String encodedParam7, @RequestParam("param8") String encodedParam8, HttpSession session)
			 {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
		byte[] encodeByte6 = Base64.getDecoder().decode(encodedParam6.getBytes());
		byte[] encodeByte7 = Base64.getDecoder().decode(encodedParam7.getBytes());
		byte[] encodeByte8 = Base64.getDecoder().decode(encodedParam8.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String param5 = (new String(encodeByte5));
		String param6 = (new String(encodeByte6));
		String param7 = (new String(encodeByte7));
		String param8 = (new String(encodeByte8));
		
		String printedBy = (String) session.getAttribute("USER_NAME");
		
		
		JsonResponse<List<GatePassOutModel>> jsonResponse = new JsonResponse<List<GatePassOutModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam1(param1);		
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);
		tableRequest.setParam7(param7);
		tableRequest.setParam8(param8);
		
		
		try {
			jsonResponse = restClient.postForObject(env.getGatepassUrl() + "getPassOutSummaryReportForPreviewPDF", tableRequest,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<GatePassOutModel> gatePassOut = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<GatePassOutModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		
	
		
	
		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);

		//String printedBy = "SRADHA";
		if (gatePassOut.size() != 0) {

			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
		} else {
			data.put("printedBy", printedBy);
			data.put("currdate", curDate);
			data.put("dateFrom", param1);
			data.put("dateTo", param2);
			data.put("gatePassOut", "");
		}

	   
		data.put("gatePassOut", gatePassOut);

		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=GatePassEntrySummaryReport.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("gatepass/outSummaryReportPDF", data);
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
