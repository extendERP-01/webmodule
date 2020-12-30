package nirmalya.aathithya.webmodule.asset.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.DataSetAccountTree;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemCategoryModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssetsClassificationController {

Logger logger = LoggerFactory.getLogger(AssetsClassificationController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Asset Classification' page
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/classification-of-assets")
	public String assetClassification(Model model, HttpSession session) {
		logger.info("Method : assetMaintenancePolicyHistory starts");
		
		JsonResponse<List<DataSetAccountTree>> respTblact = new JsonResponse<List<DataSetAccountTree>>();
		List<DataSetAccountTree>treeData = new ArrayList<DataSetAccountTree>();
		
		try {
			respTblact = restClient.getForObject(env.getAssetUrl() + "getAssetTreeDetails?Action=" + "getAssetTreeDetails",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblact = respTblact.getMessage();
		
		if (messageForTblact != null || messageForTblact != "") {
			model.addAttribute("message", messageForTblact);
		}
		
		ObjectMapper mapper4 = new ObjectMapper();
		
		treeData = mapper4.convertValue(respTblact.getBody(),
				new TypeReference<List<DataSetAccountTree>>() {
				});
		model.addAttribute("treeData1", treeData); 
		
		logger.info("Method : assetMaintenancePolicyHistory ends");
		return "asset/classification-of-assets";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "classification-of-assets-throughAjax" })
	public @ResponseBody DataTableResponse viewItemCategory(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewItemCategory starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			
			JsonResponse<List<InventoryItemCategoryModel>> jsonResponse = new JsonResponse<List<InventoryItemCategoryModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "get-all-item-category",
					tableRequest, JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryItemCategoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryItemCategoryModel>>() {
					});
			
			String s = "";
			for (InventoryItemCategoryModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getItmCategory().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+  new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
//				s = s + " &nbsp;&nbsp <a href='edit-item-category?id=" + new String(pId)
//						+ "' ><i class='fa fa-edit'></i></a> &nbsp;&nbsp; ";
//				s = s + "<a href='javascript:void(0)' onclick='deleteItemCategory(\"" +  new String(pId)
//						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getItmCatActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewItemCategory ends");
		return response;
	}

	/**
	 * View selected ItemCategory in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "classification-of-assets-model" })
	public @ResponseBody JsonResponse<InventoryItemCategoryModel> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		
		JsonResponse<InventoryItemCategoryModel> res = new JsonResponse<InventoryItemCategoryModel>();
		
		try {
			res = restClient.getForObject(
					env.getInventoryUrl() + "get-item-category-byId?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : modelView ends");
		return res;
	}
}
