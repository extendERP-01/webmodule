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

import nirmalya.aathithya.webmodule.asset.model.AssetVehicleModel;
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
public class AssetVehicleAssignController {

	Logger logger = LoggerFactory.getLogger(AssetVehicleAssignController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Asset Vehicle Assign' page
	 *
	 */
	@GetMapping("/assign-asset-to-vehicle")
	public String assignVehicleAsset(Model model, HttpSession session) {
		logger.info("Method : assignVehicleAsset starts");
		
		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl()+"getStoreForAssign", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : assignVehicleAsset ends");
		return "asset/assign-asset-to-vehicle";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-asset-to-vehicle-get-vehicle" })
	public @ResponseBody JsonResponse<ItemAssetModel> getVehicleDetailsForAssign(Model model, @RequestBody DropDownModel searchValue,
			BindingResult result) {
		logger.info("Method : getVehicleDetailsForAssign starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();
		try {
			res = restClient.postForObject(env.getAssetUrl() + "getVehicleDetailsForAssign", searchValue,
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
	@PostMapping(value = { "/assign-asset-to-vehicle-get-asset" })
	public @ResponseBody JsonResponse<ItemAssetModel> getAssetForAssign(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAssetForAssign starts");

		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getAssetForAssign?id=" + searchValue,
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

		logger.info("Method : getAssetForAssign ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "assign-asset-to-vehicle-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> assignAssetToVehicle(@RequestBody List<AssetVehicleModel> assignedAsset,
			Model model, HttpSession session) {
		logger.info("Method : assignAssetToVehicle function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < assignedAsset.size(); i++) {
				assignedAsset.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getAssetUrl() + "assignAssetToVehicle", assignedAsset, JsonResponse.class);
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
	@GetMapping("/view-assigned-asset-to-vehicle")
	public String viewAssignedAssetToVehicle(Model model, HttpSession session) {
		logger.info("Method : viewAssignedAssetToVehicle starts");

		logger.info("Method : viewAssignedAssetToVehicle ends");
		return "asset/view-assigned-asset-to-vehicle";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-assigned-asset-to-vehicle-through-ajax")
	public @ResponseBody DataTableResponse viewAssignedAssetThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, @RequestParam String param4,
			@RequestParam String param5) {
		logger.info("Method : viewAssignedAssetThroughAjax starts");

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

			JsonResponse<List<AssetVehicleModel>> jsonResponse = new JsonResponse<List<AssetVehicleModel>>();

			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getAssignedAssetVehicleDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssetVehicleModel> assignedAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetVehicleModel>>() {
					});

			String s = "";

			for (AssetVehicleModel m : assignedAsset) {
				
				if(m.getRemoveDate()==null || m.getRemoveDate()=="") {
					m.setRemoveDate("- -");
				}
				
				if(m.getAssignStatus()) {
					m.setStatus("Free");
				} else {
					m.setStatus("Assigned");
				}
				
				if(m.getAssignType()) {
					m.setType("Reserved");
				} else {
					m.setType("Running");
				}
				byte[] id = Base64.getEncoder().encode(m.getAssetId().getBytes());
				byte[] vId = Base64.getEncoder().encode(m.getVehicleAssetId().getBytes());
				s = s + "<a href='edit-assigned-asset-to-vehicle?id=" + new String(id) + "&vehicleAsset=" + new String(vId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;";
				m.setAction(s);
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
	
	@GetMapping("edit-assigned-asset-to-vehicle")
	public String editAssignedAssetVehicle(Model model, @RequestParam("id") String index, @RequestParam("vehicleAsset") String index1, HttpSession session) {
		logger.info("Method : editAssignedAssetVehicle starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		
		byte[] encodeByte1 = Base64.getDecoder().decode(index1.getBytes());
		String vehicleAsset = (new String(encodeByte1));

		try {

			AssetVehicleModel[] asset = restClient.getForObject(env.getAssetUrl() + "editAssignedAsset?id=" + id + "&vehicleAsset=" + vehicleAsset,
					AssetVehicleModel[].class);
			List<AssetVehicleModel> assetList = Arrays.asList(asset);
//			model.addAttribute("isEdit", assetList.get(0).getIsEdit());
			model.addAttribute("assignedAsset", assetList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editAssignedAssetVehicle starts");
		return "asset/assign-asset-to-vehicle";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-asset-to-vehicle-get-assigned-asset" })
	public @ResponseBody JsonResponse<AssetVehicleModel> getAssignedAssetForView(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getAssetForAssign starts");

		JsonResponse<AssetVehicleModel> res = new JsonResponse<AssetVehicleModel>();
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getAssignedAssetForView?id=" + searchValue,
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

		logger.info("Method : getAssetForAssign ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/assign-asset-to-vehicle-change-status" })
	public @ResponseBody JsonResponse<Object> changeAssignStatus(Model model, @RequestBody AssetVehicleModel searchValue,
			BindingResult result) {
		logger.info("Method : changeAssignStatus starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.postForObject(env.getAssetUrl() + "changeAssignStatus", searchValue,
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

		logger.info("Method : changeAssignStatus ends");
		return res;
	}
}
