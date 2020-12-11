package nirmalya.aathithya.webmodule.sales.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MapModel1;
import nirmalya.aathithya.webmodule.sales.model.SalesDashboardModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class SalesDashboardController {

	Logger logger = LoggerFactory.getLogger(QuotationMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * Sales Dashboard
	 *
	 */
	@GetMapping("/salesDashboard")
	public String salesDashboard(Model model, HttpSession session) {
		logger.info("Method : salesDashboard starts");
		
		try {
			SalesDashboardModel[] data = restClient.getForObject(env.getSalesUrl()+"salesDashboardData", SalesDashboardModel[].class);
			List<SalesDashboardModel> dataList = Arrays.asList(data);
			
			if(dataList.size()>0) {
				model.addAttribute("TotalSales", dataList.get(0).getTotalSales());
				model.addAttribute("TotalSaleAmount", dataList.get(0).getTotalSaleAmount());
				model.addAttribute("TotalPaidAmount", dataList.get(0).getTotalPaidAmount());
				model.addAttribute("TotalDueAmount", dataList.get(0).getTotalDueAmount());
			} else {
				model.addAttribute("TotalSales", 0);
				model.addAttribute("TotalSaleAmount", 0.0);
				model.addAttribute("TotalPaidAmount", 0.0);
				model.addAttribute("TotalDueAmount", 0.0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : salesDashboard ends");
		return "sales/salesDashboard";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "salesDashboard-sales-report" })
	public @ResponseBody JsonResponse<MapModel1> getSalesReportGraph(Model model) {
		logger.info("Method : getSalesReportGraph starts");
		JsonResponse<MapModel1> res = new JsonResponse<MapModel1>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "dbSalesReport", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getSalesReportGraph ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "salesDashboard-order-report" })
	public @ResponseBody JsonResponse<MapModel1> getOrderReportGraph(Model model) {
		logger.info("Method : getOrderReportGraph starts");
		JsonResponse<MapModel1> res = new JsonResponse<MapModel1>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "dbOrderReport", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : getOrderReportGraph ends");
		return res;
	}
}
