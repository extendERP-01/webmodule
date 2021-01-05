package nirmalya.aathithya.webmodule.master.controller;

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
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class ProductMasterController {

	Logger logger = LoggerFactory.getLogger(ProductMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "manage-product" })
	public String manageProduct(Model model, HttpSession session) {
		logger.info("Method : manageProduct starts");
		
		try {
			DropDownModel[] brand = restClient.getForObject(env.getMasterUrl()+"getBrandListForProduct", DropDownModel[].class);
			List<DropDownModel> brandList = Arrays.asList(brand);
			
			model.addAttribute("brandList", brandList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] mode = restClient.getForObject(env.getMasterUrl()+"getModeListForProduct", DropDownModel[].class);
			List<DropDownModel> modeList = Arrays.asList(mode);
			
			model.addAttribute("modeList", modeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] hsnCode = restClient.getForObject(env.getMasterUrl()+"getHSNCodeListForProduct", DropDownModel[].class);
			List<DropDownModel> hsnCodeList = Arrays.asList(hsnCode);
			
			model.addAttribute("hsnCodeList", hsnCodeList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : manageProduct ends");
		return "master/manageProduct";
	}
}
