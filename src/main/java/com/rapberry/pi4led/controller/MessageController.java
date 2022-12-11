package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;

import com.rapberry.pi4led.threads.ListenThread;

import java.util.ArrayList;

public class MessageController {

    private static GpioPinDigitalInput inputPin;
    private static GpioPinDigitalOutput outputPin;

    private ArrayList<Boolean> receivedMessage = new ArrayList<Boolean>();

    public void receiveMessage() throws InterruptedException {
        if(outputPin == null) {
            GpioController gpioController = GpioFactory.getInstance();
            inputPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01, "Input Pin");
        }
    }


    public void sendMessage(Integer message) throws InterruptedException {

        if(outputPin == null) {
            GpioController gpioController = GpioFactory.getInstance();
            outputPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "Output Pin", PinState.HIGH);
        }

        for (char bit : Integer.toBinaryString(message).toCharArray()) {
            if (bit == '1') {
                outputPin.high();
                System.out.println(bit);
                Thread.sleep(500);
                continue;
            }
            outputPin.low();
            System.out.println(bit);
            Thread.sleep(500);
        }
        outputPin.high();
    }
}
