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

/**
 * @author NirmalyaLabs
 * 
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class InventoryDemoController {

	Logger logger = LoggerFactory.getLogger(InventoryDemoController.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EnvironmentVaribles environmentVaribles;
	
	@GetMapping(value = { "define-purchase-policy" })
	public String addPurchaseOreder(Model model, HttpSession session) {
		logger.info("Method : addPurchaseOreder starts");
		
		logger.info("Method : addPurchaseOreder starts");
		return "inventory/define-purchase-policy";
	}
	
	@GetMapping(value = { "view-vendor-contract" })
	public String vendorContractUpload(Model model, HttpSession session) {
		logger.info("Method : vendorContractUpload starts");
		
		logger.info("Method : vendorContractUpload starts");
		return "inventory/view-vendor-contract";
	}
	
	@GetMapping(value = { "vendor-profile-rating" })
	public String vendorProfileRating(Model model, HttpSession session) {
		logger.info("Method : vendorProfileRating starts");
		
		logger.info("Method : vendorProfileRating starts");
		return "inventory/view-Vendor-Profiling-Rating";
	}
	
	@GetMapping(value = { "blacklist-vendor" })
	public String blacklistVendor(Model model, HttpSession session) {
		logger.info("Method : blacklistVendor starts");
		
		logger.info("Method : blacklistVendor starts");
		return "inventory/view-blackList-vendor"; 
	}
}
