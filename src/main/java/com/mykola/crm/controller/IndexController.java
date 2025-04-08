package com.mykola.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {


    //localStorage
    @GetMapping("/index")
    public String index() {
        return "forward:/index.html"; //тільки так заканало. index або index.html не канає
    }

    //Cookie
    @GetMapping("/index2")
    public String index2() {
        return "forward:/index2.html";
    }

}