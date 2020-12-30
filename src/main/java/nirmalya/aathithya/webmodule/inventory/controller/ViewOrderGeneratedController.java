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
public class ViewOrderGeneratedController {

	Logger logger = LoggerFactory.getLogger(ViewOrderGeneratedController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	
//	@GetMapping("/view-order-generated")
//	public String viewOrderGenerated(Model model, HttpSession session) {
//		logger.info("Method : viewOrderGenerated starts");
//
//
//		logger.info("Method : viewOrderGenerated ends");
//		return "inventory/view-order-generated";
//	}
	
}
