package nirmalya.aathithya.webmodule.inventory.controller;


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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.inventory.model.InventoryDebitNoteModel;

/**
* @author NirmalyaLabs
*
*/
@Controller
@RequestMapping(value = { "inventory" })
public class InventoryDeebitNoteController {
	Logger logger = LoggerFactory.getLogger(InventoryDeebitNoteController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;
	
	/*
	 * 
	 * GetMApping For Listing itemCategory
	 * 
	 * 
	 */
	@GetMapping(value = { "view-debit-note" })
	public String viewItem(Model model) {
		logger.info("Method : view debit node starts");
		JsonResponse<Object> item = new JsonResponse<Object>();
		model.addAttribute("item", item);
		logger.info("Method : view debit node end");
		return "inventory/viewDebitNote.html";
	}

	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-debit-note-throughAjax" })
	public @ResponseBody DataTableResponse viewItemCategory(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewItemCategory starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<InventoryDebitNoteModel>> jsonResponse = new JsonResponse<List<InventoryDebitNoteModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getInventoryUrl() + "get-all-debit-note",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<InventoryDebitNoteModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<InventoryDebitNoteModel>>() {
					});
				  for (InventoryDebitNoteModel m : form) { 
			if (m.getdActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewItemCategory ends");
		return response;
	}
}
