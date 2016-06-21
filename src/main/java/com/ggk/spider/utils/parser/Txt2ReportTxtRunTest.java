package com.ggk.spider.utils.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Txt2ReportTxtRunTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String src = "D:\\codes\\spider\\txt";
		String dist = "D:\\codes\\spider\\reporttxt";
		String errordist = "D:\\codes\\spider\\txt2reporttxtError";
		ConvertExecuter executer = new ConvertExecuter();
		List<String> list = executer.getFileList(src);
		String result = "";
		String fileName = "";
		Txt2ReportTxt txt = null;
		for (String string : list) {
			txt = new Txt2ReportTxt(string, dist);
			result = txt.convert();
			if ("error".equals(result)) {
				try {
					fileName = string.substring(string.lastIndexOf("\\") + 1, string.length());
					FileUtils.copyFile(new File(string), new File(errordist + File.separator + fileName));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
