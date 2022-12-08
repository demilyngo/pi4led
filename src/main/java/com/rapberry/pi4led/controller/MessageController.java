package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;

import com.rapberry.pi4led.threads.ListenThread;

import java.util.ArrayList;

public class MessageController {
    private final GpioController gpioController = GpioFactory.getInstance();
    interface MyPin extends GpioPinDigitalOutput, GpioPinDigitalInput {}
    private MyPin pin = (MyPin) gpioController.provisionPin(RaspiPin.GPIO_01, PinMode.DIGITAL_OUTPUT);
    private ArrayList<Boolean> receivedMessage = new ArrayList<Boolean>();

    private ListenThread t = new ListenThread("qwe");

    public void receiveMessage() throws InterruptedException {
        pin.setMode(PinMode.DIGITAL_INPUT);
    }


    public void sendMessage(Integer message) throws InterruptedException {
        pin.setMode(PinMode.DIGITAL_OUTPUT);
        for (char bit : Integer.toBinaryString(message).toCharArray()) {
            if (bit == '1') {
                pin.high();
                Thread.sleep(500);
                continue;
            }
            pin.low();
            Thread.sleep(500);
        }
        pin.high();
    }
}
