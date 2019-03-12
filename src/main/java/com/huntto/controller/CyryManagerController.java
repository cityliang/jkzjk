package com.huntto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CyryManagerController {
	private Logger log = LoggerFactory.getLogger(CyryManagerController.class);
	
	@GetMapping(value = "/testLog")
    public String testLog() {
        log.info("---------------------------");
        log.debug("debug debug");
        log.info("info info info");
        log.warn("warn warn warn");
        log.error("error error error ");
        log.info("---------------------------");
        return "ok";
    }
	
	public String register() {

		return "";
	}
}
