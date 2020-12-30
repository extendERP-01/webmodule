package nirmalya.aathithya.webmodule.inventory.controller;

import nirmalya.aathithya.webmodule.inventory.model.VendorItemModel;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping("inventory/")
public class VendorItemController {

	Logger logger = LoggerFactory.getLogger(VendorItemController.class);

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	RestTemplate restTemplate;

	/*
	 * view all vendors in the page
	 */
	@GetMapping("view-vendor-item-list")
	public String viewVendorPage(Model model) {
		logger.info("Mothod:view-vendor-item-list started...");

		logger.info("Mothod:view-vendor-item-list ends...");
		return "inventory/viewVendorItemList";
	}

	/*
	 * get Vendor auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-autocmpltVendor")
	public @ResponseBody JsonResponse<Object> getVendorCmplt(@RequestBody String str, HttpSession session)
			throws IOException {

		logger.info("Mothod:getVendorAutoCmplt  started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-vendor-auto-cmplt?key=" + str,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setBody(res.getBody());
		logger.info("Mothod:getVendorAutoCmplt ended...");
		return response;

	}

	/*
	 * assign item/items to vendor
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-save")
	public @ResponseBody JsonResponse<VendorItemModel> saveItem(@RequestBody List<VendorItemModel> viModel,
			HttpSession session) throws IOException {

		logger.info("Mothod:assign-item-to-vendor started...");

		JsonResponse<VendorItemModel> res = new JsonResponse<VendorItemModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < viModel.size(); i++) {
				viModel.get(i).setCreatedBy(createdBy);
			}

			res = restTemplate.postForObject(env.getInventoryUrl() + "add-vendor-item", viModel, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : assignitem to vendor function Ends");
		return res;
	}

	/*
	 * view item details through ajax
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-throughAjax")
	public @ResponseBody JsonResponse<Object> getItems(@RequestBody String vendorId, HttpSession session) {

		logger.info("Mothod:get items throughAjax started...");

		JsonResponse<List<VendorItemModel>> res = new JsonResponse<List<VendorItemModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-vendor-items?id=" + vendorId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		response.setBody(res.getBody());
		logger.info("Mothod:get items throughAjax ended...");
		return response;

	}

	/*
	 * Unassigns an item of a vendor
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-unassign")
	public @ResponseBody JsonResponse<Object> unassignItems(@RequestParam String vid, @RequestParam String name,
			HttpSession session) throws IOException {

		logger.info("Mothod:unassignItems started...");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			resp = restTemplate.getForObject(
					env.getInventoryUrl() + "unassign-vendor-items?id=" + vid + "&name=" + name, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = resp.getMessage();
		if (message != null && message != "") {
			resp.setMessage("success");
		} else {
			resp.setMessage("success");
		}
		logger.info("Mothod:unassignItems ended...");
		return resp;

	}

	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-update")
	public @ResponseBody JsonResponse<VendorItemModel> updateItem(@RequestBody VendorItemModel viModel,
			HttpSession session) throws IOException {

		logger.info("Mothod:update item started...");

		JsonResponse<VendorItemModel> res = new JsonResponse<VendorItemModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		viModel.setCreatedBy(createdBy);
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "update-vendor-item", viModel, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("success");
		}
		logger.info("Method : update item function Ends");
		return res;
	}

	/*
	 * get item autocomplete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-vendor-item-list-autocmplt")
	public @ResponseBody JsonResponse<Object> getItemAutoCmplt(@RequestParam String key, @RequestParam String vid,
			HttpSession session) throws IOException {

		logger.info("Mothod:getItemAutoCmplt  started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-item-auto-cmplt?key=" + key + "&vid=" + vid,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setBody(res.getBody());
		logger.info("Mothod:getItemAutoCmplt ended...");
		return response;

	}
}
