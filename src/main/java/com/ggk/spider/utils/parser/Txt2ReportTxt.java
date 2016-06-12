package com.ggk.spider.utils.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class Txt2ReportTxt {

	private String txtSrcFilePath;
	private String reportTxtDestFileDir;

	public Txt2ReportTxt(String txtSrcFilePath, String reportTxtDestFileDir) {
		setReportTxtDestFileDir(reportTxtDestFileDir);
		setTxtSrcFilePath(txtSrcFilePath);
	}

	public String getTxtSrcFilePath() {
		return txtSrcFilePath;
	}

	public void setTxtSrcFilePath(String txtSrcFilePath) {
		this.txtSrcFilePath = txtSrcFilePath;
	}

	public String getReportTxtDestFileDir() {
		return reportTxtDestFileDir;
	}

	public void setReportTxtDestFileDir(String reportTxtDestFileDir) {
		this.reportTxtDestFileDir = reportTxtDestFileDir;
	}
	
	public String convert(){
		
		if (StringUtils.isEmpty(getTxtSrcFilePath()) || StringUtils.isEmpty(getReportTxtDestFileDir())) {
			return null;
		}
		
		String locateRegExp = "^(速动比率).*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";// 匹配速动比率+浮点数格式
		
		String encoding = "UTF-8"; // 编码方式
		File txt = new File(getTxtSrcFilePath());
		
		InputStreamReader reader = null;
		StringBuffer result = new StringBuffer();
		try {
			reader = new InputStreamReader(new FileInputStream(txt), encoding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(null == reader){
			return null;
		}
		
		LineNumberReader br = null;
		String reportTxtName = reportTxtDestFileDir +File.separator + txt.getName();

		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		Map<String,String> reportTxtContentMap = new HashMap<String,String>();
		File reportTxt = new File(reportTxtName);
		try {
			br = new LineNumberReader(reader);
			String s = "";
			while ((s = br.readLine()) != null) {
				if (s.matches(locateRegExp)) {
					result.append("###");//TODO
					//resultList.add(this.matchFinanceData(br,reportTxt));
					// 暂时取出浮动十行
					reportTxtContentMap.put("beginLineNumber", String.valueOf(br.getLineNumber() - 10));
					reportTxtContentMap.put("endLineNumber", String.valueOf(br.getLineNumber() + 10));
					reportTxtContentMap.put("currentLineNumber", String.valueOf(br.getLineNumber()));
					this.getFullData(reportTxtContentMap, txt);
					resultList.add(this.matchFinanceData(reportTxtContentMap));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.writeReportTxt(resultList, reportTxt);
		return null;
	}
	
	private void getFullData(Map<String,String> reportTxtContentMap, File txt){
		InputStreamReader reader = null;
		String encoding = "UTF-8"; // 编码方式
		try {
			reader = new InputStreamReader(new FileInputStream(txt), encoding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		LineNumberReader br = null;
		try {
			int beginLineNumber = Integer.parseInt(reportTxtContentMap.get("beginLineNumber"));
			int endLineNumber = Integer.parseInt(reportTxtContentMap.get("endLineNumber"));
			br = new LineNumberReader(reader);
			String s = "";
			while ((s = br.readLine()) != null) {
				if (beginLineNumber <= br.getLineNumber() && endLineNumber >= br.getLineNumber()) {
					//resultList.add(this.matchFinanceData(br,reportTxt));
					reportTxtContentMap.put(String.valueOf(br.getLineNumber()), s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Map<String,String> matchFinanceData(Map<String,String> reportTxtContentMap){
		
		String rangeRegExp = "^.*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";//浮点数
		String charRegExp =  "^.*[\u4E00-\u9FA5].*$";
		Map<String,String> resultMap = new HashMap<String,String>();
		
		int beginLineNumber = Integer.parseInt(reportTxtContentMap.get("beginLineNumber"));
		int endLineNumber = Integer.parseInt(reportTxtContentMap.get("endLineNumber"));
		int currentLineNumber = Integer.parseInt(reportTxtContentMap.get("currentLineNumber"));
		
		// 向上匹配
		resultMap.put(String.valueOf(currentLineNumber), reportTxtContentMap.get(String.valueOf(currentLineNumber)));
		String value = null;
		int i = currentLineNumber;
		for (i = currentLineNumber - 1; i >= beginLineNumber; i--){
			value = reportTxtContentMap.get(String.valueOf(i));
			if( null != value && value.matches(rangeRegExp)){
				resultMap.put(String.valueOf(i), value);
			}else if(null != value && value.indexOf("20") > -1){//匹配年份 2015...
				resultMap.put(String.valueOf(i), value);
			}else{
				if(null!=value && value.matches(charRegExp)){
					resultMap.put(String.valueOf(i), value);
					break;
				}else{
					break;
				}
			}
		}
		value = null;
		
		// 向下匹配
		int j = currentLineNumber;
		for (j = currentLineNumber + 1; j <= endLineNumber; j++){
			value = reportTxtContentMap.get(String.valueOf(j));
			if( null != value && value.matches(rangeRegExp)){
				resultMap.put(String.valueOf(j), value);
			}else if(null != value && value.indexOf("-") > -1){//匹配数据为空（--）的情况
				resultMap.put(String.valueOf(j), value);
			}else{
				break;
			}
		}
		
		resultMap.put("beginLineNumber", String.valueOf(i));
		resultMap.put("endLineNumber", String.valueOf(j));
		return resultMap;
		
	}
	
	
	private void writeReportTxt(List<Map<String,String>> resultList, File reportTxt){
		if(CollectionUtils.isEmpty(resultList) || null == reportTxt){
			return;
		}
		
		StringBuffer content = new StringBuffer();
		int beginLineNumber,endLineNumber = 0;
		for(Map<String,String> reportTxtContentMap : resultList){
			
			beginLineNumber = Integer.parseInt(reportTxtContentMap.get("beginLineNumber"));
			endLineNumber = Integer.parseInt(reportTxtContentMap.get("endLineNumber"));
			
			String contentStr = null;
			for(int i = beginLineNumber; i <= endLineNumber; i++){
				contentStr = reportTxtContentMap.get(String.valueOf(i));
				if(null != contentStr){
					content.append(contentStr);
					content.append("\r\n");// \r\n即为换行 
				}
			}
		}
		
        BufferedWriter out = null;
		try {
			reportTxt.createNewFile(); // 创建新文件  
			out = new BufferedWriter(new FileWriter(reportTxt));
			out.write(content.toString()); 
			out.flush(); // 把缓存区内容压入文件  
			out.close(); // 最后记得关闭文件  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
}
