package com.ggk.spider.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ggk.spider.model.BusinessFinanceData;
import com.ggk.spider.model.SourceFiles;
import com.ggk.spider.service.BusinessFinanceDataService;
import com.ggk.spider.service.SourceFilesService;
import com.ggk.spider.utils.parser.ConvertExecuter;
import com.ggk.spider.utils.parser.ReportTxtDataOperation;

@RestController
public class PdfParserController {
	private static final Logger logger = Logger.getLogger(PdfParserController.class);
	@Autowired
	private BusinessFinanceDataService bfdService;
	
	@Autowired
	private SourceFilesService sfService;
	
//	private String dir = "F:\\ggkData\\reporttxt";
//	private ReportTxtDataOperation operation = new ReportTxtDataOperation(path);

	@RequestMapping(value = { "/spider/pdfparser/trigger" }, method = RequestMethod.POST)
	public ResponseEntity<?> trigger() {
		logger.info("come in");
		return ResponseEntity.ok("");
	}
	
	@RequestMapping(value = { "/spider/test" }, method = RequestMethod.POST)
	public ResponseEntity<?> test() {
		logger.info("test come in");
//		BusinessFinanceData data = new BusinessFinanceData();
//		data.setEnterpriseId("bounds_0001");
//		data.setEnterpriseName("chinaenterprise");
//		data.setFinanceIndexCode("qqqqqqqq");
//		data.setFinanceIndexName("QQQQQQQQ");
//		data.setFinanceIndexValue("77.8");
//		data.setYear("2500");
//		data.setAreListed("yes");

		
//		List<BusinessFinanceData> dataList = new ArrayList<>(); 
//		dataList = operation.processFinanceData();
//		for (BusinessFinanceData data : dataList) {
//			bfdService.addData(data);
//		}
//		SourceFiles fileData = operation.processSourceFilesData();
//		sfService.addData(fileData);
		save();
		return ResponseEntity.ok("ok");
	}
	
	public void save() {
		String dir = "F:\\ggkData\\reporttxt";
		ConvertExecuter executer = new ConvertExecuter();
		List<String> list = executer.getFileList(dir);
		String fileName = "";
		for (String string : list) {
			ReportTxtDataOperation operation = new ReportTxtDataOperation(string);
			List<BusinessFinanceData> dataList = new ArrayList<>();
			try {
				dataList = operation.execute();
				if (null == dataList) {
					continue;
				}
				for (BusinessFinanceData data : dataList) {
					fileName = string.substring(string.lastIndexOf("\\") + 1 , string.lastIndexOf(".")) + ".pdf";
					data.setSrcFileName(fileName);
					bfdService.addData(data);
				}
//				SourceFiles fileData = operation.processSourceFilesData();
//				sfService.addData(fileData);
			} catch (Exception e) {
				System.out.println(string);
				e.printStackTrace();
			}
		}
	}
}
