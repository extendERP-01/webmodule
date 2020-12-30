package nirmalya.aathithya.webmodule.asset.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.asset.model.AssetMaintenanceHistoryModel;
import nirmalya.aathithya.webmodule.asset.model.AssetPolicyMaster;
import nirmalya.aathithya.webmodule.asset.model.ItemAssetModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssetMaintenancePolicyHistoryController {

	Logger logger = LoggerFactory.getLogger(AssetMaintenancePolicyHistoryController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Asset Maintenance Policy History' page
	 *
	 */
	@GetMapping("/add-asset-maintenance-policy-history")
	public String assetMaintenancePolicyHistory(Model model, HttpSession session) {
		logger.info("Method : assetMaintenancePolicyHistory starts");
		
		logger.info("Method : assetMaintenancePolicyHistory ends");
		return "asset/add-asset-maintenance-policy-history";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-asset-maintenance-policy-history-get-asset-code" })
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
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-asset-maintenance-policy-history-get-maintenace-details" })
	public @ResponseBody JsonResponse<List<AssetPolicyMaster>> getMaintenanceDetails(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getMaintenanceDetails starts");
		
		JsonResponse<List<AssetPolicyMaster>> res = new JsonResponse<List<AssetPolicyMaster>>();
		
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getMaintenanceDetailsByAsset?id=" + searchValue,
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
		logger.info("Method : getMaintenanceDetails ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-asset-maintenance-policy-history-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addMaintenanceHistory(@RequestBody AssetMaintenanceHistoryModel mainHistory,
			Model model, HttpSession session) {
		logger.info("Method : addMaintenanceHistory function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {
			mainHistory.setCreatedBy(userId);
			System.out.println(mainHistory);
			res = restClient.postForObject(env.getAssetUrl() + "addMaintenanceHistory", mainHistory, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addMaintenanceHistory function Ends");
		return res;
	}
}
