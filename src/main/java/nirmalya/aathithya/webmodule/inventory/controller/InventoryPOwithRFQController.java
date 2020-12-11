package nirmalya.aathithya.webmodule.inventory.controller;
 
import java.util.Base64;

import javax.servlet.http.HttpSession;
 
import java.util.Arrays;  
import java.util.List; 
 

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
import org.springframework.web.multipart.MultipartFile;
 
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryPORFQVendorDetailModel; 

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class InventoryPOwithRFQController {

	Logger logger = LoggerFactory.getLogger(InventoryPOwithRFQController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
     

	
	/**
	 * Web Controller - Upload Purchase Order Picture
	 *
	 */
	@PostMapping("/add-purchase-order-with-rfq-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("quotationPFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	
	/*
	 *
	 * Add PO With RFQ
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/add-purchase-order-with-rfq")
	public String addPurchaseOrderRfQ(Model model, @RequestParam("id") String index, HttpSession session) {
	
		logger.info("Method : addPurchaseOrderRfQ starts");
	 
		
		 
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());   
		String id = (new String(encodeByte));
	//	System.out.println("RFQIQ------------"+id);

		try {

			InventoryPORFQVendorDetailModel[] quotation = restClient.getForObject(env.getInventoryUrl() + "getPORFQById?id=" + id,
					InventoryPORFQVendorDetailModel[].class);
			List<InventoryPORFQVendorDetailModel> quotationList = Arrays.asList(quotation);
		//	System.out.println("rfqQuotation------------"+quotation);
			model.addAttribute("quotationId", quotationList.get(0).getVenQuotId());
			model.addAttribute("taxType", quotationList.get(0).getTaxType());
			
			//System.out.println("quotation data for edit time------------------------------"+quotationList);
			String variable = env.getBaseUrlPath(); 
			String logo = "logo_grandcourtyard.png";
			//model.addAttribute("logoImage",variable+"document/invImg/"+logo+"");
			model.addAttribute("logoImage",variable+"document/hotel/"+logo+"");
			 
			//logoImage
			model.addAttribute("quotation", quotationList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		//System.out.println("rfqQuotation------------"+quotation);
	
		logger.info("Method : addPurchaseOrderRfQ ends");
		return "inventory/addPOwithRFQ";
	}
	
	
	/*
	 * post Mapping for add Purchase Order  rfq
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-purchase-order-with-rfq", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> savePurOrderRFQDetls(
			@RequestBody List<InventoryPORFQVendorDetailModel> InventoryPORFQVendorDetailModel, Model model,
			HttpSession session) {
		logger.info("Method : savePurOrderRFQDetls function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
	 
		  try {
			    for(InventoryPORFQVendorDetailModel i:InventoryPORFQVendorDetailModel) {
		  		i.setCreatedBy("abc001");
		  	 } 
			  res = restClient.postForObject(env.getInventoryUrl()
					  + "restAddPOrderRFQ", InventoryPORFQVendorDetailModel, JsonResponse.class);
		   } catch (RestClientException e) { e.printStackTrace(); 
		   
		   }
		  

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : savePurOrderRFQDetls function Ends");
		return res;
	}
	
	
	 
}
