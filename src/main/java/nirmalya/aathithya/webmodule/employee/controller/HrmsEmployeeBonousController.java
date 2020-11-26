package nirmalya.aathithya.webmodule.employee.controller;
 
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeBonousModel; 

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeBonousController {

	Logger logger = LoggerFactory.getLogger(HrmsEmployeeBonousController.class);

	private static final String MESSAGE = "message";
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee dependent
	 */

	@GetMapping("/add-employee-bonous")
	public String addProduction(Model model, HttpSession session) {

		logger.info("Method : addProduction starts");

		HrmsEmployeeBonousModel productionModel = new HrmsEmployeeBonousModel();
		HrmsEmployeeBonousModel tripBonusModel = (HrmsEmployeeBonousModel) session.getAttribute("tripBonusModel");

		String message = (String) session.getAttribute(MESSAGE);

		if (message != null && !message.isEmpty()) {
			model.addAttribute(MESSAGE, message);

		}

		session.setAttribute(MESSAGE, "");

		if (tripBonusModel != null) {
			model.addAttribute("productionModel", tripBonusModel);
			session.setAttribute("productionModel", null);
		} else {
			model.addAttribute("productionModel", productionModel);
		}

		logger.info("Method : addProduction ends");
		return "employee/add-employee-trip-bonous";
	}

	/**
	 * Web controller add new deliveryChalanModel data
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-employee-bonous-ajax")
	public @ResponseBody JsonResponse<Object> addProduction(
			@RequestBody List<HrmsEmployeeBonousModel> hrmsEmployeeBonousModelList, HttpSession session) {

		logger.info("Method : addProduction starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {

			String userId = (String) session.getAttribute("USER_ID");
			for (HrmsEmployeeBonousModel r : hrmsEmployeeBonousModelList) {
				r.setCreatedBy(userId);

			}

			resp = restClient.postForObject(env.getEmployeeUrl() + "add-trip-bonous", hrmsEmployeeBonousModelList,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = resp.getMessage();

		if (message != null && message != "") {
			resp.setMessage("UnSuccess");
		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : addProduction function Ends");
		return resp;
	}

	/*
	 * Get Mapping view employee dependent
	 */
	@GetMapping("/view-employee-trip-bonous")
	public String viewProductionMix(Model model, HttpSession session) {

		logger.info("Method : viewProductionMix   starts");

		logger.info("Method : viewProductionMix  ends");

		return "employee/view-employee-trip-bonous";
	}

	/*
	 * For view mother-coil-slit throughAjax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-trip-bonous-ThroughAjax")
	public @ResponseBody DataTableResponse viewMotherCoilSlitAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

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
			JsonResponse<List<HrmsEmployeeBonousModel>> jsonResponse;

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "view-trip-bonous-details", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeBonousModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeBonousModel>>() {
					});

			String s = "";

			for (HrmsEmployeeBonousModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getFromDate().getBytes());
				byte[] encodeTo = Base64.getEncoder().encode(m.getFromDate().getBytes());

				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ m.getFromDate()
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a> &nbsp; &nbsp; ";

				/*
				 * s = s + "<a href='view-employee-trip-bonous-edit?id=" + new String(encodeId)
				 * + "&toDate=" + new String(encodeTo) +
				 * "' ><i class=\"fa fa-edit\" ></i></a>&nbsp;&nbsp";
				 */

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

	@SuppressWarnings("unchecked")
	@PostMapping("/view-employee-trip-bonous-modalView")
	public @ResponseBody JsonResponse<List<Object>> modalPipeProductionbMixProduction(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalPipeProductionMix starts");
		JsonResponse<List<Object>> response = new JsonResponse<List<Object>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getTripDetailsById?date=" + index,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalPipeProductionMix ends ");
		return response;
	}

	/*
	 * for Edit assign Edu
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping("/view-employee-language-edit") public String
	 * editAssignLanguage(Model model, @RequestParam("toDate") String encodeId,
	 * 
	 * @RequestParam("fromDate") String encodeId2, HttpSession session) {
	 * 
	 * logger.info("Method :editAssignLanguage starts");
	 * 
	 * byte[] fromDate = Base64.getDecoder().decode(encodeId.getBytes()); byte[]
	 * toDateEnc = Base64.getDecoder().decode(encodeId.getBytes());
	 * 
	 * String id = (new String(fromDate)); String toDate = (new String(toDateEnc));
	 * JsonResponse<List<HrmsEmployeeBonousModel>> response = new
	 * JsonResponse<List<HrmsEmployeeBonousModel>>(); try {
	 * 
	 * response = restClient.getForObject( env.getEmployeeUrl() +
	 * "getTripDetailsById?fromDate=" + id + "&toDate=" + toDate,
	 * JsonResponse.class);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * List<HrmsEmployeeBonousModel> tripBonusModelList =
	 * mapper.convertValue(response.getBody(), new
	 * TypeReference<List<HrmsEmployeeBonousModel>>() { }); if (tripBonusModelList
	 * != null) { tripBonusModelList.get(0).setBonousId("edit"); }
	 * 
	 * model.addAttribute("tripBonusModel", tripBonusModelList);
	 * model.addAttribute("Edit", "For Edit"); } catch (RestClientException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * logger.info("Method : editAssignLanguage ends");
	 * 
	 * return "employee/add-employee-Language"; }
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-employee-bonous-get-trip-details" })
	public @ResponseBody JsonResponse<HrmsEmployeeBonousModel> getBatchDetails(@RequestParam String toDate,
			@RequestParam String fromDate) {
		logger.info("Method : getBatchDetails starts");

		JsonResponse<HrmsEmployeeBonousModel> res = new JsonResponse<HrmsEmployeeBonousModel>();

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "rest-get-trip-details?toDate=" + toDate + "&fromDate=" + fromDate,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("UnSuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : getBatchDetails ends");
		return res;
	}
}
