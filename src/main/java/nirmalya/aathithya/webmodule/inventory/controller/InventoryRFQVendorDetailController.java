  
package nirmalya.aathithya.webmodule.inventory.controller;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64; 
import java.util.List; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate; 

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DataSetForPropType1;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse; 
import nirmalya.aathithya.webmodule.inventory.model.InventoryRFQVendorDetailModel;  

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryRFQVendorDetailController {
	
	Logger logger = LoggerFactory.getLogger(InventoryRFQVendorDetailController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	
	/*
	 *
	 *  Add  Request For Quotation Vendor Details
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("add-rfq-quotation-detail")
	public String addRFQVendorDetails(Model model, HttpSession session) {
	
		logger.info("Method : addRFQVendorDetails starts");
		
		InventoryRFQVendorDetailModel quotation = new InventoryRFQVendorDetailModel();
			try {
			//leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", "");
			 
			model.addAttribute("quotation", quotation);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : addRFQVendorDetails ends");	
		
		return "inventory/addRFQVendorDetails";
	}
	
	
	

	/*
	 *
	 *  View Request Quotation Against Vendor
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-rfq-quotation-details")
	public String viewRFQVendorDetails(Model model, HttpSession session) {
	
		logger.info("Method : viewRFQVendorDetails starts");
		
		InventoryRFQVendorDetailModel vendorRFQDetails = new InventoryRFQVendorDetailModel();
			try {
			//leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", "");
			 
			model.addAttribute("vendorRFQDetails", vendorRFQDetails);
		}catch(Exception e) {
			e.printStackTrace();
		}

			
			//Start RFQ NAME Drop Down List
			JsonResponse<List<DropDownModel>> respTblMstr2 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> RFQList = new ArrayList<DropDownModel>(); 
			try {
				respTblMstr2 = restClient.getForObject(env.getInventoryUrl() + "getRFQNameListd?Action=" + "getRFQNameListd",
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr2 = respTblMstr2.getMessage();

			if (messageForTblMstr2 != null || messageForTblMstr2 != "") {
				model.addAttribute("message", messageForTblMstr2);
			}

			ObjectMapper mapper2 = new ObjectMapper();

			 
			RFQList = mapper2.convertValue(respTblMstr2.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("lRFQData", RFQList);
			//End RFQ NAME Drop Down List
			
		logger.info("Method : viewRFQVendorDetails ends");	
		
		return "inventory/viewRFQVendorDetails";
	} 
 
/**
	 * Web Controller - Get RFQ No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-rfq-quotation-detail-getRFQAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getRFQList(Model model, @RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getRFQList starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getRFQListByAutoSearch?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (res.getMessage() != null) {
			
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getRFQList ends");
		return res;
	}
	
	
	
	
	/**
	 * Web Controller - Get RFQ No By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-rfq-quotation-detail-getVendorName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getRFQVenList(Model model, @RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getRFQVenList starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getRFQVenListbyRfq?id=" + searchValue, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (res.getMessage() != null) {
			
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getRFQVenList ends");
		return res;
	}
	
	 
	
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-rfq-quotation-detail-menu-item" })
	public @ResponseBody JsonResponse<DataSetForPropType1> getRFQItemList(Model model,
			@RequestBody List<DataSetForPropType1> params) {
		logger.info("Method :getRFQItemList starts");
		JsonResponse<DataSetForPropType1> res = new JsonResponse<DataSetForPropType1>();
		try {
			res = restClient.postForObject(env.getInventoryUrl() + "getRFQItemList", params, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method :getRFQItemList  ends");
		return res;
	}
	
	/*
	 * post Mapping for add Purchase Order
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-rfq-quotation-detail", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveVendorRFQDetls(
			@RequestBody List<InventoryRFQVendorDetailModel> InventoryRFQVendorDetailModel, Model model,
			HttpSession session) {
		logger.info("Method : saveVendorRFQDetls function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
	 
		  try {
			    for(InventoryRFQVendorDetailModel i:InventoryRFQVendorDetailModel) {
		  		i.setCreatedBy("abc001");
		  	 } 
			  res = restClient.postForObject(env.getInventoryUrl()
					  + "restAddVendorRFQ", InventoryRFQVendorDetailModel, JsonResponse.class);
		   } catch (RestClientException e) { e.printStackTrace(); 
		   
		   }
		  

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveVendorRFQDetls function Ends");
		return res;
	}
	
	 
	
	/*
	 *
	 * View all Request Quotation data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-rfq-quotation-details-through-ajax")
	public @ResponseBody DataTableResponse viewReqQuotThroAjax(Model model, HttpServletRequest request, @RequestParam String param1) {
		
		logger.info("Method : viewReqQuotThroAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
		  
			JsonResponse<List<InventoryRFQVendorDetailModel>> jsonResponse = new JsonResponse<List<InventoryRFQVendorDetailModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getRFQVenDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryRFQVendorDetailModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryRFQVendorDetailModel>>() {
					});

			String s = ""; 
			

			for (InventoryRFQVendorDetailModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getVenQuotId().getBytes());
			  
				Byte isEdit=1;
				Byte isEditdb=m.getIsEdit();
				if(isEditdb.equals(isEdit)) {
					s = s +  "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getVenQuotId()
							+ "\")'><i class='fa fa-search search'></i></a>";
				}else {
				
					s = s +  "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getVenQuotId()
							+ "\")'><i class='fa fa-search search'></i></a>";
					
					s = s + "<a href='edit-rfq-quotation-detail?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
					
				}
				
				
				   
				m.setAction(s);
				s = ""; 
		 
			} 
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewReqQuotThroAjax ends");	
		return response;
	}
	
	 
	/**
	 * EDIT RFQ Vendor Details
	 *
	 */
 
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-rfq-quotation-detail111")
	public String editRFQVendorDtls(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
	
		logger.info("Method : editRFQVendorDtls starts"); 
	
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte)); 
	
		InventoryRFQVendorDetailModel rfqQuotation = new InventoryRFQVendorDetailModel();
		JsonResponse<InventoryRFQVendorDetailModel> jsonResponse = new JsonResponse<InventoryRFQVendorDetailModel>();
	
		try {
			jsonResponse = restClient.getForObject(
					env.getInventoryUrl() + "getRFQVDById?id=" + id + "&Action=viewEditRFQVDtls",
					JsonResponse.class); 

			
	
		} catch (RestClientException e) {
			 e.printStackTrace();
		}
	
		
		String message = (String) session.getAttribute("message");
	
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
	
		ObjectMapper mapper = new ObjectMapper();  
	
		rfqQuotation = mapper.convertValue(jsonResponse.getBody(), InventoryRFQVendorDetailModel.class);
		session.setAttribute("message", "");
		 
		model.addAttribute("rfqQuotation", rfqQuotation);
		
	
		logger.info("Method : editRFQVendorDtls ends");
		return "inventory/addRFQVendorDetails";
	}
	
	 
	
	/**
	 * Web Controller - Edit Quotation Master
	 *
	 */
	@GetMapping("edit-rfq-quotation-detail")
	public String editRFQVen(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editRFQVen starts");

		/**
		 * get DropDown value for Serve Type
		 *
		 */
		try {
			DropDownModel[] vendorList = restClient.getForObject(env.getInventoryUrl() + "restGetVendor",
					DropDownModel[].class);
			List<DropDownModel> vList = Arrays.asList(vendorList);

			model.addAttribute("vendors", vList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());   
		String id = (new String(encodeByte));

		try {

			InventoryRFQVendorDetailModel[] quotation = restClient.getForObject(env.getInventoryUrl() + "getRFQVDById?id=" + id,
					InventoryRFQVendorDetailModel[].class);
			List<InventoryRFQVendorDetailModel> quotationList = Arrays.asList(quotation);
		//	System.out.println("rfqQuotation------------"+quotation);
			model.addAttribute("quotationId", quotationList.get(0).getVenQuotId());
			model.addAttribute("taxType", quotationList.get(0).getTaxType());
			
			//System.out.println("quotation data for edit time------------------------------"+quotationList);
			model.addAttribute("quotation", quotationList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		//System.out.println("rfqQuotation------------"+quotation);
		logger.info("Method : editRFQVen starts");
		return "inventory/addRFQVendorDetails";
	}
	
	 
	
	/*
	 * 
	 * Modal View of RFQ Vendor Details
	 *
	 */
	  

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-rfq-quotation-details-model" })
	public @ResponseBody JsonResponse<InventoryRFQVendorDetailModel> modelVendorRFQ(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelVendorRFQ starts");
	 	JsonResponse<InventoryRFQVendorDetailModel> res = new JsonResponse<InventoryRFQVendorDetailModel>();
		
		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "getVendorRFQModel?id=" + index,
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
		logger.info("Method : modelVendorRFQ ends");
		return res;
	}
	

}
