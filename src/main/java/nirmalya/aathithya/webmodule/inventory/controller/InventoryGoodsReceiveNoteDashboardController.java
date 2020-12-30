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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReceiveNoteDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryGoodsReceiveNoteDashboardController {

	Logger logger = LoggerFactory.getLogger(InventoryGoodsReceiveNoteDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-goods-receive-note-dashboard")
	public String goodsReceiveNoteDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE InventoryGoodsReceiveNoteDashboardController goodsReceiveNoteDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * TODAY INVENTORY GOODS RECEIVE NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-receive-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts = mapper.convertValue(respGetAllRoomCount.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount", roomCounts);

		/**
		 * TOTAL INVENTORY GOODS RECEIVE NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts1 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-receive-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllRoomCount1.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * TODAY INVENTORY GRN PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts2 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-today-grn-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllRoomCount2.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		roomCounts2 = mapper.convertValue(respGetAllRoomCount2.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount2", roomCounts2);

		/**
		 * TOTAL INVENTORY GRN PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts3 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-total-grn-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllRoomCount3.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		roomCounts3 = mapper.convertValue(respGetAllRoomCount3.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount3", roomCounts3);

		logger.info("Method : WEB MODULE InventoryGoodsReceiveNoteDashboardController goodsReceiveNoteDashboard ends");
		return "inventory/GoodsReceiveNoteDashboard";
	}

}
