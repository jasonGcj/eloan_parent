package com.eloan.uiweb.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.PageResult;
import com.eloan.base.util.ResultJSON;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.Account;
import com.eloan.business.domain.BidQueryObject;
import com.eloan.business.domain.BidRequest;
import com.eloan.business.domain.BidRequestQueryObject;
import com.eloan.business.domain.Realauth;
import com.eloan.business.domain.Userfile;
import com.eloan.business.domain.Userinfo;
import com.eloan.business.query.UserFileAuthQueryObject;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.IBidRequestService;
import com.eloan.business.service.IRealAuthService;
import com.eloan.business.service.IUserFileService;
import com.eloan.business.service.IUserService;
import com.eloan.business.util.BidConst;

/**
 * 借款模块
 * @author Administrator
 *
 */
@Controller
public class BorrowController extends BaseController {

	@Autowired
	private IAccountService accountService;

	@Autowired
	private IUserService userService;
	@Autowired
	private IBidRequestService iBidRequestService;
	
	@Autowired
	private IRealAuthService realAuthService;
	@Autowired
    private IUserFileService userFileService;
	/**
	 * 我要借款
	 * @return
	 */
	@RequestMapping("/borrow")
	public String borrowIndex(Model model) {
		Logininfo current = UserContext.getCurrent();
		if (current == null) {
			return "redirect:borrow.html";
		}
		model.addAttribute("account", this.accountService.get(current.getId()));
		model.addAttribute("userinfo", this.userService.get(current.getId()));
		model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
		return "borrow";
	}
	

	@RequestMapping("borrowInfo.do")
	public String borrowInfo(Model model) {
		boolean canBorrow = iBidRequestService.canBorrow(UserContext.getCurrent());
		System.out.println(canBorrow);
		if(canBorrow) {
			model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BIDREQUEST_AMOUNT);
			model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);
			model.addAttribute("account", this.accountService.get(UserContext.getCurrent().getId()));
			return "borrow_apply";
		}
		return "borrow_apply_result";
	}
	/**
	  * 
	  * <p>Title: borrowApply</p>
	  * <p>Description:提交借款 </p>
	  * @return
	  */
	 @RequestMapping("/borrow_apply.do")
	 public String borrowApply(BidRequest b){
		 Logininfo current = UserContext.getCurrent();
		 b.setCurrent(current);
		 b.setCreateUser(current);
		 iBidRequestService.borrowApply(b,current);
		 return "redirect:borrowInfo.do";
	 }
	 
	 
	 /**
	  * 
	  * <p>Title: borrowInfoDatil</p>
	  * <p>Description: 借款详情</p>
	  * @param id
	  * @param model
	  * @return
	  */
	 @RequestMapping("/borrow_info.do")
	 public String borrowInfoDatil(Long id,Model model){
		  
		     // 获取投标信息
		BidRequest bidRequest =  iBidRequestService.getbidRequest(id);
		
		 // 判断 是否为空
		if(bidRequest!=null){
			
			 // 获取 借款人信息
			Userinfo userinfo = userService.get(bidRequest.getCreateUser().getId());
			     
			  // 借款人实名认证信息
			Realauth realauth = realAuthService.get(userinfo.getRealauthId());
			model.addAttribute("realAuth", realauth);
			 
			   // 借款人风控资料
			UserFileAuthQueryObject uf = new UserFileAuthQueryObject();
			    // 设置当前页数
			uf.setCurrentPage(1);
			   // 设置显示数量
			 uf.setPageSize(-1);
			 // 设置申请者id
			 uf.setApplierId(realauth.getApplier().getId());
			   
			 List<Userfile> selectFileList = userFileService.getFile(uf);
			 model.addAttribute("userFiles", selectFileList);
			 
			    
			    // 展示
			 model.addAttribute("userInfo", userinfo);
			 model.addAttribute("bidRequest", bidRequest);
			 
			 // 判断当前用户是否为空
			 if(UserContext.getCurrent()!=null){
				   
				  // 判断用户是否是自己
				  if(UserContext.getCurrent().getId().equals(userinfo.getId())){
					  
					  model.addAttribute("self", true);
					 
				  }else {
					  Account account = accountService.get(UserContext.getCurrent().getId());
					  model.addAttribute("account", account);
					  model.addAttribute("self", false);
					  
				  }
				 
			 }else {
				 
				   
				 model.addAttribute("self", false);
			 }
			 
		}
		 
		 return "borrow_info";
	 }
	 
	 
	   /**
	    * 
	    * <p>Title: borrowMoney</p>
	    * <p>Description: 投标</p>
	    * @return
	    */
	  @RequestMapping("/borrow_bid.do")
	  @ResponseBody
	  public ResultJSON borrowMoney(Long bidRequestId,BigDecimal amount){
		  
		    ResultJSON resultJSON = new ResultJSON();
		      
		    try {
		    	iBidRequestService.addBach(bidRequestId, amount,UserContext.getCurrent());
				
				resultJSON.setSuccess(true);
				
			} catch (Exception e) {
				  
				resultJSON.setSuccess(false);
				e.printStackTrace();
			}
		return resultJSON;
	  }
	  
	  
	  

	   /**
	    * 
	    * 个人中心--我的投资
	    * @return
	    */
	  @RequestMapping("/bid_list.do")
	  public String bidList(@ModelAttribute("qo")BidQueryObject qo,Model model){
		  
			qo.setBidUserId(UserContext.getCurrent().getId());//设置投资人的id
			
			//根据查询条件，对投资信息进行分页查询
			PageResult result = this.iBidRequestService.queryBids(qo);
			
			model.addAttribute("pageResult",result);
			//借款标的所有状态列表
			model.addAttribute("bidRequestStates",this.iBidRequestService.listBidRequestStates());
			
			return "bid_list";
			
			
	  }
	  
	  

	   /**
	    * 
	    * 个人中心--我的借款
	    * @return
	    */
	  @RequestMapping("/borrow_list.do")
	  public String borrowList(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
		  
			qo.setCreatorId(UserContext.getCurrent().getId());//借款人id
			PageResult result = this.iBidRequestService.selectBidRequestList(qo);
			model.addAttribute("pageResult",result);
			model.addAttribute("bidRequestStates",this.iBidRequestService.listBidRequestStates());
			
			return "bidRequest_list";
			
			
	  }
	  
	  
}
