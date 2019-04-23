package com.eloan.business.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class Sendsms {



	public static void send(Map<String,String> param) {
		String url = param.get("url");
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

		int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);

		String content = new String("您手机注册的验证码为：" + mobile_code);

		NameValuePair[] data = { //
				new NameValuePair("account", "C06692606"), //APIID
				new NameValuePair("password","c7328de02bad35dbfa3c16e385240404"), // APIKEY
				// new NameValuePair("password",
				// util.StringUtil.MD5Encode(")),
				new NameValuePair("mobile", param.get("mobile")), 
				new NameValuePair("content", content), };
		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult = method.getResponseBodyAsString();

			 System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);

			if ("2".equals(code)) {
				System.out.println("...");
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}