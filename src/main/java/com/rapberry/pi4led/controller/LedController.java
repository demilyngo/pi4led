package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;
import com.rapberry.pi4led.threads.ListenThread;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LedController {
    final MessageController messageController = new MessageController();
    Integer msg = 13;

    @RequestMapping("/")
    public String greeting() throws InterruptedException {

        messageController.sendMessage(0);
        messageController.sendMessage(msg);
        return "index";
    }

}
