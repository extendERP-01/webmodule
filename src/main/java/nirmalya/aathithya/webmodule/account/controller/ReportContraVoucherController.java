package nirmalya.aathithya.webmodule.account.controller;

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

import nirmalya.aathithya.webmodule.account.model.ContraVoucherModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "account")
public class ReportContraVoucherController {

	Logger logger = LoggerFactory.getLogger(ReportContraVoucherController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/**
	 * 
	 * PDF FOR CONTRA VOUCHER FOR REPORT
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-contra-voucher-master-download-report")
	public void generateContraVoucherReportPdf(HttpServletResponse response,Model model,@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3, HttpSession session) {
			byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
			byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
			byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
			
			String param1 = (new String(encodeByte1));
			String param2 = (new String(encodeByte2));
			String param3 = (new String(encodeByte3));
			
		JsonResponse<List<ContraVoucherModel>> jsonResponse = new JsonResponse<List<ContraVoucherModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			
			jsonResponse = restClient.postForObject(env.getAccountUrl()+"getContraVoucherReport",tableRequest, JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();		
		
		List<ContraVoucherModel> contraVoucher = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<ContraVoucherModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		//String s = "";
		
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(env.getAccountUrl() + "restLogoImage-ContraVoucher?logoType="+"background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			data.put("logoBgList", logoBgList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getAccountUrl() + "restLogoImage-ContraVoucher?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
			data.put("logoList", logoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		String currdate="";
		String dateFrom="";
		String dateTo="";
		
		String userName = "";
		
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(contraVoucher.size()!=0) {			
			
//			contraVoucher.get(0).setPrintedBy("SAGAR");
//			printedBy = contraVoucher.get(0).getPrintedBy();
			 
			contraVoucher.get(0).setCurDate(curDate);
			currdate = contraVoucher.get(0).getCurDate();
			
			contraVoucher.get(0).setDateFrom(param1);
			dateFrom = contraVoucher.get(0).getDateFrom();
			
			contraVoucher.get(0).setDateTo(param2);
			dateTo = contraVoucher.get(0).getDateTo();
			
			data.put("printedBy",userName);
			data.put("currdate",currdate);
			data.put("dateFrom",dateFrom);
			data.put("dateTo",dateTo);
		}
		else {
			data.put("printedBy",userName);
			data.put("currdate",curDate);
			data.put("dateFrom",param1);
			data.put("dateTo",param2);
			data.put("contraVoucher", "");
		}
		
		/**for(ContraVoucherModel m : contraVoucher) {
			
		}*/
		
		String variable = env.getBaseURL();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image",variable+"document/hotel/"+background+"");
	    data.put("logoImage",variable+"document/hotel/"+logo+"");
		data.put("contraVoucher", contraVoucher);
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=ContraVoucherList.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("account/pdf-contra-voucher-list", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
