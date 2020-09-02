package com.learn.web_chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class PushController {
    @Resource
    private WebSocket webSocket;

    @RequestMapping("/push")
    public String push(){
        return "push";
    }

    @RequestMapping("/info")
    @ResponseBody
    public String pushToWeb(@RequestParam("info") String message) {
        try {
            webSocket.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            return "success";
        }
        return "error";
    }
}
