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
import nirmalya.aathithya.webmodule.inventory.model.InventoryGoodsReturnNoteDashboardModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryIssueNoteDashboardModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryPurchaseOrderDashboardModel;
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryManagerDashboardController {
	Logger logger = LoggerFactory.getLogger(InventoryGoodsReceiveNoteDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-manager-dashboard")
	public String inventoryManagerDashboard(Model model, HttpSession session) {
		logger.info(
				"Method : WEB MODULE InventoryGoodsReceiveNoteDashboardController inventoryManagerDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * TODAY INVENTORY GOODS RECEIVE NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts1 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-receive-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount1.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * TOTAL INVENTORY GOODS RECEIVE NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts2 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-receive-note",
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
		 * TODAY INVENTORY GRN PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts3 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-today-grn-price",
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

		/**
		 * TOTAL INVENTORY GRN PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReceiveNoteDashboardModel> respGetAllRoomCount4 = new JsonResponse<InventoryGoodsReceiveNoteDashboardModel>();
		InventoryGoodsReceiveNoteDashboardModel roomCounts4 = new InventoryGoodsReceiveNoteDashboardModel();

		try {
			respGetAllRoomCount4 = restClient.getForObject(env.getInventoryUrl() + "rest-total-grn-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount4 = respGetAllRoomCount4.getMessage();

		if (messageForCount4 != null || messageForCount4 != "") {
			model.addAttribute("message", messageForCount4);
		}

		roomCounts4 = mapper.convertValue(respGetAllRoomCount4.getBody(),
				new TypeReference<InventoryGoodsReceiveNoteDashboardModel>() {
				});

		model.addAttribute("roomCount4", roomCounts4);

		/**
		 * INVENTORY TODAY GOODS RETURN NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount5 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts5 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount5 = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-return-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount5 = respGetAllRoomCount5.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts5 = mapper.convertValue(respGetAllRoomCount5.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount5", roomCounts5);

		/**
		 * INVENTORY TOTAL GOODS RETURN NOTE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount6 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts6 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount6 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-return-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount6 = respGetAllRoomCount6.getMessage();

		if (messageForCount6 != null || messageForCount6 != "") {
			model.addAttribute("message", messageForCount6);
		}

		roomCounts6 = mapper.convertValue(respGetAllRoomCount6.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount6", roomCounts6);

		/**
		 * INVENTORY TODAY GOODS RETURN NOTE PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount7 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts7 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount7 = restClient.getForObject(env.getInventoryUrl() + "rest-today-goods-return-note-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount7 = respGetAllRoomCount7.getMessage();

		if (messageForCount7 != null || messageForCount7 != "") {
			model.addAttribute("message", messageForCount7);
		}

		roomCounts7 = mapper.convertValue(respGetAllRoomCount7.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount7", roomCounts7);

		/**
		 * INVENTORY TOTAL GOODS RETURN NOTE PRICE
		 *
		 */

		JsonResponse<InventoryGoodsReturnNoteDashboardModel> respGetAllRoomCount8 = new JsonResponse<InventoryGoodsReturnNoteDashboardModel>();
		InventoryGoodsReturnNoteDashboardModel roomCounts8 = new InventoryGoodsReturnNoteDashboardModel();

		try {
			respGetAllRoomCount8 = restClient.getForObject(env.getInventoryUrl() + "rest-total-goods-return-note-price",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount8 = respGetAllRoomCount8.getMessage();

		if (messageForCount8 != null || messageForCount8 != "") {
			model.addAttribute("message", messageForCount8);
		}

		roomCounts8 = mapper.convertValue(respGetAllRoomCount8.getBody(),
				new TypeReference<InventoryGoodsReturnNoteDashboardModel>() {
				});

		model.addAttribute("roomCount8", roomCounts8);

		/**
		 * TODAY ISSUE NOTE
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount9 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts9 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount9 = restClient.getForObject(env.getInventoryUrl() + "rest-today-issue-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount9 = respGetAllRoomCount9.getMessage();

		if (messageForCount9 != null || messageForCount9 != "") {
			model.addAttribute("message", messageForCount9);
		}

		roomCounts9 = mapper.convertValue(respGetAllRoomCount9.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount9", roomCounts9);

		/**
		 * TOTAL ISSUE NOTE
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount10 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts10 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount10 = restClient.getForObject(env.getInventoryUrl() + "rest-total-issue-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount10 = respGetAllRoomCount10.getMessage();

		if (messageForCount10 != null || messageForCount10 != "") {
			model.addAttribute("message", messageForCount10);
		}

		roomCounts10 = mapper.convertValue(respGetAllRoomCount10.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount10", roomCounts10);

		/**
		 * TOTAL OPEN REQUISTION
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount11 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts11 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount11 = restClient.getForObject(env.getInventoryUrl() + "rest-total-open-requistion",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount11 = respGetAllRoomCount11.getMessage();

		if (messageForCount11 != null || messageForCount11 != "") {
			model.addAttribute("message", messageForCount11);
		}

		roomCounts11 = mapper.convertValue(respGetAllRoomCount11.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount11", roomCounts11);

		/**
		 * TODAY CLOSED REQUISTION
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount12 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts12 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount12 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-requistion",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount12 = respGetAllRoomCount12.getMessage();

		if (messageForCount12 != null || messageForCount12 != "") {
			model.addAttribute("message", messageForCount12);
		}

		roomCounts12 = mapper.convertValue(respGetAllRoomCount12.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount12", roomCounts12);

		/**
		 * TODAY OPEN PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount13 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts13 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount13 = restClient.getForObject(env.getInventoryUrl() + "rest-today-open-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount13 = respGetAllRoomCount13.getMessage();

		if (messageForCount13 != null || messageForCount13 != "") {
			model.addAttribute("message", messageForCount13);
		}

		roomCounts13 = mapper.convertValue(respGetAllRoomCount13.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount13", roomCounts13);

		/**
		 * TOTAL OPEN PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount14 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts14 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount14 = restClient.getForObject(env.getInventoryUrl() + "rest-total-open-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount14 = respGetAllRoomCount14.getMessage();

		if (messageForCount14 != null || messageForCount14 != "") {
			model.addAttribute("message", messageForCount14);
		}

		roomCounts14 = mapper.convertValue(respGetAllRoomCount14.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount14", roomCounts14);

		/**
		 * TODAY CLOSED PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount15 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts15 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount15 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount15 = respGetAllRoomCount15.getMessage();

		if (messageForCount15 != null || messageForCount15 != "") {
			model.addAttribute("message", messageForCount15);
		}

		roomCounts15 = mapper.convertValue(respGetAllRoomCount15.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount15", roomCounts15);

		/**
		 * TOTAL CLOSED PURCHASE ORDER
		 *
		 */

		JsonResponse<InventoryPurchaseOrderDashboardModel> respGetAllRoomCount16 = new JsonResponse<InventoryPurchaseOrderDashboardModel>();
		InventoryPurchaseOrderDashboardModel roomCounts16 = new InventoryPurchaseOrderDashboardModel();

		try {
			respGetAllRoomCount16 = restClient.getForObject(env.getInventoryUrl() + "rest-total-closed-purchase-order",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount16 = respGetAllRoomCount16.getMessage();

		if (messageForCount16 != null || messageForCount16 != "") {
			model.addAttribute("message", messageForCount16);
		}

		roomCounts16 = mapper.convertValue(respGetAllRoomCount16.getBody(),
				new TypeReference<InventoryPurchaseOrderDashboardModel>() {
				});

		model.addAttribute("roomCount16", roomCounts16);

		/**
		 * Today Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount17 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts17 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount17 = restClient.getForObject(env.getInventoryUrl() + "rest-today-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount17 = respGetAllRoomCount17.getMessage();

		if (messageForCount17 != null || messageForCount17 != "") {
			model.addAttribute("message", messageForCount17);
		}

		roomCounts17 = mapper.convertValue(respGetAllRoomCount17.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount17", roomCounts17);

		/**
		 * Past Due Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount18 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts18 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount18 = restClient.getForObject(env.getInventoryUrl() + "rest-past-due-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount18 = respGetAllRoomCount18.getMessage();

		if (messageForCount18 != null || messageForCount18 != "") {
			model.addAttribute("message", messageForCount18);
		}

		roomCounts18 = mapper.convertValue(respGetAllRoomCount18.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount18", roomCounts18);

		/**
		 * Today Closed Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount19 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts19 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount19 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount19 = respGetAllRoomCount19.getMessage();

		if (messageForCount19 != null || messageForCount19 != "") {
			model.addAttribute("message", messageForCount19);
		}

		roomCounts19 = mapper.convertValue(respGetAllRoomCount19.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount19", roomCounts19);

		/**
		 * Total Closed Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount20 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts20 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount20 = restClient.getForObject(env.getInventoryUrl() + "rest-total-closed-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount20 = respGetAllRoomCount20.getMessage();

		if (messageForCount20 != null || messageForCount20 != "") {
			model.addAttribute("message", messageForCount20);
		}

		roomCounts20 = mapper.convertValue(respGetAllRoomCount20.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount20", roomCounts20);

		logger.info("Method : WEB MODULE InventoryGoodsReceiveNoteDashboardController inventoryManagerDashboard ends");
		return "inventory/InventoryManagerDashboard";
	}

}
