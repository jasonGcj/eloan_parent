package com.eloan.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eloan.base.util.DateUtil;
import com.eloan.base.util.UserContext;
import com.eloan.business.service.ISendVerifyCodeService;
import com.eloan.business.service.VerifyCode;
import com.eloan.business.util.HttpClientUtil;
import com.eloan.business.util.Sendsms;

@Service
public class SendVerifyCodeService implements ISendVerifyCodeService {

	@Value("${m5c.url}")
	private String url;

	@Value("${m5c.username}")
	private String username;

	@Value("${m5c.password}")
	private String password;

	@Value("${m5c.apiKey}")
	private String apiKey;

	@Override
	public boolean verifyCode(String phoneNumber, String verifyCode) {
		VerifyCode vc=UserContext.getVerifyCode();
		return vc!=null//发过短信;
				&& vc.getPhoneNumber().equals(phoneNumber)
				&& vc.getRandomCode().equals(verifyCode)
				&& DateUtil.getSecondsBetweenDates(vc.getLastSendTime(),new Date())<=180;
	}

	public void sendVerifyCode(String phoneNumber) {
		if (checkUserCanSendVerifyCode()) {
			String randomCode = UUID.randomUUID().toString().substring(0, 4);
			StringBuilder sb = new StringBuilder(100).append("您的手机验证码为:")
					.append(randomCode).append(",请在3分钟之内输入有效!");
			VerifyCode vc = new VerifyCode(phoneNumber, randomCode, new Date(),
					sb.toString());
			sendMessage(vc);
		} else {
			throw new RuntimeException("你发送短信的频率太高");
		}
	}
	
	private boolean checkUserCanSendVerifyCode() {
		VerifyCode vc = UserContext.getVerifyCode();
		return vc == null
				|| (vc != null && DateUtil.getSecondsBetweenDates(
						vc.getLastSendTime(), new Date()) > 120L);
	}

	private void sendMessage(VerifyCode code) {
		//发短信
		//使用Java发送HTTP请求
			//获取连接对象
//			HttpURLConnection connection = (HttpURLConnection) new URL(url)
//					.openConnection();
			//设置请求方法
//			connection.setRequestMethod("POST");
			//添加请求参数
//			connection.setDoOutput(true);
			//拼参数
//			StringBuilder sb = new StringBuilder(100).append("username=")
//					.append(username).append("&password=").append(password)
//					.append("&apikey=").append(apiKey).append("&mobile=")
//					.append(code.getPhoneNumber()).append("&content=")
//					.append(code.getContent());
//			connection.getOutputStream().write(sb.toString().getBytes());
			//发送请求,得到响应结果
//			InputStream is = connection.getInputStream();
//			String responseText = StreamUtils.copyToString(is,
//					Charset.forName("UTF-8"));
		
		try {
			Map<String,String>param = new HashMap<String,String>();
			param.put("url", url);
			param.put("mobile", code.getPhoneNumber());
			param.put("content", code.getContent());
			Sendsms.send(param);
			String responseText = HttpClientUtil.doPost(url, param);
			
			if (responseText.indexOf("success:") != 0) {
				throw new RuntimeException("发送短信失败");
			} else {
				UserContext.putVerifyCode(code);
			}
		} catch (Exception e) {
			throw new RuntimeException("发送短信失败");
		}
	}

}
