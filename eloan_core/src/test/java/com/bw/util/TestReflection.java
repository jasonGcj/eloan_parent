package com.bw.util;

import java.lang.reflect.Field;

import com.eloan.business.domain.PaymentSchedule;

public class TestReflection {
	
	
	public static void main(String[] args) throws Exception {
		
		
		PaymentSchedule ps = new PaymentSchedule();
		ps.setId(6L);
		
		Class<?> clazz = PaymentSchedule.class;
		
		
		Field field = clazz.getDeclaredField("id");
		field.setAccessible(true);
		
		Object id = field.get(ps);
		System.out.println(id.toString());
	}

}
