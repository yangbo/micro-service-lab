package org.yangbo.microservice.house.domain;

/**
 * 房价
 * 
 * @author yangbo
 *
 */
public class HousePrice {
	private Double currentPrice;		// 房屋单价，元/平米
	private Double increasePerYear;		// 年增长率
	
	private Double afterYears;			// 预测几年后的房价
	private Double predicatePrice;		// 预测价格
	
	public Double getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	public Double getIncreasePerYear() {
		return increasePerYear;
	}
	public void setIncreasePerYear(Double increasePerYear) {
		this.increasePerYear = increasePerYear;
	}
	public Double getPredicatePrice() {
		return predicatePrice;
	}
	public void setPredicatePrice(Double predicatePrice) {
		this.predicatePrice = predicatePrice;
	}
	public Double getAfterYears() {
		return afterYears;
	}
	public void setAfterYears(Double afterYears) {
		this.afterYears = afterYears;
	}
}
