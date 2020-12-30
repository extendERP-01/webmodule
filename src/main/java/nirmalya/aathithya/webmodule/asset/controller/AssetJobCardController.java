package nirmalya.aathithya.webmodule.asset.controller;

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

import nirmalya.aathithya.webmodule.asset.model.AssetJobCardModel;
import nirmalya.aathithya.webmodule.asset.model.ItemAssetModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssetJobCardController {

	Logger logger = LoggerFactory.getLogger(AssetJobCardController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Asset Vehicle Assign' page
	 *
	 */
	@GetMapping("/add-job-card") 
	public String addJobCard(Model model, HttpSession session) {
		logger.info("Method : addJobCard starts");

		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl() + "getServices", DropDownModel[].class);
			List<DropDownModel> serviceTypeLits = Arrays.asList(dd);
			model.addAttribute("serviceTypeLits", serviceTypeLits);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addJobCard ends");
		return "asset/add-job-card";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-job-card-get-mechanic" })
	public @ResponseBody JsonResponse<DropDownModel> getMechanicAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getMechanicAutoSearch starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getMechanicAutoSearch?id=" + searchValue,
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
		logger.info("Method : getMechanicAutoSearch ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-job-card-get-asstMechanic" })
	public @ResponseBody JsonResponse<DropDownModel> getAsstMechanicAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAsstMechanicAutoSearch starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getAsstMechanicAutoSearch?id=" + searchValue,
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
		logger.info("Method : getAsstMechanicAutoSearch ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-job-card-get-vehicle" })
	public @ResponseBody JsonResponse<ItemAssetModel> getVehicleDetailsForAssign(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getVehicleDetailsForAssign starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();
		try {
			res = restClient.postForObject(env.getAssetUrl() + "getVehicleDetailsForJobCard", searchValue,
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

		logger.info("Method : getVehicleDetailsForAssign ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-job-card-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> assignAssetToVehicle(@RequestBody List<AssetJobCardModel> jobCardDetails,
			Model model, HttpSession session) {
		logger.info("Method : assignAssetToVehicle function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (AssetJobCardModel a : jobCardDetails) {
				a.setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getAssetUrl() + "addJobCard", jobCardDetails, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : assignAssetToVehicle function Ends");
		return res;
	}

	/**
	 * Default 'View Assigned Asset To Vehicle' page
	 *
	 */
	@GetMapping("/view-job-card")
	public String viewJobCard(Model model, HttpSession session) {
		logger.info("Method : viewJobCard starts");

		logger.info("Method : viewJobCard ends");
		return "asset/view-job-card";
	} 

	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-card-through-ajax")
	public @ResponseBody DataTableResponse viewJobDetailsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5) {
		logger.info("Method : viewJobDetailsThroughAjax starts");

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
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);

			JsonResponse<List<AssetJobCardModel>> jsonResponse = new JsonResponse<List<AssetJobCardModel>>();

			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getJobCardDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssetJobCardModel> assignedAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetJobCardModel>>() {
					});

			String s = "";

			for (AssetJobCardModel m : assignedAsset) {

				if (m.getEntryDate() == null || m.getEntryDate() == "") {
					m.setEntryDate("- -");
				}
				if (m.getMechanic() == null || m.getMechanic() == "") {
					m.setMechanic("N/A");
				}

				byte[] id = Base64.getEncoder().encode(m.getJobbCardId().getBytes());
				byte[] aId = Base64.getEncoder().encode(m.getVehicle().getBytes());
				s = s + "<a href='view-job-card-edit?id=" + new String(id)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"

						+ "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInModel(\"" + new String(id)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						+ "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInIssueItem(\"" + new String(id) + "," + new String(aId)
						+ "\")'><i class='fa fa-history' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";
				if (m.getJobStatus()) {
					s = s + "<a href='javascript:void(0)'"
							+ "' ><i class=\"fa fa-check-circle\" title=\"Complete\" style=\"font-size:24px\"></i></a>";
				} else {
					s = s + "<a href='javascript:void(0)'"
							+ "' ><i class=\"fa fa-times-circle\" title=\"Pending\" style=\"font-size:24px ; color:red;\"></i></a>";
				}
				m.setAtion(s);
				s = "";
				if (m.getJobStatus()) {
					m.setJobStatusName("Complete");
				} else {
					m.setJobStatusName("Pending");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignedAsset);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewJobDetailsThroughAjax ends");
		return response;
	}

	@GetMapping("view-job-card-edit")
	public String editJobCard(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editJobCard starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {

			AssetJobCardModel[] asset = restClient.getForObject(env.getAssetUrl() + "editJobCard?id=" + id,
					AssetJobCardModel[].class);
			List<AssetJobCardModel> assetList = Arrays.asList(asset);
			
			try {
				ItemAssetModel[] cardDetails = restClient.getForObject(env.getAssetUrl() + "issueItemDetailsForJobCard?id=" + assetList.get(0).getJobbCardId() + "&vehicle=" + assetList.get(0).getVehicle(),
						ItemAssetModel[].class);
				List<ItemAssetModel> cardDetailsList = Arrays.asList(cardDetails);
				
				model.addAttribute("cardDetailsList", cardDetailsList);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			model.addAttribute("Edit", assetList.get(0).getJobbCardId());
			model.addAttribute("jobCard", assetList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl() + "getServices", DropDownModel[].class);
			List<DropDownModel> serviceTypeLits = Arrays.asList(dd);
			model.addAttribute("serviceTypeLits", serviceTypeLits);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : editJobCard starts");
		return "asset/add-job-card";
	}

	/**
	 * For Modal gym transaction
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-job-card-modalview" })
	public @ResponseBody JsonResponse<List<AssetJobCardModel>> modalAssignment(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :view-job-card-modalview starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id1 = (new String(decodeId));
		JsonResponse<List<AssetJobCardModel>> response = new JsonResponse<List<AssetJobCardModel>>();
		try {
			response = restClient.getForObject(env.getAssetUrl() + "getJobCardModal?id=" + id1, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : view-job-card-modalview  ends ");
		return response;
	}

	/**
	 * job status change
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-job-card-change-status" })
	public @ResponseBody JsonResponse<Object> changeInvoiceStatus(Model model,
			@RequestBody AssetJobCardModel assetJobCardModel, BindingResult result, HttpSession session) {

		logger.info("Method : change status starts");

		byte[] encodeByte = Base64.getDecoder().decode(assetJobCardModel.getJobbCardId().getBytes());
		String index = (new String(encodeByte));

		Boolean status = assetJobCardModel.getJobStatus();
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(env.getAssetUrl() + "changeJobCardStatus?id=" + index + "&status=" + status
					+ "&createdBy=" + userId, JsonResponse.class);

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
		logger.info("Method : change status  ends");

		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-job-card-issue-modal" })
	public @ResponseBody JsonResponse<List<ItemAssetModel>> issuedItemModal(Model model, @RequestBody DropDownModel index,
			BindingResult result) {
		logger.info("Method : issuedItemModal starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getKey().getBytes());
		String id1 = (new String(decodeId));
		
		byte[] decodeId1 = Base64.getDecoder().decode(index.getName().getBytes());
		String asset = (new String(decodeId1));
		
		JsonResponse<List<ItemAssetModel>> response = new JsonResponse<List<ItemAssetModel>>();
		try {
			response = restClient.getForObject(env.getAssetUrl() + "getIssuedItemModalForJobCard?id=" + id1 + "&asset="+asset, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : issuedItemModal  ends ");
		return response;
	}

}
