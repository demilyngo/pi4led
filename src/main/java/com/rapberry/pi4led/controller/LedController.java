package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;
import com.rapberry.pi4led.threads.ListenThread;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
public class LedController {
    final MessageController messageController = new MessageController();
    Integer msg = 13;

    @RequestMapping("/")
    public String greeting(Model model) throws InterruptedException {
        while(messageController.getControl() == MessageController.Control.FIELD) {
            Thread.sleep(1);
        }
        messageController.sendMessage(0);
        messageController.sendMessage(msg);
        //return Integer.toBinaryString(msg);
        ArrayList<MessageController> data = new ArrayList<MessageController>();
        data.add(messageController);
        model.addAttribute("data", data);
        return "index";
    }

}
