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
public class TrainingNominationController {
	Logger logger = LoggerFactory.getLogger(TrainingNominationController.class);

	@Autowired
	RestTemplate restClient;
	@GetMapping("/add-training-nomination")
	public String addTrainingNomination(Model model, HttpSession session) {

		logger.info("Method : addTrainingNomination starts");
		
		logger.info("Method : addTrainingNomination ends");

		return "recruitment/add-training-nomination";
	}
	@GetMapping("/view-training-nomination")
	public String viewTrainingNomination(Model model, HttpSession session) {

		logger.info("Method : viewTrainingNomination starts");
		
		logger.info("Method : viewTrainingNomination ends");

		return "recruitment/view-training-nomination";
	}
}
