package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.GRNListModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class MultipleInvoiceController {

	Logger logger = LoggerFactory.getLogger(MultipleInvoiceController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	private static final String user_Id = "USER_ID";

	/**
	 * View Default 'Schedule Multiple Invoice' page
	 *
	 */
	@GetMapping("/add-multiple-grn-invoice")
	public String scheduleMultipleInvoice(Model model, HttpSession session) {
		logger.info("Method : scheduleMultipleInvoice starts");

	
		logger.info("Method : scheduleMultipleInvoice ends");
		return "inventory/add-multiple-grn-invoice";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-multiple-grn-invoice-get-vendor" })
	public @ResponseBody JsonResponse<DropDownModel> getVendorAutoSearchList(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getVendorAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getVendorSearchListForGRN?id=" + searchValue,
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

		logger.info("Method : getVendorAutoSearchList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-multiple-grn-invoice-get-po" })
	public @ResponseBody JsonResponse<DropDownModel> getPOAutoSearchList(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getPOAutoSearchList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getPOAutoSearchListForGRN?id=" + searchValue.getKey()
					+ "&value=" + searchValue.getName(), JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getPOAutoSearchList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-multiple-grn-invoice-get-grn-list" })
	public @ResponseBody JsonResponse<DropDownModel> getGRNList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getGRNList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getInventoryUrl() + "getGRNListByVendor?id=" + searchValue,
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

		logger.info("Method : getGRNList ends");
		return res;
	}

	@PostMapping(value = { "/add-multiple-grn-invoice-save" })
	public @ResponseBody JsonResponse<DropDownModel> getGRNListAdd(Model model,
			@RequestBody List<DropDownModel> searchValue, BindingResult result, HttpSession session) {
		logger.info("Method : getGRNList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		// addMultipleInvoice(model,searchValue);
		session.setAttribute("GRN", searchValue);

		res.setMessage("Success");

		logger.info("Method : getGRNList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/add-multiple-grn-invoice-add-invoice")
	public String addMultipleInvoice(Model model, HttpSession session) {
		logger.info("Method : addMultipleInvoice starts");

		List<DropDownModel> searchValue = new ArrayList<DropDownModel>();
		searchValue = (List<DropDownModel>) session.getAttribute("GRN");
		if (searchValue.size() <= 0) {
			session.invalidate();
		}

		List<GRNListModel> storeList = new ArrayList<GRNListModel>();

		JsonResponse<List<GRNListModel>> res = new JsonResponse<List<GRNListModel>>();
		JsonResponse<List<GRNListModel>> resReturn = new JsonResponse<List<GRNListModel>>();

		res = restClient.postForObject(env.getInventoryUrl() + "getGRNListDetails", searchValue, JsonResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		storeList = mapper.convertValue(res.getBody(), new TypeReference<List<GRNListModel>>() {
		});

		Double subTotal = 0.0;
		Double totalCGST = 0.0;
		Double totalIGST = 0.0;
		Double totalCess = 0.0;
		Double CGST = 0.0;
		Double IGST = 0.0;
//		Double cess = 0.0;
		Double grandTotal = 0.0;
		for (GRNListModel m : storeList) {
			Double amount = 0.0;
			amount = m.getPrice() * m.getQty();
			m.setAmount(amount);
			subTotal = subTotal + m.getAmount();
			CGST = ((m.getGst() / 2) / 100) * amount;
			totalCGST = CGST + totalCGST;
			IGST = (m.getGst() / 100) * amount;
//			cess = (IGST * m.getCessAmount())/100;
			totalIGST = IGST + totalIGST;
			totalCess = m.getCessAmount();
		}
		List<GRNListModel> returnList = new ArrayList<GRNListModel>();
		try {
			resReturn = restClient.postForObject(env.getInventoryUrl() + "grnReturnData", searchValue, JsonResponse.class);
			returnList = mapper.convertValue(resReturn.getBody(), new TypeReference<List<GRNListModel>>() {
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Double subTotalRT = 0.0;
		Double totalCGSTRT = 0.0;
		Double totalIGSTRT = 0.0;
		Double totalCessRT = 0.0;
		Double CGSTRT = 0.0;
		Double IGSTRT = 0.0;
		for (GRNListModel m : returnList) {
			Double amount = 0.0;
			amount = m.getPrice() * m.getQty();
			m.setAmount(amount);
			subTotalRT = subTotalRT + m.getAmount();
			CGSTRT = ((m.getGst() / 2) / 100) * amount;
			totalCGSTRT = CGSTRT + totalCGSTRT;
			IGSTRT = (m.getGst() / 100) * amount;
			totalIGSTRT = IGSTRT + totalIGSTRT;
			
			totalCessRT = m.getCessAmount();
		}
		
		Double qty = 0.0;
		Double amt = 0.0;
		if(returnList.size()>0) {
			for(int i = 0; i < storeList.size(); i++) {
				if(storeList.get(i).getItem().contentEquals(returnList.get(i).getItem())) {
					qty = storeList.get(i).getQty() - returnList.get(i).getQty();
					amt = storeList.get(i).getAmount() - returnList.get(i).getAmount();
					System.out.println(storeList.get(i).getItem()+" / "+returnList.get(i).getItem()+" = "+qty);
					storeList.get(i).setQty(qty);
					storeList.get(i).setAmount(amt);
				}
			}
		}
		
		subTotal = subTotal - subTotalRT;
		totalCGST = totalCGST - totalCGSTRT;
		totalIGST = totalIGST - totalIGSTRT;
		totalCess = totalCess - totalCessRT;
		
		if(storeList.size()>0) {
			if(storeList.get(0).getGstType()) {
				totalCGST = 0.0;
			} else {
				totalIGST = 0.0;
			}
		}
		
		for (GRNListModel m : storeList) {
//			m.setQty(qty);
			m.setCessAmount(totalCess);
			m.setSubTotal(subTotal);
			m.setTotalCGST(totalCGST);
			m.setTotalIGST(totalIGST);
			if(m.getGstType()) {
				grandTotal = totalCess + totalIGST + subTotal;
			} else {
				grandTotal = totalCess + totalCGST + totalCGST + subTotal;
			}
			
			m.setGrandTotal((double) Math.round(grandTotal));
		}
		
		System.out.println(storeList);
		
		model.addAttribute("GRNList", storeList);

		
		String userId = ""; 
		try {
			userId = (String) session.getAttribute(user_Id);
			DropDownModel[] store = restClient.getForObject(env.getInventoryUrl() + "getPOrderStoreList?id=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeDetails = Arrays.asList(store);
			model.addAttribute("storeList", storeDetails);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		logger.info("Method : addMultipleInvoice ends");
		return "inventory/add-multiple-grn-invoice-add-invoice";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "add-multiple-grn-invoice-submit")
	public @ResponseBody JsonResponse<Object> submitMultipleGRN(@RequestBody GRNListModel grn, Model model,
			HttpSession session) {
		logger.info("Method : submitMultipleGRN function starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<DropDownModel> searchValue = new ArrayList<DropDownModel>();
		searchValue = (List<DropDownModel>) session.getAttribute("GRN");
		if (searchValue.size() <= 0) {
			session.invalidate();
		}
		List<String> grnList = new ArrayList<String>();
		for (DropDownModel a : searchValue) {
			grnList.add(a.getKey());
		}
		List<GRNListModel> storeList = new ArrayList<GRNListModel>();

		JsonResponse<List<GRNListModel>> res = new JsonResponse<List<GRNListModel>>();
		JsonResponse<List<GRNListModel>> resReturn = new JsonResponse<List<GRNListModel>>();

		res = restClient.postForObject(env.getInventoryUrl() + "getGRNListDetails", searchValue, JsonResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		storeList = mapper.convertValue(res.getBody(), new TypeReference<List<GRNListModel>>() {
		});

		Double subTotal = 0.0;
		Double totalCGST = 0.0;
		Double totalIGST = 0.0;
		Double totalCess = 0.0;
		Double CGST = 0.0;
		Double IGST = 0.0;
		Double grandTotal = 0.0;
		for (GRNListModel m : storeList) {
			Double amount = 0.0;
			amount = m.getPrice() * m.getQty();
			m.setAmount(amount);
			subTotal = subTotal + m.getAmount();
			CGST = ((m.getGst() / 2) / 100) * amount;
			totalCGST = CGST + totalCGST;
			IGST = (m.getGst() / 100) * amount;
			totalIGST = IGST + totalIGST;
			totalCess = m.getCessAmount();
		}
		List<GRNListModel> returnList = new ArrayList<GRNListModel>();
		try {
			resReturn = restClient.postForObject(env.getInventoryUrl() + "grnReturnData", searchValue, JsonResponse.class);
			returnList = mapper.convertValue(resReturn.getBody(), new TypeReference<List<GRNListModel>>() {
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Double subTotalRT = 0.0;
		Double totalCGSTRT = 0.0;
		Double totalIGSTRT = 0.0;
		Double totalCessRT = 0.0;
		Double CGSTRT = 0.0;
		Double IGSTRT = 0.0;
		for (GRNListModel m : returnList) {
			Double amount = 0.0;
			amount = m.getPrice() * m.getQty();
			m.setAmount(amount);
			subTotalRT = subTotalRT + m.getAmount();
			CGSTRT = ((m.getGst() / 2) / 100) * amount;
			totalCGSTRT = CGSTRT + totalCGSTRT;
			IGSTRT = (m.getGst() / 100) * amount;
			totalIGSTRT = IGSTRT + totalIGSTRT;
			
			totalCessRT = m.getCessAmount();
		}
		
		Double qty = 0.0;
		Double amt = 0.0;
		if(returnList.size()>0) {
			for(int i = 0; i < storeList.size(); i++) {
				if(storeList.get(i).getItem().contentEquals(returnList.get(i).getItem())) {
					qty = storeList.get(i).getQty() - returnList.get(i).getQty();
					amt = storeList.get(i).getAmount() - returnList.get(i).getAmount();
					storeList.get(i).setQty(qty);
					storeList.get(i).setAmount(amt);
				}
			}
		}
		subTotal = subTotal - subTotalRT;
		totalCGST = totalCGST - totalCGSTRT;
		totalIGST = totalIGST - totalIGSTRT;
		totalCess = totalCess - totalCessRT;

		if (storeList.size() > 0) {
			
			if(storeList.get(0).getGstType()) {
				totalCGST = 0.0;
				grandTotal = totalIGST + subTotal + totalCess;
			} else {
				totalIGST = 0.0;
				grandTotal = totalCGST + totalCGST + subTotal + totalCess;
			}
			grn.setAmount(subTotal);
			grn.setGstType(storeList.get(0).getGstType());
			grn.setCessAmount(totalCess);
			grn.setTotalCGST(totalCGST);
			grn.setTotalIGST(totalIGST);
			grn.setGrandTotal(grandTotal);
			grn.setCreatedBy(userId);
			grn.setVendorId(storeList.get(0).getVendorId());
			grn.setpOrder(storeList.get(0).getpOrder());
			grn.setGrnList(grnList);
		}

		try {
			resp = restClient.postForObject(env.getInventoryUrl() + "addMultipleGRN", grn, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();
		if (message != null && message != "") {

		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : submitMultipleGRN function Ends");
		return resp;
	}
}
