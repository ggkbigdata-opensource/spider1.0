package com.ggk.spider.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggk.spider.mapper.BusinessFinanceDataMapper;
import com.ggk.spider.model.BusinessFinanceData;
import com.ggk.spider.service.BusinessFinanceDataService;

@Service
public class BusinessFinanceDataSerivceImpl implements BusinessFinanceDataService {
	
	private static final Logger logger = Logger.getLogger(BusinessFinanceDataSerivceImpl.class);
	@Autowired
	private BusinessFinanceDataMapper bfdMapper;

	@Override
	public void batchAddData(List<BusinessFinanceData> datas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addData(BusinessFinanceData data) {
		logger.info("add BusinessFinanceData : " + data.toString());;
		bfdMapper.add(data);
	}

}
