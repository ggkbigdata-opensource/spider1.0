package com.ggk.spider.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ggk.spider.mapper.BusinessFinanceDataMapper;
import com.ggk.spider.model.BusinessFinanceData;

@RestController
public class PdfParserController {
	private static final Logger logger = Logger.getLogger(PdfParserController.class);
	@Autowired
	private BusinessFinanceDataMapper bfdMapper;

	@RequestMapping(value = { "/spider/pdfparser/trigger" }, method = RequestMethod.POST)
	public ResponseEntity<?> trigger() {
		logger.info("come in");
		return ResponseEntity.ok("");
	}
	
	@RequestMapping(value = { "/spider/test" }, method = RequestMethod.POST)
	public ResponseEntity<?> test() {
		logger.info("test come in");
		BusinessFinanceData data = new BusinessFinanceData();
		data.setEnterpriseId("bounds_0001");
		data.setEnterpriseName("chinaenterprise");
		data.setFinanceIndexCode("qqqqqqqq");
		data.setFinanceIndexName("QQQQQQQQ");
		data.setFinanceIndexValue("77.8");
		data.setYear("2500");
		data.setAreListed("yes");
		bfdMapper.add(data);
		return ResponseEntity.ok("ok");
	}
}
