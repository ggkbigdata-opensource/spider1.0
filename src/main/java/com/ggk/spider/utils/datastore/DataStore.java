package com.ggk.spider.utils.datastore;

import java.util.concurrent.LinkedBlockingQueue;

public class DataStore {
	private static LinkedBlockingQueue<String> datas = new LinkedBlockingQueue<String>(500);
	private static LinkedBlockingQueue<String> allPdfFilePath = new LinkedBlockingQueue<String>(500);
	
	public static LinkedBlockingQueue<String> getDatas() {
		return datas;
	}

	public static void setDatas(LinkedBlockingQueue<String> datas) {
		DataStore.datas = datas;
	}
	
	public static LinkedBlockingQueue<String> getAllPdfFilePath() {
		return allPdfFilePath;
	}

	public static void setAllPdfFilePath(LinkedBlockingQueue<String> allPdfFilePath) {
		DataStore.allPdfFilePath = allPdfFilePath;
	}

}
