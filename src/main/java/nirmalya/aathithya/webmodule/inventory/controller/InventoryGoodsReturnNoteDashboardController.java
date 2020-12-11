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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReturnNoteDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryGoodsReturnNoteDashboardController {

	Logger logger = LoggerFactory.getLogger(InventoryGoodsReturnNoteDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-goods-return-note-dashboard")
	public String goodsReturnNoteDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE InventoryGoodsReturnNoteDashboardController goodsReturnNoteDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * INVENTORY TODAY GOODS RETURN NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-return-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts = mapper.convertValue(respGetAllRoomCount.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount", roomCounts);

		/**
		 * INVENTORY TOTAL GOODS RETURN NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts1 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-return-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllRoomCount1.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * INVENTORY TODAY GOODS RETURN NOTE PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts2 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-return-note-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllRoomCount2.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		roomCounts2 = mapper.convertValue(respGetAllRoomCount2.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount2", roomCounts2);

		/**
		 * INVENTORY TOTAL GOODS RETURN NOTE PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts3 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-return-note-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllRoomCount3.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		roomCounts3 = mapper.convertValue(respGetAllRoomCount3.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount3", roomCounts3);
		

		logger.info("Method : WEB MODULE InventoryGoodsReturnNoteDashboardController goodsReturnNoteDashboard ends");
		return "inventory/GoodsReturnNoteDashboard";
	}

}
