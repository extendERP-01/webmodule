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
import nirmalya.aathithya.webmodule.inventory.model.InventoryIssueNoteDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory/")
public class InventoryIssueNoteDashboardController {

	Logger logger = LoggerFactory.getLogger(InventoryIssueNoteDashboardController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("inventory-issue-note-dashboard")
	public String issueNoteDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE InventoryIssueNoteDashboardController issueNoteDashboard starts");

		ObjectMapper mapper = new ObjectMapper();

		/**
		 * TODAY ISSUE NOTE
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount = restClient.getForObject(env.getInventoryUrl() + "rest-today-issue-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllRoomCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		roomCounts = mapper.convertValue(respGetAllRoomCount.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount", roomCounts);

		/**
		 * TOTAL ISSUE NOTE
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount1 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts1 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount1 = restClient.getForObject(env.getInventoryUrl() + "rest-total-issue-note",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllRoomCount1.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		roomCounts1 = mapper.convertValue(respGetAllRoomCount1.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount1", roomCounts1);

		/**
		 * TOTAL OPEN REQUISTION
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount2 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts2 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount2 = restClient.getForObject(env.getInventoryUrl() + "rest-total-open-requistion",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllRoomCount2.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		roomCounts2 = mapper.convertValue(respGetAllRoomCount2.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount2", roomCounts2);

		/**
		 * TODAY CLOSED REQUISTION
		 *
		 */

		JsonResponse<InventoryIssueNoteDashboardModel> respGetAllRoomCount3 = new JsonResponse<InventoryIssueNoteDashboardModel>();
		InventoryIssueNoteDashboardModel roomCounts3 = new InventoryIssueNoteDashboardModel();

		try {
			respGetAllRoomCount3 = restClient.getForObject(env.getInventoryUrl() + "rest-today-closed-requistion",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllRoomCount3.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		roomCounts3 = mapper.convertValue(respGetAllRoomCount3.getBody(),
				new TypeReference<InventoryIssueNoteDashboardModel>() {
				});

		model.addAttribute("roomCount3", roomCounts3);
		
		
		logger.info("Method : WEB MODULE InventoryIssueNoteDashboardController issueNoteDashboard ends");
		return "inventory/IssueNoteDashboard";
	}

}
