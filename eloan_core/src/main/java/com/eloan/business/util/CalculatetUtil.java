package com.eloan.business.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算器Util
 * 
 */
public class CalculatetUtil {

	public static final BigDecimal ONE_HUNDRED = new BigDecimal("100.0000");
	public static final BigDecimal NUMBER_MONTHS_OF_YEAR = new BigDecimal("12.0000");
	public static final BigDecimal ACCOUNT_MANAGER_CHARGE_RATE = new BigDecimal("0.0500");
	public static final BigDecimal INTEREST_MANAGER_CHARGE_RATE = new BigDecimal("0.1000");

	/**
	 * 获取月利率
	 *     年利率/12
	 */
	public static BigDecimal getMonthlyRate(BigDecimal yearRate) {
		if (yearRate == null)
			return BigDecimal.ZERO;
		return yearRate.divide(ONE_HUNDRED).divide(NUMBER_MONTHS_OF_YEAR, BidConst.CAL_SCALE, RoundingMode.HALF_UP);
	}

	/**
	 * 计算借款总利息
	 * 
	 * @param returnType
	 *            还款类型
	 * @param bidRequestAmount
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param monthes2Return
	 *            还款期限
	 * @return
	 */
	public static BigDecimal calTotalInterest(int returnType, BigDecimal bidRequestAmount, BigDecimal yearRate, int monthes2Return) {
		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal monthlyRate = getMonthlyRate(yearRate);//根据年利率计算月利率
		if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL) {//等额本息 (按月分期)
			// 只借款一个月
			if (monthes2Return == 1) {
				totalInterest = bidRequestAmount.multiply(monthlyRate).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			} else {
				BigDecimal temp1 = bidRequestAmount.multiply(monthlyRate);//月利息
				BigDecimal temp2 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return);
				BigDecimal temp3 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return).subtract(BigDecimal.ONE);
				// 算出每月还款
				BigDecimal monthToReturnMoney = temp1.multiply(temp2).divide(temp3, BidConst.CAL_SCALE, RoundingMode.HALF_UP);
				// 算出总还款
				BigDecimal totalReturnMoney = monthToReturnMoney.multiply(new BigDecimal(monthes2Return));
				// 算出总利息
				totalInterest = totalReturnMoney.subtract(bidRequestAmount);
			}
		} else if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST) {// 按月到期
			BigDecimal monthlyInterest = DecimalFormatUtil.amountFormat(bidRequestAmount.multiply(monthlyRate));
			totalInterest = monthlyInterest.multiply(new BigDecimal(monthes2Return));
		}else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
			//每月要还的本金=借款额度/月份n
			//第一个月  =  借款额度*月利率
			//第2个月  =  (借款额度-每月要还的本金)*月利率
			//第3个月  =  (借款额度-每月要还的本金*2)*月利率
			//....
			//第n个月 =   (借款额度-每月要还的本金*(n-1))*月利率

			//   

			//平均利息=[借款额度*月利率 + (借款额度-每月要还的本金*(n-1))*月利率]/2
			//	=     （月利率*(借款额度*2 -每月要还的本金*(n-1) )）/2
			//总利息= 平均利息*月份n

			BigDecimal monthlyPrincipal = bidRequestAmount.divide(BigDecimal.valueOf(monthes2Return), BidConst.CAL_SCALE, RoundingMode.HALF_UP);//每月本金
			BigDecimal avgInterest = (monthlyRate.multiply(bidRequestAmount.multiply(BigDecimal.valueOf(2)).
					subtract(monthlyPrincipal.multiply(BigDecimal.valueOf(monthes2Return).subtract(BigDecimal.ONE))))).divide(BigDecimal.valueOf(2));

			totalInterest = avgInterest.multiply(BigDecimal.valueOf(monthes2Return));
		}
		return DecimalFormatUtil.formatBigDecimal(totalInterest, BidConst.STORE_SCALE);
	}

	/**
	 * 计算每期利息
	 * 
	 * @param returnType
	 *            还款类型
	 * @param bidRequestAmount
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param monthIndex
	 *            第几期
	 * @param monthes2Return
	 *            还款期限
	 * @return
	 */
	public static BigDecimal calMonthlyInterest(int returnType, BigDecimal bidRequestAmount, BigDecimal yearRate, int monthIndex, int monthes2Return) {
		BigDecimal monthlyInterest = BigDecimal.ZERO;
		BigDecimal monthlyRate = getMonthlyRate(yearRate);
		if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL) {// 按月分期
			// 只借款一个月
			if (monthes2Return == 1) {
				monthlyInterest = bidRequestAmount.multiply(monthlyRate).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			} else {
				BigDecimal temp1 = bidRequestAmount.multiply(monthlyRate);
				BigDecimal temp2 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return);
				BigDecimal temp3 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return).subtract(BigDecimal.ONE);
				BigDecimal temp4 = (BigDecimal.ONE.add(monthlyRate)).pow(monthIndex - 1);
				// 算出每月还款
				BigDecimal monthToReturnMoney = temp1.multiply(temp2).divide(temp3, BidConst.CAL_SCALE, RoundingMode.HALF_UP);
				// 算出总还款
				BigDecimal totalReturnMoney = monthToReturnMoney.multiply(new BigDecimal(monthes2Return));
				// 算出总利息
				BigDecimal totalInterest = totalReturnMoney.subtract(bidRequestAmount);

				if (monthIndex < monthes2Return) {
					monthlyInterest = (temp1.subtract(monthToReturnMoney)).multiply(temp4).add(monthToReturnMoney).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
				} else if (monthIndex == monthes2Return) {
					BigDecimal temp6 = BigDecimal.ZERO;
					// 汇总最后一期之前所有利息之和
					for (int i = 1; i < monthes2Return; i++) {
						BigDecimal temp5 = (BigDecimal.ONE.add(monthlyRate)).pow(i - 1);
						monthlyInterest = (temp1.subtract(monthToReturnMoney)).multiply(temp5).add(monthToReturnMoney).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
						temp6 = temp6.add(monthlyInterest);
					}
					monthlyInterest = totalInterest.subtract(temp6);
				}

			}
		} else if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST) {// 按月到期
			monthlyInterest = DecimalFormatUtil.amountFormat(bidRequestAmount.multiply(monthlyRate));
		}else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
			//每月要还的本金=借款额度/月份n
			//第一个月  =  借款额度*月利率
			//第2个月  =  (借款额度-每月要还的本金)*月利率
			//第3个月  =  (借款额度-每月要还的本金*2)*月利率
			//....
			//第n个月 =   (借款额度-每月要还的本金*(n-1))*月利率

			BigDecimal monthlyPrincipal = bidRequestAmount.divide(BigDecimal.valueOf(monthes2Return),BidConst.CAL_SCALE, RoundingMode.HALF_UP);//每月本金
			//第monthIndex个月的利息
			monthlyInterest = bidRequestAmount.subtract(
					monthlyPrincipal.multiply(BigDecimal.valueOf(monthIndex).subtract(BigDecimal.ONE))
						).multiply(monthlyRate).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			
		}
		return monthlyInterest;
	}

	/**
	 * 计算每期还款
	 * 
	 * @param returnType
	 *            还款类型
	 * @param bidRequestAmount
	 *            借款金额
	 * @param yearRate
	 *            年利率
	 * @param monthIndex
	 *            第几期
	 * @param monthes2Return
	 *            还款期限
	 * @return
	 */
	public static BigDecimal calMonthToReturnMoney(int returnType, BigDecimal bidRequestAmount, BigDecimal yearRate, int monthIndex, int monthes2Return) {
		BigDecimal monthToReturnMoney = BigDecimal.ZERO;
		BigDecimal monthlyRate = getMonthlyRate(yearRate);
		if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL) {// 按月分期（等额本息）
			if (monthes2Return == 1) {
				monthToReturnMoney = bidRequestAmount.add(bidRequestAmount.multiply(monthlyRate)).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			} else {
				BigDecimal temp1 = bidRequestAmount.multiply(monthlyRate);
				BigDecimal temp2 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return);
				BigDecimal temp3 = (BigDecimal.ONE.add(monthlyRate)).pow(monthes2Return).subtract(BigDecimal.ONE);
				// 算出每月还款
				monthToReturnMoney = temp1.multiply(temp2).divide(temp3, BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			}
		} else if (returnType == BidConst.RETURN_TYPE_MONTH_INTERST) {// 按月到期
			BigDecimal monthlyInterest = bidRequestAmount.multiply(monthlyRate).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			if (monthIndex == monthes2Return) {
				monthToReturnMoney = bidRequestAmount.add(monthlyInterest).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			} else if (monthIndex < monthes2Return) {
				monthToReturnMoney = monthlyInterest;
			}
		}else if(returnType == BidConst.RETURN_TYPE_MONTH_PRINCIPAL){//等额本金
			//每月要还的本金=借款额度/月份n
			
			//第一个月  =  借款额度*月利率
			//第2个月  =  (借款额度-每月要还的本金)*月利率
			//第3个月  =  (借款额度-每月要还的本金*2)*月利率
			//....
			//第n个月 =   (借款额度-每月要还的本金*(n-1))*月利率

			BigDecimal monthlyPrincipal = bidRequestAmount.divide(BigDecimal.valueOf(monthes2Return),BidConst.CAL_SCALE, RoundingMode.HALF_UP);//每月本金
			//第monthIndex个月的利息
			BigDecimal monthlyInterest = bidRequestAmount.subtract(
					monthlyPrincipal.multiply(BigDecimal.valueOf(monthIndex).subtract(BigDecimal.ONE))
						).multiply(monthlyRate).setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP);
			monthToReturnMoney = monthlyPrincipal.add(monthlyInterest);
		
		}
		return DecimalFormatUtil.formatBigDecimal(monthToReturnMoney, BidConst.STORE_SCALE);
	}

	/**
	 * 计算一次投标实际获得的利息=投标金额/借款金额 *总利息
	 * 
	 * @param bidRequestAmount
	 *            借款金额
	 * @param monthes2Return
	 *            还款期数
	 * @param yearRate
	 *            年利率
	 * @param returnType
	 *            还款类型
	 * @param acturalBidAmount
	 *            投标金额
	 * @return
	 */
	public static BigDecimal calBidInterest(BigDecimal bidRequestAmount, int monthes2Return, BigDecimal yearRate, int returnType, BigDecimal acturalBidAmount) {
		// 借款产生的总利息
		BigDecimal totalInterest = calTotalInterest(returnType, bidRequestAmount, yearRate, monthes2Return);
		// 所占比例
		BigDecimal proportion = acturalBidAmount.divide(bidRequestAmount, BidConst.CAL_SCALE, RoundingMode.HALF_UP);
		
		
		BigDecimal bidInterest = totalInterest.multiply(proportion);
		return DecimalFormatUtil.formatBigDecimal(bidInterest, BidConst.STORE_SCALE);
	}

	/**
	 * 计算充值手续费
	 * 
	 * @param amount
	 * @return
	 */
	/*public static BigDecimal calRechargeFee(BigDecimal amount) {
		return DecimalFormatUtil.formatBigDecimal(amount.multiply(BidConst.RECHARGE_FEE_RATE), BidConst.STORE_SCALE);
	}*/

	/**
	 * 计算利息管理费
	 * 
	 * @param interest
	 *            利息
	 * @param interestManagerChargeRate
	 *            利息管理费比例
	 * @return
	 */

	public static BigDecimal calInterestManagerCharge(BigDecimal interest) {
		return DecimalFormatUtil.formatBigDecimal(
				interest.multiply(INTEREST_MANAGER_CHARGE_RATE),
				BidConst.STORE_SCALE);
	}

	/**
	 * 计算借款管理费
	 * 
	 * @param bidRequestAmount
	 *            借款金额
	 * @param returnType
	 *            还款类型
	 * @param monthes2Return
	 *            还款期限
	 * @return
	 */
	public static BigDecimal calAccountManagementCharge(
			BigDecimal bidRequestAmount) {
		BigDecimal accountManagementCharge = DecimalFormatUtil
				.formatBigDecimal(
						bidRequestAmount.multiply(ACCOUNT_MANAGER_CHARGE_RATE),
						BidConst.CAL_SCALE);
		return accountManagementCharge;
	}

}
