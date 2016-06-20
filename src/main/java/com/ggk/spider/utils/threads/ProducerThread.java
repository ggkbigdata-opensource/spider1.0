package com.ggk.spider.utils.threads;

import com.ggk.spider.utils.datastore.DataStore;
import com.ggk.spider.utils.parser.Pdf2Txts;

public class ProducerThread implements Runnable {

	private String pdfSrcFilePath;
	private String txtDestFileDir;

	public ProducerThread(String txtDestFileDir) {
		this.txtDestFileDir = txtDestFileDir;
	}

	@Override
	public void run() {
		while (true) {
			processCommand();
		}
	}

	private void processCommand() {
		try {
		this.pdfSrcFilePath = DataStore.getAllPdfFilePath().take();
		Pdf2Txts txt2ReportTxt = new Pdf2Txts(pdfSrcFilePath, txtDestFileDir);
		String txtPath = txt2ReportTxt.convert();
		if(null == txtPath){
			return;
		}
		DataStore.getDatas().put(txtPath);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return this.pdfSrcFilePath;
	}
}
