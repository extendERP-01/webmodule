/*
 *
 * Defines UserComparePropertyController method calls 
 */
package nirmalya.aathithya.webmodule.property.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DataSetForPropType1;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "property")
public class ComparePropertyController {

	Logger logger = LoggerFactory.getLogger(ComparePropertyController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 *
	 * View Compare Property Master page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-compare-property")
	public String viewAutoCompare(Model model) {

		/* property category Name */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> propertyCatData = new ArrayList<DropDownModel>();

		try {
			respTblMstr = restClient.getForObject(env.getPropertyUrl() + "getPropCatgName?Action=" + "getPropCatgName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		propertyCatData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		List<String> propertyCategory = new ArrayList<String>();
		for (DropDownModel f : propertyCatData) {
			propertyCategory.add(f.getName());
		}
		model.addAttribute("propertyCatData", propertyCatData);
		return "property/ListingCompareProperty";
	}

	/*
	 * ######################################## END
	 * #################################################
	 */

	/**
	 * DROP DOWN DATA FOR NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-compare-property-onchange" })
	public @ResponseBody JsonResponse<DropDownModel> getPropertyTypeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : WEBMODULE getPropertyTypeList getPropertyTypeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "restGetPropertyTypeById?proType=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : WEBMODULE getPropertyTypeList  ends");
		return res;
	}

	/*
	 * ######################################## END
	 * #################################################
	 */

	/**
	 * Attribute details
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-compare-property-get-attribute-list-through-ajax" })
	public @ResponseBody JsonResponse<DropDownModel> getAttributeList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :getAttributeList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getPropertyUrl() + "restGetAttributeById?proType=" + index,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method :getAttributeList  ends");
		return res;
	}

	/*
	 * ######################################## END
	 * #################################################
	 */

	/*
	 * View all Property Type 1 list through AJAX
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-compare-property-get-property-type11-list-through-ajax" })
	public @ResponseBody JsonResponse<DataSetForPropType1> getProperType11List(Model model,
			@RequestBody List<DataSetForPropType1> params) {
		logger.info("Method :getProperType11List starts");
		JsonResponse<DataSetForPropType1> res = new JsonResponse<DataSetForPropType1>();
		try {
			res = restClient.postForObject(env.getPropertyUrl() + "restPropertyType11", params, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method :getProperType11List  ends");
		return res;
	}

	/*
	 * ######################################## END
	 * #################################################
	 */

	/*
	 * View all Property Type 2 list through AJAX
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-compare-property-get-property-type22-list-through-ajax" })
	public @ResponseBody JsonResponse<DataSetForPropType1> getProperType22List(Model model,
			@RequestBody List<DataSetForPropType1> params) {
		logger.info("Method :getProperType22List starts");
		JsonResponse<DataSetForPropType1> res = new JsonResponse<DataSetForPropType1>();

		try {
			res = restClient.postForObject(env.getPropertyUrl() + "restPropertyType22", params, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method :getProperType22List  ends");
		return res;
	}
	/*
	 * ######################################## END
	 * #################################################
	 */
}
