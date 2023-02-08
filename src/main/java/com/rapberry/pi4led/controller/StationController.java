package com.rapberry.pi4led.controller;

import com.pi4j.io.gpio.*;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.BitSet;

@Getter
enum State{
    WAITING("Ожидание"),
    SORTING("Сортировка"),
    LEAVING("Отбытие");

    private final String displayValue;
    State(String state) {
        this.displayValue = state;
    }
}
@Getter
enum Control {
    FIELD("Управление по месту"),
    SERVER("Управление с АРМ");

    private final String displayValue;
    Control(String control) {
        this.displayValue = control;
    }
}

@Getter
@Setter
@ToString
public class StationController {
    private State state;
    private Control control;

    private int trainCounter = 0;
    private String nameOfStation;

    boolean sending, receiving;
    private static GpioPinDigitalMultipurpose pin;
    private final BitSet receivedMessage = new BitSet(8);

    private final GpioController gpioController = GpioFactory.getInstance();

//    public GpioPinListenerDigital listener = new GpioPinListenerDigital() {
//        @Override
//        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
//            if(pin.isHigh() && !receiving) {
//                try {
//                    receiveMessage();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

//    public void receiveMessage() throws InterruptedException {
//        receivedMessage.clear();
//        receiving = true;
//        for (int i=0; i!=8; i++) {
//            if (pin.isHigh()) {
//                receivedMessage.set(i);
//            } else {
//                receivedMessage.clear(i);
//            }
//            System.out.println("Received: " + receivedMessage.get(i));
//            Thread.sleep(500);
//        }
//        receiving = false;
        ///////////////////////
//        if(receivedMessage.get(0)) {
//            if(!receivedMessage.get(1)) {
//                //sensors
//                /*if( xx && state == State.WAITING) {
//                    trainCounter++;
//                }*/
//
//                /*if( xx && state == State.SORTING) {
//                    sendMessage(); // NA OTSCEP
//                    trainCounter--;
//                    if(trainCounter == 0) {
//                        state = State.WAITING;
//                    }
//                }*/
//                //Start sorting if button pressed
//                /*if() {
//                    state = State.SORTING;
//                }*/
//            }
//            if(receivedMessage.get(1)) { //stand buttons
//                switch (convertReceived(receivedMessage)) {
//                    case 1:
//                        sendMessage(1); //semaphore way 1
//                        sendMessage(17); //rails way 1
//                        break;
//                    case 2:
//                        sendMessage(3); //semaphore way 2
//                        sendMessage(19); //rails way 2
//                        break;
//                    case 3:
//                        sendMessage(5); //semaphore way 3
//                        sendMessage(21); //rails way 3
//                        break;
//                    case 4:
//                        sendMessage(7); //semaphore way 4
//                        sendMessage(23); //rails way 4
//                        break;
//                    case 5:
//                        sendMessage(9); //semaphore way 5
//                        sendMessage(25); //rails way 5
//                        break;
//                    case 6:
//                        sendMessage(11); //semaphore way 6
//                        sendMessage(27); //rails way 6
//                        break;
//                }
//            }
//        }
        //receivedMessage.clear();


    //}
///////////////////////////////////////
    public void sendMessage(Integer message) throws InterruptedException {
        if (!receiving) {
            //removeListener();
            sending = true;

            for(int i=0; i != Integer.toBinaryString(message).toCharArray().length - 8; i++) {
                pin.low();
                Thread.sleep(500);
            }

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
            //setListener();
        }
    }

    public static Integer convertReceived(BitSet bits) {
        int value = 0;
        for (int i = 2; i != bits.length(); i++) {
            value += bits.get(i) ? (1 << i) : 0;
        }
        return value;
    }
//
//
//    public void setInput() {
//        if (pin == null) {
//            pin = gpioController.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, PinMode.DIGITAL_INPUT);
//        }
//        if (pin.getMode() == PinMode.DIGITAL_OUTPUT) {
//            pin.setMode(PinMode.DIGITAL_INPUT);
//        }
//    }
//
    public void setOutput() {
        if (pin == null) {
            pin = gpioController.provisionDigitalMultipurposePin(RaspiPin.GPIO_01, PinMode.DIGITAL_OUTPUT);
        }
        if (pin.getMode() == PinMode.DIGITAL_INPUT) {
            pin.setMode(PinMode.DIGITAL_OUTPUT);
        }
    }
//
//    public void setListener() {
//        setInput();
//        gpioController.addListener(listener, pin);
//    }
//
//    public void removeListener() {
//        gpioController.removeListener(listener, pin);
//        setOutput();
//    }
//
//    StationController() {
//        setListener();
//        state = State.WAITING;
//        control = Control.SERVER;
//    }

    StationController(State state, Control control, int trainCounter, String name) {
        setOutput();
        this.state = state;
        this.control = control;
        this.trainCounter = trainCounter;
        this.nameOfStation = name;
    }
}
