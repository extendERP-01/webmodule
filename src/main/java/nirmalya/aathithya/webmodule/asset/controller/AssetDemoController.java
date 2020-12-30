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
@RequestMapping(value = "asset/")
public class AssetDemoController {

	Logger logger = LoggerFactory.getLogger(AssetDemoController.class);
	@GetMapping("list")
	public String viewAssetList(Model model, HttpSession session) {
		
		logger.info("Method : viewAssetList starts");
		model.addAttribute("pageName", "Asset List Page Coming Soon");
		logger.info("Method : viewAssetList ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("policy")
	public String viewAssetPolicy(Model model, HttpSession session) {
		
		logger.info("Method : viewAssetPolicy starts");
		model.addAttribute("pageName", "Asset List Page Coming Soon");
		logger.info("Method : viewAssetPolicy ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("schedule")
	public String viewAssetSchedule(Model model, HttpSession session) {
		
		logger.info("Method : viewAssetSchedule starts");
		model.addAttribute("pageName", "Asset schedule Page Coming Soon");
		logger.info("Method : viewAssetSchedule ends");
		return "recruitment/view-action";
		
	}
}
