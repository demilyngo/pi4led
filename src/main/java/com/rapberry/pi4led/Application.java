package com.rapberry.pi4led;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import com.rapberry.pi4led.controller.MessageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
//        MessageController messageController = new MessageController();
//        GpioController gpioController = GpioFactory.getInstance();
//        //GpioPinDigitalOutput gpioPinDigitalOutput = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "OutputSignal", PinState.LOW);
//        GpioPinDigitalInput gpioPinDigitalInput1 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
//        GpioPinDigitalInput gpioPinDigitalInput2 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
//        GpioPinDigitalInput gpioPinDigitalInput3 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
//        GpioPinDigitalInput gpioPinDigitalInput4 = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
//        gpioPinDigitalInput1.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
//                try {
//                    messageController.SendMessage(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        gpioPinDigitalInput2.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
//                try {
//                    messageController.SendMessage(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        gpioPinDigitalInput3.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
//                try {
//                    messageController.SendMessage(3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        gpioPinDigitalInput4.addListener(new GpioPinListenerDigital() {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
//                try {
//                    messageController.SendMessage(4);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


//        while (true) {
//            gpioPinDigitalOutput.toggle();
//            Thread.sleep(500);
//        }
        //SpringApplication.run(Application.class, args);

    }

}
