package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList; 
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
import nirmalya.aathithya.webmodule.inventory.model.InventoryRFQVendorDetailModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryReqDetailsModel; 

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryPurchaseDeptRFQController {
	
	Logger logger = LoggerFactory.getLogger(InventoryPurchaseDeptRFQController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	
	 

	/*
	 *
	 *  View Request Quotation Against RFQ
	 * 
	 */	
	 
	@SuppressWarnings("unchecked")
	@GetMapping("/view-purchase-dept-rfq-vendor")
	public String viewReqQuotationPurchaseDept(Model model, HttpSession session) {
	
		logger.info("Method : viewReqQuotationPurchaseDept starts");
		
		InventoryReqDetailsModel changeApprove = new InventoryReqDetailsModel();
		try {
			//leavelist.setEmpl("EMPL0004");
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", "");
			 
			model.addAttribute("changeApprove", changeApprove);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	
		

		//Start Vendor name Drop Down List
		JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> VendorList = new ArrayList<DropDownModel>(); 
		try {
			respTblMstr1 = restClient.getForObject(env.getInventoryUrl() + "getVendorsList?Action=" + "getVendorsList",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr1 = respTblMstr1.getMessage();

		if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
			model.addAttribute("message", messageForTblMstr1);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		 
		VendorList = mapper1.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("VendorList", VendorList);
		//End VENDOR NAME Drop Down List
		
		
		 
		
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
		
		
		
		logger.info("Method : viewReqQuotationPurchaseDept ends");	
		
		return "inventory/viewRFQPurchaseDeptVend";
	}
	
	 
	 
	
	/*
	 *
	 * View all Request Quotation data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-purchase-dept-rfq-vendor-through-ajax")
	public @ResponseBody DataTableResponse viewRFQVendorThroughAjax(Model model, HttpServletRequest request, @RequestParam String param1) {
		
		logger.info("Method : viewReqQuotThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
		 
			
		//	System.out.println("param 1 is---------"+param1);
		 

			JsonResponse<List<InventoryReqDetailsModel>> jsonResponse = new JsonResponse<List<InventoryReqDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getReqQuotDataVendor", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryReqDetailsModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryReqDetailsModel>>() {
					});

			String s = "";
			String p = "";
			

			for (InventoryReqDetailsModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getReqQuotId().getBytes());
				String qId =m.getQuotationNo();
			
				
				 
				
			//	TRQ_ApprovedStatus
				 
				
				//s = s + //"<a href='edit-rfq?id=" + new String(pId)
						//+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						//+"<a href='javascript:void(0)'" 
						//+ "' onclick='DeleteItem(\"" + m.getReqQuotId() + "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
					//	 "<a data-toggle='modal' title='View'  "
					//	+ "href='javascript:void' onclick='viewInModel(\"" + m.getReqQuotId()
					//	+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
			//	Byte fStatusDb=1;
				 
				 
				/*	s = s + "<a href='javascript:void(0)'" + "' onclick='changeFoodOrderStatusData(\"" + new String(pId)
							+ ',' + m.getApprovedStatus() + ',' + new String(pId)
							+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px;color:#090\"></i></a>&nbsp";

			 */
				String quotationnumber=m.getQuotationNo();
				String pOrderNo=m.getPurOrderNo();
				
			 
				
				if(quotationnumber != null ) {
					
					if(pOrderNo != null) {
						//s = s	+"<a href='view-purchase-dept-rfq-vendor' > Purchase Order Generated </a>";
						s = s	+"<a href='javascript:void(0)' onclick='GeneratedPO()' > Purchase Order Generated </a>";
						m.setPurchaseOrderS(pOrderNo);
					}else {
						s = s	+"<a href='add-purchase-order-with-rfq?id=" +  new String(pId)
								+ "' ><button type='button' class='btn btn-info'>Generate PO</button></a>";
						m.setPurchaseOrderS("Yet to Generate");
					}
				 
					 
					
				}else {
					/*
					 * <button class="GFG"
					 * onclick="window.location.href = 'https://ide.geeksforgeeks.org';"> Click Here
					 * </button>
					 */
				//	s = s	+"<a href='view-purchase-dept-rfq-vendor-select?id=" + new String(pId)
					//		+ "' >Select Vendor</a>";
					s = s	+"<a href='view-purchase-dept-rfq-vendor-select?id=" + new String(pId)
							+ "' ><button type='button' class='btn btn-info'>Select Vendor</button></a>";
					
					
					
				}
				 
				if(quotationnumber != null ) {
					
					String modelPop = "";
					modelPop = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
							+ m.getQuotationNo() + "\")'> "+m.getQuotationNo()+"  </a>";
					m.setQuotationNo(modelPop); 
				}else {
					m.setQuotationNo("Yet to Select");
				}
				
				
				
					
				/*	s = s	+ "<a data-toggle='modal' title='Approve'  "
					+ "href='javascript:void' onclick='viewInModelApprove(\"" + m.getReqQuotId()
					+ "\")'>Vendor Selection</a>";*/

			 
				System.out.println("Approved Status in View RFQ-----------------"+m.getApprovedStatus());
				m.setAction(s);
				s = "";
			//	System.out.println("data in status   "+m.getDistrictStatus());
			}

			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewRFQVendorThroughAjax ends");	
		return response;
	}
	
	/*
	 *
	 * View all Request Quotation data through AJAX for Staff
	 *
	 */
/*	@SuppressWarnings("unchecked")
	//@GetMapping("/view-purchase-dept-rfq-vendor-through-ajax")
	@GetMapping("/view-rfq-through-ajax-purchase-dept")
	public @ResponseBody DataTableResponse viewReqQuotThroughAjaxStaff(Model model, HttpServletRequest request, @RequestParam String param1) {
		
		logger.info("Method : viewReqQuotThroughAjaxstaff starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
		 
			
		//	System.out.println("param 1 is---------"+param1);
		 

			JsonResponse<List<InventoryReqDetailsModel>> jsonResponse = new JsonResponse<List<InventoryReqDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getReqQuotDataStaff", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryReqDetailsModel> reqQuotation = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryReqDetailsModel>>() {
					});

			String s = "";
			

			for (InventoryReqDetailsModel m : reqQuotation) {
				byte[] pId = Base64.getEncoder().encode(m.getReqQuotId().getBytes());
			
				
			 
				s = s + "<a href='edit-rfq?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						 + "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInModel(\"" + m.getReqQuotId()
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				 
				System.out.println("Approved Status in View RFQ-----------------"+m.getApprovedStatus());
				m.setAction(s);
				s = "";
			//	System.out.println("data in status   "+m.getDistrictStatus());
			}

			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reqQuotation);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewReqQuotThroughAjaxstaff ends");	
		return response;
	}*/
	
	
	/**
	 * Select RFQ Quotation 
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-purchase-dept-rfq-vendor-select")
	public String selectVnedorQuotation(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {
	
		logger.info("Method : selectVnedorQuotation starts"); 
	
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte)); 
	
		 
		model.addAttribute("rfqId", id);
	
		logger.info("Method : selectVnedorQuotation ends");
		return "inventory/selectVenRFQ";
	} 
	
	 
	/*
	 *
	 * View all   Quotation data for approve or reject through AJAX 
	 *
	 */
 
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-purchase-dept-rfq-vendor-select-through-ajax")
	public @ResponseBody DataTableResponse viewRFQQuotThroughAjaxSelect(Model model, HttpServletRequest request, @RequestParam String param1) {
		
		logger.info("Method : viewRFQQuotThroughAjaxSelect starts");
		
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

			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getRFQDataVendorSelect", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<InventoryRFQVendorDetailModel> rfqQuotationSelect = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryRFQVendorDetailModel>>() {
					});

			String s = "";
			
			
			

			for (InventoryRFQVendorDetailModel m : rfqQuotationSelect) {
				byte[] pId = Base64.getEncoder().encode(m.getVenQuotId().getBytes());
				String a="Pending";
				String b="Approved";
				String c="Rejected";
				Byte Approvestatus=m.getApproveStatus();
				if(Approvestatus== 0) {
					m.setShowStatus(a);
				}else if
				(Approvestatus== 1) {
					m.setShowStatus(b);
				}else {
					m.setShowStatus(c);
				}
				
				String modelPop = "";
				modelPop = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ m.getVenQuotId() + "\")'> "+m.getVenQuotId()+"  </a>";
				m.setVenQuotId(modelPop);
				
			 
				Byte pendingDb=0;
				Byte approveDb=1;
				Byte rejectedDb=2;
				 
				 if(m.getApproveStatus()==approveDb) {
					 s = s + "<a href='javascript:void(0)'" + "' onclick='approveVendor1(\"" + new String(pId)
								+ ',' + m.getApproveStatus() + ',' + new String(pId)
									+ "\")' ><i class=\"fa fa-check-circle\" title=\"Complete\"></i></a>&nbsp";

					 
				 }
				 
				 if(m.getApproveStatus()==rejectedDb) {
					 
				 }
				 
				 if(m.getApproveStatus()==pendingDb) {
					 s = s + "<a href='javascript:void(0)'" + "' onclick='approveVendor(\"" + new String(pId)
								+ ',' + m.getRfqNo() +','+ m.getApproveStatus()
								+ "\")' ><i class=\"fa fa-times-circle\" title=\"Pending\"></i></a>";

				 }
				 
				 
					
				 
			 
			//	System.out.println("Approved Status in View RFQ-----------------"+m.getApprovedStatus());
				m.setAction(s);
				s = "";
			//	System.out.println("data in status   "+m.getDistrictStatus());
			}

			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(rfqQuotationSelect);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewRFQQuotThroughAjaxSelect ends");	
		return response;
	}
	
	
	/**
	 * InVentory Approve Vendor STATUS CHANGE 
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-purchase-dept-rfq-vendor-select-change-status")
	public @ResponseBody JsonResponse<Object> ApproveStatusQuote(Model model,
			@RequestParam("id") String id1, @RequestParam("rfqId") String rfqId,
			@RequestParam("approveStatus") Byte approveStatus, HttpSession session) {

		logger.info("Method : WEBMODULE  ApproveStatusQuote starts");
		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());
		//byte[] encodeByte5 = Base64.getDecoder().decode(createdBy1.getBytes());

		String id = (new String(encodeByte));
		//String createdBy = (new String(encodeByte5));

		JsonResponse<Object> resp = new JsonResponse<Object>();
		// foodOrderData.get(0).setCreatedBy("u0010");
		try {
			resp = restClient.getForObject(env.getInventoryUrl() + "approveQuotStatus?id=" + id
					+ "&rfqId=" + rfqId + "&approveStatus=" + approveStatus, JsonResponse.class);
		
		 
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") { 
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : WEBMODULE ApproveStatusQuote ends");
		return resp;
	}
	
	/**
	 * View selected Quotation Details in Modal
	 *
	 */


	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-purchase-dept-rfq-vendor-select-model" })
	public @ResponseBody JsonResponse<InventoryRFQVendorDetailModel> getQuotationModel(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modelviewForMembershipReg starts");
	 	JsonResponse<InventoryRFQVendorDetailModel> res = new JsonResponse<InventoryRFQVendorDetailModel>();
		
		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "getQuotationModel?id=" + index,
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
		logger.info("Method : getQuotationModel ends");
		return res;
	}

	
}
