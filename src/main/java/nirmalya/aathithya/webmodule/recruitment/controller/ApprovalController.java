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

@Controller
@RequestMapping(value = "recruitment")
public class ApprovalController {
	Logger logger = LoggerFactory.getLogger(TrainingNominationController.class);

	@Autowired
	RestTemplate restClient;
	@GetMapping("/add-approval")
	public String addApproval(Model model, HttpSession session) {

		logger.info("Method : addApproval starts");
		
		logger.info("Method : addApproval ends");

		return "recruitment/add-approval";
	}
	@GetMapping("/view-approval")
	public String viewApproval(Model model, HttpSession session) {

		logger.info("Method : viewApproval starts");
		
		logger.info("Method : viewApproval ends");

		return "recruitment/view-approval";
	}
}
