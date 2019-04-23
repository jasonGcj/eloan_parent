package com.eloan.uiweb.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.eloan.base.domain.PaymentInfo;
import com.eloan.base.service.PaymentInfoService;
import com.eloan.uiweb.config.AlipayConfig;

/**
 * 线下充值
 * @author Administrator
 *
 */
@Controller
public class CallbackController {
	
	
	@Autowired
	private PaymentInfoService paymentInfoService;
	/**
	 * [异步通知]
	 * 程序执行完后必须打印输出“success”（不包含引号）。
	 * 如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
	 * 一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h
	 */
	@RequestMapping("/callback/asynCallback")
	@ResponseBody
	public String asynCallback(HttpServletRequest request) throws Exception { 
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		//付款金额
		String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
				

		System.out.println("异步通知=======trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);

		
		//根据商家订单号查询payment_info表中的信息
		
		//为了防止支付宝重复通知支付成功的结果，导致后续的业务逻辑重复执行， 我们必须对接口做幂等处理
		
		PaymentInfo paymentInfo = paymentInfoService.selectById(Integer.valueOf(out_trade_no));
		if(paymentInfo != null) {
			//获取支付状态
			Integer state = paymentInfo.getState();
			
			if(state.intValue() == 1) {
				return "success";
			}else {
				
				//支付成功后的业务逻辑处理
				
				//1.更新支付状态
				paymentInfo.setState(1);
				
				//2.更新账户可用余额
				Long createuserId = paymentInfo.getCreateuserId();//createuserId即为账户表的主键
				
				//需要查到该账户，并增加可用余额，增加total_amount
				
				
				//3.记录账户流水
				
				
				return "success";
				
			}
			
			
		}
		
		
		
		
		
		return "success";
	}
	

	 
	@RequestMapping("/callback/synCallback")
	public String synCallback(HttpServletRequest request) throws Exception {
		
		//输出请求中的所有参数信息
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		
		//验签
		boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE); //调用SDK验证签名

		System.out.println("同步回调的验签:"+signVerified);
		//——请在这里编写您的程序（以下代码仅作参考）——
		
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
	
		//付款金额
		String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
		
		System.out.println("同步通知--------trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);

	
		
		return "pay_success";
	}
	
	

}
