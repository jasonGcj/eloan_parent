package com.eloan.mgrtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
      @RequestMapping("/login.html")
      public String page() {
    	  return "login";
      }
}
