package com.ggk.spider.utils.threads;

import com.ggk.spider.utils.datastore.DataStore;
import com.ggk.spider.utils.parser.Txt2ReportTxt;

public class ConsumerThread implements Runnable {
	
    private String txtSrcFilePath;
    private String reportTxtDestFileDir;
 
    public ConsumerThread(String reportTxtDestFileDir){
        this.reportTxtDestFileDir = reportTxtDestFileDir;
    }
 
    @Override
    public void run() {
    	while(true){
    		System.out.println(Thread.currentThread().getName()+" Start. txtSrcFilePath = "+txtSrcFilePath);
    		processCommand();
    		System.out.println(Thread.currentThread().getName()+" End.");
    	}
    }
 
    private void processCommand() {
    	try {
    		this.txtSrcFilePath = DataStore.getDatas().take();
			Txt2ReportTxt txt2ReportTxt = new Txt2ReportTxt(txtSrcFilePath, reportTxtDestFileDir);
			txt2ReportTxt.convert();
		} catch (Exception e) {
			System.out.println(this.toString() + " due to " + e.getMessage());
		}
    }
 
    @Override
    public String toString(){
        return this.txtSrcFilePath;
    }
}
