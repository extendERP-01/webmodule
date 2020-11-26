package nirmalya.aathithya.webmodule.inventory.controller;
 
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
import nirmalya.aathithya.webmodule.inventory.model.AssignStockPriceModel;
import nirmalya.aathithya.webmodule.inventory.model.ItemModel;

@Controller
@RequestMapping("inventory/")
public class AssignStockPriceController {
	Logger logger = LoggerFactory.getLogger(AssignStockPriceController.class);

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	RestTemplate restTemplate;

	/*
	 * view all stores in the page
	 */
	@GetMapping("assign-stock-price")
	public String viewCustomerItemPage(Model model, HttpSession session) {
		logger.info("Mothod:view store page started...");

		logger.info("Mothod: view store page ends...");
		return "inventory/assign-stock-price";
	}

	/*
	 * assign items to a store
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("assign-stock-price-save")
	public @ResponseBody JsonResponse<AssignStockPriceModel> saveItem(@RequestBody List<AssignStockPriceModel> ciModel,
			HttpSession session) {

		logger.info("Mothod:assign-item-to-store started...");

		JsonResponse<AssignStockPriceModel> res = new JsonResponse<AssignStockPriceModel>();

		try {
			final String cb = (String) session.getAttribute("USER_ID");
			ciModel.forEach(c -> c.setCreatedBy(cb));
			res = restTemplate.postForObject(env.getInventoryUrl() + "add-stock-item", ciModel, JsonResponse.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage(message);
		}
		logger.info("Method : assignitem to store function Ends");

		return res;
	}

	/*
	 * view item details through ajax
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("assign-stock-price-view-throughAjax")
	public @ResponseBody JsonResponse<Object> getItems(@RequestBody String cId, HttpSession session) {

		logger.info("Mothod:get items throughAjax started...");

		JsonResponse<List<AssignStockPriceModel>> res = new JsonResponse<List<AssignStockPriceModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "get-store-items?id=" + cId, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setBody(res.getBody());
		logger.info("Mothod:get items throughAjax ended...");
		return response;

	}

	/*
	 * get item auto complete
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("assign-stock-price-autocmplt-item")
	public @ResponseBody JsonResponse<Object> getItemAutoCmplt(@RequestParam String key, @RequestParam String storeId,
			HttpSession session) {

		logger.info("Mothod:getItemAutoCmplt  started...");

		JsonResponse<List<ItemModel>> res = new JsonResponse<List<ItemModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(
					env.getInventoryUrl() + "store-get-item-auto-cmplt?key=" + key + "&storeId=" + storeId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setBody(res.getBody());
		logger.info("Mothod:getItemAutoCmplt ended...");
		return response;

	}

	/*
	 * Unassigns/delete item
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("assign-stock-price-delete")
	public @ResponseBody JsonResponse<Object> deleteItem(@RequestBody String data, HttpSession session) {

		logger.info("Mothod:unassign item started...");
		String[] str = data.split("-");
		String cid = str[0];
		String itemId = str[1];
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			String params = cid + "-" + itemId;
			res = restTemplate.getForObject(env.getInventoryUrl() + "delete-stock-item?params=" + params,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("error");
		}
		logger.info("Method : unassign items Ends");
		return res;
	}

	/*
	 * update store item
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("assign-stock-price-update")
	public @ResponseBody JsonResponse<AssignStockPriceModel> updateItem(@RequestBody AssignStockPriceModel ciModel,
			HttpSession session) {

		logger.info("Mothod:update item started...");

		JsonResponse<AssignStockPriceModel> res = new JsonResponse<AssignStockPriceModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ciModel.setCreatedBy(createdBy);
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "update-stock-item", ciModel, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("error");
		}
		logger.info("Method : update item function Ends");
		return res;
	}

	/*
	 * get store auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("assign-stock-price-autocmplt-store")
	public @ResponseBody JsonResponse<List<DropDownModel>> getCustomerAutoCmplt(@RequestBody String str,
			HttpSession session) {

		logger.info("Mothod:get Customer Name AutoCmplt  started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		// JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "store-get-store-auto-cmplt?key=" + str,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// response.setBody(res.getBody());
		logger.info("Mothod:get Customer Name AutoCmplt ended...");
		return res;

	}
}
