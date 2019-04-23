package com.eloan.uiweb.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eloan.base.util.UserContext;
import com.eloan.business.domain.Userfile;
import com.eloan.business.service.IUserFileService;
import com.eloan.uiweb.util.UploadUtil;


@Controller
public class UserFileController extends BaseController {

	@Value("#{imageProperties['uploadDir']}")
	private String uploadDir;
	@Value("#{imageProperties['uploadUrl']}")
	private String uploadUrl;
	
	@Autowired
	private IUserFileService userFileService;

	@Autowired
	private ServletContext servletContext;


	
	@RequestMapping("userFile")
	public String userFile(Model model, HttpSession session) {
		
		//  查询当前用户上传的所有类型为空的风控材料  where fileType is  null
		List<Userfile> unSetFileTypes = this.userFileService
				.listUnSetTypeFiles(UserContext.getCurrent().getId(), true);
		if (unSetFileTypes.size() > 0) {
			//若存在未分类的风控材料（filetype_id为空），那么就执行这里
			model.addAttribute("userFiles", unSetFileTypes);
			return "userFiles_commit";
		} else {
			//若没有类型为空的风控材料，则查询当前用户上传的所有类型不为空的风控材料  where fileType is not null
			unSetFileTypes = this.userFileService.listUnSetTypeFiles(
					UserContext.getCurrent().getId(), false);
			
			model.addAttribute("userFiles", unSetFileTypes);
			model.addAttribute("sessionid", session.getId());
			return "userFiles";
		}
		
	}

	
	//风控材料的文件上传
	@RequestMapping("userFileUpload")
	@ResponseBody
	public String userFileUpload(MultipartFile file) {
		//获取实际路径
		//String filePath = servletContext.getRealPath("/upload");
		
		String filePath = uploadDir;//FIXME 需要从配置文件中读取过来
		
		//调用自己的工具类 进行文件上传操作
		String fileName = UploadUtil.upload(file, filePath);
		
		//String path = "/upload/" + fileName;
		String path = uploadUrl+fileName;
		//向数据库的userfile表中插入一条记录
		this.userFileService.applyFile(path);
		return path;
	}

	@RequestMapping("userFile_selectType")
	public String selectType(Long[] id, Long[] fileType) {
		if (id.length == fileType.length) {
			this.userFileService.applyTypes(id, fileType);
		}
		return "redirect:userFile.do";
	}

}
