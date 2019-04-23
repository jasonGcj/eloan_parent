package com.bw.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

	
	public static void main(String[] args) {
		
		BigDecimal a = new BigDecimal(5);
		BigDecimal b = BigDecimal.valueOf(8);
		
		//加
		BigDecimal jia = a.add(b);
		//减
		BigDecimal jian = a.subtract(b); // a-b
		//乘
		BigDecimal cheng = a.multiply(b);
		//除
		//BigDecimal chu = a.divide(b).setScale(2, RoundingMode.HALF_UP);//  5/8
		BigDecimal chu = a.divide(b, 2, RoundingMode.HALF_UP);
		
		//幂    乘方
		BigDecimal pow = a.pow(3);
		
		System.out.println(jia);
		System.out.println(jian);
		System.out.println(cheng);
		System.out.println(chu);
		System.out.println(pow);//125
		
		//两个常量
		System.out.println(BigDecimal.ZERO);
		System.out.println(BigDecimal.ONE);
		
	}
}
