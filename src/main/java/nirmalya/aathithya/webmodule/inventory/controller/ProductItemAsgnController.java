package nirmalya.aathithya.webmodule.inventory.controller;
 
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.ProductModel;

@Controller
@RequestMapping("inventory/")
public class ProductItemAsgnController {

	Logger logger = LoggerFactory.getLogger(ProductItemAsgnController.class);

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	RestTemplate restTemplate;

	/*
	 * view all products in the page
	 */
	@GetMapping("product-item-asgn")
	public String viewVendorPage(Model model) {
		logger.info("Mothod:view-product-item-list started...");

		logger.info("Mothod:view-product-item-list ends...");
		return "inventory/ProductItemAsgn";
	}

	/*
	 * get product auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("product-item-asgn-autocmpltVendor")
	public @ResponseBody JsonResponse<Object> getVendorCmplt(@RequestBody String str, HttpSession session) {

		logger.info("Mothod:get Product AutoCmplt  started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-product-auto-cmplt?key=" + str,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setBody(res.getBody());
		logger.info("Mothod:get Product AutoCmplt ended...");
		return response;

	}

	/*
	 * add a product
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("product-item-asgn-save")
	public @ResponseBody JsonResponse<ProductModel> saveItem(@RequestBody List<ProductModel> pModel,
			HttpSession session) {

		logger.info("Mothod:product-item-asgn started...");

		JsonResponse<ProductModel> res = new JsonResponse<ProductModel>();

		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < pModel.size(); i++) {
				pModel.get(i).setCreatedBy(createdBy);
			}

			res = restTemplate.postForObject(env.getInventoryUrl() + "add-product-item", pModel, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("success");
		}
		logger.info("Method : product-item-asgn Ends");
		return res;
	}

	/*
	 * view product details
	 */
	@GetMapping(value = { "product-item-asgn-view" })
	public String viewFunction(Model model) {
		logger.info("Method : view Product starts");

		logger.info("Method : view Product ends");
		return "inventory/viewProduct";
	}

	/*
	 * view product details through ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("product-item-asgn-view-throughAjax")
	public @ResponseBody DataTableResponse getItems(Model model, @RequestParam String param1,
			HttpServletRequest request) {

		logger.info("Mothod:get Products throughAjax started...");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<ProductModel>> res;

			res = restTemplate.postForObject(env.getInventoryUrl() + "get-product-items", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			List<ProductModel> products = mapper.convertValue(res.getBody(), new TypeReference<List<ProductModel>>() {
			});

			for (ProductModel product : products) {
				byte[] pId = Base64.getEncoder().encode(product.getProductId().getBytes());

				String s = "";
				s = s + "<a href='product-item-asgn-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteProduct(\"" + new String(pId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";

				product.setAction(s);
			}

			response.setRecordsTotal(res.getTotal());
			response.setRecordsFiltered(res.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(products);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Mothod:get Products throughAjax ended...");
		return response;

	}

	/*
	 * delete an product
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("product-item-asgn-delete")
	public @ResponseBody JsonResponse<Object> unassignItems(@RequestParam String id, HttpSession session) {

		logger.info("Mothod:delete product started...");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] pId = Base64.getDecoder().decode(id.getBytes());
		String nid = (new String(pId));

		try {

			resp = restTemplate.getForObject(env.getInventoryUrl() + "delete-product?id=" + nid, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = resp.getMessage();
		if (message != null && message != "") {
			resp.setMessage("success");
		} else {
			resp.setMessage("success");
		}
		logger.info("Mothod: delete product ended...");
		return resp;

	}

	/*
	 * edit product
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("product-item-asgn-edit")
	public String updateItem(@RequestParam String id, HttpSession session, Model model) {

		logger.info("Mothod:update Product started...");

		JsonResponse<List<ProductModel>> res = new JsonResponse<List<ProductModel>>();
		byte[] pId = Base64.getDecoder().decode(id.getBytes());
		String nid = (new String(pId));

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-product-itemById?id=" + nid,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("success");
		}
		ProductModel m = new ProductModel();
		ObjectMapper mapper = new ObjectMapper();
		m = mapper.convertValue(res.getBody().get(0), ProductModel.class);

		model.addAttribute("product", res.getBody());
		model.addAttribute("productID", m.getProduct());
		model.addAttribute("storeProductId", m.getProductId());
		model.addAttribute("productName", m.getProductName());

		logger.info("Method : update product Ends");
		return "inventory/ProductItemAsgn";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("product-item-asgn-viewInModal")
	public @ResponseBody JsonResponse<List<ProductModel>> getProductForModal(@RequestBody String id) {

		logger.info("Mothod:get Product for modal started...");

		JsonResponse<List<ProductModel>> res = new JsonResponse<List<ProductModel>>();
		byte[] pId = Base64.getDecoder().decode(id.getBytes());
		String nid = (new String(pId));

		try {

			res = restTemplate.getForObject(env.getInventoryUrl() + "get-product-itemById?id=" + nid,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String message = res.getMessage();
		if (message != null && message != "") {

		} else {
			res.setMessage("success");
		}

		logger.info("Method :get Product for modal Ends");
		return res;
	}

	/*
	 * get item auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("product-item-asgn-item-autocmplt")
	public @ResponseBody JsonResponse<Object> getItemAutoCmplt(@RequestParam String key, @RequestParam String vid,
			HttpSession session) {

		logger.info("Mothod:getItemAutoCmplt  started...");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();
		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(
					env.getInventoryUrl() + "get-product-item-auto-cmplt?key=" + key + "&vid=" + vid,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setBody(res.getBody());
		logger.info("Mothod:getItemAutoCmplt ended...");
		return response;

	}
}
