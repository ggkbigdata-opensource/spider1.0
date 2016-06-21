package com.ggk.spider.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
		this.runFailReport();
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
		String dir = "D:\\codes\\spider\\reporttxt";
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
				String txtErrorFilePath = string.replace("reporttxt", "reporttxt2dataerror");
				try {
					FileUtils.copyFile(new File(string), new File(txtErrorFilePath));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void runFailReport() {
		String src = "D:\\codes\\spider\\fail";
		ConvertExecuter executer = new ConvertExecuter();
		List<String> list = executer.getFileList(src);
		String fileName = null;
		String enterpriseName = null;
		File txt = null;
		for (String filepath : list) {
			String encoding = "UTF-8"; // 编码方式
			txt = new File(filepath);
			fileName = filepath.substring(filepath.lastIndexOf(File.separator) + 1, filepath.length());
			InputStreamReader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(txt), encoding);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			if(null == reader){
				return;
			}
			
			LineNumberReader br = null;
			try {
				br = new LineNumberReader(reader);
				String s = "";
				while ((s = br.readLine()) != null) {
					if (this.isEnterpriseName(s)) {
						enterpriseName = s;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SourceFiles sf = new SourceFiles();
			sf.setEnterpriseName(enterpriseName);
			sf.setFileName(fileName);
			
			sfService.addData(sf);
		}
		
	}
	
	/**
	 * 判断是否是公司名称
	 * */
	private boolean isEnterpriseName(String s) {
		if (null == s) {
			return false;
		}
		// 匹配公司名称
		if (s.contains("公司")) {
			if (s.contains("有限") || s.contains("责任") || s.contains("集团") || s.contains("总公司") || s.contains("股份")) {
				return true;
			}
		}
		return false;
	}

}
