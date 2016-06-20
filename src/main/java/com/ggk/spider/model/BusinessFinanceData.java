package com.ggk.spider.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BusinessFinanceData {
	private Long id;
	private String enterpriseId;//企业实体ID
	private String enterpriseName;// 企业名称
	private String year;//财务数据年份
	private String financeIndexCode;//财务指标编码
	private String financeIndexName;//财务指标名称
	private String financeIndexValue;//财务指标值
	private String reportTitle;//报表标题
	private String areListed;//企业是否已上市
	private String srcFileName;//pdf文件

	public BusinessFinanceData() {
	}
	

	public BusinessFinanceData(String enterpriseId, String enterpriseName, String year, String financeIndexCode,
			String financeIndexName, String financeIndexValue, String reportTitle, String areListed) {
		super();
		this.enterpriseId = enterpriseId;
		this.enterpriseName = enterpriseName;
		this.year = year;
		this.financeIndexCode = financeIndexCode;
		this.financeIndexName = financeIndexName;
		this.financeIndexValue = financeIndexValue;
		this.reportTitle = reportTitle;
		this.areListed = areListed;
	}



	public String getSrcFileName() {
		return srcFileName;
	}


	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}


	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

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
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
