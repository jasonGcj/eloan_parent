package com.eloan.mgrtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.query.PageResult;
import com.eloan.base.util.ResultJSON;
import com.eloan.business.domain.PlatformBankinfo;
import com.eloan.business.domain.PlatformBankinfoQueryObject;
import com.eloan.business.service.PlatformBankinfoService;




@Controller
public class PlatformBankinfoController {
	 
	      @Autowired
	     private PlatformBankinfoService platformBankinfoService;
	      
	      
	      /**
	       * 
	       * <p>Title: paltList</p>
	       * <p>Description: 后台列表展示</p>
	       * @param model
	       * @param q
	       * @return
	       */
	      @RequestMapping("/companyBank_list.do")
	      public String paltList(Model model,@ModelAttribute("q")PlatformBankinfoQueryObject q){
	    	  
	    	       // 查询列表
	    	       PageResult list = platformBankinfoService.selectPlat(q);
	    	       model.addAttribute("pageResult", list);
	    	          
	    	  
	    	  return "platformbankinfo/list";
	      }
	      
            
	       /**
	        * 
	        * <p>Title: paltList</p>
	        * <p>Description: 开户行修改保存</p>
	        * @param p
	        * @return
	        */
	      @RequestMapping("/companyBank_update.do")
	      @ResponseBody
	      public ResultJSON  paltList(PlatformBankinfo p){
	    	  
	    	      ResultJSON resultJSON = new ResultJSON();
	    	       
	    	      try {
					platformBankinfoService.saveOrUpdate(p);
					resultJSON.setSuccess(true);
					 
					
				} catch (Exception e) {
					resultJSON.setSuccess(false);
					    e.printStackTrace();
				}
			return resultJSON;
	      }
	      
	      
}
