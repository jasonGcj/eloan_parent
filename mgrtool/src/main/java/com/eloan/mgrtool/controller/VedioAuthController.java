package com.eloan.mgrtool.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.service.ILogininfoService;
import com.eloan.base.util.ResultJSON;
import com.eloan.business.query.VedioAuthQueryObject;
import com.eloan.business.service.IVedioAuthService;

@Controller
public class VedioAuthController extends BaseController {

	@Autowired
	private IVedioAuthService vedioAuthService;
	
	@Autowired
	private ILogininfoService logininfoService;

	@RequestMapping("vedioAuth")
	public String vedioAuth(@ModelAttribute("qo") VedioAuthQueryObject qo,
			Model model) {
		model.addAttribute("pageResult", this.vedioAuthService.query(qo));
		return "vedioAuth/list";
	}

	@RequestMapping("vedioAuth_audit")
	@ResponseBody
	public ResultJSON vedioAuthAudit(Long loginInfoValue, String remark,
			int state) {
		ResultJSON json = new ResultJSON();
		try {
			this.vedioAuthService.audit(loginInfoValue,remark,state);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	@RequestMapping("vedioauth_autocomplate")
	@ResponseBody
	public List<Map<String,Object>> autoComplate(String word){
		return this.logininfoService.autoComplate(word,Logininfo.USERTYPE_NORMAL);
	}
}
