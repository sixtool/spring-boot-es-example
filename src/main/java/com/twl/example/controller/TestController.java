package com.twl.example.controller;

import com.twl.example.config.EsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    EsConfig es;

    @RequestMapping("testEs")
    public String testEs() throws Exception {
        es.getEs().createIndex("xxxx333334777dmp1");
        return null;
    }

    @RequestMapping("testSearch")
    public String testSearch() throws Exception {
        return null;
    }
}
