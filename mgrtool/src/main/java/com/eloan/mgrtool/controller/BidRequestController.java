package com.eloan.mgrtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.query.PageResult;
import com.eloan.base.util.ResultJSON;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.BidRequestQueryObject;
import com.eloan.business.service.IBidRequestService;
import com.eloan.business.util.BidConst;



/**
 * 
 * <p>Title: BidRequestController</p>
 * <p>Description:借款申请 </p>
 */
@Controller
public class BidRequestController {
	 
	      
       @Autowired
       private  IBidRequestService bidRequestService;
        
        
        /**
         * 
         * <p>Title: bidRequestList</p>
         * <p>Description: 后台审核列表展示</p>
         * @param model
         * @param q
         * @return
         */
       @RequestMapping("/bidrequest_publishaudit_list.do")
       public String bidRequestList(Model model,@ModelAttribute("q")BidRequestQueryObject q){
    	   
	    	   
	    	// 设置状态 为待发布
	    	q.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
	    	    
	         // 通过封装类 查询展示
	        PageResult pageResult =  bidRequestService.selectBidRequestList(q);
	        
	        model.addAttribute("pageResult", pageResult);
    	   
    	   return "bidrequest/publish_audit";
       }
       
       
       /**
        * 
        * <p>Title: bidrequestPublishaudit</p>
        * <p>Description:发标审核 </p>
        * @return
        */
       @RequestMapping("/bidrequest_publishaudit.do")
       @ResponseBody
       public ResultJSON bidrequestPublishaudit(String remark,int state,Long id){
    	   
    	       ResultJSON resultJSON = new ResultJSON();
    	   
    	   try {
			bidRequestService.addBidRequest(remark,state,id,UserContext.getCurrent());
			resultJSON.setSuccess(true);
		} catch (Exception e) {
			resultJSON.setSuccess(false);
			
			e.printStackTrace();
		}
    	   
    	   return resultJSON;
       }
       
       
       
        /**
         * 
         * <p>Title: bidrequestAuditListAndOne</p>
         * <p>Description: 满一审</p>
         * @param model
         * @param q
         * @return
         */
       @RequestMapping("bidrequest_audit1_list.do")
       public String bidrequestAuditListAndOne(Model model,@ModelAttribute("q")BidRequestQueryObject q){
    	   

    	     // 设置状态 为满标一审
    	   	q.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
    	      
	       // 通过封装类 查询展示
	        PageResult pageResult =  bidRequestService.selectBidRequestList(q);
	        
	        model.addAttribute("pageResult", pageResult);
    	   
    	   return "bidrequest/audit1";
       }
       
       
        /**
         * 
         * <p>Title: bidUpdateAndOne</p>
         * <p>Description: 满一审修改</p>
         * @param id
         * @param state
         * @param remark
         * @return
         */
       @RequestMapping("/bidrequest_audit1.do")
       @ResponseBody
       public ResultJSON bidUpdateAndOne(Long id,int state,String remark){
    	       
    	      // 创建返回对象
    	       ResultJSON resultJSON = new ResultJSON();
    	        
    	       try {
    	    	     // 进行修改
				bidRequestService.bidUpdateAndOne(id, state, remark,UserContext.getCurrent());
				resultJSON.setSuccess(true);
			} catch (Exception e) {
				  e.printStackTrace();
				  resultJSON.setSuccess(false);
			}
		return resultJSON;
       }
       
       
       /**
         * 
         * <p>Title: bidrequestAuditListAndOne</p>
         * <p>Description: 满二审</p>
         * @param model
         * @param q
         * @return
         */
       @RequestMapping("bidrequest_audit2_list.do")
       public String bidrequestAuditListAndTwo(Model model,@ModelAttribute("q")BidRequestQueryObject q){
    	   

    	     // 设置状态 为满标二审
    	   q.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
    	      
	       // 通过封装类 查询展示
	        PageResult pageResult =  bidRequestService.selectBidRequestList(q);
	        
	        model.addAttribute("pageResult", pageResult);
    	   
    	   return "bidrequest/audit2";
       }

       /**
         * 
         * <p>Title: bidUpdateAndOne</p>
         * <p>Description: 满二审修改</p>
         * @param id
         * @param state
         * @param remark
         * @return
         */
       @RequestMapping("/bidrequest_audit2.do")
       @ResponseBody
       public ResultJSON bidUpdateTwo(Long id,int state,String remark){
    	       
    	      // 创建返回对象
    	       ResultJSON resultJSON = new ResultJSON();
    	        
    	       try {
    	    	     // 进行修改
				bidRequestService.bidUpdateAndTwo(id, state, remark,UserContext.getCurrent());
				resultJSON.setSuccess(true);
			} catch (Exception e) {
				  e.printStackTrace();
				  resultJSON.setSuccess(false);
			}
		return resultJSON;
       }

	/**
	 *
	 * <p>Title: bidrequestAuditListAndOne</p>
	 * <p>Description: 满三审</p>
	 * @param model
	 * @param q
	 * @return
	 */
	@RequestMapping("bidrequest_audit3_list.do")
	public String bidrequestAuditListAndThree(Model model,@ModelAttribute("q")BidRequestQueryObject q){
		System.out.println("bidrequest_audit3_list");
		// 设置状态 为满标三审
		q.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_3);

		// 通过封装类 查询展示
		PageResult pageResult =  bidRequestService.selectBidRequestList(q);

		model.addAttribute("pageResult", pageResult);

		return "bidrequest/audit3";
	}

	/**
	 *
	 * <p>Title: bidUpdateAndOne</p>
	 * <p>Description: 满三审修改</p>
	 * @param id
	 * @param state
	 * @param remark
	 * @return
	 */
	@RequestMapping("/bidrequest_audit3.do")
	@ResponseBody
	public ResultJSON bidUpdateThree(Long id,int state,String remark){
		System.out.println("bidrequest_audit3");
		// 创建返回对象
		ResultJSON resultJSON = new ResultJSON();

		try {
			// 进行修改
			bidRequestService.bidUpdateAndThree(id, state, remark,UserContext.getCurrent());
			resultJSON.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			resultJSON.setSuccess(false);
		}
		return resultJSON;
	}

}
