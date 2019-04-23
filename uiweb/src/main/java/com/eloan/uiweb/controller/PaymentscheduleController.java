package com.eloan.uiweb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.eloan.business.domain.PaymentScheduleDetail;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eloan.base.query.PageResult;
import com.eloan.base.util.DateUtil;
import com.eloan.base.util.ResultJSON;
import com.eloan.base.util.UserContext;
import com.eloan.business.domain.Account;
import com.eloan.business.domain.PaymentSchedule;
import com.eloan.business.domain.PaymentScheduleQueryObject;
import com.eloan.business.service.IAccountService;
import com.eloan.business.service.PaymentscheduleService;

@Controller
public class PaymentscheduleController {
      @Autowired
      private PaymentscheduleService paymentscheduleService;

  	  @Autowired
  	  private IAccountService accountService;

  	
      @PostMapping("/returnMoney")
      @ResponseBody
      public ResultJSON returnMoney(@RequestParam("id") Long id) {

    	  
	       ResultJSON resultJSON = new ResultJSON();
	   
		    try {
		    	 this.paymentscheduleService.returnMoney(id, UserContext.getCurrent());
				resultJSON.setSuccess(true);
			} catch (Exception e) {
				resultJSON.setSuccess(false);
				
				e.printStackTrace();
			}

		    return resultJSON;
      
      }

     /* @RequestMapping("/selectRetungList")
      public PageResult selectRetungList(@RequestBody PaymentScheduleQueryObject q) {
    	  return this.paymentscheduleService.selectReturnList(q);
      }*/
    /**
     *
     * 个人中心--收款明细
     * @return
     */
    @RequestMapping("/callbackmoney_list")
    public String callbackmoney_list(@ModelAttribute("q")PaymentScheduleQueryObject q,Model model){
		System.out.println("here");
        q.setLogininfoId(UserContext.getCurrent().getId());
        PageResult result = this.paymentscheduleService.selectshouList(q);
		System.out.println(result+"-----------------------------");
        Account account = accountService.get(UserContext.getCurrent().getId());


        model.addAttribute("pageResult",result);
        model.addAttribute("account",account);

        return "shoumoney_list";
    }

	   /**
	    * 
	    * 个人中心--还款明细
	    * @return
	    */
	  @RequestMapping("/borrowBidReturn_list.do")
	  public String selectReturnList(@ModelAttribute("q")PaymentScheduleQueryObject q,Model model){
		  
			q.setLogininfoId(UserContext.getCurrent().getId());
			PageResult result = this.paymentscheduleService.selectReturnList(q);
			Account account = accountService.get(UserContext.getCurrent().getId());
			
			
			model.addAttribute("pageResult",result);
			model.addAttribute("account",account);
			
			return "returnmoney_list";
			
			
	  }
	  
	  
	  
	  
	  //导出还款明细
	  @RequestMapping("/export_return_list.do")
	  public void exportReturnList(@ModelAttribute("q")PaymentScheduleQueryObject q,HttpServletResponse response) throws Exception{
		  
			q.setLogininfoId(UserContext.getCurrent().getId());
			
			List<PaymentSchedule> allReturnList = this.paymentscheduleService.selectAllReturnList(q);

			
			//①响应Excel文件?
			String filename = new String("还款计划明细".getBytes("UTF-8"), "ISO_8859_1");  
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition","attachment;filename="+filename+".xls");  

			//②如何创建Excel，并将列表数据写入到Excel中?
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("明细"); 
			
			//创建标题
			HSSFRow titleRow = sheet.createRow(0);
			//借款	还款金额	还款本金	还款利息	还款期数	还款期限	还款状态
			String[] titleArr = new String[]{"id","借款","还款金额","还款本金","还款利息","还款期数","还款期限","还款状态"};
			String[] fieldArr = new String[]{"id","bidRequestTitle","totalAmount","principal","interest","monthIndex","deadLine","state"};
			for(int i=0; i<titleArr.length; i++) {
				HSSFCell cell = titleRow.createCell(i);
				cell.setCellValue(titleArr[i]);
			}
			
			//创建数据所在行  TODO:改造成反射的方式
			if(!CollectionUtils.isEmpty(allReturnList)) {// if(allReturnList != null && allReturnList.size()>0 )
				Class<?> clazz = PaymentSchedule.class;
				for(int i=0; i<allReturnList.size(); i++) {
					HSSFRow row = sheet.createRow(i+1);
					PaymentSchedule paymentSchedule = allReturnList.get(i);
					
					for (int j = 0; j < fieldArr.length; j++) {
						String fieldName = fieldArr[j];//属性名
						
						Field field = clazz.getDeclaredField(fieldName);
						field.setAccessible(true);
						//获取属性值
						Object obj = field.get(paymentSchedule);
						
						Class<?> fieldType = field.getType();
						if(fieldType == Date.class) {
							obj = DateUtil.formatDateStr((Date)obj);
						}else if(fieldName.equals("state")) {
							obj = paymentSchedule.getStateDisplay();
						}
						
						HSSFCell cell = row.createCell(j);
						cell.setCellValue(obj.toString());
					}
					
					
					
					
					/*
					
					HSSFCell cell0 = row.createCell(0);
					cell0.setCellValue(paymentSchedule.getId());
					
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellValue(paymentSchedule.getBidRequestTitle());
					
					
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellValue(paymentSchedule.getTotalAmount().toString());
					
					
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellValue(paymentSchedule.getPrincipal().toString());
					
					
					HSSFCell cell4 = row.createCell(4);
					cell4.setCellValue(paymentSchedule.getInterest().toString());
					
					
					HSSFCell cell5 = row.createCell(5);
					cell5.setCellValue(paymentSchedule.getMonthIndex());
					
					HSSFCell cell6 = row.createCell(6);
					cell6.setCellValue(DateUtil.formatDateStr(paymentSchedule.getDeadLine()));
					
					HSSFCell cell7 = row.createCell(7);
					//cell7.setCellValue(paymentSchedule.getState()==0?"正常待还":(paymentSchedule.getState()==1?"已还":"逾期"));
					cell7.setCellValue(paymentSchedule.getStateDisplay());*/
					
					
					
					
					
				}
			}
			 
			
			try {
				workbook.write(response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	  }	
}
