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
@RequestMapping(value = "inventory/")
public class InventoryDemoController1 {
	Logger logger = LoggerFactory.getLogger(InventoryDemoController1.class);

	@GetMapping("view-quatation")
	public String getQuatationDemo(Model model, HttpSession session) {

		logger.info("Method : getQuatationDemo starts");
		model.addAttribute("pageName", "RFQ Page Coming Soon");
		logger.info("Method : getQuatationDemo ends");
		return "recruitment/view-action";

	}
	@GetMapping("generate-po")
	public String generatePo(Model model, HttpSession session) {

		logger.info("Method : generatePo starts");
		model.addAttribute("pageName", "Generate Page Coming Soon");
		logger.info("Method : generatePo ends");
		return "recruitment/view-action";

	}
	@GetMapping("generate-order")
	public String generateOrder(Model model, HttpSession session) {
		
		logger.info("Method : generateOrder starts");
		model.addAttribute("pageName", "Generate Order Page Coming Soon");
		logger.info("Method : generateOrder ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("view-payment")
	public String viewPayment(Model model, HttpSession session) {
		
		logger.info("Method : viewPayment starts");
		model.addAttribute("pageName", "Payment Page Coming Soon");
		logger.info("Method : viewPayment ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("view-grn-receipt")
	public String viewGrnReceipt(Model model, HttpSession session) {
		
		logger.info("Method : viewGrnReceipt starts");
		model.addAttribute("pageName", "Grn Receipt Page Coming Soon");
		logger.info("Method : viewGrnReceipt ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("view-grn-return")
	public String viewGrnReturn(Model model, HttpSession session) {
		
		logger.info("Method : viewGrnReturn starts");
		model.addAttribute("pageName", "Grn Return Page Coming Soon");
		logger.info("Method : viewGrnReturn ends");
		return "recruitment/view-action";
		
	}
	@GetMapping("physical-verification")
	public String viewPhysical(Model model, HttpSession session) {
		
		logger.info("Method : viewPhysical starts");
		model.addAttribute("pageName", "Physical Verification Page Coming Soon");
		logger.info("Method : viewPhysical ends");
		return "recruitment/view-action";
		
	}
	
}
