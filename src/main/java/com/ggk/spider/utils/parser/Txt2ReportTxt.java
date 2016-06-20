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
	
	private String[] financeIndexeKeys = {"合并报表口径", "母公司报表口径", "主要财务指标"};

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
	
	/**
	 * 重新得到reportTxt文件
	 * */
	public void getReportTxt() {
		
		if (StringUtils.isEmpty(getTxtSrcFilePath()) || StringUtils.isEmpty(getReportTxtDestFileDir())) {
			return;
		}
		
		String locateRegExp = "^(速动比率).*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";// 匹配速动比率+浮点数格式
		String rangeRegExp = "^.*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";//浮点数
		String charRegExp =  "^.*[\u4E00-\u9FA5].*$";
		
		String encoding = "UTF-8"; // 编码方式
		File txt = new File(getTxtSrcFilePath());
		
		InputStreamReader reader = null;
		StringBuffer result = new StringBuffer();
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
		String reportTxtName = reportTxtDestFileDir +File.separator + txt.getName();

		// 一个公司一个list
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		// 一个表一个map
		Map<String,String> reportTxtContentMap = new HashMap<String,String>();
		File reportTxt = new File(reportTxtName);
		
		try {
			br = new LineNumberReader(reader);
			String s = "";
			while ((s = br.readLine()) != null) {
				int beginLineNumber = -1;
				int endLineNumber = -1;
				
				// 1.匹配标题  主要财务指标， 母公司报表口径，合并报表口径
				if (s.contains("主要财务指标") && !s.contains(".")) {
					beginLineNumber = br.getLineNumber();
					
					while ((s = br.readLine()) != null) {
						if (!s.matches(rangeRegExp)) {
							s = br.readLine();
							if (!s.matches(rangeRegExp)) {
								s = br.readLine();
								endLineNumber = br.getLineNumber() - 2;
							} else {
								continue;
							}
						}
					}
					if (endLineNumber > beginLineNumber) {
						reportTxtContentMap.put("beginLineNumber", String.valueOf(beginLineNumber));
						reportTxtContentMap.put("endLineNumber", String.valueOf(endLineNumber));
					}
					continue;
				}
				
				// 2.含主要财务指标的表头
				if (s.contains("主要财务指标") && !s.contains(".") && 
						(s.indexOf("201") != s.lastIndexOf("201") || s.indexOf("200") != s.lastIndexOf("200"))) {
					beginLineNumber = br.getLineNumber();
					
					// 找结束行号
					while ((s = br.readLine()) != null) {
						if (!s.matches(rangeRegExp)) {
							s = br.readLine();
							if (!s.matches(rangeRegExp)) {
								s = br.readLine();
								endLineNumber = br.getLineNumber() - 2;
							} else {
								continue;
							}
						}
					}
					if (endLineNumber > beginLineNumber) {
						reportTxtContentMap.put("beginLineNumber", String.valueOf(beginLineNumber));
						reportTxtContentMap.put("endLineNumber", String.valueOf(endLineNumber));
					}
				}
				
			}
		} catch (IOException e) {
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
		
		this.writeReportTxt(resultList, reportTxt);
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
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
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
					result.append("###");
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
				e.printStackTrace();
			}
		}
		
		this.writeReportTxt(resultList, reportTxt);
		return null;
	}
	
	/**
	 * 找到企业名字
	 * */
	public String getEnterpriseName(String path) {
		int tag = 0; // 声明前是0，声明后是1
		String name = null;
		String encoding = "UTF-8"; // 编码方式
		InputStreamReader reader = null;
		LineNumberReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(path),
					encoding);
			br = new LineNumberReader(reader);
			String s = "";
			while ((s = br.readLine()) != null) {
				if (s.contains("声") && s.contains("明")) {
					tag = 1;
				}
				if (tag == 0) { // 声明前
					if (s.contains("公司") || s.contains("有限")) {
						name = s.substring(0, s.indexOf("司") + 1);
						break;
					}
				} else {
					if (s.contains("募集说明书 ") && s.contains("公司")) {
						name = s.substring(0, s.indexOf("司") + 1);
						break;
					}
				}
				
				if (tag == 1) { // 声明后
					if (s.contains("债券名称：") && s.contains("公司")) {
//						if (s.contains("年")) {
//							name = s.substring(s.indexOf("年") + 1, s.indexOf("司"));
//							break;
//						} else {
							name = s.substring(s.indexOf("：") + 1, s.indexOf("司") + 1);
							break;
//						}
					}
				}
			}
			tag = 0;
		} catch (Exception e) {
			System.out.println(path);
//			e.printStackTrace();
		}
		return name;
	}
	
	private void getFullData(Map<String,String> reportTxtContentMap, File txt){
		InputStreamReader reader = null;
		String encoding = "UTF-8"; // 编码方式
		try {
			reader = new InputStreamReader(new FileInputStream(txt), encoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
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
				e.printStackTrace();
			}
		}
	}
	
	private Map<String,String> matchFinanceData(Map<String,String> reportTxtContentMap){
		String cmdCopy = "cmd /c copy";
		String cmd = "";
		Runtime runtime = Runtime.getRuntime();
		String failDir = "F:\\ggkData\\fail";
		
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
		
		// 查询企业名称
		String enterpriseName = this.getEnterpriseName(getTxtSrcFilePath());
		if (null == enterpriseName) { // 没找到公司名称，就把该公司的txt文件移动到fail文件夹中
			cmd = cmdCopy + " \"" + getTxtSrcFilePath() + "\" " + failDir;
			try {
				runtime.exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			resultMap.put("enterpriseName", enterpriseName);
		}
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
			
			// 专门填上企业名称
			content.append(reportTxtContentMap.get("enterpriseName"));
			content.append("\r\n");
		}
		
        BufferedWriter out = null;
		try {
			reportTxt.createNewFile(); // 创建新文件  
			out = new BufferedWriter(new FileWriter(reportTxt));
			out.write(content.toString()); 
			out.flush(); // 把缓存区内容压入文件  
			out.close(); // 最后记得关闭文件  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
}
