package nirmalya.aathithya.webmodule.asset.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
 

import nirmalya.aathithya.webmodule.asset.model.AssetPolicyMaster; 
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.controller.HrmEmployeeEducationController;
import nirmalya.aathithya.webmodule.sales.model.SaleOrderPoDetailsModel;

@Controller
@RequestMapping(value = "asset")
public class AssetPolicyMasterController {
	Logger logger = LoggerFactory.getLogger(HrmEmployeeEducationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee education
	 */
	@GetMapping("/add-policy-master")
	public String addpolicyMaster(Model model, HttpSession session) {

		logger.info("Method : addpolicyMaster starts");

		AssetPolicyMaster assetpolicy = new AssetPolicyMaster();
		AssetPolicyMaster sessionassetpolicy = (AssetPolicyMaster) session.getAttribute("assetpolicy");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionassetpolicy != null) {
			model.addAttribute("assetpolicy", sessionassetpolicy);
			session.setAttribute("assetpolicy", null);
		} else {
			model.addAttribute("assetpolicy", assetpolicy);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getAssetUrl() + "getFreqList",
					DropDownModel[].class);
			List<DropDownModel> freqList = Arrays.asList(Employee);
			model.addAttribute("freqList", freqList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addpolicyMaster ends");
		return "asset/asset-policy-master";
	}
	
	/*
	 * delivery challan getPodetails
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-policy-master-getItemDetails" })
	public @ResponseBody JsonResponse<DropDownModel> getDriverDetails(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getDriverDetails starts");
System.out.println(index);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAssetUrl() + "getItemDetailsAutoSearch?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getDriverDetails  ends");
		return res;
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-policy-master-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addServicePrice(@RequestBody List<AssetPolicyMaster> AssetPolicyMaster,
			Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addServicePrice function starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (AssetPolicyMaster r : AssetPolicyMaster) {
				r.setCreatedBy(userId);
			}

			res = restClient.postForObject(env.getAssetUrl() + "restAddPolicy", AssetPolicyMaster,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addServicePrice function Ends");
		return res;
	}
	
	
	/*
	 * add fuel consumption serviceDetailsOnItemChange
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-policy-master-serviceDetailsOnItemChange" })
	public @ResponseBody JsonResponse<AssetPolicyMaster> serviceDetailsOnItemChange(@RequestBody String itemid 
			 ) {
		logger.info("Method : serviceDetailsOnItemChange starts");

		JsonResponse<AssetPolicyMaster> res = new JsonResponse<AssetPolicyMaster>();

		try {
			res = restClient.getForObject(
					env.getAssetUrl() + "getServiceDetails?itemId=" + itemid ,
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

		logger.info("Method : serviceDetailsOnItemChange  ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-policy-master-get-option-value" })
	public @ResponseBody JsonResponse<AssetPolicyMaster> getOptionValue() {
		logger.info("Method : getOptionValue starts");

		JsonResponse<AssetPolicyMaster> res = new JsonResponse<AssetPolicyMaster>();

		try {
			res = restClient.getForObject(
					env.getAssetUrl() + "getOptionValue" ,
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

		logger.info("Method : getOptionValue  ends");
		return res;
	}

}
