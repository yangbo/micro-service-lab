package org.yangbo.microservice.house.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangbo.microservice.house.HouseInvestComputer;
import org.yangbo.microservice.house.domain.InvestContext;

@RestController
public class HouseController {

	@Autowired
	private HouseInvestComputer investComputer;
	
	@GetMapping("/")
	public String findById() {
		return "房产投资计算器";
	}

	/**
	 * sample A - http://localhost:8010/roi?buyPrice=5000&sellPrice=7000&totalArea=93&holdYears=3&firstPayRate=1
	 */
	@GetMapping("/roi")
	public String returnOfInvest(Double sellPrice, Double buyPrice, Double holdYears, Double totalArea,
			Double firstPayRate) {
		
		InvestContext investContext = new InvestContext();
		
		if (sellPrice != null){
			investContext.setSellPrice(sellPrice);
		}
		if (buyPrice != null){
			investContext.setBuyPrice(buyPrice);
		}
		if (holdYears != null){
			investContext.setHoldYears(holdYears);
		}
		if (totalArea != null){
			investContext.setTotalArea(totalArea);
		}
		if (firstPayRate != null){
			investContext.setFirstPayRate(firstPayRate);
		}
		
		double roi = investComputer.computeReturnRatePerYear(investContext);
		double grossProfit = investComputer.computeGrossProfit(investContext);
		double netProfit = investComputer.computeNetProfit(investContext);
		double firstPay = investComputer.computeFirstPay(investContext);
		double grossROI = investComputer.computeGrossReturnOnInvestPerYear(investContext);
		double netROI = investComputer.computeNetReturnOnInvestPerYear(investContext);
		double totalInvest = investComputer.computeTotalCost(investContext);
		
		String response = String.format("投资信息: <br/>%s:"
				+ "<p> 总投资 = %.2f 元</p>"
				+ "<p> 毛利润 = %.2f 元</p>"
				+ "<p> 净利润 = %.2f 元"
				+ "<p> 年投资回报率(net) = %.2f%%</p>"
				+ "<p> 年投资回报率(gross) = %.2f%%</p>"
				+ "<p> 首付 = %.2f 元 </p>"
				+ "<p> 无贷款年投资回报率 = %.2f%%</p>"
				, 
				investContext, totalInvest, grossProfit, netProfit, 
				netROI*100, grossROI*100, firstPay, roi*100);
		return response;
	}
}
