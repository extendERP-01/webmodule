package nirmalya.aathithya.webmodule.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
 
 
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.Activity;
import nirmalya.aathithya.webmodule.user.model.Function;
import nirmalya.aathithya.webmodule.user.model.Module;
import nirmalya.aathithya.webmodule.user.model.User;
import nirmalya.aathithya.webmodule.user.model.UserRolesAndModuleIdModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
public class AccessController {

	Logger logger = LoggerFactory.getLogger(AccessController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRolesAndModuleIdModel userModel;

	/**
	 * Function to check connection
	 *
	 */
	@GetMapping("welcome")
	public String welcome(Model model, HttpSession session) {
		logger.info("Method : welcome starts");

		logger.info("Method : welcome ends");
		return "welcome";
	}
	/**
	 * Function to check connection
	 *
	 */
	@GetMapping("hrms-dashboard")
	public String hrmsDashboard(Model model, HttpSession session) {
		logger.info("Method : hrmsDashboard starts");
		
		logger.info("Method : hrmsDashboard ends");
		return "employee/hrms-dashboard";
	}

	@GetMapping("/hrms-index")
	public String hrmsIndex(Model model, HttpSession session) {
		logger.info("Method : hrmsIndex starts");

		logger.info("Method : hrmsIndex ends");
		return "hrms-index";
	}

	/**
	 * Function for home page
	 *
	 */
	@GetMapping("/")
	public String home(Model model) {
		logger.info("Method : / starts");

		logger.info("Method : / ends");
		// return "app_index";
		// return "nerp-login";
		// return "nerp_home";
		return "index2";
	}

	/**
	 * Function to show register user form
	 *
	 */
	@GetMapping("register")
	public String addUser(Model model, HttpSession session) {
		logger.info("Method : register starts");

		User user = new User();

		User form = (User) session.getAttribute("suser");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			form.setUserPassword(null);
			model.addAttribute("user", form);
			session.setAttribute("suser", null);
		} else {
			model.addAttribute("user", user);
		}

		logger.info("Method : register ends");
		return "register";
	}

	/**
	 * Function show login form
	 *
	 */
	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		logger.info("Method : login starts");

		String message = (String) session.getAttribute("loginMessage");

		if (message != null && message != "") {
			model.addAttribute("loginMessage", message);
			session.setAttribute("loginMessage", null);
		}

		logger.info("Method : login starts");
		// return "app_index";
		// return "nerp-login";
		// return "nerp_home";
		return "index2";
	}

	/**
	 * Function show index page after login
	 *
	 */
 

	@GetMapping("access-denied")
	public String accessDenied(Model model, HttpSession session) {
		logger.info("Method : access-denied starts");

		logger.info("Method : access-denied ends");
		return "accessDenied";
	}

	/**
	 * Function to logout user
	 *
	 */
	@GetMapping("logout")
	public String logout(Model model, HttpSession session) {
		logger.info("Method : access-denied Starts");

		session.invalidate();

		logger.info("Method : access-denied ends");
		return "redirect:";
	}

	/**
	 * Function to post register user form
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("addUser")
	public String addUserForm(@ModelAttribute User user, Model model, HttpSession session) {
		logger.info("Method POST : addUser starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			String enc = user.getUserPassword();
			if (enc != null && enc != "") {
				enc = passwordEncoder.encode(enc);
				user.setUserPassword(enc);
			}

			jsonResponse = restTemplate.postForObject(env.getUserUrl() + "registerUser", user, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("suser", user);
			return "redirect:register";
		}

		logger.info("Method POST : addUser ends");
		return "redirect:login";
	}

	/**
	 * for dashboard index page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/index")
	public String index(Model model, HttpSession session) {
		logger.info("Method : index starts");

		String dashboard = (String) session.getAttribute("DASHBOARD");
 
		logger.info("Method : index  extend-index ends");
		//return dashboard;
		return "extend-index";
	}

//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "index-sales-report" })
//	public @ResponseBody JsonResponse<MapModel1> getSalesReportGraph(Model model) {
//		logger.info("Method : getSalesReportGraph starts");
//		JsonResponse<MapModel1> res = new JsonResponse<MapModel1>();
//
//		try {
//			res = restTemplate.getForObject(env.getRestaurantUrl() + "dbSalesReport", JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : getSalesReportGraph ends");
//
//		return res;
//	}
//
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "index-order-report" })
//	public @ResponseBody JsonResponse<MapModel1> getOrderReportGraph(Model model) {
//		logger.info("Method : getOrderReportGraph starts");
//		JsonResponse<MapModel1> res = new JsonResponse<MapModel1>();
//
//		try {
//			res = restTemplate.getForObject(env.getRestaurantUrl() + "dbOrderReport", JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : getOrderReportGraph ends");
//
//		return res;
//	}

	// for order status page

//	@GetMapping("/order-status")
//	public String dashboard(Model model) {
//		logger.info("Method : /dashboard starts");
//
//		try {
//			OrderStatusModel[] order = restTemplate.getForObject(env.getRestaurantUrl() + "getOrderStatus",
//					OrderStatusModel[].class);
//			List<OrderStatusModel> orderList1 = Arrays.asList(order);
//
//			model.addAttribute("orderList1", orderList1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			OrderStatusModel[] order2 = restTemplate.getForObject(env.getRestaurantUrl() + "getOrderStatusReady",
//					OrderStatusModel[].class);
//			List<OrderStatusModel> orderList2 = Arrays.asList(order2);
//
//			model.addAttribute("orderList2", orderList2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			OrderStatusModel[] discount = restTemplate.getForObject(env.getRestaurantUrl() + "getDiscountDetails",
//					OrderStatusModel[].class);
//			List<OrderStatusModel> discountList = Arrays.asList(discount);
//
//			for (int i = 0; i < discountList.size(); i++) {
//				if (i % 4 == 0) {
//					discountList.get(i).setStatus("bg1.jpg");
//					discountList.get(i).setDiscountImage("offer.png");
//				} else if (i % 4 == 1) {
//					discountList.get(i).setStatus("bg2.jpg");
//					discountList.get(i).setDiscountImage("offer2.png");
//				} else if (i % 4 == 2) {
//					discountList.get(i).setStatus("bg3.jpg");
//					discountList.get(i).setDiscountImage("offer3.png");
//				} else {
//					discountList.get(i).setStatus("bg4.jpg");
//					discountList.get(i).setDiscountImage("offer4.png");
//				}
//			}
//
//			model.addAttribute("discountList", discountList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : /dashboard ends");
//		return "dashboard";
//		// return "app_index";
//	}

	/**
	 * Web Controller for view all assigned kitchen to restaurant
	 *
	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/restaurant/kitchen-staff-order-details")
//	public String getKitchenStaffOrderDetails(@RequestParam String id, Model model) {
//
//		logger.info("Method : getKitchenStaffOrderDetails starts");
//
//		DataTableRequest tableRequest = new DataTableRequest(); 
//		 
//		try {
//			// String UserId = (String) session.getAttribute("USER_ID");
//
//			tableRequest.setParam1(id);
//			// tableRequest.setUserId(UserId);
//
//			JsonResponse<List<KitchenStaffFoodOrderListModel>> jsonResponse = new JsonResponse<List<KitchenStaffFoodOrderListModel>>();
//
//			jsonResponse = restTemplate.postForObject(env.getKitchenUrl() + "getFoodListForView", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<KitchenStaffFoodOrderListModel> assignTS = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<KitchenStaffFoodOrderListModel>>() {
//					});
//
//			String s = "";
//
//			for (KitchenStaffFoodOrderListModel m : assignTS) {
//
//				byte[] pId = Base64.getEncoder().encode(m.getFoodOrderId().getBytes());
//
//				/*
//				 * m.
//				 * setFoodOrderId("<a data-toggle='modal' title='View' data-target='#myModal1' href='javascript:void(0)' onclick='viewInModel(\""
//				 * + new String(pId) + "\")'>"+m.getFoodOrderId()+"</a>");
//				 */
//
//				s = s + "<a href='javascript:void(0)'" + " onclick='printPDF(\"" + new String(pId)
//						+ "\")' ><i class=\"fa fa-download\" title=\"PDF\" style=\"color:#d00c08;font-size:24px;\"></i></a>&nbsp;&nbsp;";
//
//				if (m.getFoodPrepareStatus() == 1) {
//					s = s + "<a href='javascript:void(0)'" + " onclick='changePrepareStatus(\"" + new String(pId)
//							+ "\")' ><i class=\"fa fa-cutlery\" title=\"Receive\" style=\"color:#e30f0f;font-size:24px;\"></i></a>&nbsp;&nbsp;";
//				} else {
//					if (m.getKitchenStatus() == 1) {
//
//						s = s + "<a href='javascript:void(0)'" + " onclick='changeKitchenStatus(\"" + new String(pId)
//								+ "\")' ><i class=\"fa fa-times-circle\" title=\"In Progress\" style=\"color:#e30f0f;font-size:24px;\"></i></a>&nbsp;&nbsp;";
//
//					} else if (m.getKitchenStatus() == 2) {
//
//						s = s + "<a href='javascript:void(0)'"
//								+ "' onclick='' ><i class=\"fa fa-check-circle\" title=\"Ready To Delivered\" style=\"color:#090;cursor: context-menu;font-size:24px;\"></i></a>&nbsp;&nbsp;";
//					}
//				}
//
//				m.setAction(s);
//				s = "";
//
//			}
//			model.addAttribute("orderData", assignTS);
//			model.addAttribute("storeId", id);
//			JsonResponse<Object> res = new JsonResponse<Object>();
//
//			try {
//				res = restTemplate.getForObject(env.getKitchenUrl() + "getOrderSummary?id=" + id, JsonResponse.class);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			ObjectMapper mapper1 = new ObjectMapper();
//
//			List<KitchenItemDetailsModel> itemSummery = mapper1.convertValue(res.getBody(),
//					new TypeReference<List<KitchenItemDetailsModel>>() {
//					});
//			model.addAttribute("itemSummery", itemSummery);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : getKitchenStaffOrderDetails ends");
//		return "kitchen/gocool-get-kitchen-order-status";
//	}

	/**
	 * Web Controller - Get details For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/restaurant/kitchen-staff-order-details-modal" })
	public @ResponseBody JsonResponse<Object> modalQuotation(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : summaryModal starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getKitchenUrl() + "getOrderSummary?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : summaryModal ends");
		return res;
	}

	/**
	 * for dashboard index page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/index-get-function-list/{id}")
	public String getMenuDetails(Model model, HttpSession session, @PathVariable String id) {
		logger.info("Method : index starts");

		List<Module> module = new ArrayList<Module>();
		List<Function> funDetails = new ArrayList<Function>();
		String activityUrl = "";
		try {

			module = (List<Module>) session.getAttribute("MENU");
			if (module != null && module.size() > 0) {
				List<Module> fList = module.stream().filter(s -> s.getModuleId().equals(id))
						.collect(Collectors.toList());
				if (!fList.isEmpty()) {
					funDetails = fList.get(0).getModule();
					for (Function a : funDetails) {
						if (a.getFunction() != null) {
							a.setDefaultUrl(a.getFunction().get(0).getActivity());
						}

					}
				}

				session.setAttribute("funList", funDetails);
				session.setAttribute("moduleId", id);
				if (!funDetails.isEmpty()) {
					activityUrl = funDetails.get(0).getFunction().get(0).getActivity();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : index  extend-index ends");

		// return "extend-index2";
		return "redirect:" + activityUrl;

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/index-get-activity-list" })
	public @ResponseBody JsonResponse<List<Activity>> getActivityList(@RequestParam String funId,
			@RequestParam String moduleId, HttpSession session) {
		logger.info("Method : getActivityList starts");

		JsonResponse<List<Activity>> res = new JsonResponse<List<Activity>>();
		List<Activity> activityList = new ArrayList<Activity>();

		try {
			List<Module> module = new ArrayList<Module>();
			try {

				module = (List<Module>) session.getAttribute("MENU");
				if (module != null && module.size() > 0) {
					List<Module> fList = module.stream().filter(s -> s.getModuleId().equals(moduleId))
							.collect(Collectors.toList());
					if (fList != null) {
						List<Function> funDetails = (List<Function>) fList.get(0).getModule().stream()
								.filter(a -> a.getFunctionId().equals(funId)).collect(Collectors.toList());
						activityList = funDetails.get(0).getFunction();
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			res.setBody(activityList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getActivityList ends");
		return res;
	}

	/**
	 * for dashboard index page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/index-get-function-list-resp")
	public @ResponseBody JsonResponse<List<Function>> getFunctionDetails(Model model, HttpSession session,
			@RequestParam String moduleId) {
		logger.info("Method : index starts");

		JsonResponse<List<Function>> res = new JsonResponse<List<Function>>();
		List<Activity> activityList = new ArrayList<Activity>();

		List<Module> module = new ArrayList<Module>();
		List<Function> funDetails = new ArrayList<Function>();
		String activityUrl = "";
		try {

			module = (List<Module>) session.getAttribute("MENU");
			if (module != null && module.size() > 0) {
				List<Module> fList = module.stream().filter(s -> s.getModuleId().equals(moduleId))
						.collect(Collectors.toList());
				if (!fList.isEmpty()) {
					funDetails = fList.get(0).getModule();
					for (Function a : funDetails) {
						if (a.getFunction() != null) {
							a.setDefaultUrl(a.getFunction().get(0).getActivity());
							a.setDefaultUrlId(a.getFunction().get(0).getActivityId());
						}

					}
				}

				session.setAttribute("funList", funDetails);
				session.setAttribute("moduleId", moduleId);
				if (!funDetails.isEmpty()) {
					activityUrl = funDetails.get(0).getFunction().get(0).getActivity();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.setBody(funDetails);
		logger.info("Method : index  extend-index ends");

		// return "extend-index2";
		return res;

	}

	
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/index-get-module-list" })
	public @ResponseBody JsonResponse<List<Module>> getModuleDetails(Model model, HttpSession session,
			@RequestParam String moduleId) {
		logger.info("Method : getModuleDetails starts");

		JsonResponse<List<Module>> res = new JsonResponse<List<Module>>();

		List<Module> module = (List<Module>) session.getAttribute("MENU");

		res.setBody(module);
		logger.info("Method : getModuleDetails ends");
 
		return res;

	}
	
}
