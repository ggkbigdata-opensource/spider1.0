package com.ggk.spider.service;

import java.util.List;

import com.ggk.spider.model.SourceFiles;

public interface SourceFilesService {
	
	public void batchAddData(List<SourceFiles> datas);
	
	public void addData(SourceFiles data);
}
