package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class DispatchItemToVendorController {

	Logger logger = LoggerFactory.getLogger(DispatchItemToVendorController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping(value = { "view-dispatched-item-to-vendor" })
	public String viewDispatchItemToVendor(Model model) {
		logger.info("Method : viewDispatchItemToVendor starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getInventoryUrl() + "rest-get-invoices-numbers",
					DropDownModel[].class);
			List<DropDownModel> invoiceNumber = Arrays.asList(dropDownModel);
			model.addAttribute("invoice", invoiceNumber);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "rest-gets-purchases-orders", DropDownModel[].class);
			List<DropDownModel> purchaseOrder = Arrays.asList(dropDownModel);
			model.addAttribute("purchase", purchaseOrder);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewDispatchItemToVendor ends");
		return "inventory/viewDispatchItemToVendor";
	}
	
	@GetMapping(value = { "view-dispatched-item-to-vendor-details" })
	public String viewDispatchItemToVendorDetails(Model model) {
		logger.info("Method : viewDispatchItemToVendorDetails starts");
		
		logger.info("Method : viewDispatchItemToVendorDetails ends");
		return "inventory/viewDispatchItemToVendorDetails";
	}
}
