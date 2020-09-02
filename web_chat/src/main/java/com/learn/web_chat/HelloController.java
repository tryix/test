package com.learn.web_chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping("/chat")
    public String webSocket(){

            return "hello";

    }

}
