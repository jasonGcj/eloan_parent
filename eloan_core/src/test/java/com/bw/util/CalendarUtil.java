package com.bw.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	
	public static void main(String[] args) {
		   Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        c.add(Calendar.DATE, 3);
	        
	        Date result = c.getTime();
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        System.out.println(sdf.format(result));
	}
}
