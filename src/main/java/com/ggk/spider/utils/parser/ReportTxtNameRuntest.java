package com.ggk.spider.utils.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ReportTxtNameRuntest {

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
