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
public class AddRecruitmentController {

	Logger logger = LoggerFactory.getLogger(AddRecruitmentController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping add recruitment
	 */
	@GetMapping("/add-recruitment")
	public String addRecruitment(Model model, HttpSession session) {

		logger.info("Method : addRecruitment starts");

		logger.info("Method : addRecruitment ends");

		return "recruitment/add-recruitment";
	}

	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-recruitment")
	public String viewRecruitment(Model model, HttpSession session) {

		logger.info("Method : viewRecruitment starts");

		logger.info("Method : viewRecruitment ends");

		return "recruitment/view-recruitment";
	}

	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource")
	public String viewResource(Model model, HttpSession session) {

		logger.info("Method : viewResource starts");

		logger.info("Method : viewResource ends");

		return "recruitment/view-resource";
	}

	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-add-vendor")
	public String viewResourceAddVendor(Model model, HttpSession session) {

		logger.info("Method : viewResourceAddVendor starts");

		logger.info("Method : viewResourceAddVendor ends");

		return "recruitment/view-resource-add-vendor";
	}
	
	@GetMapping("/view-resource-upload-resume")
	public String uploadResume(Model model, HttpSession session) {
		logger.info("Method : uploadResume starts");
		
		logger.info("Method : uploadResume ends");
		return "recruitment/upload-resume";
	}
	
	@GetMapping("/upload-candidate-resume")
	public String viewJobListForUploadRsume(Model model, HttpSession session) {
		logger.info("Method : viewJobListForUploadRsume starts");
		
		logger.info("Method : viewJobListForUploadRsume ends");
		return "recruitment/upload-candidate-resume";
	}

	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-shortlist")
	public String viewResourceshortlist(Model model, HttpSession session) {

		logger.info("Method : viewResourceshortlist starts");

		logger.info("Method : viewResourceshortlist ends");

		return "recruitment/view-resource-shortlist";
	}

	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-add-shortlist")
	public String viewResourceAddshortlist(Model model, HttpSession session) {

		logger.info("Method : viewResourceshortlist starts");

		logger.info("Method : viewResourceshortlist ends");

		return "recruitment/view-resource-add-shortlist";
	}
	
	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-employee")
	public String viewResourceAddshortlistEmployee(Model model, HttpSession session) {

		logger.info("Method : viewResourceAddshortlistAddEmployee starts");

		logger.info("Method : viewResourceAddshortlistAddEmployee ends");

		return "recruitment/view-resource-employee";
	}
	
	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-add-to-employee")
	public String viewResourceAddshortlistAddEmployee(Model model, HttpSession session) {

		logger.info("Method : viewResourceAddshortlistAddEmployee starts");

		logger.info("Method : viewResourceAddshortlistAddEmployee ends");

		return "recruitment/view-resource-add-to-employee";
	}
	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-resource-final")
	public String viewResourceAddshortlistFinal(Model model, HttpSession session) {

		logger.info("Method : viewResourceAddshortlistAddEmployee starts");

		logger.info("Method : viewResourceAddshortlistAddEmployee ends");

		return "recruitment/view-resource-final";
	}
	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-employee-feedback")
	public String viewEmployeeFeedback(Model model, HttpSession session) {
		logger.info("Method : viewResourceAddshortlistAddEmployee starts");
		
		String id = (String) session.getAttribute("id");
		
		model.addAttribute("id", id);
		
		session.removeAttribute("id");
		
		logger.info("Method : viewResourceAddshortlistAddEmployee ends");
		return "recruitment/view-employee-feedback";
	}
	
	/*
	 * Get Mapping view recruitment
	 */
	@GetMapping("/view-employee-feedback-add")
	public String viewEmployeeFeedbackAdd(Model model, HttpSession session) {
		logger.info("Method : viewResourceAddshortlistAddEmployee starts");
		
		session.setAttribute("id", "1");
		
		logger.info("Method : viewResourceAddshortlistAddEmployee ends");
		return "recruitment/add-employee-feedback";
	}
	
	@GetMapping("/add-training-vendor-sourcing")
	public String addVendorsourcing(Model model, HttpSession session) {
		logger.info("Method : uploadResume starts");
		
		logger.info("Method : uploadResume ends");
		return "recruitment/add-training-vendor-sourcing";
	}
	
	
	@GetMapping("/view-training-vendor-sourcing")
	public String viewVendorSourcing(Model model, HttpSession session) {
		logger.info("Method : viewJobListForUploadRsume starts");
		
		logger.info("Method : viewJobListForUploadRsume ends");
		return "recruitment/view-training-vendor-sourcing";
	}
}
