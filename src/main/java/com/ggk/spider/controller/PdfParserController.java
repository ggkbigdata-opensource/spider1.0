package com.ggk.spider.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class PdfParserController {
	private static final Logger logger = Logger.getLogger(PdfParserController.class);
	@Autowired

	@RequestMapping(value = { "/spider/pdfparser/trigger" }, method = RequestMethod.POST)
	public ResponseEntity<?> trigger() {
		logger.info("come in");
		return ResponseEntity.ok("");
	}
}
