package com.eloan.uiweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eloan.base.query.PageResult;
import com.eloan.business.domain.BidRequestQueryObject;
import com.eloan.business.service.IBidRequestService;

/**
 * 投资模块
 * @author Administrator
 *
 */
@Controller
public class InvestController extends BaseController {


	@Autowired
	private IBidRequestService iBidRequestService;

	/**
	 * 我要投资  借款标列表
	 * @return
	 */
	@RequestMapping("/invest_list")
	public String investList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model) {
		
		PageResult result = iBidRequestService.selectBidRequestList(qo);
		model.addAttribute("pageResult", result);
		return "invest";
	}
	
	
	
	
	
	
}
