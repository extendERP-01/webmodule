package nirmalya.aathithya.webmodule.inventory.controller;

import java.util.Arrays;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.AssignedItemModel;
import nirmalya.aathithya.webmodule.inventory.model.ReOrderItemModel;

@Controller
@RequestMapping(value = "inventory")
public class ViewReOrderController {

	Logger logger = LoggerFactory.getLogger(ViewReOrderController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	
	@GetMapping("/view-Re-Order")
	public String viewReOrder(Model model, HttpSession session) {
		logger.info("Method : viewReOrder starts");
		
		String userId = "";
		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			ReOrderItemModel[] dropDownModel = restClient
					.getForObject(env.getInventoryUrl() + "restGetBelowMinQtyItem?id=" + userId, ReOrderItemModel[].class);
			List<ReOrderItemModel> itemList = Arrays.asList(dropDownModel);
			
			for(ReOrderItemModel m : itemList) {
				AssignedItemModel[] dd = restClient
						.getForObject(env.getInventoryUrl() + "restGetItemWiseVendor?id=" + m.getItemId(), AssignedItemModel[].class);
				List<AssignedItemModel> vendorList = Arrays.asList(dd);
				
				m.setVendorList(vendorList);
			}
			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewReOrder ends");
		return "inventory/view-Re-Order";
	}
	
	@GetMapping("/view-order")
	public String viewOrder(Model model, HttpSession session) {
		logger.info("Method : viewOrder starts");

		logger.info("Method : viewOrder ends");
		return "inventory/view-vendor-order";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = {"view-order-submit-po"})
	public @ResponseBody JsonResponse<Object> postAddItems(@RequestBody List<ReOrderItemModel> itemModel, Model model,
			HttpSession session) {
		logger.info("Method : postAddItems starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		try {
			for(ReOrderItemModel m : itemModel) {
				m.setCreatedBy(userId);
			}
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "restSubmitVendorForPO",
					itemModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			jsonResponse.setMessage("Failed");
		} else {
			jsonResponse.setMessage("Success");
		}
		logger.info("Method : postAddItems ends");
		return jsonResponse;
	}
	
	@GetMapping("/view-order-generated")
	public String viewOrderGenerated(Model model, HttpSession session) {
		logger.info("Method : viewOrderGenerated starts");

		logger.info("Method : viewOrderGenerated ends");
		return "inventory/view-order-generated";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-order-generated-through-ajax" })
	public @ResponseBody DataTableResponse viewGeneratedOrderThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2) {
		logger.info("Method : viewGeneratedOrderThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			
			JsonResponse<List<ReOrderItemModel>> jsonResponse = new JsonResponse<List<ReOrderItemModel>>();
			jsonResponse = restClient.postForObject(env.getInventoryUrl() + "getAllVendorsFromReOrder",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<ReOrderItemModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ReOrderItemModel>>() {
					});
			String s = "";
			for (ReOrderItemModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getPo().getBytes());

				s = "";
				s = s + "<a href='javascript:void(0)' onclick='sendMail(\"" + new String(pId)
						+ "\")'><i class='fa fa-envelope'></i></a> &nbsp;";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewGeneratedOrderThroughAjax ends");
		return response;
	}
}
