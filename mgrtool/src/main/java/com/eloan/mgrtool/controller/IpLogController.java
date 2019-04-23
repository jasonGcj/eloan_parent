package com.eloan.mgrtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eloan.base.query.IpLogQueryObject;
import com.eloan.base.query.PageResult;
import com.eloan.base.service.IIpLogService;
import com.eloan.base.util.AccessLimit;

@Controller
public class IpLogController extends BaseController {
	
	@Autowired
	private IIpLogService ipLogService;

	
	//private RedisService redisService;
	
	/**
	 * 让该接口被访问的频次控制在指定的范围内
	 * 
	 *    实现思路:
	 *    	对该接口被访问的频次做记录-------redis
	 *    	每次同一个用户或客户端进行访问时，就增加次数-----拦截器
	 *      若增加后超出了指定的次数，则拒绝处理
	 * 
	 * @param qo
	 * @param model
	 * @return
	 */
	@AccessLimit(needLogin=true,seconds=5,count=3)
	@RequestMapping("/iplog")
	public String ipLog(@ModelAttribute("qo") IpLogQueryObject qo, Model model) {
			
		qo.setLike(true);
		PageResult result = this.ipLogService.query(qo);
		model.addAttribute("pageResult", result);
		return "ipLog/list";
	}
	
	
}
