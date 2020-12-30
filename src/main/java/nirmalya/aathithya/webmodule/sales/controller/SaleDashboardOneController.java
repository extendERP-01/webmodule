package nirmalya.aathithya.webmodule.sales.controller;

import java.util.ArrayList;
import java.util.Base64;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.SalesDashboardoneModel;

/**
 * @author ashish
 *
 */
@Controller
@RequestMapping(value = "sales")
public class SaleDashboardOneController {
	Logger logger = LoggerFactory.getLogger(SaleInvoiceController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View sales dashboard
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/sales-dashboard")
	public String salesDashboardone(Model model,HttpSession session) {
		logger.info("Method :SaleDashboardOneController salesDashboardone starts");
		JsonResponse<SalesDashboardoneModel> jsonResponse = new JsonResponse<SalesDashboardoneModel>();
		SalesDashboardoneModel salesdashboard = new SalesDashboardoneModel();
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonResponse = restClient.getForObject(env.getSalesUrl()+"rest-sales-dashboardone",JsonResponse.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		salesdashboard = mapper.convertValue(jsonResponse.getBody(),new TypeReference<SalesDashboardoneModel>() {});
		model.addAttribute("salesdashboard", salesdashboard);
		
		/**
		 * get top 5 selling inv items
		 *
		 */
		
		JsonResponse<List<SalesDashboardoneModel>> top5ItemsJson = new JsonResponse<List<SalesDashboardoneModel>>();
		List<SalesDashboardoneModel> topitems = new ArrayList<SalesDashboardoneModel>();
		try {
			top5ItemsJson = restClient.getForObject(env.getSalesUrl()+"rest-sales-top-items",JsonResponse.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		topitems = mapper.convertValue(top5ItemsJson.getBody(),new TypeReference<List<SalesDashboardoneModel>>() {});
		System.out.println("topitems : "+topitems);
		model.addAttribute("topitems", topitems);
		
		/**
		 * get top 5 sale invoices amount wise
		 *
		 */
		
		JsonResponse<List<SalesDashboardoneModel>> top5SaleInv = new JsonResponse<List<SalesDashboardoneModel>>();
		List<SalesDashboardoneModel> saleInv = new ArrayList<SalesDashboardoneModel>();
		try {
			top5SaleInv = restClient.getForObject(env.getSalesUrl()+"rest-top-sale-invoices",JsonResponse.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		saleInv = mapper.convertValue(top5SaleInv.getBody(),new TypeReference<List<SalesDashboardoneModel>>() {});
		System.out.println("saleInv : "+saleInv);
		String s="";
		for (SalesDashboardoneModel m : saleInv) {
			byte[] pId = Base64.getEncoder().encode(m.getItemName().getBytes());
			s = new String(pId) ;
			m.setEncodedInv(s);
		}
		model.addAttribute("saleInv", saleInv);
		
		logger.info("Method :SaleDashboardOneController salesDashboardone ends");
		return "sales/sales-dashboardone";
	}
}
