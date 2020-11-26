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
/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "inventory")
public class PhysicalVarificationWareHouseController {
	Logger logger = LoggerFactory.getLogger(PhysicalVarificationWareHouseController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	private static final String user_Id = "USER_ID";

	@GetMapping("/add-physical-varification-warehouse")
	public String addPhysicalVarificationWareHouse(Model model, HttpSession session) {
		logger.info("Method : addPhysicalVarificationWareHouse starts");

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(
					env.getInventoryUrl() + "get-pOrder-itemCategory", DropDownModel[].class);
			List<DropDownModel> itemCategoryList = Arrays.asList(dropDownModel);
			model.addAttribute("itemCategoryList", itemCategoryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] store = restClient.getForObject(
					env.getInventoryUrl() + "getPOrderStoreList?id=" + user_Id, DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(store);
			model.addAttribute("storeList", storeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addPhysicalVarificationWareHouse ends");
		return "inventory/add-physical-varification-warehouse";
	}

	@GetMapping("/view-physical-varification-warehouse")
	public String viewPhysicalVarificationWareHouse( HttpSession session) {
		logger.info("Method : viewPhysicalVarificationWareHouse starts");

		
		logger.info("Method : viewPhysicalVarificationWareHouse ends");
		return "inventory/view-physical-varification-warehouse";
	}
}
