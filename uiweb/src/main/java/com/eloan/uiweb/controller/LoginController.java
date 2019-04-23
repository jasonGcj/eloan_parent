package com.eloan.uiweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.service.ILogininfoService;
import com.eloan.base.util.ResultJSON;
import com.eloan.base.util.UserContext;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private ILogininfoService loginInfoService;

	@RequestMapping("/login")
	@ResponseBody
	public ResultJSON login(@RequestParam String username, String password,HttpServletRequest request) {
		ResultJSON json = new ResultJSON();
		Logininfo login = this.loginInfoService.login(username, password,
				Logininfo.USERTYPE_NORMAL,request.getRemoteAddr());
		if (login != null) {
			json.setSuccess(true);
		} else {
			json.setMsg("用户名或者密码错误!");
		}
		return json;
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public ResultJSON logout(HttpSession session){
		Logininfo current = UserContext.getCurrent();
		ResultJSON json  = new ResultJSON();
		if(current != null){
			session.removeAttribute(UserContext.LOGIN_IN_SESSION);
			json.setMsg("退出登录！");
			json.setSuccess(true);
		}else{
			json.setMsg("用户未登录！");
			json.setSuccess(false);
		}
		return json;
	}

}
