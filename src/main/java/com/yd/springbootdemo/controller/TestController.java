package com.yd.springbootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author： 叶小东
 * @date： 2019/12/4 15:48
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/test")
    public Object test(){
        return "hello word!";
    }
}
