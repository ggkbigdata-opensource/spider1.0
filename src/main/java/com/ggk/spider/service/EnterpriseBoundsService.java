package com.ggk.spider.service;

import java.util.List;

import com.ggk.spider.model.EnterpriseBounds;

public interface EnterpriseBoundsService {
	
	public void batchAddData(List<EnterpriseBounds> datas);
	
	public void addData(EnterpriseBounds data);

}
