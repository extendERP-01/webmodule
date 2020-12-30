package nirmalya.aathithya.webmodule.asset.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = "recruitment/")
public class RecrumentDemoController {
	Logger logger = LoggerFactory.getLogger(RecrumentDemoController.class);

	@GetMapping("view-action")
	public String generateInventoryStockReport(Model model, HttpSession session) {

		logger.info("Method : generateInventoryStockReport starts");
		model.addAttribute("pageName", "Action Page Coming Soon");
		logger.info("Method : generateInventoryStockReport ends");
		return "recruitment/view-action";

	}

	/*
	 * @GetMapping("employee") public String recurementEmployee(Model model,
	 * HttpSession session) {
	 * 
	 * logger.info("Method : recurementEmployee starts");
	 * model.addAttribute("pageName", "Employee Page Coming Soon");
	 * logger.info("Method : recurementEmployee ends"); return
	 * "/new-employee/review";
	 * 
	 * }
	 */
	@GetMapping("time-sheet")
	public String recurementTimeSheet(Model model, HttpSession session) {

		logger.info("Method : recurementTimeSheet starts");
		model.addAttribute("pageName", "Time Sheet Page Coming Soon");
		logger.info("Method : recurementTimeSheet ends");
		return "recruitment/view-action";

	}

	@GetMapping("exit")
	public String recurementExit(Model model, HttpSession session) {

		logger.info("Method : recurementExit starts");
		model.addAttribute("pageName", "Exit Page Coming Soon");
		logger.info("Method : recurementExit ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-plan")
	public String recurementPlan(Model model, HttpSession session) {

		logger.info("Method : recurementPlan starts");
		model.addAttribute("pageName", "Plan Page Coming Soon");
		logger.info("Method : recurementPlan ends");
		return "recruitment/view-action";

	}

	@GetMapping("set-goal")
	public String recurementSetGoal(Model model, HttpSession session) {

		logger.info("Method : recurementSetGoal starts");
		model.addAttribute("pageName", "Set Goal Coming Soon");
		logger.info("Method : recurementSetGoal ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-outcome")
	public String recurementViewOutCome(Model model, HttpSession session) {

		logger.info("Method : recurementViewOutCome starts");
		model.addAttribute("pageName", "OutCome Coming Soon");
		logger.info("Method : recurementViewOutCome ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-payrole")
	public String viewPayRole(Model model, HttpSession session) {

		logger.info("Method : viewPayRole starts");
		model.addAttribute("pageName", "Payrole Coming Soon");
		logger.info("Method : viewPayRole ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-advance")
	public String viewAdvance(Model model, HttpSession session) {

		logger.info("Method : viewAdvance starts");
		model.addAttribute("pageName", "Advance Coming Soon");
		logger.info("Method : viewAdvance ends");
		return "recruitment/view-action";

	}

	@GetMapping("view-expense")
	public String viewExpense(Model model, HttpSession session) {

		logger.info("Method : viewExpense starts");
		model.addAttribute("pageName", "Expense Coming Soon");
		logger.info("Method : viewExpense ends");
		return "recruitment/view-action";

	}
}
