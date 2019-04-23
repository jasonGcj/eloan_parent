package com.bw.util;

public class BidStateUtil {
	
	
	public static final int INFO_FLAG = 1;//个人资料状态位
	public static final int REAL_AUTH_FLAG = 1 << 1;//实名认证状态位
	public static final int SMS_FLAG = 1 << 2;//短信认证状态位
	public static final int EMAIL_FLAG = 1 << 3;//邮箱认证状态位
	
	
	public static void main(String[] args) {
		
		int oldState = 10;//   1010
		//int state = 1 << 1;//短信认证状态位
		
		/*int newState = addState(oldState, state);
		System.out.println(newState);*/
		
		boolean hasEmail = hasState(oldState, REAL_AUTH_FLAG);
		System.out.println(hasEmail);
	}
	
	//新增一个位状态
	public static int addState(int oldState, int state) {
		return oldState | state;
	}
	
	//移除一个位状态
	public static int removeState(int oldState, int state) {
		if(hasState(oldState, state)) {
			
			return oldState ^ state;
		}
		return oldState;
	
	}
	
	//判断是否具有某个位状态
	public static boolean hasState(int oldState, int state) {
		
		return (oldState & state)  > 0;
		
	}

}
