/**
 * web Controller for property AssignConsume Item
 * 
 *//*
	 * package nirmalya.aathithya.webmodule.property.controller;
	 * 
	 * import java.util.Arrays; import java.util.List;
	 * 
	 * import javax.servlet.http.HttpSession;
	 * 
	 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
	 * org.springframework.beans.factory.annotation.Autowired; import
	 * org.springframework.stereotype.Controller; import
	 * org.springframework.ui.Model; import
	 * org.springframework.validation.BindingResult; import
	 * org.springframework.web.bind.annotation.GetMapping; import
	 * org.springframework.web.bind.annotation.ModelAttribute; import
	 * org.springframework.web.bind.annotation.PostMapping; import
	 * org.springframework.web.bind.annotation.RequestBody; import
	 * org.springframework.web.bind.annotation.RequestMapping; import
	 * org.springframework.web.bind.annotation.ResponseBody; import
	 * org.springframework.web.client.RestClientException; import
	 * org.springframework.web.client.RestTemplate;
	 * 
	 * import nirmalya.aathithya.webmodule.common.utils.DropDownModel; import
	 * nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles; import
	 * nirmalya.aathithya.webmodule.common.utils.JsonResponse; import
	 * nirmalya.aathithya.webmodule.property.model.PropertyAssignItemModel; import
	 * nirmalya.aathithya.webmodule.property.model.PropertyMasterModel;
	 * 
	 * @Controller
	 * 
	 * @RequestMapping(value = "property") public class PropertyAssignItemController
	 * { Logger logger =
	 * LoggerFactory.getLogger(PropertyAssignItemController.class);
	 * 
	 * @Autowired RestTemplate restClient;
	 * 
	 * @Autowired EnvironmentVaribles env;
	 * 
	 * 
	 * GEt Mapping for Add property assign item view page
	 * 
	 * @GetMapping("/add-property-assignitem") public String addAssignItem(Model
	 * model, HttpSession session) { logger.info("Method : addAssignItem starts");
	 * PropertyAssignItemModel assignItem = new PropertyAssignItemModel();
	 * 
	 * PropertyAssignItemModel form = (PropertyAssignItemModel)
	 * session.getAttribute("sassignItem");
	 * 
	 * String message = (String) session.getAttribute("message"); if (message !=
	 * null && message != "") { model.addAttribute("message", message); }
	 * 
	 * session.setAttribute("message", "");
	 * 
	 * if (form != null) { model.addAttribute("assignItem", form);
	 * session.setAttribute("sassignItem", null); } else {
	 * model.addAttribute("assignItem", assignItem); }
	 * 
	 * dropdown data for property category
	 * 
	 * try { DropDownModel[] propertyCategory =
	 * restClient.getForObject(env.getPropertyUrl() + "getCategoryName",
	 * DropDownModel[].class);
	 * 
	 * List<DropDownModel> categoryList = Arrays.asList(propertyCategory);
	 * model.addAttribute("categoryList", categoryList); } catch
	 * (RestClientException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * for viewing drop down list for item Category
	 * 
	 * try { DropDownModel[] iteCategory =
	 * restClient.getForObject(env.getPropertyUrl() + "getItemCategoryName",
	 * DropDownModel[].class); List<DropDownModel> itemCategoryList =
	 * Arrays.asList(iteCategory); model.addAttribute("itemCategoryList",
	 * itemCategoryList); } catch (RestClientException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * logger.info("Method : addPropertyMaster end"); return
	 * "property/assignConsumedItemToProperty"; }
	 * 
	 * for drop down Sub Category item list
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/getSubCategoryItemList" }) public @ResponseBody
	 * JsonResponse<DropDownModel> getSubCategoryItemList(Model model, @RequestBody
	 * String index, BindingResult result) {
	 * logger.info("Method : getSubCategoryItemList starts");
	 * 
	 * JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>(); try {
	 * res = restClient.getForObject(env.getPropertyUrl() +
	 * "getSubCategoryItemList?proCategoryId=" + index, JsonResponse.class); } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * if (res.getMessage() != null) {
	 * System.out.println("if block getmsg() not false : " + res.getMessage());
	 * res.setCode(res.getMessage()); res.setMessage("Unsuccess"); } else {
	 * res.setMessage("success"); }
	 * 
	 * logger.info("Method : getSubCategoryItemList ends"); return res; }
	 * 
	 * for drop down item list
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/getItemList" }) public @ResponseBody
	 * JsonResponse<DropDownModel> getItemList(Model model, @RequestBody String
	 * index, BindingResult result) {
	 * logger.info("Method : getSubCategoryItemList starts");
	 * 
	 * JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
	 * 
	 * try { res = restClient.getForObject(env.getPropertyUrl() +
	 * "getItemList?ItemSubCat=" + index, JsonResponse.class); } catch (Exception e)
	 * { e.printStackTrace(); }
	 * 
	 * if (res.getMessage() != null) {
	 * System.out.println("if block getmsg() not false : " + res.getMessage());
	 * res.setCode(res.getMessage()); res.setMessage("Unsuccess"); } else {
	 * res.setMessage("success"); }
	 * 
	 * logger.info("Method : getSubCategoryItemList ends"); return res; }
	 * 
	 * 
	 * post Mapping for submit data 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping("/post-property-assignitem") public String
	 * postPropertyAssignItem(@ModelAttribute PropertyAssignItemModel assignItem,
	 * Model model, HttpSession session) {
	 * logger.info("Method : postPropertyAssignItem starts");
	 * 
	 * JsonResponse<Object> resp = new JsonResponse<Object>(); try { resp =
	 * restClient.postForObject(env.getPropertyUrl() + "addPropertyAssignItem",
	 * assignItem, JsonResponse.class);
	 * 
	 * } catch (RestClientException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } if (resp.getMessage() != "") {
	 * session.setAttribute("message", resp.getMessage());
	 * session.setAttribute("sassignItem", assignItem); return
	 * "redirect:/property/add-property-assignitem";
	 * 
	 * } session.setAttribute("spropertyMaster", null);
	 * logger.info("Method : postPropertyMaster end"); return
	 * "redirect:/property/view-property-assignitem"; }
	 * 
	 * @GetMapping("/view-property-assignitem") public String viewHotel(Model model,
	 * HttpSession session) { return "property/listAssignConsumedItemToProperty"; }
	 * }
	 */