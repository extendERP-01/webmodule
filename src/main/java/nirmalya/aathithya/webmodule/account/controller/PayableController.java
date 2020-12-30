package nirmalya.aathithya.webmodule.account.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.DataModel;
import nirmalya.aathithya.webmodule.account.model.FinancialModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping(value = "account/")
public class PayableController {

	Logger logger = LoggerFactory.getLogger(PayableController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping("/manage-vendor-bills")
	public String manageVendorBills(Model model) {
		logger.info("Method : manageVendorBills starts");

		logger.info("Method : manageVendorBills end");
		return "account/vendorBills";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/manage-vendor-bills-grn")
	public @ResponseBody DataTableResponse viewVendorBillsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2,@RequestParam String param3) {
		logger.info("Method : viewVendorBillsThroughAjax starts");

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

			JsonResponse<List<DataModel>> jsonResponse = new JsonResponse<List<DataModel>>();
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getVendorBills", tableRequest,
					JsonResponse.class);
		
			ObjectMapper mapper = new ObjectMapper();

			List<DataModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DataModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
				//System.out.println(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewVendorBillsThroughAjax end");
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/manage-vendor-bills-others")
	public @ResponseBody DataTableResponse viewOthersBillsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2,@RequestParam String param3) {
		logger.info("Method : viewOthersBillsThroughAjax starts");
		
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
			
			JsonResponse<List<DataModel>> jsonResponse = new JsonResponse<List<DataModel>>();
			jsonResponse = restClient.postForObject(env.getAccountUrl() + "getOtherBills", tableRequest,
					JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			
			List<DataModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DataModel>>() {
			});
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
			//System.out.println(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewOthersBillsThroughAjax end");
		return response;
	}
}
