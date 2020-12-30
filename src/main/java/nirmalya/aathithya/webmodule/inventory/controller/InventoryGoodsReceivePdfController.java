/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveNoteModel;

/**
 * @author USER
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryGoodsReceivePdfController 
{

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	Logger logger = LoggerFactory.getLogger(InventoryGoodsReceivePdfController.class);
	
	@SuppressWarnings("unchecked")
	@GetMapping("download-goodsreceive-note")
	public void generateRestaurantMenuItemsPdf(HttpServletResponse response,Model model,@RequestParam("param1") String encodedParam1 , @RequestParam("param2") String encodedParam2, @RequestParam("param3") String encodedParam3,
			@RequestParam("param4") String encodedParam4)
	{
			byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
			byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
			byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
			byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
			String param1 = (new String(encodeByte1));
			String param2 = (new String(encodeByte2));
			String param3 = (new String(encodeByte3));
			String param4 = (new String(encodeByte4));
			
			Double gtotal =0.0;
			Double ggtotal=0.0;
		
		JsonResponse<List<InventoryGoodsReceiveNoteModel>> jsonResponse = new JsonResponse<List<InventoryGoodsReceiveNoteModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			
			jsonResponse = restClient.postForObject(env.getInventoryUrl()+"get-all-goodsreceive-pdf",tableRequest, JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(env.getInventoryUrl() + "restLogoImage-return?logoType="+"background-Logo", DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);
			model.addAttribute("logoBgList", logoBgList);
			
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/**
		 * get Hotel Logo
		 *
		 */
		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logo = restClient.getForObject(env.getInventoryUrl() + "restLogoImage-return?logoType="+"header-Logo", DropDownModel[].class);
			logoList = Arrays.asList(logo);
			model.addAttribute("logoList", logoList);
		
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<InventoryGoodsReceiveNoteModel>inventoryGoodsReceiveNoteModel = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<InventoryGoodsReceiveNoteModel>>() { });
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		for(InventoryGoodsReceiveNoteModel m : inventoryGoodsReceiveNoteModel)
		{
		
			Float total =m.getgRnInvoiceQuantity() * m.getgRnInvoicePrice();
			m.setTotal(total);
			gtotal=gtotal+total;
			ggtotal=gtotal;
			
		}
		
		inventoryGoodsReceiveNoteModel.get(0).setgTotal(ggtotal);
		
		data.put("items", inventoryGoodsReceiveNoteModel);
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InventoryGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("inventory/InventoryGoodsReceiveNote_pdf", data);
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
	@GetMapping("view-inventory-goods-pdf")
	public void generateOneInventoryGoodsReceivePdf(HttpServletResponse response, Model model,@RequestParam("id") String encodeId) 
	{
		
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<InventoryGoodsReceiveNoteModel> jsonResponse = new JsonResponse<InventoryGoodsReceiveNoteModel>();
		try {

			jsonResponse = restClient.getForObject(env.getInventoryUrl() + "get-goodsreceive-modal?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();

		InventoryGoodsReceiveNoteModel inventoryGoodsReceiveNoteModel = mapper.convertValue(jsonResponse.getBody(),new TypeReference<InventoryGoodsReceiveNoteModel>() {});
		
		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";
		
		Float total =inventoryGoodsReceiveNoteModel.getgRnInvoiceQuantity() * inventoryGoodsReceiveNoteModel.getgRnInvoicePrice();
		inventoryGoodsReceiveNoteModel.setTotal(total);
		System.out.println(inventoryGoodsReceiveNoteModel);
		data.put("OneGoodsReceive", inventoryGoodsReceiveNoteModel);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=InvGoodsReceiveNote.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("inventory/goodsreceive_pdf", data);
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
