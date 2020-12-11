/*
 * package nirmalya.aathithya.webmodule.account.controller;
 * 
 * import java.util.Arrays; import java.util.List;
 * 
 * import javax.servlet.http.HttpSession;
 * 
 * import org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.client.RestClientException; import
 * org.springframework.web.client.RestTemplate;
 * 
 * import nirmalya.aathithya.webmodule.common.utils.DropDownModel; import
 * nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles; import
 * nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
 * 
 * @Controller
 * 
 * @RequestMapping(value = "account") public class
 * AccountsTrialBalanceController {
 * 
 * Logger logger =
 * LoggerFactory.getLogger(AccountsTrialBalanceController.class);
 * 
 * @Autowired RestTemplate restClient;
 * 
 * @Autowired EnvironmentVaribles env;
 * 
 * @Autowired PdfGeneratatorUtil pdfGeneratorUtil;
 * 
 * 
 * get mapping for view payment voucher
 * 
 * @GetMapping("/view-trial-balance-report") public String
 * viewaBalanceSheet(Model model, HttpSession session) {
 * 
 * logger.info("Method : viewaBalanceSheet starts"); // for get cost center list
 * try { DropDownModel[] payMode = restClient.getForObject(env.getAccountUrl() +
 * "getCostCenterTB", DropDownModel[].class); List<DropDownModel> costCenterList
 * = Arrays.asList(payMode);
 * 
 * model.addAttribute("costCenterList", costCenterList);
 * 
 * } catch (RestClientException e) { e.printStackTrace(); }
 * 
 * logger.info("Method : viewaBalanceSheet ends"); return
 * "account/balance-sheet-report"; } }
 */