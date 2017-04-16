package org.yangbo.microservice.house;

import org.springframework.stereotype.Service;
import org.yangbo.microservice.house.domain.InvestContext;

@Service
public class HouseInvestComputer {

	/**
	 * 计算年投资回报率
	 * 
	 * @param investContext 投资信息上下文
	 * @return 年投资回报率
	 */
	public double computeReturnRatePerYear(InvestContext investContext) {
		double grossROI = (investContext.getSellPrice() - investContext.getBuyPrice()) / investContext.getBuyPrice();
		double yearGrossROI = grossROI/investContext.getHoldYears();
		double netROI = yearGrossROI - investContext.getLoanRate();
		return netROI;
	}
	
	/** 实际年投资回报率 **/
	public double computeActualReturnOnInvestPerYear(InvestContext investContext) {
		double totalIncome = (investContext.getSellPrice() - investContext.getBuyPrice())*investContext.getTotalArea();
		double grossROI = totalIncome / computeTotalCost(investContext);
		double yearGrossROI = grossROI/investContext.getHoldYears();
		return yearGrossROI;
	}
	
	/** 利润率 **/
	public double computeProfitRate(InvestContext investContext) {
		double interest = computeNetProfit(investContext);
		double totalCost = computeTotalCost(investContext);
		return interest / totalCost;				// 收益率
	}
	
	/** 毛利润 **/
	public double computeGrossProfit(InvestContext investContext) {
		double grossIncome = (investContext.getSellPrice() - investContext.getBuyPrice())*investContext.getTotalArea();
		return grossIncome;
	}
	
	/**
	 * 净利润
	 */
	public double computeNetProfit(InvestContext investContext) {
		double totalPaidInterest = computePaidInterest(investContext);
		double aheadPayCost = computeAheadPayFee(investContext);
		double cost = totalPaidInterest + aheadPayCost;
		double profit = computeGrossProfit(investContext);
		double netProfit = profit - cost;		// 收益
		return netProfit;
	}
	
	// 总支出，成本
	public double computeTotalCost(InvestContext investContext) {
		double firstPay = computeFirstPay(investContext);								// 首付
		double totalPaid = computeTotalPaid(investContext); 							// 总还款
		double aheadPayFee = computeAheadPayFee(investContext);							// 提前还款手续费
		// TODO: 需要加上赎楼贷款费用
		double totalCost = firstPay + totalPaid + aheadPayFee;
		System.out.println(String.format("总还款: %.2f元，提前还款违约金：%.2f", totalPaid, aheadPayFee));
		return totalCost;
	}

	/**
	 * @return 提前还款违约金
	 */
	public double computeAheadPayFee(InvestContext investContext) {
		switch(investContext.getLoanBank()) {
			case 交行:
				// 交行提前还款违约金，收取当次还款金额的1％
				double aheadPay = investContext.getLoanPrincipal() - computePaidPrincipal(investContext);
				System.out.println("提前还款额：" + aheadPay);
				return aheadPay * 0.01;
			default:
				throw new IllegalArgumentException("贷款银行的提前还款违约金计算还未实现: " + investContext.getLoanBank());
		}
	}

	/**
	 * 按等额本金计算还款本金+利息
	 * <pre>
	 *   每月还本付息金额=（本金/还款月数）+（本金-累计已还本金）×月利率
     *   每月本金=总本金/还款月数
     *   每月利息=（本金-累计已还本金）×月利率
     *   还款总利息=（还款月数+1）×贷款额×月利率/2
     *   还款总额=（还款月数+1）×贷款额×月利率/2+ 贷款额
     * </pre>
     * 
	 * @return 已还款总额
	 * @deprecated 计算不准确
	 */
	public double computePaid(InvestContext investContext) {
		int paidPeriodNum = (int) Math.ceil(investContext.getHoldYears() * 12);			// 已还期数, 月数
		double loanPrincipal = investContext.getLoanPrincipal();
		double paidPrincipal = computePaidPrincipal(investContext);
		double monthLoanRate = investContext.getLoanRate() / 12.0;
		double monthPrincipal = loanPrincipal * monthLoanRate;
		double paidInterest = (paidPeriodNum * loanPrincipal - monthPrincipal * (paidPeriodNum-1) * paidPeriodNum / 2.) * monthLoanRate;
		System.out.println(String.format("已还本金：%.2f，已还利息：%.2f", paidPrincipal, paidInterest));
		return paidPrincipal + paidInterest;
	}
	
	public double computePaidPrincipal(InvestContext investContext) {
		int paidPeriodNum = (int) Math.ceil(investContext.getHoldYears() * 12);			// 已还期数, 月数
		int loanPeriodNum = (int) Math.ceil(investContext.getLoanYears() * 12);			// 贷款总期数
		double loanPrincipal = investContext.getLoanPrincipal();
		double paidPrincipal = (loanPrincipal / loanPeriodNum) * paidPeriodNum;
		return paidPrincipal;
	}

	/**
	 * 已还利息
	 * 
	 * @param investContext
	 * @return
	 */
	public double computePaidInterest(InvestContext investContext) {
		int periodNo = (int) Math.ceil(investContext.getHoldYears() * 12);
		int totalPeriods = (int) Math.ceil(investContext.getLoanYears()*12);
		double loan = investContext.getLoanPrincipal();
		double loanRateYear = investContext.getLoanRate();
		double principalPerPeriod = loan / totalPeriods;
		double totalInterest = 0;
		for (int i = 1; i <= periodNo; i++) {
			double interest = interestOfPeriod(loan, i, loanRateYear, totalPeriods);
			System.out.println(String.format("第%d期已付，本金：%.2f, 利息：%.2f元，合计：%.2f",
					i, principalPerPeriod, interest, principalPerPeriod+interest));
			totalInterest += interest;
		}
		return totalInterest;
	}
	
	/**
	 * 已付本金和利息
	 * 
	 * @param investContext
	 * @return
	 */
	public double computeTotalPaid(InvestContext investContext) {
		int periodNo = (int) Math.ceil(investContext.getHoldYears() * 12);
		int totalPeriods = (int) Math.ceil(investContext.getLoanYears()*12);
		double loan = investContext.getLoanPrincipal();
		double loanRateYear = investContext.getLoanRate();
		double principalPerPeriod = loan / totalPeriods;
		double paidPrincipal = periodNo * principalPerPeriod;
		double totalInterest = 0;
		for (int i = 1; i <= periodNo; i++) {
			double interest = interestOfPeriod(loan, i, loanRateYear, totalPeriods);
			System.out.println(String.format("第%d期已付，本金：%.2f, 利息：%.2f元，合计：%.2f",
					i, principalPerPeriod, interest, principalPerPeriod+interest));
			totalInterest += interest;
		}
		double totalPaid = paidPrincipal + totalInterest;
		System.out.println(String.format("累计已付本金为: %.2f元, 利息为: %.2f元, 合计：%.2f元", 
				paidPrincipal, totalInterest, totalPaid));
		return totalPaid;
	}
	
	/**
	 * 递归形式计算某期应还利息
	 * 
	 * @param loan 总贷款额
	 * @param periodNo 指定要计算的期次，即本期应还利息，期数从1开始
	 * @param loanRateYear 贷款年利率
	 * @param totalPeriods 总期数
	 * @return 指定期的应还利息
	 */
	public double interestOfPeriod(double loan, int periodNo, double loanRateYear, int totalPeriods) {
		double principalPerPeriod = loan / totalPeriods;
		double loanRatePeriod = loanRateYear / 12.;		// 月贷款利率
		double sumOfPaidPrincipal = (periodNo - 1)  * principalPerPeriod;
		double interest = (loan - sumOfPaidPrincipal) * loanRatePeriod;
		return interest;
	}
	
	/**
	 * 计算首付款
	 * 
	 * @param investContext 投资信息上下文
	 * 
	 * @return 首付款，单位元
	 */
	public double computeFirstPay(InvestContext investContext) {
		return investContext.getBuyPrice() * investContext.getTotalArea() * investContext.getFirstPayRate();
	}
}
