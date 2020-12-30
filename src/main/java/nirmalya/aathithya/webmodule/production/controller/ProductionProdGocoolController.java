package nirmalya.aathithya.webmodule.production.controller;

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
import org.springframework.validation.BindingResult;
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
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.ResponseConstands;
import nirmalya.aathithya.webmodule.common.utils.StringConstands;
import nirmalya.aathithya.webmodule.production.model.ProductionGocoolProdModel;
import nirmalya.aathithya.webmodule.production.model.ProductionPipeProductionModel;

@Controller
@RequestMapping(value = "production")
public class ProductionProdGocoolController {

	Logger logger = LoggerFactory.getLogger(ProductionProdGocoolController.class);

	private static final String MESSAGE = "message";
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view employee dependent
	 */
	@GetMapping("/view-productions")
	public String viewProductionMix(Model model, HttpSession session) {

		logger.info("Method : viewProductionMix   starts");

		try {
			String userId = (String) session.getAttribute(StringConstands.USERID);
			DropDownModel[] payMode = restClient.getForObject(env.getProduction() + "getPlant?userId=" + userId,
					DropDownModel[].class);
			List<DropDownModel> storeList = Arrays.asList(payMode);

			model.addAttribute("storeList", storeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] items = restClient.getForObject(env.getProduction() + "getProdItemList",
					DropDownModel[].class);
			List<DropDownModel> itemList = Arrays.asList(items);

			model.addAttribute("itemList", itemList);
		} catch (RestClientException e) {
			logger.error(e.getMessage());
		}
		logger.info("Method : viewProductionMix  ends");

		return "production/view-add-production";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-productions-through-ajax")
	public @ResponseBody DataTableResponse viewMotherCoilSlitAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {

		logger.info("Method : viewMotherCoilSlitAjax statrs");

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
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			JsonResponse<List<ProductionGocoolProdModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getProduction() + "view-productions", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProductionGocoolProdModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProductionGocoolProdModel>>() {
					});

			String s = "";

			for (ProductionGocoolProdModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getProdId().getBytes());
				// byte[] encodeId2 = Base64.getEncoder().encode(m.getBatchId().getBytes());

				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a> &nbsp; &nbsp; ";

				if (m.getProductionStage() == 0) {
					s = s + "<input type='button' onclick='addProdDtls(\"" + m.getProdId()
							+ "\")' class='favorite styled' value='Add Production Dtls'>  ";
				} else {
					s = s + "<input type='button'  class='favorite styled' value='Production Dtls Added'>  ";
				}

				m.setAction(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewMotherCoilSlitAjax ends");

		return response;
	}

	/**
	 * Web controller add production data through ajax
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-add-ajax")
	public @ResponseBody JsonResponse<Object> addProductionDtls(
			@RequestBody List<ProductionGocoolProdModel> productionPipeProductionModelList, HttpSession session) {

		logger.info("Method : addProductionDtls starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute(StringConstands.USERID);
			for (ProductionGocoolProdModel r : productionPipeProductionModelList) {
				r.setCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getProduction() + "add-production-dtl",
					productionPipeProductionModelList, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && !message.isEmpty()) {
			resp.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			resp.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : addProductionDtls function Ends");
		return resp;
	}

	/**
	 * modal view
	 * 
	 * @param model
	 * @param index
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/view-productions-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalProduction(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method :modalProduction starts");

		byte[] batchId = Base64.getDecoder().decode(index.getBytes());

		String prodId = (new String(batchId));

		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getProduction() + "viewProductionById?prodId=" + prodId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage(ResponseConstands.UNSUCCESS);
		} else {
			response.setMessage(ResponseConstands.SUCCESS);
		}
		logger.info("Method : modalProduction  ends ");
		return response;
	}

}
