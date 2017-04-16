package org.yangbo.microservice.house;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.yangbo.microservice.house.domain.InvestContext;

public class HouseInvestComputerTest {

	private HouseInvestComputer houseInvestComputer = new HouseInvestComputer();
	
	@Test
	public void testInterestOfPeriod() {
		double loanRateYear = 0.12;
		int totalPeriods = 10;
		int periodNo = 1;  	// 第几期
		double loan = 10000;
		double loanRatePeriod = loanRateYear / 12;
		
		double interest = houseInvestComputer.interestOfPeriod(loan, periodNo, loanRateYear, totalPeriods);
		assertThat(interest, equalTo(loan*loanRatePeriod));
		
		periodNo = 2;
		interest = houseInvestComputer.interestOfPeriod(loan, periodNo, loanRateYear, totalPeriods);
		assertThat(interest, equalTo((loan-loan/totalPeriods)*loanRatePeriod));
		
		periodNo = 3;
		interest = houseInvestComputer.interestOfPeriod(loan, periodNo, loanRateYear, totalPeriods);
		assertThat(interest, equalTo((loan-2*loan/totalPeriods)*loanRatePeriod));
	}
	
	@Test
	public void testComputePaid() {
		InvestContext investContext = new InvestContext();
		double paid = houseInvestComputer.computePaid(investContext);
		System.out.println("paid: " + paid);
		
		double paid2 = houseInvestComputer.computeTotalPaid(investContext);
		System.out.println("paid2: " + paid2);
	}

}
