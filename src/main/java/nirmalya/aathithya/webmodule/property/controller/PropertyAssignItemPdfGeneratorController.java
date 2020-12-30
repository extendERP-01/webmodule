package nirmalya.aathithya.webmodule.property.controller;

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
import nirmalya.aathithya.webmodule.property.model.PropertyAssignConsumedItemModel;

@Controller
@RequestMapping(value = "download/")
public class PropertyAssignItemPdfGeneratorController 
{

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	Logger logger = LoggerFactory.getLogger(PropertyAssignedConsumedItemController.class);
	@SuppressWarnings("unchecked")
	@GetMapping(value = {"/assigned-consumed-item-pdf"})
	public void generatePropertyAssignedConsumedItemPdf(HttpServletResponse response,@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4)
	{
	
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4=  (new String(encodeByte4));
		JsonResponse<List<PropertyAssignConsumedItemModel>> jsonResponse = new JsonResponse<List<PropertyAssignConsumedItemModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
//			
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			
			jsonResponse = restClient.postForObject(env.getPropertyUrl()+"get-all-assign-consumeditem-pdf",tableRequest, JsonResponse.class);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<PropertyAssignConsumedItemModel> propertyAssignConsumedItemModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<PropertyAssignConsumedItemModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		System.out.println(curDate);
		String currdate="";
		String dateFrom="";
		String dateTo="";
		//System.out.println("bookTableModel: "+laundryItemForLaundryServiceModel);
		if(propertyAssignConsumedItemModel.size()!=0) {


			propertyAssignConsumedItemModel.get(0).setCurDate(curDate);
		currdate = propertyAssignConsumedItemModel.get(0).getCurDate();
		System.out.println(currdate);

		propertyAssignConsumedItemModel.get(0).setDateFrom(param3);
		dateFrom = propertyAssignConsumedItemModel.get(0).getDateFrom();

		propertyAssignConsumedItemModel.get(0).setDateTo(param4);
		dateTo = propertyAssignConsumedItemModel.get(0).getDateTo();

		data.put("currdate",currdate);
		data.put("dateFrom",dateFrom);
		data.put("dateTo",dateTo);
		//data.put("bookTableModels", bookTableModel);
		}
		else {

		data.put("currdate",curDate);
		data.put("dateFrom",param3);
		data.put("dateTo",param4);
		//data.put("items", "");
		}
		for(PropertyAssignConsumedItemModel m : propertyAssignConsumedItemModel) {
			
		}
		data.put("items", propertyAssignConsumedItemModel);
		data.put("a", propertyAssignConsumedItemModel);
		
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=PropertyAssignConsumedItem.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("property/Assigneditems_pdf", data);
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
	@SuppressWarnings("unchecked")
	@GetMapping(value = {"/view-assign-consumed-download-report"})
	public void generatePropertyAssignedConsumedItemReport(HttpServletResponse response,@RequestParam("param1") String encodedParam1,@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4)
	{
	
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4=  (new String(encodeByte4));
		JsonResponse<List<PropertyAssignConsumedItemModel>> jsonResponse = new JsonResponse<List<PropertyAssignConsumedItemModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
//			
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			
			jsonResponse = restClient.postForObject(env.getPropertyUrl()+"get-all-assign-consumed-report",tableRequest, JsonResponse.class);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<PropertyAssignConsumedItemModel> propertyAssignConsumedItemModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<PropertyAssignConsumedItemModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		System.out.println(curDate);
		String currdate="";
		String dateFrom="";
		String dateTo="";
		//System.out.println("bookTableModel: "+laundryItemForLaundryServiceModel);
		if(propertyAssignConsumedItemModel.size()!=0) {


			propertyAssignConsumedItemModel.get(0).setCurDate(curDate);
		currdate = propertyAssignConsumedItemModel.get(0).getCurDate();
		System.out.println(currdate);

		propertyAssignConsumedItemModel.get(0).setDateFrom(param3);
		dateFrom = propertyAssignConsumedItemModel.get(0).getDateFrom();

		propertyAssignConsumedItemModel.get(0).setDateTo(param4);
		dateTo = propertyAssignConsumedItemModel.get(0).getDateTo();

		data.put("currdate",currdate);
		data.put("dateFrom",dateFrom);
		data.put("dateTo",dateTo);
		//data.put("bookTableModels", bookTableModel);
		}
		else {

		data.put("currdate",curDate);
		data.put("dateFrom",param3);
		data.put("dateTo",param4);
		//data.put("items", "");
		}
		for(PropertyAssignConsumedItemModel m : propertyAssignConsumedItemModel) {
			
		}
		data.put("items", propertyAssignConsumedItemModel);
		data.put("a", propertyAssignConsumedItemModel);
		
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=PropertyAssignConsumedItem.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("property/Assigneditems_pdf", data);
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
