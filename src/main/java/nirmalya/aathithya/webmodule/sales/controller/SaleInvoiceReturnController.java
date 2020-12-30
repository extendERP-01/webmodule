package nirmalya.aathithya.webmodule.sales.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.SaleInvoiceReturnModel;
import nirmalya.aathithya.webmodule.sales.model.SalesInvoiceModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class SaleInvoiceReturnController {

	Logger logger = LoggerFactory.getLogger(SaleInvoiceReturnController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Add Sale Invoice Return Note' page
	 *
	 */
	@GetMapping("/add-saleinvoice-return-note")
	public String defaultSaleInvoiceReturnNote(Model model, HttpSession session) {

		logger.info("Method : defaultSaleInvoiceReturnNote starts");
		
		SaleInvoiceReturnModel salesInvoiceReturn = new SaleInvoiceReturnModel();
		SaleInvoiceReturnModel salesInvoiceReturnSession = (SaleInvoiceReturnModel) session.getAttribute("ssalesInvoiceReturn");
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (salesInvoiceReturnSession != null) {
				model.addAttribute("salesInvoiceReturn", salesInvoiceReturnSession);
				session.setAttribute("salesInvoiceReturn", null);
			} else {
				model.addAttribute("salesInvoiceReturn", salesInvoiceReturn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : defaultSaleInvoiceReturnNote ends");
		return "sales/add-saleinvoice-return-note";
	}
	
	/**
	 * Web Controller - Get Sales Order List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-saleinvoice-return-note-salesOrderAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesOrderListForReturn(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesOrderListForReturn starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesOrderListForReturn?id=" + searchValue,
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

		logger.info("Method : getSalesOrderListForReturn ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Sales Invoice List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-saleinvoice-return-note-saleInvoiceAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesInvoicerListForReturn(@RequestParam("id") String salesOrder, @RequestParam("searchValue") String searchValue) {
		logger.info("Method : getSalesInvoicerListForReturn starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesInvoiceListForReturn?id=" + salesOrder + "&searchValue=" + searchValue,
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

		logger.info("Method : getSalesInvoicerListForReturn ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Sale Invoice List
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-saleinvoice-return-note-get-saleinvoice-list" })
	public @ResponseBody JsonResponse<Object> getSaleInvoiceList(Model model, @RequestBody String id,
			BindingResult result) {

		logger.info("Method : getSaleInvoiceList starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleInvoiceList?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getSaleInvoiceList ends");
		return res;
	}
	
	/**
	 * Web Controller - Check Sale Invoice Return
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/add-saleinvoice-return-note-checkSaleInvReturn" })
	public @ResponseBody JsonResponse<DropDownModel> checkSaleInvReturn(@RequestParam("id") String salesOrder, @RequestParam("saleInvId") String saleInvId) {
		logger.info("Method : checkSaleInvReturn starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "checkSaleInvReturn?id=" + salesOrder + "&saleInvId=" + saleInvId,
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

		logger.info("Method : checkSaleInvReturn ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Item List
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-saleinvoice-return-note-get-item-list" })
	public @ResponseBody JsonResponse<Object> getItemListForReturn(Model model, @RequestBody Map<String,String> saleInvoice,
			BindingResult result) {
		logger.info("Method : getItemListForReturn starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		if(saleInvoice.get("saleItemCode")!="" && saleInvoice.get("saleItemCode")!=null && saleInvoice.get("salesInvoice")!=""
				&& saleInvoice.get("salesInvoice")!=null) { 
			try {
				res = restClient.postForObject(env.getSalesUrl() + "getItemListForReturn" , saleInvoice, JsonResponse.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
	
				res.setMessage("success");
			}
		}
		logger.info("Method : getItemListForReturn ends");
		return res;

	}
	
	/**
	 * Web Controller - Add Sale Invoice Return Note
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-saleinvoice-return-note-save-data", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addSalesInvoiceReturn(@RequestBody List<SaleInvoiceReturnModel> salesInvoiceReturn,
			Model model, HttpSession session) {

		logger.info("Method : addSalesInvoiceReturn function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			for (int i = 0; i < salesInvoiceReturn.size(); i++) {
				salesInvoiceReturn.get(i).setCreatedBy(userId);
				salesInvoiceReturn.get(i).setCostCenter("cc006");
			}
			res = restClient.postForObject(env.getSalesUrl() + "addSalesInvoiceReturn", salesInvoiceReturn, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addSalesInvoiceReturn function Ends");
		return res;
	}
	
	/**
	 * Default 'View Sales Invoice Return' page
	 *
	 */
	@GetMapping("/view-saleinvoice-return-note")
	public String viewSalesInvoiceReturn(Model model, HttpSession session) {

		logger.info("Method : viewSalesInvoiceReturn starts");

		logger.info("Method : viewSalesInvoiceReturn ends");
		return "sales/view-saleinvoice-return-note";
	}
	
	/**
	 * Web Controller - Get Sale Invoice Return
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-saleinvoice-return-note-through-ajax")
	public @ResponseBody DataTableResponse viewSaleInvReturnThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewSaleInvReturnThroughAjax starts");

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

			JsonResponse<List<SaleInvoiceReturnModel>> jsonResponse = new JsonResponse<List<SaleInvoiceReturnModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getSaleInvReturn", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SaleInvoiceReturnModel> saleInvReturn = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SaleInvoiceReturnModel>>() {
					});

			String s = "";

			for (SaleInvoiceReturnModel m : saleInvReturn) {
				byte[] pId = Base64.getEncoder().encode(m.getSaleInvReturn().getBytes());
				
				m.setSaleInvReturn("<a href='javascript:void' onclick='pdfCreateSaleInvReturn(\"" + new String(pId) + "\")'>"
						+ m.getSaleInvReturn() + "</a>");
				
				if(m.getPayStatus()) {
					s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='DeleteSaleInvReturn(\"" + new String(pId) + "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; ";
					m.setAction(s);
					s = "";
				} else {
					s = s + "<a href='edit-saleinvoicee-return-note?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;"
							+ " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='DeleteSaleInvReturn(\"" + new String(pId) + "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; ";
					m.setAction(s);
					s = "";
				}
				
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(saleInvReturn);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSaleInvReturnThroughAjax ends");
		return response;
	}
	
	/**
	 * Web Controller - Get Sale Invoice Return For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-saleinvoice-return-note-modal" })
	public @ResponseBody JsonResponse<Object> modalSaleInvoiceReturn(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalSaleInvoiceReturn starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesInvReturnById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalSaleInvoiceReturn ends");
		return res;
	}
	
	/**
	 * Web Controller - Edit Sale Invoice Return
	 *
	 */
	@GetMapping("edit-saleinvoicee-return-note")
	public String editSaleInvoiceReturn(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editSaleInvoiceReturn starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			
			SaleInvoiceReturnModel[] salesInvoiceReturn = restClient.getForObject(env.getSalesUrl() + "editSaleInvReturnById?id=" + id,
					SaleInvoiceReturnModel[].class);
			List<SaleInvoiceReturnModel> salesInvoiceReturnList = Arrays.asList(salesInvoiceReturn);
			
			String invId = salesInvoiceReturnList.get(0).getSaleInvId();
			
			/** GET SALE INVOICE DETAILS LIST **/
			try {
				SalesInvoiceModel[] saleInv = restClient.getForObject(env.getSalesUrl() + "editSaleInvList?id=" + invId, SalesInvoiceModel[].class);
				List<SalesInvoiceModel> saleInvoiceList = Arrays.asList(saleInv);
				model.addAttribute("saleInvoiceList", saleInvoiceList);
				
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			/** GET ITEM CODE LIST **/
			try {
				DropDownModel[] item = restClient.getForObject(env.getSalesUrl() + "editItemList?id=" + invId, DropDownModel[].class);
				List<DropDownModel> itemList = Arrays.asList(item);
				model.addAttribute("itemList", itemList);
				
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			model.addAttribute("saleInvReturn", salesInvoiceReturnList.get(0).getSaleInvReturn());
			model.addAttribute("salesInvoiceReturn", salesInvoiceReturnList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editSaleInvoiceReturn starts");
		return "sales/add-saleinvoice-return-note";
	}
	
	/**
	 * Web Controller -  Delete Sale Invoice Return
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-saleinvoice-return-note-delete")
	public @ResponseBody JsonResponse<Object> deleteSaleInvoiceReturn(Model model, @RequestParam String id, HttpSession session) {
		
		logger.info("Method : deleteSaleInvoiceReturn starts");
		byte[] encodeByte=Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String createdBy = "u0001";
		
		try {
			resp = restClient.getForObject(env.getSalesUrl() + "deleteSaleInvReturnById?id=" + index +"&createdBy=" + createdBy, JsonResponse.class);

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
		logger.info("Method : deleteSaleInvoiceReturn ends");
		
		return resp;
	}
	
	/**
	 * Web Controller - Get Sales Invoice Return List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-saleinvoice-return-note-saleInvReturnAutoCompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesInvoiceReturnListAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesInvoiceReturnListAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleInvReturnList?id=" + searchValue,
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

		logger.info("Method : getSalesInvoiceReturnListAutoSearch ends");
		return res;
	}
	
	/**
	 * Web Controller - Sales Invoice Return Report
	 *
	 */
	@GetMapping("view-saleinvoice-return-note-generate-report")
	public String salesInvoiceReturnGenerateReport(Model model, HttpSession session) {
		
		logger.info("Method : salesInvoiceReturnGenerateReport starts");
		
		
		logger.info("Method : salesInvoiceReturnGenerateReport ends");
		return "sales/report-saleinvoice-return-note";
	}
	
	/**
	 * Web Controller - Get Sales Order List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-saleinvoice-return-note-generate-report-salesOrder" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesOrderReport(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getSalesOrderReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesOrderReport?id=" + searchValue,
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

		logger.info("Method : getSalesOrderReport ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Sales Invoice List By AutoSearch For Report
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-saleinvoice-return-note-generate-report-saleInvoice" })
	public @ResponseBody JsonResponse<DropDownModel> getSalesInvoiceReport(@RequestParam("id") String salesOrder, @RequestParam("searchValue") String searchValue) {
		logger.info("Method : getSalesInvoiceReport starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSalesInvReport?id=" + salesOrder + "&searchValue=" + searchValue,
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

		logger.info("Method : getSalesInvoiceReport ends");
		return res;
	}
}
