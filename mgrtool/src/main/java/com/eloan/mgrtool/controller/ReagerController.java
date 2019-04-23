package com.eloan.mgrtool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.query.PageResult;
import com.eloan.base.util.ResultJSON;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.PlatformBankinfo;
import com.eloan.business.domain.RechargeOfflineQueryObject;
import com.eloan.business.service.PlatformBankinfoService;
import com.eloan.business.service.RechargeService;



// 后台管理充值
@Controller
public class ReagerController { 
	  
	 
	    @Autowired
	    private RechargeService rechargeService;
	    
	    
	    @Autowired
	    private PlatformBankinfoService platformBankinfoService;
	     
	    
	    /**
	     * 
	     * <p>Title: rechargeOffline</p>
	     * <p>Description: 充值审核列表</p>
	     * @param model
	     * @return
	     */
	    @RequestMapping("/rechargeOffline.do")
	    public String rechargeOffline(Model model,@ModelAttribute("q")RechargeOfflineQueryObject q){
	    	  
	    	      //查询列表
	    	     PageResult list = rechargeService.selecRchargetList(q);
	    	     
	    	      // 账户信息
	    	     List<PlatformBankinfo> au = platformBankinfoService.selectAll();
	    	     
	    	     model.addAttribute("pageResult",list);
	    	     model.addAttribute("banks",au);
	    	
	    	return "rechargeOffline/list";
	    }
	    
	    /**
	     * 
	     * <p>Title: checkedRed</p>
	     * <p>Description: 修改审核</p>
	     * @param id
	     * @param state
	     * @param remark
	     * @return
	     */
	    @RequestMapping("/rechargeOffline_audit.do")
	    @ResponseBody
	    public ResultJSON checkedRed(Long id, int state, String remark){
	    	    
	    	     // 创建一个返回对象
	    	       ResultJSON resultJSON = new ResultJSON();
	    	   try {
				rechargeService.checkedRed(id,state,remark,UserContext.getCurrent());
				resultJSON.setSuccess(true);
			} catch (Exception e) {
				  
				resultJSON.setSuccess(false);
				e.printStackTrace();
			}
	    	
	    	return resultJSON;
	    }

}
