package com.eloan.uiweb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil {
	
	
	
	public static void main(String[] args) throws IOException {
		
		//创建一个Excel，并写入数据
		//WorkBook  ->  excel
		//Sheet   -> 工作簿
		//Row      -> 行
		//Cell      -> 单元格
		
		File file = new File("D:\\同学录.xls");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("联系方式"); 
		HSSFRow row = sheet.createRow(0);
		
		 HSSFCell cell1 = row.createCell(0);
		 cell1.setCellValue("张三");
		 
		 
		 HSSFCell cell2 = row.createCell(1);
		 cell2.setCellValue("167-3543534");
		 
		 
		 workbook.write(new FileOutputStream(file));
		 
		
	}

}
