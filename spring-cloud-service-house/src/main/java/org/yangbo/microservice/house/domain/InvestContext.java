package org.yangbo.microservice.house.domain;

/**
 * 投资计算需要考虑的因素
 * 
 * @author yangbo
 */
public class InvestContext {

	private double buyPrice;		// 买入时的房屋单价，元/平米
	private double sellPrice;		// 买出时的房屋单价，元/平米
	private double loanRate;		// 贷款利率，百分比/年
	private double loanYears;		// 贷款年限
	private double totalArea;		// 总面积，平方米
	private double firstPayRate;	// 首付比例
	private double holdYears;		// 持有时间,按年计算
	private Bank loanBank;			// 贷款银行

	public InvestContext() {
		this.buyPrice = 9000;		// 买入单价，RMB/m^2
		this.sellPrice = 11000;		// 卖出单价，RMB/m^2
		this.loanRate = 0.05;		// 贷款利率为 5% / 年
		this.loanYears = 30;		// 贷款年限
		this.firstPayRate = 0.2;	// 首付 20%
		this.totalArea = 88.81;		// 面积88平方米
		this.holdYears = 3;
		this.loanBank = Bank.交行;
	}
	
	public String toString() {
		return String.format("买入单价：%.2f元; 卖出单价: %.2f元; 贷款利率：%.2f%%; 首付比例：%.2f%%; 总面积: %.2f平米; 持有时间：%.2f年；贷款年限：%d年",
				buyPrice, sellPrice, loanRate*100, firstPayRate*100, totalArea, holdYears, (int)loanYears);
	}

	public double getLoanYears() {
		return loanYears;
	}

	public void setLoanYears(double loanYears) {
		this.loanYears = loanYears;
	}
	
	public double getHoldYears() {
		return holdYears;
	}

	public void setHoldYears(double holdYears) {
		this.holdYears = holdYears;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(double loanRate) {
		this.loanRate = loanRate;
	}

	public double getTotalArea() {
		return totalArea;
	}

	public void setTotalArea(double totalArea) {
		this.totalArea = totalArea;
	}

	public double getFirstPayRate() {
		return firstPayRate;
	}

	public void setFirstPayRate(double firstPayRate) {
		this.firstPayRate = firstPayRate;
	}

	/**
	 * 房子总价
	 */
	public double getHouseValue() {
		return this.buyPrice * this.totalArea;
	}

	/**
	 * 贷款本金
	 */
	public double getLoanPrincipal() {
		double houseCost = getBuyPrice() * getTotalArea();	// 购买总价
		double firstPay = getFirstPayRate() * houseCost;	// 首付款
		return houseCost - firstPay;
	}

	public Bank getLoanBank() {
		return loanBank;
	}

	public void setLoanBank(Bank loanBank) {
		this.loanBank = loanBank;
	}

}
