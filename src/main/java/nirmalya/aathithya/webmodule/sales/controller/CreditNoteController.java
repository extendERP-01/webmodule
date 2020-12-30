package nirmalya.aathithya.webmodule.sales.controller;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.sales.model.CreditNoteModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "sales")
public class CreditNoteController {

	Logger logger = LoggerFactory.getLogger(CreditNoteController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * Default 'View Credit Note' page
	 *
	 */
	@GetMapping("/view-credit-note")
	public String viewCreditNote(Model model, HttpSession session) {

		logger.info("Method : viewCreditNote starts");

		logger.info("Method : viewCreditNote ends");
		return "sales/view-credit-note";
	}
	
	/**
	 * Web Controller - Get Credit Note Details
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-credit-note-through-ajax")
	public @ResponseBody DataTableResponse viewCreditNoteThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewCreditNoteThroughAjax starts");

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

			JsonResponse<List<CreditNoteModel>> jsonResponse = new JsonResponse<List<CreditNoteModel>>();

			jsonResponse = restClient.postForObject(env.getSalesUrl() + "getCreditNote", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<CreditNoteModel> creditNote = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<CreditNoteModel>>() {
					});

			String s = "";

			for (CreditNoteModel m : creditNote) {
				byte[] pId = Base64.getEncoder().encode(m.getCreditNote().getBytes());
				
				//m.setSaleInvReturn("<a href='javascript:void' onclick='pdfCreateSaleInvReturn(\"" + new String(pId) + "\")'>"
					//	+ m.getSaleInvReturn() + "</a>");
				
					s = s + " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";
					m.setAction(s);
					s = "";
				
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(creditNote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewCreditNoteThroughAjax ends");
		return response;
	}
	
	/**
	 * Web Controller - Get Credit Note For Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-credit-note-modal" })
	public @ResponseBody JsonResponse<Object> modalCreditNote(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalCreditNote starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getCreditNoteById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalCreditNote ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Credit Note List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-credit-note-getCreditNote" })
	public @ResponseBody JsonResponse<DropDownModel> getcreditNote(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getcreditNote starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getCreditNoteAutoSearch?id=" + searchValue,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getcreditNote ends");
		return res;
	}
	
	/**
	 * Web Controller - Get Sale Invoice Return List By AutoSearch
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-credit-note-getSaleInvReturn" })
	public @ResponseBody JsonResponse<DropDownModel> getsaleInvoiceReturn(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getsaleInvoiceReturn starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getSalesUrl() + "getSaleInvReturnAutoSearch?id=" + searchValue,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getsaleInvoiceReturn ends");
		return res;
	}
}
