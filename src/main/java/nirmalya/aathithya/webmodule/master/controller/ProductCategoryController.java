package nirmalya.aathithya.webmodule.master.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.ProductCategoryModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = { "master/" })
public class ProductCategoryController {

	Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "product-category" })
	public String productCategory(Model model, HttpSession session) {
		logger.info("Method : productCategory starts");
		
		logger.info("Method : productCategory ends");
		return "master/productCategory";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-save")
	public @ResponseBody JsonResponse<Object> saveProductCategory(@RequestBody ProductCategoryModel category, HttpSession session) {
		logger.info("Method : saveProductCategory starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		category.setCreatedBy(userId);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveProductCategory", category,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();

		if (message != null && message != "") {

		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveProductCategory starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-save-subcat")
	public @ResponseBody JsonResponse<Object> saveProductSubCategory(@RequestBody ProductCategoryModel category, HttpSession session) {
		logger.info("Method : saveProductSubCategory starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		category.setCreatedBy(userId);
		
		try {
			resp = restClient.postForObject(env.getMasterUrl() + "saveProductSubCategory", category,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : saveProductSubCategory starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-get-total-list")
	public @ResponseBody JsonResponse<List<ProductCategoryModel>> getAllProductCategoryList(HttpSession session) {
		logger.info("Method : getAllProductCategoryList starts");
		
		JsonResponse<List<ProductCategoryModel>> resp = new JsonResponse<List<ProductCategoryModel>>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "getAllProductCategoryList",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : getAllProductCategoryList starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-get-category-list-by-id")
	public @ResponseBody JsonResponse<List<ProductCategoryModel>> getProductCategoryListById(@RequestBody String id,HttpSession session) {
		logger.info("Method : getProductCategoryListById starts");
		
		JsonResponse<List<ProductCategoryModel>> resp = new JsonResponse<List<ProductCategoryModel>>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "getProductCategoryListById?id="+id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : getProductCategoryListById starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-get-category-dtls-by-id")
	public @ResponseBody JsonResponse<ProductCategoryModel> getProductCategoryById(@RequestBody String id,HttpSession session) {
		logger.info("Method : getProductCategoryById starts");
		
		JsonResponse<ProductCategoryModel> resp = new JsonResponse<ProductCategoryModel>();
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "getProductCategoryById?id="+id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : getProductCategoryById starts");
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/product-category-delete")
	public @ResponseBody JsonResponse<Object> deleteCategory(@RequestBody String id, HttpSession session) {
		logger.info("Method : deleteCategory starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteCategory?id="+id+"&createdBy="+userId,
					JsonResponse.class);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = resp.getMessage();
		
		if (message != null && message != "") {
			
		} else {
			resp.setMessage("Success");
		}
		
		logger.info("Method : deleteCategory starts");
		return resp;
	}
}
