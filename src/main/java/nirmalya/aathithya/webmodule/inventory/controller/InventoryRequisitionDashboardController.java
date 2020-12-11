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
import nirmalya.aathithya.webmodule.inventory.model.InventoryRequisitionDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryRequisitionDashboardController {

	Logger logger = LoggerFactory.getLogger(InventoryRequisitionDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-requisition-dashboard")
	public String requisitionDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE InventoryRequisitionDashboardController requisitionDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * Today Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount = restClient.getForObject(env.getInventoryUrl() + "rest-today-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts = mapper.convertValue(respGetAllRoomCount.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount", roomCounts);

		/**
		 * Past Due Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts1 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-past-due-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllRoomCount1.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * Today Closed Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts2 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllRoomCount2.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		roomCounts2 = mapper.convertValue(respGetAllRoomCount2.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount2", roomCounts2);

		/**
		 * Total Closed Requisition
		 *
		 */

		JsonResponse<InventoryRequisitionDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryRequisitionDashboardModel>();
		InventoryRequisitionDashboardModel roomCounts3 = new InventoryRequisitionDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-total-closed-requisition",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllRoomCount3.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		roomCounts3 = mapper.convertValue(respGetAllRoomCount3.getBody(),
				new TypeReference<InventoryRequisitionDashboardModel>() {
				});

		model.addAttribute("roomCount3", roomCounts3);

		logger.info("Method : WEB MODULE InventoryRequisitionDashboardController requisitionDashboard ends");
		return "inventory/RequisitionDashboard";
	}

}
