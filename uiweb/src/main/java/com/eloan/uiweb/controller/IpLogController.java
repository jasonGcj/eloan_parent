package com.eloan.uiweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.eloan.base.domain.Logininfo;
import com.eloan.base.query.IpLogQueryObject;
import com.eloan.base.query.PageResult;
import com.eloan.base.service.IIpLogService;
import com.eloan.base.util.UserContext;
import com.eloan.uiweb.interceptor.RequiredLogin;
import com.eloan.uiweb.util.UploadUtil;
import com.eloan.business.util.AccessLimit;

@Controller
public class IpLogController extends BaseController {

	@Autowired
	private IIpLogService ipLogService;
	
	@Autowired
	private ServletContext servletContext;

	@RequiredLogin
	@AccessLimit(seconds = 3,count = 5)
	@RequestMapping("/iplog")
	public String ipLog(@ModelAttribute("qo")IpLogQueryObject qo, Model model) {
		qo.setUsername(UserContext.getCurrent().getUsername());
		qo.setLike(false);
		qo.setUserType(Logininfo.USERTYPE_NORMAL);
		PageResult result = this.ipLogService.query(qo);
		model.addAttribute("pageResult", result);
		return "iplog_list";
	}

	
	@RequestMapping("/ipLogUpload")
	@ResponseBody
	public String ipLogUpload(MultipartFile file) throws Exception {
		String filePath = servletContext.getRealPath("/upload");
		
		//①上传
		String fileName = UploadUtil.upload(file, filePath);
		
		
		//②解析Excel数据
		File excelFile = new File(filePath);
		InputStream is = new FileInputStream(excelFile);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		
		HSSFSheet sheet = hssfWorkbook.getSheet("联系方式");
		
		HSSFRow row = sheet.getRow(0);
		
		HSSFCell cell = row.getCell(0);
		
		String name = cell.getStringCellValue();
		System.out.println("获取的第一个单元格的值为:"+name);
		
		return "/upload/" + fileName;
	}
}
