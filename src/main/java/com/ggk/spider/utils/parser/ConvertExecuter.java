package com.ggk.spider.utils.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.ggk.spider.utils.datastore.DataStore;
import com.ggk.spider.utils.threads.ConsumerThread;
import com.ggk.spider.utils.threads.ProducerThread;

public class ConvertExecuter {
	
	private static String  reportTxtDestFileDir = "D:\\codes\\spider\\reporttxt";
	private static String  txtDestFileDir = "D:\\codes\\spider\\txt";
	private static String  allPdfFileDir = "D:\\codes\\spider\\sourcepdfs";
	
	public void excute(){
		List<String> pathList = this.getFileList(allPdfFileDir);
		LinkedBlockingQueue<String> datas = new LinkedBlockingQueue<String>(pathList);
		DataStore.setAllPdfFilePath(datas);
		// 遍历list，根据内容转换数据
		for (int i = 0; i < 5; i++) {
			System.out.println("producer" + i);
			Thread producer = new Thread(new ProducerThread(txtDestFileDir));
			producer.start();
		}
		
		for (int j = 0; j< 2; j++) {
			System.out.println("consumer" + j);
			Thread consumer = new Thread(new ConsumerThread(reportTxtDestFileDir));
			consumer.start();
		}
	}
	
	/**
	 * 拿到所有的文件全路径
	 * */ 
	private List<String> getFileList(String path) {
		// 存放所有文件名的全路径
		List<String> list = new ArrayList<String>();
		try {
			File file = new File(path);
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				list.add(path + File.separator + filelist[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
