package com.ggk.spider.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggk.spider.mapper.SourceFilesMapper;
import com.ggk.spider.model.SourceFiles;
import com.ggk.spider.service.SourceFilesService;

@Service
public class SourceFilesServiceImpl implements SourceFilesService {
	
	private static final Logger logger = Logger.getLogger(BusinessFinanceDataSerivceImpl.class);
	@Autowired
	private SourceFilesMapper sfMapper;

	@Override
	public void batchAddData(List<SourceFiles> datas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addData(SourceFiles data) {
		logger.info("add SourceFiles : " + data.toString());;
		sfMapper.add(data);
	}

}
