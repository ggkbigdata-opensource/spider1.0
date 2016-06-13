package com.ggk.spider.model;

public class EnterpriseBounds {
	
	private Long id;
	private Long enterpirseId;//企业实体ID
	private String boundsCode;//债券编码
	private String boundsName;//债券简称
	private String boundsFullName;//债券全称
	private String boundsType;// 债券类型 公司债或企业债
	private String duration;//发行时长
	private String issuingDate;//发行时间
	private String matureDate;//到期时间
	
	public Long getEnterpirseId() {
		return enterpirseId;
	}
	public void setEnterpirseId(Long enterpirseId) {
		this.enterpirseId = enterpirseId;
	}
	public String getBoundsCode() {
		return boundsCode;
	}
	public void setBoundsCode(String boundsCode) {
		this.boundsCode = boundsCode;
	}
	public String getBoundsName() {
		return boundsName;
	}
	public void setBoundsName(String boundsName) {
		this.boundsName = boundsName;
	}
	public String getBoundsType() {
		return boundsType;
	}
	public void setBoundsType(String boundsType) {
		this.boundsType = boundsType;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getIssuingDate() {
		return issuingDate;
	}
	public void setIssuingDate(String issuingDate) {
		this.issuingDate = issuingDate;
	}
	public String getMatureDate() {
		return matureDate;
	}
	public void setMatureDate(String matureDate) {
		this.matureDate = matureDate;
	}
	public String getBoundsFullName() {
		return boundsFullName;
	}
	public void setBoundsFullName(String boundsFullName) {
		this.boundsFullName = boundsFullName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
