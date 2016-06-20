package com.ggk.spider.service;

import java.util.List;

import com.ggk.spider.model.BusinessFinanceData;

public interface BusinessFinanceDataService {
	
	public void batchAddData(List<BusinessFinanceData> datas);
	
	public void addData(BusinessFinanceData data);

}
