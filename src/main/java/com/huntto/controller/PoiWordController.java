package com.huntto.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_VALUE)
public class PoiWordController {
	@RequestMapping(value = "/word",method = RequestMethod.GET)
    public String helloWord(@RequestParam(value = "name") String string){
        return string;
    }
}
