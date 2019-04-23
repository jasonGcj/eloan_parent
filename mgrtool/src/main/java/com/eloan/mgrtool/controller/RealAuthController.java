package com.eloan.mgrtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.util.ResultJSON;
import com.eloan.business.query.RealAuthQueryObject;
import com.eloan.business.service.IRealAuthService;

@Controller
public class RealAuthController extends BaseController {
	@Autowired
	private IRealAuthService realAuthService;

	@RequestMapping("realAuth")
	public String realAuth(@ModelAttribute("qo") RealAuthQueryObject qo,
			Model model) {
		model.addAttribute("pageResult", this.realAuthService.query(qo));
		return "/realAuth/list";
	}

	@RequestMapping("realAuth_audit")
	@ResponseBody
	public ResultJSON realAuthAudit(Long id, String remark, int state) {
		ResultJSON json = new ResultJSON();
		try {
			this.realAuthService.audit(id, remark, state);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setMsg(e.getMessage());
		}
		return json;

	}

}
