package nirmalya.aathithya.webmodule.asset.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.asset.model.AssetJobCardModel;
import nirmalya.aathithya.webmodule.asset.model.AssetMaintenanceHistoryModel;
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
public class AssetDashboardController {

Logger logger = LoggerFactory.getLogger(AssetDashboardController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Asset Maintenance Policy History' page
	 *
	 */
	@GetMapping("/asset-management-dashboard")
	public String assetDashboard(Model model, HttpSession session) {
		logger.info("Method : assetDashboard starts");
		
		logger.info("Method : assetDashboard ends");
		return "asset/asset-management-dashboard";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/asset-management-dashboard-through-ajax")
	public @ResponseBody DataTableResponse viewAssignedAssetThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : viewAssignedAssetThroughAjax starts");
 
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));

			JsonResponse<List<AssetJobCardModel>> jsonResponse = new JsonResponse<List<AssetJobCardModel>>();

			jsonResponse = restClient.postForObject(env.getAssetUrl() + "dashboardJobCard", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssetJobCardModel> assignedAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetJobCardModel>>() {
					});

			String s = "";

			for (AssetJobCardModel m : assignedAsset) {
				
				if (m.getMechanic() == null || m.getMechanic() == "") {
					m.setMechanic("N/A");
				}
				
				byte[] id = Base64.getEncoder().encode(m.getJobbCardId().getBytes());
				byte[] aId = Base64.getEncoder().encode(m.getVehicle().getBytes());
				s = s + "<a href='javascript:void(0)'"
						+ "' onclick='viewInModel(\"" + new String(id)
						+ "\")' ><button class='btn btn-info'>View</button></a>&nbsp;"
						+ "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInIssueItem(\"" + new String(id) + "," + new String(aId)
						+ "\")'><button class='btn btn-success'><i class='fa fa-history' style=\"font-size:18px\"></i></button></a>&nbsp;&nbsp;";
				m.setAtion(s);
				s = "";
				
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignedAsset);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAssignedAssetThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/asset-management-dashboard-get-maintenance")
	public @ResponseBody DataTableResponse viewMaintenanceDetailsThroughAjax(Model model, HttpServletRequest request) {
		logger.info("Method : viewMaintenanceDetailsThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			
			JsonResponse<List<AssetMaintenanceHistoryModel>> jsonResponse = new JsonResponse<List<AssetMaintenanceHistoryModel>>();
			
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "dashboardMaintenance", tableRequest,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<AssetMaintenanceHistoryModel> assignedAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetMaintenanceHistoryModel>>() {
			});
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignedAsset);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewMaintenanceDetailsThroughAjax ends");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/asset-management-dashboard-modal" })
	public @ResponseBody JsonResponse<AssetJobCardModel> modalJobCardDetails(Model model,
			@RequestBody String index, BindingResult result) {
		logger.info("Method :modalJobCardDetails starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(decodeId));

		JsonResponse<AssetJobCardModel> response = new JsonResponse<AssetJobCardModel>();
		try {
			response = restClient.getForObject(env.getAssetUrl() + "getJobCardDetailsById?id=" + id,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalJobCardDetails  ends ");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/asset-management-dashboard-issue-modal" })
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
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/asset-management-dashboard-get-asset-code" })
	public @ResponseBody JsonResponse<ItemAssetModel> getAssetCodeAutoSearch(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAssetCodeAutoSearch starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getAssetCodeAutoSearch?id=" + searchValue,
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
		logger.info("Method : getAssetCodeAutoSearch ends");
		return res;
	}
}
