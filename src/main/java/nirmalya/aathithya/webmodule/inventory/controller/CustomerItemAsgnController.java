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
import nirmalya.aathithya.webmodule.common.utils.ResponseConstands;
import nirmalya.aathithya.webmodule.inventory.model.CustomerItemModel;
import nirmalya.aathithya.webmodule.inventory.model.ItemModel;

@Controller
@RequestMapping("inventory/")
public class CustomerItemAsgnController {

	Logger logger = LoggerFactory.getLogger(CustomerItemAsgnController.class);

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	RestTemplate restTemplate;

	/*
	 * view all customers in the page
	 */
	@GetMapping("customer-item-assign")
	public String viewCustomerItemPage(Model model) {
		logger.info("Mothod:view customer page started...");

		logger.info("Mothod: view customer page ends...");
		return "inventory/viewCustomerItemList";
	}

	/*
	 * assign items to a customer
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("customer-item-assign-save")
	public @ResponseBody JsonResponse<CustomerItemModel> saveItem(@RequestBody List<CustomerItemModel> ciModel,
			HttpSession session) {

		logger.info("Mothod:assign-item-to-customer started...");

		JsonResponse<CustomerItemModel> res = new JsonResponse<CustomerItemModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < ciModel.size(); i++) {
				ciModel.get(i).setCreatedBy(createdBy);
			}
			res = restTemplate.postForObject(env.getInventoryUrl() + "add-coustomer-item", ciModel, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && !message.isEmpty()) {
			res.setMessage(ResponseConstands.SUCCESS);
		} else {
			res.setMessage(ResponseConstands.UNSUCCESS);
		}
		logger.info("Method : assignitem to customer function Ends");
		return res;
	}

	/*
	 * view item details through ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("customer-item-assign-view-throughAjax")
	public @ResponseBody JsonResponse<Object> getItems(@RequestParam String customerId) {

		logger.info("Mothod:get items throughAjax started...");

		JsonResponse<List<CustomerItemModel>> res = new JsonResponse<List<CustomerItemModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "get-customer-items?id=" + customerId,
					JsonResponse.class);

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
	@GetMapping("customer-item-assign-autocmplt-item")
	public @ResponseBody JsonResponse<Object> getItemAutoCmplt(@RequestParam String key,
			@RequestParam String customerId, HttpSession session) {

		logger.info("Mothod:getItemAutoCmplt  started...");

		JsonResponse<List<ItemModel>> res = new JsonResponse<List<ItemModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();
		try {

			res = restTemplate.getForObject(
					env.getInventoryUrl() + "customer-get-item-auto-cmplt?key=" + key + "&customerId=" + customerId,
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
	@PostMapping("customer-item-assign-delete")
	public @ResponseBody JsonResponse<Object> deleteItem(@RequestBody String data, HttpSession session) {

		logger.info("Mothod:unassign item started...");
		String[] str = data.split("-");
		String cid = str[0];
		String itemId = str[1];
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			String params = cid + "-" + itemId;
			res = restTemplate.getForObject(env.getInventoryUrl() + "delete-coustomer-item?params=" + params,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && !message.isEmpty()) {
			res.setMessage(ResponseConstands.SUCCESS);
		} else {
			res.setMessage("error");
		}
		logger.info("Method : unassign items Ends");
		return res;
	}

	/*
	 * update customer item
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("customer-item-assign-update")
	public @ResponseBody JsonResponse<CustomerItemModel> updateItem(@RequestBody CustomerItemModel ciModel,
			HttpSession session) {

		logger.info("Mothod:update item started...");

		JsonResponse<CustomerItemModel> res = new JsonResponse<CustomerItemModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ciModel.setCreatedBy(createdBy);
		try {

			res = restTemplate.postForObject(env.getInventoryUrl() + "update-customer-item", ciModel,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && !message.isEmpty()) {
			res.setMessage(ResponseConstands.SUCCESS);
		} else {
			res.setMessage("error");
		}
		logger.info("Method : update item function Ends");
		return res;
	}

	/*
	 * get customer auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("customer-item-assign-autocmplt-customer")
	public @ResponseBody JsonResponse<Object> getCustomerAutoCmplt(@RequestBody String str, HttpSession session) {

		logger.info("Mothod:get Customer Name AutoCmplt  started...");

		JsonResponse<List<ItemModel>> res = new JsonResponse<List<ItemModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "customer-get-customer-auto-cmplt?key=" + str,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setBody(res.getBody());
		logger.info("Mothod:get Customer Name AutoCmplt ended...");
		return response;

	}

	/*
	 * get customer auto complete
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("customer-item-assign-autocmplt-category")
	public @ResponseBody JsonResponse<Object> getCategoryAutocomplete(@RequestParam String key, HttpSession session) {

		logger.info("Mothod: getCategoryAutocomplete started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getInventoryUrl() + "get-category-auto-cmplt?key=" + key,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setBody(res.getBody());
		logger.info("Mothod: getCategoryAutocomplete  ended...");
		return response;

	}

}
