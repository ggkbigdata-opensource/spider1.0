package com.ggk.spider.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class EnterpriseBounds {
	
	private Long id;
	private Long enterpirseId;//企业实体ID
	private String bondsCode;//债券编码
	private String bondsName;//债券简称
	private String bondsFullName;//债券全称
	private String bondsType;// 债券类型 公司债或企业债
	private String duration;//发行时长
	private String issuingDate;//发行时间
	private String matureDate;//到期时间
	
	public Long getEnterpirseId() {
		return enterpirseId;
	}
	public void setEnterpirseId(Long enterpirseId) {
		this.enterpirseId = enterpirseId;
	}
	public String getBondsCode() {
		return bondsCode;
	}
	public void setBondsCode(String bondsCode) {
		this.bondsCode = bondsCode;
	}
	public String getBondsName() {
		return bondsName;
	}
	public void setBondsName(String bondsName) {
		this.bondsName = bondsName;
	}
	public String getBondsType() {
		return bondsType;
	}
	public void setBondsType(String bondsType) {
		this.bondsType = bondsType;
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
	public String getBondsFullName() {
		return bondsFullName;
	}
	public void setBondsFullName(String bondsFullName) {
		this.bondsFullName = bondsFullName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
