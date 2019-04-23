package com.eloan.business.util;

import java.math.BigDecimal;

/**
 * 系统需要的常量
 * @author Administrator
 *
 */
public class BidConst {

	public static final int DISPLAY_SCALE = 2;//显示精度
	public static final int CAL_SCALE = 8;//计算精度
	public static final int STORE_SCALE = 4;//保存精度
	
	
	

	public static final BigDecimal ZERO = new BigDecimal("0.0000");//系统中需要的zero
	public static final BigDecimal DEFALUT_BORROWLIMITAMOUNT = new BigDecimal(
			"2000.0000");//初始用户授信额度
		
	public static final int RETURN_TYPE_MONTH_INTERST_PRINCIPAL=0;//按月分期还款(等额本息)
	public static final int RETURN_TYPE_MONTH_INTERST=1;//按月到期还款
	public static final int RETURN_TYPE_MONTH_PRINCIPAL=2;//等额本金
	
	public static final int BIDREQUEST_TYPE_NORMAL=0;//普通信用标
	public static final int BIDREQUEST_TYPE_CAR=1;//车易贷
	public static final int BIDREQUEST_TYPE_HOUSE=2;//房易贷
	
	public final static int BIDREQUEST_STATE_PUBLISH_PENDING=0;//待发布=待发标前审核
	public final static int BIDREQUEST_STATE_BIDDING=1;//招标中=发标前审核通过
	public final static int BIDREQUEST_STATE_UNDO=2;//已撤销（借款方主动发起的）
	public final static int BIDREQUEST_STATE_BIDDING_OVERDUE=3;//流标（招标期限内没有满标）
	public final static int BIDREQUEST_STATE_APPROVE_PENDING_1=4;//满标1审（一旦在招标期限内满标，则自动进入待满标一审）
	public final static int BIDREQUEST_STATE_APPROVE_PENDING_2=5;//满标2审（一旦满标1审通过，则自动进入待满标二审）
	public final static int BIDREQUEST_STATE_APPROVE_PENDING_3=33;//满标3审（一旦满标2审通过，则自动进入待满标三审）
	public final static int BIDREQUEST_STATE_REJECTED=6;//一审或者二审不通过
	public final static int BIDREQUEST_STATE_PAYING_BACK=7;//通过三审（会放款）,还款中
	public final static int BIDREQUEST_STATE_COMPLETE_PAY_BACK=8;//已还清
	public final static int BIDREQUEST_STATE_PAY_BACK_OVERDUE=9;//逾期
	public final static int BIDREQUEST_STATE_PUBLISH_REFUSE=10;//初审拒绝(发标前审核不通过)
	
	public final static BigDecimal SMALLEST_BID_AMOUNT=new BigDecimal("50.0000");//最小投标金额
	public final static BigDecimal SMALLEST_RECHARGE_AMOUNT=new BigDecimal("100.0000");//最小充值金额
	public final static BigDecimal SMALLEST_BIDREQUEST_AMOUNT=new BigDecimal("500.0000");//最小借款金额
	
	
	public static final String DEFAULT_ADMIN_NAME="admin";
	public static final String DEFAULT_ADMIN_PASSWORD="1111";
	
	public static final int CREDIT_BORROW_SCORE=30;//信用信用分数
	
	
	public final static int PAYMENT_STATE_NORMAL = 0; // 正常待还
	public final static int PAYMENT_STATE_DONE = 1; // 已还
	public final static int PAYMENT_STATE_OVERDUE = 2; // 逾期
	
	
	// 资金流水类别：线下充值---> 可用余额增加
	public final static int ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE = 0;
	// 资金流水类别：提现成功---> 冻结金额减少
	public final static int ACCOUNT_ACTIONTYPE_WITHDRAW = 1;
	// 资金流水类别：成功借款 -->可用余额增加,待还本息增加
	public final static int ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL = 2;
	// 资金流水类别：成功投标--->冻结金额减少，待收本金、待收利息增加
	public final static int ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL = 3;
	// 资金流水类别：还款-->可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_RETURN_MONEY = 4;
	// 资金流水类别：回款-->可用余额增加
	public final static int ACCOUNT_ACTIONTYPE_CALLBACK_MONEY = 5;
	// 资金流水类别：支付平台管理费-->可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_CHARGE = 6;
	// 资金流水类别：利息管理费--->可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_INTEREST_SHARE = 7;
	// 资金流水类别：提现手续费 --->可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 8;
	// 资金流水类别：充值手续费--->可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE = 9;
	// 资金流水类别：投标冻结金额--->冻结金额增加  可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_BID_FREEZED = 10;
	// 资金流水类别：取消投标冻结金额 --->冻结金额减少 可用余额增加
	public final static int ACCOUNT_ACTIONTYPE_BID_UNFREEZED = 11;
	// 资金流水类别：提现申请冻结金额--->冻结金额增加  可用余额减少
	public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED = 12;
	// 资金流水类别:提现申请失败取消冻结金额--->冻结金额减少 可用余额增加
	public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED = 13;
	
	
	
	/** ============系统账户流水类型============= */
	public final static int SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE = 1;// 系统账户收到账户管理费（借款管理费）
	public final static int SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE = 2;// 系统账户收到利息管理费
	public final static int SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 3;// 系统账户收到提现手续费
	
	public static final BigDecimal SMALLEST_CURRENT_RATE = new BigDecimal(
			"5.0000");// 系统最小借款利息
	public static final BigDecimal MAX_CURRENT_RATE = new BigDecimal("20.0000");// 系统最大借款利息
	
	public static final BigDecimal MIN_WITHDRAW_AMOUNT = new BigDecimal(
			"500.0000");// 系统最小提现金额
	public static final BigDecimal MONEY_WITHDRAW_CHARGEFEE = new BigDecimal(
			"2.0000");// 系统提现手续费
}
