package nirmalya.aathithya.webmodule.inventory.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

@Controller
@RequestMapping(value = "inventory")
public class ViewReOrderController {

	Logger logger = LoggerFactory.getLogger(ViewReOrderController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	
	@GetMapping("/view-Re-Order")
	public String viewReOrder(Model model, HttpSession session) {

		logger.info("Method : viewReOrder starts");


		logger.info("Method : viewReOrder ends");

		return "inventory/view-Re-Order";
	}
	
}
