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

import nirmalya.aathithya.webmodule.asset.model.IssueConsumedItemModel;
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
public class IssueConsumedAssetController {
	
	Logger logger = LoggerFactory.getLogger(IssueConsumedAssetController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Asset Vehicle Assign' page
	 *
	 */
	@GetMapping("/issue-consumed-asset-to-vehicle")
	public String issueConsumedItem(Model model, HttpSession session) {
		logger.info("Method : issueConsumedItem starts");
		
		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl()+"getStoreForAssign", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : issueConsumedItem ends");
		return "asset/issue-consumed-asset-to-vehicle";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/issue-consumed-asset-to-vehicle-get-item" })
	public @ResponseBody JsonResponse<DropDownModel> getConsumeItemForIssue(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getConsumeItemForIssue starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getConsumeItemForIssue?id=" + searchValue,
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

		logger.info("Method : getConsumeItemForIssue ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/issue-consumed-asset-to-vehicle-get-job-card" })
	public @ResponseBody JsonResponse<DropDownModel> getJobCardForIssue(Model model, @RequestBody DropDownModel searchValue,
			BindingResult result) {
		logger.info("Method : getJobCardForIssue starts");
		
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getJobCardForIssue?id=" + searchValue.getKey() + "&jobCard=" + searchValue.getName(),
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
		
		logger.info("Method : getJobCardForIssue ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/issue-consumed-asset-to-vehicle-get-item-details" })
	public @ResponseBody JsonResponse<ItemAssetModel> getConsumeItemDetails(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getConsumeItemDetails starts");
		
		JsonResponse<ItemAssetModel> res = new JsonResponse<ItemAssetModel>();
		try {
			res = restClient.getForObject(env.getAssetUrl() + "getConsumeItemDetails?id=" + searchValue,
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
		
		logger.info("Method : getConsumeItemDetails ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "issue-consumed-asset-to-vehicle-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> issueItemToVehicle(@RequestBody List<IssueConsumedItemModel> issuedItem,
			Model model, HttpSession session) {
		logger.info("Method : issueItemToVehicle function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < issuedItem.size(); i++) {
				issuedItem.get(i).setCreatedBy(userId);
			}
			res = restClient.postForObject(env.getAssetUrl() + "issueItemToVehicle", issuedItem, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : issueItemToVehicle function Ends");
		return res;
	}
	
	@GetMapping("/view-issued-consumed-asset-to-vehicle")
	public String viewIssuedConsumedItem(Model model, HttpSession session) {
		logger.info("Method : viewIssuedConsumedItem starts");
		 
		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl()+"getStoreForAssign", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewIssuedConsumedItem ends");
		return "asset/view-issued-consumed-item";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-issued-consumed-asset-to-vehicle-through-ajax")
	public @ResponseBody DataTableResponse viewIssuedConsumedItemThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2) {
		logger.info("Method : viewIssuedConsumedItemThroughAjax starts");

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

			JsonResponse<List<IssueConsumedItemModel>> jsonResponse = new JsonResponse<List<IssueConsumedItemModel>>();

			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getIssuedConsumedItem", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<IssueConsumedItemModel> assignedAsset = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<IssueConsumedItemModel>>() {
					});

			String s = "";

			for (IssueConsumedItemModel m : assignedAsset) {
				
				byte[] id = Base64.getEncoder().encode(m.getIssueDate().getBytes());
				byte[] vId = Base64.getEncoder().encode(m.getVehicleAssetId().getBytes());
				s = s + "<a href='view-issued-consumed-asset-to-vehicle-edit?id=" + new String(id) + "&vehicleAsset=" + new String(vId)
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

		logger.info("Method : viewIssuedConsumedItemThroughAjax ends");
		return response;
	}
	
	@GetMapping("view-issued-consumed-asset-to-vehicle-edit")
	public String editIssuedItemVehicle(Model model, @RequestParam("id") String index, @RequestParam("vehicleAsset") String index1, HttpSession session) {
		logger.info("Method : editIssuedItemVehicle starts");

		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		
		byte[] encodeByte1 = Base64.getDecoder().decode(index1.getBytes());
		String vehicleAsset = (new String(encodeByte1));
		
		try {
			DropDownModel[] dd = restClient.getForObject(env.getAssetUrl()+"getStoreForAssign", DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(dd);
			model.addAttribute("storeList", storeList);
		} catch(Exception e) {
			e.printStackTrace();
		}

		try {

			IssueConsumedItemModel[] asset = restClient.getForObject(env.getAssetUrl() + "editIssuedConsumedItem?id=" + id + "&vehicleAsset=" + vehicleAsset,
					IssueConsumedItemModel[].class);
			List<IssueConsumedItemModel> assetList = Arrays.asList(asset);
			model.addAttribute("isEdit", assetList.get(0).getIsEdit());
			model.addAttribute("assignedAsset", assetList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editIssuedItemVehicle starts");
		return "asset/issue-consumed-asset-to-vehicle";
	}
}
