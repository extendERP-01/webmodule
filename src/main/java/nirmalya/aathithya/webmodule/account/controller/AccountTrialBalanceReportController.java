package nirmalya.aathithya.webmodule.account.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.AccountTrialBalanceModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

@Controller
@RequestMapping(value = "account")
public class AccountTrialBalanceReportController {

	Logger logger = LoggerFactory.getLogger(AccountTrialBalanceReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/********************************************************************** R A J E S H ***********************************************************************************/
	/******************** F I L T E R  F U N C T I O N ****************/
	public List<AccountTrialBalanceModel> getFilteredList(int start,int length,List<AccountTrialBalanceModel> model){
		List<AccountTrialBalanceModel> filteredData = new ArrayList<AccountTrialBalanceModel>();
		if((model.size()-start) > length) {
			for(int i = start;i < (start+length);i++) {
				filteredData.add(model.get(i));
			}
		}else {
			for(int i = start;i < model.size();i++) {
				filteredData.add(model.get(i));
			}
		}
		return filteredData;
	}
	/************************* TRIAL BALANCE ***************************/
	/*
	 * get mapping for view trial balance
	 */
	@GetMapping("/view-trial-balance-report")
	public String viewaBalanceSheet(Model model, HttpSession session) {

		logger.info("Method : viewaBalanceSheet starts");
		// for get cost center list
		try {
			String costCenter = (String)session.getAttribute("costcenter");
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			
			if(costCenter!=null) {
				model.addAttribute("costCenter",costCenter);
			}else {
				model.addAttribute("costCenter",null);
			}
			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewaBalanceSheet ends");
		return "account/trial-balance-search";
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-ajax")
	public @ResponseBody DataTableResponse viewaBalanceSheetAjax(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String param4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		session.setAttribute("costcenter", param3);
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-view", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		
		//System.out.println("accountTrialBalanceModel: "+accountTrialBalanceModel);
		List<AccountTrialBalanceModel> trialBalList = new ArrayList<AccountTrialBalanceModel>();
		List<String> accGrList = new ArrayList<String>();
		Map  staticAccGrp = new HashMap<String,String>();
		try {

			DropDownModel[] x = restClient.getForObject(env.getAccountUrl() + "get-all-accHeads",DropDownModel[].class);
			for(DropDownModel dm: x) {
				staticAccGrp.put(dm.getKey(), dm.getName());
				accGrList.add(dm.getKey());
			}
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}		
		
		for(int i = 0;i<accGrList.size();i++) {
			AccountTrialBalanceModel model = new AccountTrialBalanceModel();
			model.setAccountHeadId(accGrList.get(i));
			model.setDesc((String) staticAccGrp.get(accGrList.get(i)));
			model.setDebitBal(0.00);
			model.setCreditVal(0.00);
			String s= "<a href='javascript:void(0)'  title='Drilldown'>"+model.getDesc()+"</a>";
			model.setAction(s);
			model.setSlNo(i+1);
			trialBalList.add(model);
		}
		
		
		String s="";
		for(int i = 0;i<accGrList.size();i++) {
			for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
				
				//m.setOpenBal("0");
		
				byte[] encodeId = Base64.getEncoder().encode(m.getDesc().getBytes()); //desc contains accountgroup id
				
					s= "<a href='javascript:void(0)' onclick='showTrialModal(\""+m.getParentSeqList()+"\",\""+m.getDesc()+"\")' title='Drilldown'>"+m.getParent().getName()+"</a>";
					
				m.setAction(s);
				if(accGrList.get(i).equals(m.getParent().getKey())){
					trialBalList.get(i).setAction(s);
					trialBalList.get(i).setDebitBal(m.getDebitBal());
					trialBalList.get(i).setCreditVal(m.getCreditVal());
				}
				s="";
			}
		}
		if(param4.equals("specific")) {
			List<AccountTrialBalanceModel> filterList = new ArrayList<AccountTrialBalanceModel>();
			for(AccountTrialBalanceModel f:trialBalList) {
				if(f.getCreditVal() > 1 || f.getDebitBal() > 1) {
					filterList.add(f);
				}
			}
			response.setRecordsTotal(filterList.size());
			response.setRecordsFiltered(filterList.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),filterList);
			response.setData(filterList1);
		}else {
			response.setRecordsTotal(trialBalList.size());
			response.setRecordsFiltered(trialBalList.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),trialBalList);
			response.setData(filterList1);
		}
		
		return response;
	}
	
	/************************* BALANCE SHEET ***************************/
	/*
	 * get mapping for view-balance-sheet
	 */
	@GetMapping("/view-balance-sheet")
	public String viewBalanceSheet(Model model, HttpSession session) {

		logger.info("Method : viewaBalanceSheet starts");
		// for get cost center list
		try {
			String costCenter = (String)session.getAttribute("costcenter");
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			
			if(costCenter!=null) {
				model.addAttribute("costCenter",costCenter);
			}else {
				model.addAttribute("costCenter",null);
			}
			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewaBalanceSheet ends");
		return "account/balance-sheet-search";
	}
	
	/*
	 * view-balance-sheet-through-ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-balance-sheet-through-ajax")
	public @ResponseBody DataTableResponse viewBalanceSheetAjax(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String param4) {
		
		logger.info("Method : viewaBalanceSheet through ajax starts"); 
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		session.setAttribute("costcenter", param3);
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-view", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		
		List<AccountTrialBalanceModel> balanceSheetList = new ArrayList<AccountTrialBalanceModel>();
		List<String> balaceSheetPList = new ArrayList<String>();
		balaceSheetPList.add("GN0001");
		balaceSheetPList.add("GN0002");
		balaceSheetPList.add("GN0003");
		Map  staticBalanceSh = new HashMap<String,String>();
		staticBalanceSh.put("GN0001", "CAPITAL ACCOUNT");
		staticBalanceSh.put("GN0002", "LIABILITIES");
		staticBalanceSh.put("GN0003", "ASSETS");
		for(int i = 0;i<balaceSheetPList.size();i++) {
			AccountTrialBalanceModel model = new AccountTrialBalanceModel();
			model.setAccountHeadId(balaceSheetPList.get(i));
			model.setDesc((String) staticBalanceSh.get(balaceSheetPList.get(i)));
			model.setDebitBal(0.00);
			model.setCreditVal(0.00);
			String s= "<a href='javascript:void(0)'  title='Drilldown'>"+model.getDesc()+"</a>";
			model.setAction(s);
			model.setSlNo(i+1);
			balanceSheetList.add(model);
		}
		
		String s="";
		
		for(int i = 0;i<balaceSheetPList.size();i++) {
			for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
				byte[] encodeId = Base64.getEncoder().encode(m.getDesc().getBytes()); //desc contains accountgroup id
				
					s= "<a href='javascript:void(0)' onclick='showTrialModal(\""+m.getParentSeqList()+"\",\""+m.getDesc()+"\")' title='Drilldown'>"+m.getParent().getName()+"</a>";
					
				m.setAction(s);
				
				if(balaceSheetPList.get(i).equals(m.getParent().getKey())){
					balanceSheetList.get(i).setAction(s);
					balanceSheetList.get(i).setDebitBal(m.getDebitBal());
					balanceSheetList.get(i).setCreditVal(m.getCreditVal());
				}
				s="";
			}
		}
		//System.out.println("param4::::"+param4);
		if(param4.equals("specific")) {
			List<AccountTrialBalanceModel> filterList = new ArrayList<AccountTrialBalanceModel>();
			for(AccountTrialBalanceModel f:balanceSheetList) {
				if(f.getCreditVal() > 1 || f.getDebitBal() > 1) {
					filterList.add(f);
				}
			}
			response.setRecordsTotal(filterList.size());
			response.setRecordsFiltered(filterList.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),filterList);
			response.setData(filterList1);
		}else {
			//System.out.println("BalanceSheet:::"+balanceSheetList);
			response.setRecordsTotal(balanceSheetList.size());
			response.setRecordsFiltered(balanceSheetList.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),balanceSheetList);
			response.setData(filterList1);
		}
		
		logger.info("Method : viewaBalanceSheet through ajax ends");
		return response;
	}
	
	/************************* PROFIT LOSS SHEET ***************************/
	
	/*
	 * get mapping for viewPLSheet
	 */
	@GetMapping("/view-pl-sheet")
	public String viewPLSheet(Model model, HttpSession session) {

		logger.info("Method : viewPLSheet starts");
		// for get cost center list
		try {
			String costCenter = (String)session.getAttribute("costcenter");
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			
			if(costCenter!=null) {
				model.addAttribute("costCenter",costCenter);
			}else {
				model.addAttribute("costCenter",null);
			}
			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewPLSheet ends");
		return "account/pl-sheet";
	}

	/*
	 * view-pl-sheet-through-ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-pl-sheet-through-ajax")
	public @ResponseBody DataTableResponse viewPLSheetAjax(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String param4) {
		
		logger.info("Method : viewPLSheet through ajax starts"); 
		
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		session.setAttribute("costcenter", param3);
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-view", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		
		List<AccountTrialBalanceModel> plSheetRep = new ArrayList<AccountTrialBalanceModel>();
		List<String> balaceSheetPList = new ArrayList<String>();
		balaceSheetPList.add("GN0001");
		balaceSheetPList.add("GN0002");
		balaceSheetPList.add("GN0003");
		List<String> plSheetist = new ArrayList<String>();
		Map  staticBalanceSh = new HashMap<String,String>();
		try {

			DropDownModel[] x = restClient.getForObject(env.getAccountUrl() + "get-all-accHeads",DropDownModel[].class);
			for(DropDownModel dm: x) {
				staticBalanceSh.put(dm.getKey(), dm.getName());
				if(!balaceSheetPList.contains(dm.getKey())) {
					plSheetist.add(dm.getKey());
				}
			}
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}		
		
		for(int i = 0;i<plSheetist.size();i++) {
			AccountTrialBalanceModel model = new AccountTrialBalanceModel();
			model.setAccountHeadId(plSheetist.get(i));
			model.setDesc((String) staticBalanceSh.get(plSheetist.get(i)));
			model.setDebitBal(0.00);
			model.setCreditVal(0.00);
			String s= "<a href='javascript:void(0)'  title='Drilldown'>"+model.getDesc()+"</a>";
			model.setAction(s);
			model.setSlNo(i+1);
			plSheetRep.add(model);
		}
		
		String s="";
		
		for(int i = 0;i<plSheetist.size();i++) {
			for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
				byte[] encodeId = Base64.getEncoder().encode(m.getDesc().getBytes()); //desc contains accountgroup id
				
					s= "<a href='javascript:void(0)' onclick='showTrialModal(\""+m.getParentSeqList()+"\",\""+m.getDesc()+"\")' title='Drilldown'>"+m.getParent().getName()+"</a>";
					
				m.setAction(s);
				
				if(plSheetist.get(i).equals(m.getParent().getKey())){
					plSheetRep.get(i).setAction(s);
					plSheetRep.get(i).setDebitBal(m.getDebitBal());
					plSheetRep.get(i).setCreditVal(m.getCreditVal());
				}
				s="";
			}
		}
		if(param4.equals("specific")) {
			List<AccountTrialBalanceModel> filterList = new ArrayList<AccountTrialBalanceModel>();
			for(AccountTrialBalanceModel f:plSheetRep) {
				if(f.getCreditVal() > 1 || f.getDebitBal() > 1) {
					filterList.add(f);
				}
			}
			response.setRecordsTotal(filterList.size());
			response.setRecordsFiltered(filterList.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),filterList);
			response.setData(filterList1);
		}else {
			//System.out.println("viewPLSheet:::"+plSheetRep);
			response.setRecordsTotal(plSheetRep.size());
			response.setRecordsFiltered(plSheetRep.size());
			response.setDraw(Integer.parseInt(draw));
			List<AccountTrialBalanceModel> filterList1 = new ArrayList<AccountTrialBalanceModel>();
			filterList1 = getFilteredList(Integer.parseInt(start),Integer.parseInt(length),plSheetRep);
			response.setData(filterList1);
		}
		
		logger.info("Method : viewPLSheet through ajax ends");
		return response;
	}

	/******************************************************************************************************************************************************************/
	/*
	 * view trial balance details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-detls")
	public @ResponseBody DataTableResponse viewTrialBalanceAjax(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		session.setAttribute("costcenter", param3);
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		//String start = request.getParameter("start");
		//String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		//tableRequest.setStart(Integer.parseInt(start));
		//tableRequest.setLength(Integer.parseInt(length));
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-detls", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		Integer i=1;
		String s="";
		System.out.println("@@accountTrialBalance: "+accountTrialBalanceModel);
		
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		//response.setDraw(Integer.parseInt(draw));
		response.setData(accountTrialBalanceModel);
		
		return response;
	}
	
	/*******************************************************************************************************************************************/
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-account-group")
	public String viewaAccountGroupDrilldown(Model model, HttpSession session,@RequestParam("accountGroup")String param1,
			@RequestParam("fromdate")String param2,@RequestParam("fromdate")String param3,
			@RequestParam("costcenter")String param4) {
		
		logger.info("Method : viewaBalanceSheet starts");
		String accountGroup= new String(Base64.getDecoder().decode(param1.getBytes()));
		String fromDate = new String(Base64.getDecoder().decode(param2.getBytes()));
		String toDate = new String(Base64.getDecoder().decode(param3.getBytes()));
		String costcenter = new String(Base64.getDecoder().decode(param4.getBytes()));
		try {
			
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);
			model.addAttribute("costCenterList", costCenterList);
			
			DropDownModel[] subGrp = restClient.getForObject(env.getAccountUrl() + "get-account-subgroups",
					DropDownModel[].class);
			List<DropDownModel> subGroupListList = Arrays.asList(subGrp);
			model.addAttribute("SubgroupList", subGroupListList);
			
			model.addAttribute("drillup","/account/view-trial-balance-report");
			model.addAttribute("fromDate",fromDate);
			model.addAttribute("toDate",toDate);
			model.addAttribute("costcenter",costcenter );
			session.setAttribute("accountGroupS", accountGroup);
		}catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewaBalanceSheet ends");
		return "account/trialbalanceaccountgroup";
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-trial-balance-report-account-group-ajax")
	public @ResponseBody DataTableResponse viewaBalanceSheetSubgroup(HttpSession session,HttpServletRequest request,@RequestParam("param1") String encodedParam1,
			@RequestParam("param2") String encodedParam2,@RequestParam("param3") String encodedParam3,@RequestParam("param4") String encodedParam4) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		byte[] encodeByte4 = Base64.getDecoder().decode(encodedParam4.getBytes());
		
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		String param4 = (new String(encodeByte4));
		String accountGroup = (String)session.getAttribute("accountGroupS");
		
		DataTableRequest tableRequest = new DataTableRequest();
		DataTableResponse response = new DataTableResponse();
		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		
		String start = request.getParameter("start");
		String length = request.getParameter("length");
		String draw = request.getParameter("draw");
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(accountGroup);
		tableRequest.setStart(Integer.parseInt(start));
		tableRequest.setLength(Integer.parseInt(length));
		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "all-trail-balance-report-accountgroup", tableRequest,JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {});
		Integer i=1;
		String s="";
		for(AccountTrialBalanceModel m:accountTrialBalanceModel) {
			m.setSlNo(i);
			//System.out.println("here aa gya ");
			byte[] encodeId = Base64.getEncoder().encode(m.getCostCenter().getBytes());
			s= "<a href='/account/view-trial-balance-report-account-group?accountGroup="+new String(encodeId)+"' title='Drilldown'>"+m.getCostCenter()+"</a>";
			m.setAction(s);
			s="";
			i++;
		}
		response.setRecordsTotal(jsonResponse.getTotal());
		response.setRecordsFiltered(jsonResponse.getTotal());
		response.setDraw(Integer.parseInt(draw));
		response.setData(accountTrialBalanceModel);
		
		return response;
	}
	
	
	/*
	 * Generate Pdf For trial balance
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-trial-balance-download-report" })
	public String generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {

		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		double totalDebit = 0;
		double totalCredit = 0;
		for (AccountTrialBalanceModel a : accountTrialBalanceModel) {
			double creditBal = 0;
			double debitBal = 0;

			if (a.getCreditVal() != null) {
				creditBal = a.getCreditVal();
			}
			if (a.getDebitBal() != null) {
				debitBal = a.getDebitBal();
			}

			double total = creditBal - debitBal;
			totalDebit = totalDebit + debitBal;
			totalCredit = totalCredit + creditBal;
			if (total > 0) {
				a.setCreditVal(total);
				a.setDebitBal(null);
			} else if (total < 0) {
				a.setCreditVal(null);
				a.setDebitBal(Math.abs(total));
			}
		}
		data.put("accountTrialBalanceModel", accountTrialBalanceModel);

		data.put("totalDebit", totalDebit);

		data.put("totalCredit", totalCredit);
		data.put("fromDate", param1);
		data.put("toDate", param2);
		
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);

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
			DropDownModel[] logo = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		/*response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=TrialBalance.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("account/trial-balance-report", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		model.addAttribute("param1",param1);
		model.addAttribute("param2",param2);
		model.addAttribute("param3",param3);
		
		return "account/trialPdf";
	}

	/*
	 * get mapping for view trial balance total
	 */
	@GetMapping("/view-trial-balance-total-report")
	public String viewTrialBalanceTotal(Model model, HttpSession session) {

		logger.info("Method : viewTrialBalanceTotal starts");
		// for get cost center list
		try {
			DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() + "getCostCenterTB",
					DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(payMode);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewTrialBalanceTotal ends");
		return "account/trial-balance-total-search";
	}

	/*
	 * Generate Pdf For total balance sheet
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-trial-balance-total-download-report" })
	public void generateTrialBalanceTotalPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		logger.info("Method : generateTrialBalanceTotalPdf starts");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());
		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<List<AccountTrialBalanceModel>> jsonResponse = new JsonResponse<List<AccountTrialBalanceModel>>();
		DataTableRequest tableRequest = new DataTableRequest();

		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		try {

			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AccountTrialBalanceModel> accountTrialBalanceModel = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AccountTrialBalanceModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String pdfCurrentDate = sdf.format(date);
		data.put("pdfCurrentDate", pdfCurrentDate);
		double totalDebit = 0;
		double totalCredit = 0;
		for (AccountTrialBalanceModel a : accountTrialBalanceModel) {
			double creditBal = 0;
			double debitBal = 0;

			if (a.getCreditVal() != null) {
				creditBal = a.getCreditVal();
			}
			if (a.getDebitBal() != null) {
				debitBal = a.getDebitBal();
			}

			totalCredit = totalCredit + creditBal;
			totalDebit = totalDebit + debitBal;
		}
		data.put("accountTrialBalanceModel", accountTrialBalanceModel);

		data.put("totalDebit", totalDebit);

		data.put("totalCredit", totalCredit);
		data.put("fromDate", param1);
		data.put("toDate", param2);
		/**
		 * get Hotel Logo Background
		 *
		 */
		List<DropDownModel> logoBgList = new ArrayList<DropDownModel>();
		try {
			DropDownModel[] logoBg = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "background-Logo",
					DropDownModel[].class);
			logoBgList = Arrays.asList(logoBg);

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
			DropDownModel[] logo = restClient.getForObject(
					env.getAccountUrl() + "restLogoImage-TrialBalance?logoType=" + "header-Logo",
					DropDownModel[].class);
			logoList = Arrays.asList(logo);

			data.put("logoList", logoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String variable = env.getBaseUrlPath();
		String background = logoBgList.get(0).getName();
		String logo = logoList.get(0).getName();
		data.put("image", variable + "document/hotel/" + background + "");
		data.put("logoImage", variable + "document/hotel/" + logo + "");
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=TrialBalanceTotal.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("account/trial-balance-total-report", data);
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
		logger.info("Method : generateTrialBalanceTotalPdf  ends");
	}

	/*
	 * Get Mapping for excel view in Trial balance totals
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-trial-balance-total")
	public void downloadExcelTrailBalanceTotal(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedParam3) {

		logger.info("Method : downloadExcelTrailBalanceTotal start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountTrialBalanceModel> trialBalance = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountTrialBalanceModel>>() {
					});
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet realSheet = workbook.createSheet("trial");
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);
			style.setFont(font);

			realSheet.setDefaultColumnWidth(12);
			XSSFRow row = realSheet.createRow(0);
			XSSFCell cell = row.createCell(0);

			row.getCell(0).setCellStyle(style);
			cell.setCellValue("Sl No.");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("ACCOUNT Name");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("DEBIT");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("CREDIT");
			double totalDebit = 0;
			double totalCredit = 0;
			int i = 1;
			int j = 1;
			int count = 2;
			for (AccountTrialBalanceModel m : trialBalance) {
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getAccountHeadId());

				cell = row.createCell(2);
				if (m.getDebitBal() != null)
					cell.setCellValue(m.getDebitBal());

				cell = row.createCell(3);
				if (m.getCreditVal() != null)
					cell.setCellValue(m.getCreditVal());

				count = count + 1;
				double creditBal = 0;
				double debitBal = 0;

				if (m.getCreditVal() != null) {
					creditBal = m.getCreditVal();
				}
				if (m.getDebitBal() != null) {
					debitBal = m.getDebitBal();
				}

				totalCredit = totalCredit + creditBal;
				totalDebit = totalDebit + debitBal;
			}

			row = realSheet.createRow(count);
			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Total");
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue(totalDebit);
			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue(totalCredit);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
			workbook.write((OutputStream) servResponse.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelTrailBalanceTotal -> downloadExcel GET", e);
		}
		logger.info("Method : downloadExcelTrailBalanceTotal ends");

	}
	
	/*
	 * Get Mapping for excel view in Trial balance 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("download-excel-trial-balance")
	public void downloadExcelTrailBalance(HttpServletResponse servResponse, HttpSession session,
			@RequestParam("param1") String encodedPraram1, @RequestParam("param2") String encodedPraram2,
			@RequestParam("param3") String encodedParam3) {

		logger.info("Method : downloadExcelTrailBalanceTotal start");
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedPraram1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedPraram2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);

		try {
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getAllTrialBalanceReport", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountTrialBalanceModel> trialBalance = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountTrialBalanceModel>>() {
					});
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet realSheet = workbook.createSheet("trial");
			CellStyle style = workbook.createCellStyle();
			Font font = workbook.createFont();

			font.setBold(true);
			font.setColor(HSSFColor.RED.index);
			style.setFont(font);

			realSheet.setDefaultColumnWidth(12);
			XSSFRow row = realSheet.createRow(0);
			XSSFCell cell = row.createCell(0);

			row.getCell(0).setCellStyle(style);
			cell.setCellValue("Sl No.");

			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("ACCOUNT Name");

			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue("DEBIT");

			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue("CREDIT");
			double totalDebit = 0;
			double totalCredit = 0;
			int i = 1;
			int j = 1;
			int count = 2;
			for (AccountTrialBalanceModel m : trialBalance) {
				
				double creditBal = 0;
				double debitBal = 0;

				if (m.getCreditVal() != null) {
					creditBal = m.getCreditVal();
				}
				if (m.getDebitBal() != null) {
					debitBal = m.getDebitBal();
				}

				double total = creditBal - debitBal;
				totalDebit = totalDebit + debitBal;
				totalCredit = totalCredit + creditBal;
				if (total > 0) {
					m.setCreditVal(total);
					m.setDebitBal(null);
				} else if (total < 0) {
					m.setCreditVal(m.getCreditVal());
					m.setDebitBal(m.getDebitBal());
				}else {
					m.setCreditVal(m.getCreditVal());
					m.setCreditVal(total);
				}
				row = realSheet.createRow(i++);

				cell = row.createCell(0);
				cell.setCellValue(j++);

				cell = row.createCell(1);
				cell.setCellValue(m.getAccountHeadId());

				cell = row.createCell(2);
				if (total<0)
					cell.setCellValue(m.getDebitBal());

				cell = row.createCell(3);
				if (total>0)
					cell.setCellValue(m.getCreditVal());

				count = count + 1;
				
			}

			row = realSheet.createRow(count);
			cell = row.createCell(1);
			row.getCell(1).setCellStyle(style);
			cell.setCellValue("Total");
			cell = row.createCell(3);
			row.getCell(3).setCellStyle(style);
			cell.setCellValue(totalDebit);
			cell = row.createCell(2);
			row.getCell(2).setCellStyle(style);
			cell.setCellValue(totalCredit);
			servResponse.setContentType("application/ms-excel");
			servResponse.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
			workbook.write((OutputStream) servResponse.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadExcelTrailBalanceTotal -> downloadExcel GET", e);
		}
		logger.info("Method : downloadExcelTrailBalanceTotal ends");

	}
	
}
