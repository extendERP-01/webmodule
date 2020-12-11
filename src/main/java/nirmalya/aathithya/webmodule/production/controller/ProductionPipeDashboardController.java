package nirmalya.aathithya.webmodule.production.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
 

import org.springframework.ui.Model;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

import nirmalya.aathithya.webmodule.production.model.PipeProductionDashboardModel;

@Controller
@RequestMapping(value = "production")
public class ProductionPipeDashboardController {

	Logger logger = LoggerFactory.getLogger(ProductionPipeDashboardController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping("/view-pipe-production-dashboard")
	public String pipeProductionDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE RestaurantDashboardController restaurantDashboard starts");

		try {
			PipeProductionDashboardModel[] productionData = restClient.getForObject(
					env.getProduction() + "get-total-productions-dashboard", PipeProductionDashboardModel[].class);
			List<PipeProductionDashboardModel> respGetAllProductionCount = Arrays.asList(productionData);

			model.addAttribute("roomCounts", respGetAllProductionCount);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : WEB MODULE RestaurantDashboardController restaurantDashboard ends");
		return "production/PipeProductionDashboard";

	}

}
