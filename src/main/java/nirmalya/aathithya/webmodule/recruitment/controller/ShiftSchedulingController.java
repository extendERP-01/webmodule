package nirmalya.aathithya.webmodule.recruitment.controller;

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
@RequestMapping(value = "recruitment")
public class ShiftSchedulingController {
	Logger logger = LoggerFactory.getLogger(ShiftSchedulingController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@GetMapping("/add-shift-scheduling")
	public String addShiftScheduling(Model model, HttpSession session) {

		logger.info("Method : addShiftScheduling starts");
		
		logger.info("Method : addShiftScheduling ends");

		return "recruitment/add-shift-scheduling";
	}
	@GetMapping("/view-shift-scheduling")
	public String viewShiftScheduling(Model model, HttpSession session) {

		logger.info("Method : viewShiftScheduling starts");
		
		logger.info("Method : viewShiftScheduling ends");

		return "recruitment/view-shift-scheduling";
	}
}
