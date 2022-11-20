//package com.rapberry.pi4led.controller;
//
//import com.pi4j.io.gpio.*;
//import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
//import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//
//public class MessageController {
//
//    private final GpioController gpioController = GpioFactory.getInstance();
//    private final GpioPinDigitalOutput gpioPinDigitalOutput = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "OutputSignal", PinState.LOW);
//    private final GpioPinDigitalInput gpioPinDigitalInput = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_10);
//    private boolean isMessageReceived = false;
//
//    public void SendMessage(Integer inputPinNumber) throws InterruptedException {
//        gpioPinDigitalInput.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                gpioPinDigitalOutput.high();
//                gpioPinDigitalOutput.toggle();
//                long waitFor = System.currentTimeMillis() + 5000;
//                while (System.currentTimeMillis() < waitFor) {
//                    if (event.getState().toString().equalsIgnoreCase("HIGH")) {
//                        isMessageReceived = true;
//                        break;
//                    }
//                }
//                System.out.println(isMessageReceived?"Can send message.":"Time out.");
//                isMessageReceived = false;
//            }
//        });
//
//        String bits = Integer.toString(inputPinNumber, 2);
//        for (char bit : bits.toCharArray()) {
//            if (bit == '1') {
//                gpioPinDigitalOutput.high();
//                gpioPinDigitalOutput.toggle();
//                Thread.sleep(5000);
//                continue;
//            }
//            gpioPinDigitalOutput.low();
//            gpioPinDigitalOutput.toggle();
//            Thread.sleep(5000);
//        }
//
//        gpioPinDigitalInput.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//                long waitFor = System.currentTimeMillis() + 5000;
//                while (System.currentTimeMillis() < waitFor) {
//                    if (event.getState().toString().equalsIgnoreCase("HIGH")) {
//                        isMessageReceived = true;
//                        break;
//                    }
//                }
//                System.out.println(isMessageReceived?"Can send message.":"Time out.");
//                isMessageReceived = false;
//            }
//        });
//    }
//}
