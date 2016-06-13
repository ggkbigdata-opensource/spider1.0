package com.ggk.spider.model;

public class BusinessFinanceData {
	private Long id;
	private String enterpriseId;//企业实体ID
	private String enterpriseName;// 企业名称
	private String year;//财务数据年份
	private String financeIndexCode;//财务指标编码
	private String financeIndexName;//财务指标名称
	private String financeIndexValue;//财务指标值
	private String areListed;//企业是否已上市
	
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getFinanceIndexCode() {
		return financeIndexCode;
	}
	public void setFinanceIndexCode(String financeIndexCode) {
		this.financeIndexCode = financeIndexCode;
	}
	public String getFinanceIndexName() {
		return financeIndexName;
	}
	public void setFinanceIndexName(String financeIndexName) {
		this.financeIndexName = financeIndexName;
	}
	public String getFinanceIndexValue() {
		return financeIndexValue;
	}
	public void setFinanceIndexValue(String financeIndexValue) {
		this.financeIndexValue = financeIndexValue;
	}
	public String getAreListed() {
		return areListed;
	}
	public void setAreListed(String areListed) {
		this.areListed = areListed;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
}
