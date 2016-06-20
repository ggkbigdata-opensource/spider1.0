package com.ggk.spider.utils.parser;

import java.io.IOException;
import java.util.List;

public class Test {
	
	

	public static void main(String[] args) {
		String pdfSrcFilePath = "F:\\test\\pdf1";
		String txtDestFilePath = "F:\\test\\txt";
		String failPdfFilePath = "F:\\test\\fail";
		String cmdCopy = "cmd /c copy";
		String cmd = "";
		Runtime runtime = Runtime.getRuntime();
		ConvertExecuter operation = new ConvertExecuter();
		List<String> paths = operation.getFileList(pdfSrcFilePath);
		for (String path : paths) {
			Pdf2Txts txt = new Pdf2Txts(path, txtDestFilePath);
			try {
				txt.convert();
			} catch (Exception e) {
				System.out.println(path + " 转化失败！");
				cmd = cmdCopy + " \"" + path + "\" " + failPdfFilePath;
				try {
					runtime.exec(cmd);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}	

}
