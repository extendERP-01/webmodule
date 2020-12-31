package nirmalya.aathithya.webmodule.asset.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "settings/")
public class SettingDemoController {

	Logger logger = LoggerFactory.getLogger(SettingDemoController.class);

	@GetMapping("chart-account")
	public String chartAccount(Model model, HttpSession session) {

		logger.info("Method : viewProduct starts");
		model.addAttribute("pageName", "Chart Of Account Page Coming Soon");
		logger.info("Method : viewProduct ends");
		return "recruitment/view-action";

	}

	@GetMapping("cost-center")
	public String costCenter(Model model, HttpSession session) {

		logger.info("Method : costCenter starts");
		model.addAttribute("pageName", "Cost Center Page Coming Soon");
		logger.info("Method : costCenter ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-company")
	public String viewCompany(Model model, HttpSession session) {

		logger.info("Method : viewCompany starts");
		model.addAttribute("pageName", "Company Page Coming Soon");
		logger.info("Method : viewCompany ends");
		return "recruitment/view-action";

	}
	@GetMapping("view-geography")
	public String viewGeoGraphy(Model model, HttpSession session) {

		logger.info("Method : viewGeoGraphy starts");
		model.addAttribute("pageName", "Geography Coming Soon");
		logger.info("Method : viewGeoGraphy ends");
		return "master/geography-master";

	}

	@GetMapping("view-hr")
	public String viewHr(Model model, HttpSession session) {

		logger.info("Method : viewHr starts");
		model.addAttribute("pageName", "Hr Page Coming Soon");
		logger.info("Method : viewHr ends");
		return "master/referenceHr";

	}

	@GetMapping("view-procurement")
	public String viewProcurement(Model model, HttpSession session) {

		logger.info("Method : viewProcurement starts");
		model.addAttribute("pageName", "Procurement Page Coming Soon");
		logger.info("Method : viewProcurement ends");
		return "master/view-procurement";

	}

	@GetMapping("view-template")
	public String viewTemplate(Model model, HttpSession session) {

		logger.info("Method : viewProcurement starts");
		model.addAttribute("pageName", "Template Page Coming Soon");
		logger.info("Method : viewProcurement ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-bank")
	public String viewBank(Model model, HttpSession session) {

		logger.info("Method : viewBank starts");
		model.addAttribute("pageName", "Bank Page Coming Soon");
		logger.info("Method : viewBank ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-job-board")
	public String viewJobBoard(Model model, HttpSession session) {

		logger.info("Method : viewBank starts");
		model.addAttribute("pageName", "Job Board Page Coming Soon");
		logger.info("Method : viewBank ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-other")
	public String viewOther(Model model, HttpSession session) {

		logger.info("Method : viewOther starts");
		model.addAttribute("pageName", "Other Page Coming Soon");
		logger.info("Method : viewOther ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-polices")
	public String viewPolices(Model model, HttpSession session) {

		logger.info("Method : viewPolices starts");
		model.addAttribute("pageName", "Polices Page Coming Soon");
		logger.info("Method : viewPolices ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-access")
	public String viewAccess(Model model, HttpSession session) {

		logger.info("Method : viewAccess starts");
		model.addAttribute("pageName", "Access Page Coming Soon");
		logger.info("Method : viewAccess ends");
		return "recruitment/view-action";

	}

	@GetMapping("product-catagory")
	public String productCategory(Model model, HttpSession session) {

		logger.info("Method : productCategory starts");
		model.addAttribute("pageName", "Product Category Page Coming Soon");
		logger.info("Method : productCategory ends");
		return "recruitment/view-action";

	}

	@GetMapping("product")
	public String viewProduct(Model model, HttpSession session) {

		logger.info("Method : viewProduct starts");
		model.addAttribute("pageName", "Product Page Coming Soon");
		logger.info("Method : viewProduct ends");
		return "recruitment/view-action";

	}

	@GetMapping("asset-classification")
	public String viewAssetClassification(Model model, HttpSession session) {

		logger.info("Method : viewProduct starts");
		model.addAttribute("pageName", "Asset Classification Page Coming Soon");
		logger.info("Method : viewProduct ends");
		return "recruitment/view-action";

	}

}
