/**
 * 
 */
package nirmalya.aathithya.webmodule.sales.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.sales.model.SalesCustomerModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "download/")
public class CustomerPDFGeneratorController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	Logger logger = LoggerFactory.getLogger(CustomerPDFGeneratorController.class);
	@SuppressWarnings("unchecked")
	@GetMapping("sales-customer-pdf")
		public void generateSalesCustomerPdf(HttpServletResponse response,Model model,HttpSession session){		
		JsonResponse<List<SalesCustomerModel>> jsonResponse = new JsonResponse<List<SalesCustomerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			
			jsonResponse = restTemplate.postForObject(environmentVaribles.getSalesUrl()+"get-all-CustomersPdf",tableRequest, JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<SalesCustomerModel> salesCustomerModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<SalesCustomerModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		String printBy="";
		String curDate="";
		try {
			printBy = (String)session.getAttribute("USER_NAME");
		}catch(Exception e) {
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		data.put("printDate",curDate);
		for(SalesCustomerModel m : salesCustomerModel) {
			
			if (m.getCustomerActive()) {
				s="Active";
			} else {
				s="Inactive";
			}
			m.setStatus(s);
		}
		
		
		data.put("printedBy", printBy);
	    data.put("status", s);
	    data.put("watermark_image", environmentVaribles.getBaseURL()+"assets/images/invoicebg.png");
	    data.put("courtyard_logo", environmentVaribles.getBaseURL()+"assets/images/grandcourtyard_logo.png");
		data.put("customerId", salesCustomerModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=SalesCustomer.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("sales/customer", data);
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