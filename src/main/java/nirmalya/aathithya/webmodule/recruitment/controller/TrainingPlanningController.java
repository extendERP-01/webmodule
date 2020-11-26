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
public class TrainingPlanningController {
	Logger logger = LoggerFactory.getLogger(TrainingPlanningController.class);

	@Autowired
	RestTemplate restClient;
	@GetMapping("/add-training-planning")
	public String addTrainingPlanning(Model model, HttpSession session) {

		logger.info("Method : addTrainingPlanning starts");
		
		logger.info("Method : addTrainingPlanning ends");

		return "recruitment/add-training-planning";
	}
	@GetMapping("/view-training-planning")
	public String viewTrainingPlanning(Model model, HttpSession session) {

		logger.info("Method : viewTrainingPlanning starts");
		
		logger.info("Method : viewTrainingPlanning ends");

		return "recruitment/view-training-planning";
	}
}
