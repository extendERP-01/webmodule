package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import nirmalya.aathithya.webmodule.inventory.model.InventoryItemModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "inventory/" })
public class ManageVendorController {

	Logger logger = LoggerFactory.getLogger(ManageVendorController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;

	/*
	 * GetMapping for Adding new items
	 *
	 */
	@GetMapping(value = { "manage-vendor" })
	public String manageVendor(Model model, HttpSession session) {
		logger.info("Method : manageVendor starts");
		InventoryItemModel itemModel = new InventoryItemModel();
		InventoryItemModel item = (InventoryItemModel) session.getAttribute("sitem");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (item != null) {
			model.addAttribute("itemModel", item);
			session.setAttribute("itemModel", null);
		} else {
			model.addAttribute("itemModel", itemModel);
		}
    

		logger.info("Method : manageVendor ends");
		return "inventory/manageVendor";
	}


	
}
