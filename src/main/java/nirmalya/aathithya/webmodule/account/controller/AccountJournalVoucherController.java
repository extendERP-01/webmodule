/**
 * 
 */
package nirmalya.aathithya.webmodule.account.controller;

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

import nirmalya.aathithya.webmodule.account.model.AccountJournalVoucherModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/*
 * 
 *  @author NirmalyaLabs
 * 
 */
@Controller
@RequestMapping(value = { "account" })
public class AccountJournalVoucherController {
	Logger logger = LoggerFactory.getLogger(AccountJournalVoucherController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-journal-voucher-payment-getAccountSubGroup" })
	public @ResponseBody JsonResponse<AccountJournalVoucherModel> getSubgroup(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getSubgroups starts");

		JsonResponse<AccountJournalVoucherModel> res = new JsonResponse<AccountJournalVoucherModel>();

		try {
			res = restTemplate.getForObject(
					environmentVaribles.getAccountUrl() + "getinvsubGroupPayment?id=" + searchValue,
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

		logger.info("Method : getSubgroup ends");
		return res;
	}

	/*
	 * 
	 * GetMapping to get Table Name onChange Of SPA Name
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-spa-tabla-staff-assign-getStaffName-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getViewSpaTableName(Model model, @RequestBody String spaName,
			BindingResult result) {
		logger.info("Method : getViewSpaTableName starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getAccountUrl() + "search-staff-name?id=" + spaName,
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
		logger.info("Method : getViewSpaTableName ends");
		return res;
	}

	/*
	 * 
	 * GetMapping for add SpaTAble Assign
	 * 
	 */
	@SuppressWarnings({ "unused" })
	@GetMapping(value = { "add-journal-voucher" })
	public String getSubGroup(Model model, HttpSession session) {
		logger.info("Method : getSubGroup starts");
		AccountJournalVoucherModel invJournalVoucherPaymentModel = new AccountJournalVoucherModel();
		ObjectMapper mapper = new ObjectMapper();

		try {
			invJournalVoucherPaymentModel = (AccountJournalVoucherModel) session
					.getAttribute("requisitionIssueNote");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restTemplate
					.getForObject(environmentVaribles.getAccountUrl() + "restGetCostCenterList", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		model.addAttribute("invJournalVoucherPaymentModel", invJournalVoucherPaymentModel);
		logger.info("Method : getSubGroup ends");
		return "account/addInvJournalVoucher";
	}
	/*
	 * post Mapping for add SPA table staff assign
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-journal-voucher", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> saveJournalVoucher(
			@RequestBody List<AccountJournalVoucherModel> journalVoucherModel, Model model, HttpSession session) {
		logger.info("Method : saveJournalVoucher function starts");
		System.out.println(journalVoucherModel + "journalVoucherModel");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		journalVoucherModel.get(0).setCreatedBy(userId);
		try {

			res = restTemplate.postForObject(environmentVaribles.getAccountUrl() + "saveJournalVoucher",
					journalVoucherModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : saveJournalVoucher function Ends");
		return res;
	}

	/*
	 * 
	 * view SPA Table Assign Staff
	 * 
	 */
	@GetMapping(value = { "view-journal-voucher" })
	public String viewJournalVoucher(Model model) {
		logger.info("Method : viewJournalVoucher starts");
		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restTemplate
					.getForObject(environmentVaribles.getAccountUrl() + "restGetCostCenterList", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewJournalVoucher ends");
		return "account/viewJournalVoucher";
	}

	/**
	 * For view journal voucher for dataTable Ajax call
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-journal-voucher-ThroughAjax")
	public @ResponseBody DataTableResponse viewJournalVoucherThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewJournalVoucherThroughAjax statrs");

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
			JsonResponse<List<AccountJournalVoucherModel>> jsonResponse = new JsonResponse<List<AccountJournalVoucherModel>>();

			jsonResponse = restTemplate.postForObject(environmentVaribles.getAccountUrl() + "getAllJournalVoucher",
					tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AccountJournalVoucherModel> accountJournalVoucherModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountJournalVoucherModel>>() {
					});

			String s = "";

			for (AccountJournalVoucherModel m : accountJournalVoucherModel) {

				byte[] encodeId = Base64.getEncoder().encode(m.getJournalVoucher().getBytes());
				s = s + "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if(m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if(m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if(m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(accountJournalVoucherModel);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method :viewJournalVoucherThroughAjax   ends");

		return response;
	}
	/*
	 * post Mapping for viewInModelData
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-journal-voucher-inModel" })
	public @ResponseBody JsonResponse<AccountJournalVoucherModel> modalView(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalView starts");
		byte[] pId = Base64.getDecoder().decode(index.getBytes());	
		String id = (new String(pId));
		JsonResponse<AccountJournalVoucherModel> res = new JsonResponse<AccountJournalVoucherModel>();

		try {
			res = restTemplate.getForObject(environmentVaribles.getAccountUrl() + "view-journal-voucher-inModel?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalView ends");
		return res;
	}

	/*
	 * 
	 * GetMapping For Journal Voucher  approve by approver
	 * 
	 * 
	 */
	@GetMapping(value = { "view-journal-voucher-approval" })
	public String viewJVApproval(Model model) {
		logger.info("Method : viewJVApproval starts");
		/**
		 * get DropDown value for Cost Center
		 *
		 */

		try {
			DropDownModel[] costCenter = restTemplate
					.getForObject(environmentVaribles.getAccountUrl() + "restGetCostCenterList", DropDownModel[].class);
			List<DropDownModel> costCenterList = Arrays.asList(costCenter);

			model.addAttribute("costCenterList", costCenterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewJVApproval ends");
		return "account/approveJournalVoucher";
	}
	
	
	/*
	 * view  Item throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-journal-voucher-approval-throughAjax" })
	public @ResponseBody DataTableResponse viewJVApprovalListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, HttpSession session) {
		logger.info("Method : viewJVApprovalListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String UserId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setUserId(UserId);
			JsonResponse<List<AccountJournalVoucherModel>> jsonResponse = new JsonResponse<List<AccountJournalVoucherModel>>();
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getAccountUrl() + "get-all-JV-approve", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<AccountJournalVoucherModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountJournalVoucherModel>>() {
					});
			String s = "";
		
			for (AccountJournalVoucherModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getJournalVoucher().getBytes());
				s = "";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
					
				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {
					if(m.getApproveStatus() != 3){
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardJournalVoucher(\""
						+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					}else{
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectJournalVoucher(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectJournalVoucher(\""
						+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectJournalVoucher(\""
						+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				}
				
				m.setAction(s);
				s = "";

				if(m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if(m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if(m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewJVApprovalListThroughAjax ends");
		return response;
	}
	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-journal-voucher-approval-forward-action" })
	public @ResponseBody JsonResponse<Object> saveJVApprovalAction(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {
		logger.info("Method : saveJVApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		};
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restTemplate.getForObject(environmentVaribles.getAccountUrl() + "save-JV-approval-action?id=" + id+"&createdBy="+userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		System.out.println("message=="+resp.getMessage());
		logger.info("Method : saveJVApprovalAction ends");
		return resp;
	}
	
	/*
	 * Reject Journal Voucher 
	 * 
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-journal-voucher-approval-reject-action" })
	public @ResponseBody JsonResponse<Object> saveJVRejectAction(Model model, @RequestBody AccountJournalVoucherModel reqobject,
			BindingResult result,HttpSession session) {
		logger.info("Method : saveJVRejectAction starts");
		System.out.println(reqobject);
		byte[] encodeByte = Base64.getDecoder().decode(reqobject.getJournalVoucher());
		String reqstnId = (new String(encodeByte));
		
		String userId = "";
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			
		};
		
		reqobject.setCreatedBy(userId);
		reqobject.setJournalVoucher(reqstnId);
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.postForObject(environmentVaribles.getAccountUrl() + "save-JV-reject-action",reqobject,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("res=="+res);
		System.out.println("res getMessage"+res.getMessage());
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : saveJVRejectAction ends");
		return res;
	}
}
