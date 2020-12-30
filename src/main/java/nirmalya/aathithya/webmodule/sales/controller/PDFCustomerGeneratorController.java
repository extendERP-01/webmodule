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
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import nirmalya.aathithya.webmodule.sales.model.SalesCustomerModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "download/")
public class PDFCustomerGeneratorController {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles environmentVaribles;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	Logger logger = LoggerFactory.getLogger(CustomerPDFGeneratorController.class);
	@SuppressWarnings("unchecked")
	@GetMapping("sales-customer-generate-pdf")
		public void generateSalesCustomerPdf(HttpServletResponse response,Model model,@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3, @RequestParam("param4") String encodedParam4 , @RequestParam("param5") String encodedParam5  ){
			byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
			byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
			byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
			byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
			byte[] encodeByte5 = Base64.getDecoder().decode(encodedParam5.getBytes());
			
			String param1 = (new String(encodeByte1));
			String param2 = (new String(encodeByte2));
			String param3 = (new String(encodeByte3));
			String param4 = (new String(encodeByte4));
			String param5 = (new String(encodeByte5));
		
		JsonResponse<List<SalesCustomerModel>> jsonResponse = new JsonResponse<List<SalesCustomerModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			jsonResponse = restTemplate.postForObject(environmentVaribles.getSalesUrl()+"get-CustomersPdf",tableRequest, JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<SalesCustomerModel> salesCustomerModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<SalesCustomerModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		String printBy="abcde";
		String curDate="";
		String printDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		salesCustomerModel.get(0).setCurrentDate(curDate);;
		printDate = salesCustomerModel.get(0).getCurrentDate();
		data.put("printDate",printDate);
		for(SalesCustomerModel m : salesCustomerModel) {
			m.setPrintedBy(printBy);
			if (m.getCustomerActive()) {
				s="Active";
			} else {
				s="Inactive";
			}
			m.setStatus(s);
		}
		
		String printedBy = salesCustomerModel.get(0).getPrintedBy();
		data.put("printedBy", printedBy);
	    data.put("status", s);
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