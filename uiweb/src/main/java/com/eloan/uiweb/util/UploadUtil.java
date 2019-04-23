package com.eloan.uiweb.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传工具
 * @author Administrator
 *
 */
public class UploadUtil {

	public static String upload(MultipartFile file, String basePath) {
		//获取原始文件名
		String orgFileName = file.getOriginalFilename();
		//生成新的文件名
		String fileName = UUID.randomUUID().toString() + "."
				+ FilenameUtils.getExtension(orgFileName);
		try {
			//FileUtils是Apache 工具包中的一个文件操作的工具类
			FileUtils.writeByteArrayToFile(new File(basePath, fileName),
					file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

}
