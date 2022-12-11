package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


import javax.annotation.PostConstruct;
import java.util.ArrayList;

public class MessageController {

    boolean sending, receiving;
    private static GpioPinDigitalMultipurpose pin;
    private final ArrayList<Boolean> receivedMessage = new ArrayList<Boolean>();

    private final GpioController gpioController = GpioFactory.getInstance();

    public GpioPinListenerDigital listener = new GpioPinListenerDigital() {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
            if(pin.isHigh() && !receiving) {  //
                receiving = true;
                for (int i=0; i!=8; i++) {
                    receivedMessage.add(pin.isHigh());
                    System.out.println("Received: " + receivedMessage.get(i));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                receivedMessage.clear();
                receiving = false;
//                try {
//                    receiveMessage();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    };

    public void receiveMessage() throws InterruptedException {
        receiving = true;
        for (int i=0; i!=8; i++) {
            receivedMessage.add(pin.isHigh());
            System.out.println("Received: " + receivedMessage.get(i));
            Thread.sleep(500);
        }
        receiving = false;
    }

    public void sendMessage(Integer message) throws InterruptedException {
        if (!receiving) {
            removeListener();
            sending = true;
            for (char bit : Integer.toBinaryString(message).toCharArray()) {
                if (bit == '1') {
                    pin.high();
                    System.out.println("Sent: " + bit);
                    Thread.sleep(500);
                    continue;
                }
                pin.low();
                System.out.println("Sent: " + bit);
                Thread.sleep(500);
            }
            pin.high();
            sending = false;
            setListener();
        }
    }

    public void setInput() {
        if (pin == null) {
            pin = gpioController.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, PinMode.DIGITAL_INPUT);
        }
        if (pin.getMode() == PinMode.DIGITAL_OUTPUT) {
            pin.setMode(PinMode.DIGITAL_INPUT);
        }
    }

    public void setOutput() {
        if (pin == null) {
            pin = gpioController.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, PinMode.DIGITAL_OUTPUT);
        }
        if (pin.getMode() == PinMode.DIGITAL_INPUT) {
            pin.setMode(PinMode.DIGITAL_OUTPUT);
        }
    }

    public void setListener() {
        setInput();
        gpioController.addListener(listener, pin);
    }

    public void removeListener() {
        gpioController.removeListener(listener, pin);
        setOutput();
    }

    MessageController() {
        setListener();
    }
}
