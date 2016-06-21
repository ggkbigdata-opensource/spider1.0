package com.ggk.spider.utils.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.util.StringUtils;

public class Pdf2Txts {

	private String pdfSrcFilePath;
	private String txtDestFileDir;

	public Pdf2Txts(String pdfSrcFilePath, String txtDestFilePath) {
		this.setPdfSrcFilePath(pdfSrcFilePath);
		this.setTxtDestFileDir(txtDestFilePath);
	}

	public String getPdfSrcFilePath() {
		return pdfSrcFilePath;
	}

	public void setPdfSrcFilePath(String pdfSrcFilePath) {
		this.pdfSrcFilePath = pdfSrcFilePath;
	}

	public String convert() {

		if (StringUtils.isEmpty(getPdfSrcFilePath()) || StringUtils.isEmpty(getTxtDestFileDir()) || getPdfSrcFilePath().contains("公告")) {
			return null;
		}

		String fileName = this.getPdfSrcFilePath().substring(this.getPdfSrcFilePath().lastIndexOf(File.separator) + 1,
				this.getPdfSrcFilePath().lastIndexOf(".")); // pdf文件名

		String txtPath = this.getTxtDestFileDir() + File.separator + fileName + ".txt"; // 准备输出的txt文件全路径

		String encoding = "UTF-8"; // 编码方式

		int startPage = 1; // 开始提取页数

		int endPage = Integer.MAX_VALUE; // 结束提取页数

		Writer output = null; // 文件输入流，生成文本文件

		PDDocument document = null; // 内存中存储的PDF Document
		
		PDFParser parser = null;
		
//		COSDocument cosDoc = null;

		PDFTextStripper stripper = null; // PDFTextStripper来提取文本
		try {
			File f = new File(this.getPdfSrcFilePath());
			if (!f.isFile()) {
				System.out.println("File " + this.getPdfSrcFilePath() + " does not exist.");
				return null;
			}
			
			try {
				parser = new PDFParser(new FileInputStream(f));
				} catch (Exception e) {
				System.out.println("Unable to open PDF Parser.");
				return null;
			}
			
			parser.parse();
//			cosDoc = parser.getDocument();
//			document = new PDDocument(cosDoc);
			document = PDDocument.load(this.getPdfSrcFilePath());
			// 文件输入流，写入文件倒textFile
			output = new OutputStreamWriter(new FileOutputStream(txtPath), encoding);

			stripper = new PDFTextStripper();
//			String s = stripper.getText(document);
//			output.write(s);
			// 设置是否排序
			stripper.setSortByPosition(false);
			// 设置起始页
			stripper.setStartPage(startPage);
			// 设置结束页
			stripper.setEndPage(endPage);
			// 调用PDFTextStripper的writeText提取并输出文本
			stripper.writeText(document, output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null) {
					// 关闭输出流
					output.close();
				}
				if (document != null) {
					// 关闭PDF Document
					document.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			document = null;
			output = null;
		}
		return txtPath;
	}

	public String getTxtDestFileDir() {
		return txtDestFileDir;
	}

	public void setTxtDestFileDir(String txtDestFileDir) {
		this.txtDestFileDir = txtDestFileDir;
	}
	
//	public static void main(String[] args) {
//		ConvertExecuter executer = new ConvertExecuter();
//		List<String> paths = executer.getFileList("F:\\ggkData\\pdf");
//		int count = 0;
//		String destDir = "F:\\ggkData\\txt";
//		for (String path : paths) {
//			Pdf2Txts pdfOperation = new Pdf2Txts(path, destDir);
//			pdfOperation.convert();
//			System.out.println(++count);
//		}
//	}
}
