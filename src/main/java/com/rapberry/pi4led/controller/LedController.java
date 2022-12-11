package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;
import com.rapberry.pi4led.threads.ListenThread;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LedController {
    final MessageController messageController = new MessageController();
    GpioController gpioController = GpioFactory.getInstance();

    private ListenThread t = new ListenThread("qwe");

    Integer msg = 13;
    @RequestMapping("/")
    public String greeting() throws InterruptedException {
        t.start();
        messageController.sendMessage(0);
        messageController.sendMessage(msg);
        return Integer.toBinaryString(msg);

    }
}
