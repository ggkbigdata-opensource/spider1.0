package com.ggk.spider.utils.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import com.ggk.spider.model.BusinessFinanceData;
import com.ggk.spider.model.SourceFiles;
import com.ggk.spider.utils.encrypt.MD5Util;

public class ReportTxtDataOperation {
	
	// 指标数量
	private int indexCount = 0;
	private int titleCount = 0;
	
	private String rangeRegExp = "^.*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";
	private String pokFileDir = "D:\\codes\\spider\\pok";
	private String failFileDir = "D:\\codes\\spider\\fail";
	private String enterpriseName = "";

//	public static void main(String[] args) {
//		String reportTxtFilePath = "F:\\ggkData\\reporttxt\\fa88ceaf660e4bdda3dc2730f709ea72.txt";
//		ReportTxtDataOperation operation = new ReportTxtDataOperation(reportTxtFilePath);
//		operation.processFinanceData();
//	}

	private String reportTxtFilePath; // 报表txt全路径

	public String getReportTxtFilePath() {
		return reportTxtFilePath;
	}

	public void setReportTxtFilePath(String reportTxtFilePath) {
		this.reportTxtFilePath = reportTxtFilePath;
	}

	public ReportTxtDataOperation(String reportTxtFilePath) {
		setReportTxtFilePath(reportTxtFilePath);
	}
	
		
	/**
	 * 得到源文件文件名信息
	 * */
	public SourceFiles processSourceFilesData() {
		// 容错处理
		if (StringUtils.isEmpty(getReportTxtFilePath())) {
			return null;
		}
		
		SourceFiles data = new SourceFiles();
		
		// 文件全路径
		String txtFilePath = this.getReportTxtFilePath();
		String txtFileName = txtFilePath.substring(txtFilePath.lastIndexOf("\\") + 1, txtFilePath.indexOf(".")) + ".pdf";
		data.setFileName(txtFileName);
		
		String encoding = "UTF-8"; // 编码方式
		InputStreamReader reader = null;
		LineNumberReader br = null;
		
		try {
			reader = new InputStreamReader(new FileInputStream(txtFilePath),
					encoding);
			br = new LineNumberReader(reader);
			String s = "";
			while((s = br.readLine()) != null) {
				// 匹配公司名称
				if (s.contains("公司")) {
					if (s.contains("有限") || s.contains("责任") || s.contains("集团") || s.contains("总公司") || s.contains("股份")) {
						data.setEnterpriseName(s);
						data.setEnterpriseId(this.getEnterpriseId(s));
						break;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			copyErrorFile();
		} catch (FileNotFoundException e) {
			copyErrorFile();
		} catch (IOException e) {
			copyErrorFile();
		}		
		return data;
	}
	
	/**
	 * 将数据提取出来转化成BusinessFinanceData集
	 * */
	public List<BusinessFinanceData> execute() {
		String txtFilePath = getReportTxtFilePath();
		String encoding = "UTF-8"; // 编码方式
		InputStreamReader reader = null;
		LineNumberReader br = null;
		
		int beginLineNumber = 0;
		int endLineNumber = 0;
		
		Runtime runtime = Runtime.getRuntime();
		
		String cmdCopy = "cmd /c copy";
		String cmd = "";
		int dirtyTag = 0;
		
		// 用来存正确表行号map的list
		List<Map<String, Integer>> mapList = new ArrayList<>();
		try {
			reader = new InputStreamReader(new FileInputStream(txtFilePath),
					encoding);
			br = new LineNumberReader(reader);
			String s = "";
			StringBuffer content = new StringBuffer();
			
			while ((s = br.readLine()) != null) {
				
				// 判断是否是脏数据，如果是，continue
				if (this.isDirtyData(s)) {
					content.append(s);
					content.append("\r\n");
					dirtyTag = 1;
					continue;
				}
				
				// 判断是不是标题，如果是，设置初始行号，然后开始找结束行号，找到后记录下来，将初始行号和结束行号存到map当中
				// 从这里进去要判断是否是一张正确的表
				if (this.isTitle(s)) {
					
					s = br.readLine();
					
					if (this.isHead(s)) { // 是表头
						beginLineNumber = br.getLineNumber() - 1;
						while ((s = br.readLine()) != null) {
							
							if (this.isEnterpriseName(s)) { // 如果是公司名称，那就给enterpriseName赋值
								if (s.contains("年")) {
									enterpriseName = s.substring(s.indexOf("年") + 1).trim();
									continue;
								} else {
									enterpriseName = s;
									continue;
								}
								
							}
							
							if (this.isHead(s)) { // 还是表头
								continue;
							} else if (this.isReport(s)) { // 是报表信息
								endLineNumber = br.getLineNumber();
								continue;
							} else {
								endLineNumber = br.getLineNumber() - 1;
								
								// 如果有结尾行号大于开始行号，则证明该表是正确的
								if (endLineNumber > beginLineNumber) {
									Map<String, Integer> indexMap = new HashMap<>();
									indexMap.put("beginIndex", beginLineNumber);
									indexMap.put("endIndex", endLineNumber);
									mapList.add(indexMap);
									beginLineNumber = 0;
									endLineNumber = 0;
									break;
								}
								
							}
							
						}
					}
					
				}
				
				if (null == s) {
					if (endLineNumber > beginLineNumber) {
						Map<String, Integer> indexMap = new HashMap<>();
						indexMap.put("beginIndex", beginLineNumber);
						indexMap.put("endIndex", endLineNumber);
						mapList.add(indexMap);
						beginLineNumber = 0;
						endLineNumber = 0;
						break;
					}
				}
				
				// 判断是不是表头，如果是，设置初始行号，然后开始找结束行号，找到后记录下来，将初始行号和结束行号存到map当中
				// 从这里进去要判断是否是一张正确的表
				if (this.isHead(s)) {
					s = br.readLine();
					if (!this.isHead(s) && !this.isReport(s)) {
						continue;
					} else {
						beginLineNumber = br.getLineNumber() - 1;
						while ((s = br.readLine()) != null) {
							
							if (this.isEnterpriseName(s)) { // 如果是公司名称，那就给enterpriseName赋值
								if (s.contains("年")) {
									enterpriseName = s.substring(s.indexOf("年") + 1).trim();
									continue;
								} else {
									enterpriseName = s;
									continue;
								}
								
							}
							
							if (this.isHead(s)) { // 还是表头
								continue;
							} else if (this.isReport(s)) { // 是报表信息
								endLineNumber = br.getLineNumber();
								continue;
							} else  {
								endLineNumber = br.getLineNumber() - 1;
								
								// 如果有结尾行号大于开始行号，则证明该表是正确的
								if (endLineNumber > beginLineNumber) {
									Map<String, Integer> indexMap = new HashMap<>();
									indexMap.put("beginIndex", beginLineNumber);
									indexMap.put("endIndex", endLineNumber);
									mapList.add(indexMap);
									beginLineNumber = 0;
									endLineNumber = 0;
									break;
								}
								
							}
							
						}
						
					}
				}
				
				if (null == s) {
					if (endLineNumber > beginLineNumber) {
						Map<String, Integer> indexMap = new HashMap<>();
						indexMap.put("beginIndex", beginLineNumber);
						indexMap.put("endIndex", endLineNumber);
						mapList.add(indexMap);
						beginLineNumber = 0;
						endLineNumber = 0;
						break;
					}
				}
	
			}
			if(null != mapList && mapList.isEmpty()){
				cmd = cmdCopy + " \"" + txtFilePath + "\" " + failFileDir;
				runtime.exec(cmd);
				
			}else if (dirtyTag == 1) {
				cmd = cmdCopy + " \"" + txtFilePath + "\" " + pokFileDir;
				runtime.exec(cmd);
				
				dirtyTag = 0;
			}
			
		} catch (UnsupportedEncodingException e) {
			copyErrorFile();
		} catch (FileNotFoundException e) {
			copyErrorFile();
		} catch (IOException e) {
			copyErrorFile();
		} 
		return this.getBusinessFinanceDataList(mapList);
	}
	
	/**
	 * 判断是否是标题
	 * */ 
	private boolean isTitle(String s) {
		if (null == s) {
			return false;
		}
		if (s.contains("表") || s.contains("近三年")|| s.contains("合并口径") || 
				(s.contains("主要财务指标") && s.contains(")"))) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是是否是表头
	 * */
	private boolean isHead(String s) {
		if (null == s) {
			return false;
		}
		if (s.contains("注")) {
			return false;
		}
		if (s.contains("200") || s.contains("201")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是包含浮点数或者“-”的表内容
	 * */
	private boolean isReport(String s) {
		if (null == s) {
			return false;
		}
		if (s.matches(rangeRegExp)) {
			return true;
		} else if (s.contains(".")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断是否是脏数据
	 * */
	private boolean isDirtyData(String s) {
		if (null == s) {
			return false;
		}
		if (this.isReport(s) && !this.isHead(s)) {
			return true;
		}
		return false;
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
	
	public List<BusinessFinanceData> getBusinessFinanceDataList(List<Map<String, Integer>> lineNumberList) {
				
		List<BusinessFinanceData> dataList = new ArrayList<>();
		int beginIndex = 0;
		int endIndex = 0;
		
		// 遍历lineNumberList，找到开始行号和结束行号，然后将其转化为模型数据
		for (Map<String, Integer> indexMap : lineNumberList) {
			beginIndex = indexMap.get("beginIndex");
			endIndex = indexMap.get("endIndex");
			if (beginIndex >= endIndex) {
				continue;
			}
			List<BusinessFinanceData> datas = this.processFinanceData(beginIndex, endIndex);
			if (null == datas) {
				return dataList;
			}
			for (BusinessFinanceData data : datas) {
				dataList.add(data);
			}
		}
		
		return dataList;
	}
	
	
	/**
	 * 将财务数据切割存入数据库当中
	 */
	public List<BusinessFinanceData> processFinanceData(int beginIndex, int endIndex) {

		// 容错处理
		if (StringUtils.isEmpty(getReportTxtFilePath())) {
			return null;
		}
		
		Map<String, String> dataMap = new HashMap<String, String>(); // 存储数据的map
		
		// 文件全路径
		String txtFilePath = this.getReportTxtFilePath();

		String encoding = "UTF-8"; // 编码方式
		InputStreamReader reader = null;
		LineNumberReader br = null;
		// 存储每一行的数组
		String[] strings = new String[20];

		// 长表头信息
		String headString = "";
		try {
			reader = new InputStreamReader(new FileInputStream(txtFilePath),
					encoding);
			br = new LineNumberReader(reader);
			String s = "";
			// 正则表达式
			String rangeRegExp = "^.*-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)./*$";
			while ((s = br.readLine()) != null) {
				
				if (br.getLineNumber() < beginIndex) {
					continue;
				}
				
				if (br.getLineNumber() > endIndex) {
					break;
				}
				
				// 过滤历史数和模拟数
				if (s.contains("历史数")) {
					continue;
				}
				
				// 过滤发行前和发行后
				if (s.contains("发行前")) {
					continue;
				}
				
				// 存储标题信息
				if (s.contains("表") || s.contains("近三年")|| s.contains("合并口径") || (s.contains("主要财务指标") && s.contains(")"))) {
					titleCount += 1;
					dataMap.put("reportTitle" + titleCount, s);
					dataMap.put("titleCount", String.valueOf(titleCount));
					continue;
				}
				
				// 匹配公司名称
				if (s.contains("公司")) {
					if (s.contains("有限") || s.contains("责任") || s.contains("集团")) {
						dataMap.put("enterpriseName", s);
						dataMap.put("enterpriseId", this.getEnterpriseId(s));
						continue;
					}
				}
				
				// 处理表头
				if (s.contains("项目") || (s.contains("项") && s.contains("目")) 
						|| (s.contains("财务指标") && s.contains("20")) 
						|| (s.contains("短") && s.contains("20"))
						|| (s.contains("指标") && s.contains("20"))) {
					headString += s;
					while ((s = br.readLine()) != null) {
						// 如果表头包含“20”的字符串，则代表有年份，如2015
						if (!s.contains("项") && s.indexOf("20") > -1 && !s.contains(".")) {
							headString += " " + s;
						} else {
							strings = this.operateLongHead(headString);
							this.saveData(strings, dataMap);
							headString = "";
							
							// 处理指标数值
							if (s.matches(rangeRegExp)) { // 匹配浮点数
								strings = s.split(" ");
								strings = this.removeArraySpace(strings);
								this.saveData(strings, dataMap);
							} else if (s.contains(".")) {
								strings = s.split(" ");
								strings = this.removeArraySpace(strings);
								this.saveData(strings, dataMap);
							}
							
							break;
						}
					}
					continue;
				}

				// 处理指标数值
				if (s.matches(rangeRegExp)) { // 匹配浮点数
					strings = s.split(" ");
					strings = this.removeArraySpace(strings);
					this.saveData(strings, dataMap);
				} else if (s.contains(".")) {
					strings = s.split(" ");
					strings = this.removeArraySpace(strings);
					this.saveData(strings, dataMap);
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			copyErrorFile();
		} catch (FileNotFoundException e) {
			copyErrorFile();
		} catch (IOException e) {
			copyErrorFile();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return this.traversalMap(dataMap);
	}
	
	/**
	 * 长表头信息的处理
	 * */
	private String[] operateLongHead(String string) {
		// 用list存储
		List<String> list = new ArrayList<String>();
		String firstYearString = "";
		char[] chs = string.toCharArray();
		// 遍历字符串拿到第一个年份
		for (char ch : chs) {
			if (ch >= '0' && ch <= '9') {
				firstYearString += ch;
			} 
			if (ch == '年' || ch == '/' || ch == '.' || ch == '-') {
				break;
			}
		}
		
		if (firstYearString == "") {
			return null;
		}
		String subStringName = string.substring(0, string.indexOf(firstYearString));
		String subStringDate = string.substring(string.indexOf(firstYearString));
		list.add(subStringName);
		String[] strs = subStringDate.split(" ");
		if (strs.length == 3 || strs.length == 4) {
			for (String s : strs) {
				list.add(s);
			}
			String[] strings = this.list2Array(list);
			return strings;
			
		}
		
		int firstYear;
		try {
			firstYear = Integer.valueOf(firstYearString);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			return null;
		}
		
		// 得到每个年份的String
		String secondYearString = String.valueOf(firstYear - 1);
		String thirdYearString = String.valueOf(firstYear - 2);
		String fourthYearString = String.valueOf(firstYear - 3);
		String fifthYearString = String.valueOf(firstYear - 4);
		int beginIndex = 0;
		int endIndex = 0;
		
		if (string.contains(secondYearString)) {
			beginIndex = string.indexOf(firstYearString);
			endIndex = string.indexOf(secondYearString);
			list.add(string.substring(beginIndex, endIndex));
			beginIndex = endIndex;
		} 
		if (string.contains(thirdYearString)) {
			endIndex = string.indexOf(thirdYearString);
			list.add(string.substring(beginIndex, endIndex));
			beginIndex = endIndex;
			if (!string.contains(fourthYearString)) {
				list.add(string.substring(beginIndex));
			}
		}
		if (string.contains(fourthYearString)) {
			endIndex = string.indexOf(fourthYearString);
			list.add(string.substring(beginIndex, endIndex));
			beginIndex = endIndex;
			if (!string.contains(fifthYearString)) {
				list.add(string.substring(beginIndex));
			}
		}

		// 将list转为数组返回
		String[] strings = this.list2Array(list);
		return strings;
	}
	
	/**
	 * 遍历map
	 * */
	private List<BusinessFinanceData> traversalMap(Map<String, String> map) {
		List<BusinessFinanceData> financeDataList = new ArrayList<BusinessFinanceData>();
		
		
		// 拿到年份数量
		String yearString = map.get("yearCount");
		int yearCount;
		try {
			yearCount = Integer.valueOf(yearString);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			return null;
		}
		
		// 拿到指标数量
		String financeIndexNameString = map.get("financeIndexNameCount");
		int financeIndexNameCount;
		try {
			financeIndexNameCount = Integer.valueOf(financeIndexNameString);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			return null;
		}
		
		// 拿到标题数量
		String reportTitleString = map.get("titleCount");
		int reportTitleCount;
		try {
			reportTitleCount = Integer.valueOf(reportTitleString);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			return null;
		}
		
		// 先遍历标题数量
		for (int k = 1; k <= reportTitleCount; k++) {
			for (int j = 1; j < financeIndexNameCount; j++) {
				if (null == map.get("financeIndexCode" + k + j)) {
					continue;
				}
				for (int i = 1; i <= yearCount; i++) {
					if (null == map.get("financeIndexValue" + k + j + i)) {
						continue;
					}
					BusinessFinanceData data = new BusinessFinanceData();
					data.setEnterpriseId(map.get("enterpriseId"));
					data.setEnterpriseName(map.get("enterpriseName"));
					data.setYear(map.get("year" + k + i));
					data.setFinanceIndexCode(map.get("financeIndexCode" + k + j));
					data.setFinanceIndexName(map.get("financeIndexName" + k + j));
					data.setFinanceIndexValue(map.get("financeIndexValue" + k + j + i));
					data.setAreListed(map.get("areListed"));
					data.setReportTitle(map.get("reportTitle" + k));
					financeDataList.add(data);
				}
			}
		}
		return financeDataList;
	}
	
	/**
	 * 将数组元素存储到map当中
	 * */
	private void saveData(String[] strings, Map<String, String> dataMap) {
		
		if (null == strings) {
			return;
		}
		
		// 表头信息，该数组为年份
		if (strings[0].contains("项") || 
				strings[0].contains("目") || 
				strings[0].contains("指") || 
				strings[0].contains("标")) {
			for (int i = 1; i < strings.length; i++) {
				dataMap.put("year" + titleCount + i, strings[i]);
			}
			// 存储年份数量
			dataMap.put("yearCount", String.valueOf(strings.length - 1));
		} else { // 表内容信息
			indexCount += 1;
			dataMap.put("financeIndexName" + titleCount + indexCount, strings[0]);
			dataMap.put("financeIndexCode" + titleCount + indexCount, this.getMD5SubString(strings[0]));
			// 存储所有指标的组数
			dataMap.put("financeIndexNameCount", String.valueOf(indexCount));
			for (int j = 1; j < strings.length; j++) {
				dataMap.put("financeIndexValue" + titleCount + indexCount + j, strings[j]);
			}
			// 存储每组指标的个数
			dataMap.put("financeIndexValueCount", String.valueOf(strings.length - 1));
		}
		dataMap.put("areListed", "Yes");
		
	}
	
	/**
	 * 将list转化成数组
	 * */
	private String[] list2Array(List<String> list) {
		if (null == list) {
			return null;
		}
		String[] strings = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strings[i] = list.get(i);
		}
		return strings;
	}
	
	/**
	 * 数组去空值操作
	 * */
	private String[] removeArraySpace(String[] strings) {
		int tag = -1;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals("")) {
				tag = i;
				for (int j = i + 1; j < strings.length; j++) {
					if (!strings[j].equals("")) {
						strings[tag] = strings[j];
						strings[j] = "";
						break;
					}
				}
			}
		}
		return strings;
	}
	
	/**
	 * 得到企业id
	 * */ 
	private String getEnterpriseId(String enterpriseName) {
		String bonds = "bonds_" + this.getMD5SubString(enterpriseName);
		return bonds;
	}
	
	/**
	 * 得到md5编码的前10位
	 * */
	private String getMD5SubString(String string) {
		String md5SubString = "";
		char[] md5String = MD5Util.MD5(string).toCharArray();
		for (int i = 0; i < 10; i++) {
			md5SubString += md5String[i];
		}
		return md5SubString;
	}
	
	private void copyErrorFile(){
		String txtFilePath = this.getReportTxtFilePath();
		String txtErrorFilePath = txtFilePath.replace("reporttxt", "reporttxt2dataerror");
		try {
			FileUtils.copyFile(new File(txtFilePath), new File(txtErrorFilePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
