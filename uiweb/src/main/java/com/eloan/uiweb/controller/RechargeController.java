package com.eloan.uiweb.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.eloan.base.domain.Logininfo;
import com.eloan.base.domain.PaymentInfo;
import com.eloan.base.service.PaymentInfoService;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.Account;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.IUserService;
import com.eloan.business.service.PlatformBankinfoService;
import com.eloan.business.service.RechargeService;
import com.eloan.business.util.BidConst;
import com.eloan.uiweb.config.AlipayConfig;

/**
 * 线下充值
 * @author Administrator
 *
 */
@Controller
public class RechargeController {

	@Autowired
	private RechargeService rechargeService;

	@Autowired
	private IUserService userService;
	
	 @Autowired
     private PlatformBankinfoService platformBankinfoService;
	 @Autowired
	 private IAccountService accountService;
	 @Autowired
	 private PaymentInfoService paymentInfoService;
	 
	 //模拟支付token的map
	 private Map<String, Integer> payTokenMap = new HashMap<>();
      
	/**
	 * 展示充值页面
	 * @return
	 */
	@RequestMapping("/recharge")
	public String rechargeIndex(Model model) {
		Logininfo current = UserContext.getCurrent();
		if (current == null) {
			return "redirect:login.html";
		}
		
		//查询当前用户的账户信息
		Account account = accountService.get(current.getId());
				
		model.addAttribute("account",account);
		model.addAttribute("minRechargeAmount",BidConst.SMALLEST_RECHARGE_AMOUNT);
		model.addAttribute("userinfo", this.userService.get(current.getId()));
		model.addAttribute("banks", platformBankinfoService.selectAll());
		return "recharge";
	}
	

   
	/**
	 * 准备进行线上充值
	 * @return
	 */
	@RequestMapping("/recharge/pay.do")
	public String rechargeOnline(Model model, double amount) {
		Logininfo current = UserContext.getCurrent();
		if (current == null) {
			return "redirect:login.html";
		}
		model.addAttribute("userinfo", this.userService.get(current.getId()));
		
		
		
		//插入一条支付信息
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setAmount(BigDecimal.valueOf(amount));
		paymentInfo.setChannel(0);//默认使用支付宝支付
		paymentInfo.setCreated(new Date());
		paymentInfo.setCreateuserId(current.getId());//当前登录的用户
		paymentInfo.setState(0);//默认0，表示 待支付
		paymentInfoService.insert(paymentInfo);//为了确保payInfo能够在插入的同时将主键赋值给payInfo
		
		
		//支付信息表的id
		//String payToken = "payToken:"+UUID.randomUUID().toString();
		String payToken = "payToken:12345";
		
		
		//将支付token存入到redis中
		//redis.set(payToken, paymentInfo.getId());
		payTokenMap.put(payToken, paymentInfo.getId());
		
		//支付倒计时的过期时间    
		//每次刷新时 都要进行20分钟倒计时，这个问题如何解决呢? redis  指定键 expire:userid---->支付截止时间
		model.addAttribute("disableDate",new Date(System.currentTimeMillis()+20*60*1000));
		//将支付token传到前台页面
		model.addAttribute("payToken", payToken);
		return "recharge_pay";
	}
	
	
	//支付线上支付
	@RequestMapping(value="/recharge/alipay.do",produces="text/html;charset=utf-8")
	@ResponseBody
	public String toAlipay(String payToken, Model model,HttpServletResponse response) throws Exception {
		
		//获取支付token,从redis中取得支付信息的主键
		PaymentInfo paymentInfo = paymentInfoService.selectLatest();
		
		System.out.println("******"+paymentInfo);
		
		
		//如果找不到，则意味着订单超时
		//找到后，根据支付信息的主键找到该条支付信息，封装为支付宝所需要的支付请求对象
		
		//获得初始化的AlipayClient
	   AlipayClient alipayClient = new DefaultAlipayClient
	                (AlipayConfig.URL,//支付宝网关
	                        AlipayConfig.APPID,//商户id
	                        AlipayConfig.RSA_PRIVATE_KEY,//商户的私钥
	                        AlipayConfig.FORMAT,// 返回数据的格式
	                        AlipayConfig.CHARSET,// 返回数据的字符集
	                        AlipayConfig.ALIPAY_PUBLIC_KEY,//阿里的公钥
	                        AlipayConfig.SIGNTYPE);//签名的加密方式
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		

		alipayRequest.setBizContent("{\"out_trade_no\":\""+ paymentInfo.getId() +"\"," 
				+ "\"total_amount\":\""+ paymentInfo.getAmount().setScale(2) +"\"," 
				+ "\"subject\":\"在线充值\"," 
				+ "\"body\":\"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		
		
		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		System.out.println(result);
		return result;

		
	}
}
