package nirmalya.aathithya.webmodule.asset.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.asset.model.AssetWithPOInventoryModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "asset")
public class AssetsWithPOInventoryController {

	Logger logger = LoggerFactory.getLogger(AssetsWithPOInventoryController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * View Default 'Linked With PO ANd Inventory' page
	 *
	 */
	@GetMapping("/linked-with-po-inventory")
	public String linkedWithPOInventory(Model model, HttpSession session) {
		logger.info("Method : linkedWithPOInventory starts");
		
		logger.info("Method : linkedWithPOInventory ends");
		return "asset/linked-with-po-inventory";
	}
	
	@GetMapping("/asset-with-vendor")
	public String assetWithVendor(Model model, HttpSession session) {
		logger.info("Method : assetWithVendor starts");
		
		logger.info("Method : assetWithVendor ends");
		return "asset/asset-with-vendor";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/linked-with-po-inventory-throughajax")
	public @ResponseBody DataTableResponse viewLinkedAssetWithPOThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param3, @RequestParam String param4) {
		logger.info("Method : viewLinkedAssetWithPOThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);

			JsonResponse<List<AssetWithPOInventoryModel>> jsonResponse = new JsonResponse<List<AssetWithPOInventoryModel>>();
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getAllLinkedAssets", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AssetWithPOInventoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetWithPOInventoryModel>>() {
					});

//			String s = "";
			for (AssetWithPOInventoryModel m : form) {
				if(m.getQrCode()==null || m.getQrCode()=="") {
					m.setQrCode("NA");
				}
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewLinkedAssetWithPOThroughAjax end");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/asset-with-vendor-throughajax")
	public @ResponseBody DataTableResponse viewAssetWithVendorThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param3, @RequestParam String param4) {
		logger.info("Method : viewAssetWithVendorThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			
			JsonResponse<List<AssetWithPOInventoryModel>> jsonResponse = new JsonResponse<List<AssetWithPOInventoryModel>>();
			jsonResponse = restClient.postForObject(env.getAssetUrl() + "getAllAssetsWithVendor", tableRequest,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<AssetWithPOInventoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AssetWithPOInventoryModel>>() {
			});
			
//			String s = "";
			for (AssetWithPOInventoryModel m : form) {
				m.setRemainingDays("5");
				if(m.getQrCode()==null || m.getQrCode()=="") {
					m.setQrCode("NA");
				}
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewAssetWithVendorThroughAjax end");
		return response;
	}
}
