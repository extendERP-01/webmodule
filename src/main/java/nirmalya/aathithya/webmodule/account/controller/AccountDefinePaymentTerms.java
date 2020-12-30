package nirmalya.aathithya.webmodule.account.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.account.model.DefinePaymentTermsModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.recruitment.model.JobTitleModel;

@Controller
@RequestMapping(value = "account")
public class AccountDefinePaymentTerms {

	Logger logger = LoggerFactory.getLogger(AccountDefinePaymentTerms.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;
	
	@SuppressWarnings("unchecked")
	@GetMapping("/define-payment-terms")
	public String viewDefineTerms(Model model, HttpSession session) {
		logger.info("Method : viewDefineTerms starts");
		
		JsonResponse<DefinePaymentTermsModel> res = new JsonResponse<DefinePaymentTermsModel>();
		DefinePaymentTermsModel jobForm = new DefinePaymentTermsModel();
		
		try {
			res = restTemplate.getForObject(env.getAccountUrl() + "edit-details", JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		jobForm = mapper.convertValue(res.getBody(), DefinePaymentTermsModel.class);
		session.setAttribute("message", "");
		System.out.println(jobForm);
		if(jobForm != null) {
			model.addAttribute("jobForm", jobForm);
			model.addAttribute("termId", jobForm.getTermId());
		} else {
			model.addAttribute("jobForm", new DefinePaymentTermsModel() );
			model.addAttribute("termId","");
		}
		
		logger.info("Method : viewDefineTerms ends");
		return "account/add-define-payments-terms";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "define-payment-terms-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addJobDetailsPost(@RequestBody DefinePaymentTermsModel terms, Model model,
			HttpSession session) {
		logger.info("Method : addPatientDetailsPost function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			terms.setCreatedBy(userId);

			res = restTemplate.postForObject(env.getAccountUrl() + "addTerms", terms,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addJobDetailsPost ends");
		return res;
	}
	
}
