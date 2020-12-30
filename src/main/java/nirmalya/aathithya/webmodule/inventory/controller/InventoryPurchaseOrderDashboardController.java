/**
 * 
 */
package nirmalya.aathithya.webmodule.inventory.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryPurchaseOrderDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryPurchaseOrderDashboardController {

	Logger logger = LoggerFactory.getLogger(InventoryPurchaseOrderDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-purchase-order-dashboard")
	public String purchaseOrderDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE InventoryPurchaseOrderDashboardController purchaseOrderDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * TODAY OPEN PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount = restClient.getForObject(env.getInventoryUrl() + "rest-today-open-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts = mapper.convertValue(respGetAllRoomCount.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount", roomCounts);

		/**
		 * TOTAL OPEN PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts1 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-total-open-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllRoomCount1.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * TODAY CLOSED PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts2 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllRoomCount2.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		roomCounts2 = mapper.convertValue(respGetAllRoomCount2.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount2", roomCounts2);

		/**
		 * TOTAL CLOSED PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts3 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-total-closed-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllRoomCount3.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		roomCounts3 = mapper.convertValue(respGetAllRoomCount3.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount3", roomCounts3);
		

		logger.info("Method : WEB MODULE InventoryPurchaseOrderDashboardController purchaseOrderDashboard ends");
		return "inventory/PurchaseOrderDashboard";
	}

}
