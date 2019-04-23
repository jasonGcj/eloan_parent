package com.eloan.mgrtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.util.ResultJSON;
import com.eloan.business.query.UserFileAuthQueryObject;
import com.eloan.business.service.IUserFileService;

@Controller
public class UserFileController extends BaseController {

	
	
	
	@Autowired
	private IUserFileService userFileService;


	@RequestMapping("userFileAuth")
	public String realAuth(@ModelAttribute("qo") UserFileAuthQueryObject qo,
			Model model) {
		model.addAttribute("pageResult", this.userFileService.query(qo));
		return "/userFileAuth/list";
	}

	@RequestMapping("userFileAuth_audit")
	@ResponseBody
	public ResultJSON userFileAuthAudit(Long id, String remark, int state,int score) {
		ResultJSON json = new ResultJSON();
		try {
			this.userFileService.audit(id, remark, state, score);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setMsg(e.getMessage());
		}
		return json;

	}

}
