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
    private static final int startBitLength = 1;
    private static final int stopBitLength = 1;
    private static final int controllerLength = 2;
    private static final int taskLength = 4;

    private Integer checkController1 = 256;
    private Integer checkController2 = 320;
    private Integer checkController3 = 384;
    private Integer checkController4 = 448;
    private Integer checkControllerMessage;

    private State state;
    private Control control;

    private int trainCounter;
    private String nameOfStation;

    boolean sending, receiving;
    private static GpioPinDigitalMultipurpose pin;
    private final BitSet receivedMessage = new BitSet(8);

    private final GpioController gpioController = GpioFactory.getInstance();

    public GpioPinListenerDigital listener = new GpioPinListenerDigital() {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
            if (!receiving && !sending) {
                try {
                    checkControllerMessage = checkController1;
                    sendMessage(checkControllerMessage);
                    receiveMessage();
                    checkControllerMessage = checkController2;
                    sendMessage(checkControllerMessage);
                    receiveMessage();
                    checkControllerMessage = checkController3;
                    sendMessage(checkControllerMessage);
                    receiveMessage();
                    if(getControl() == Control.FIELD) {
                        checkControllerMessage = checkController4;
                        sendMessage(checkControllerMessage);
                        receiveMessage();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void receiveMessage() throws InterruptedException {
        receivedMessage.clear();
        receiving = true;
        for (int i=0; i!=startBitLength+startBitLength+controllerLength+taskLength; i++) {
            if (pin.isHigh()) {
                receivedMessage.set(i);
            } else {
                receivedMessage.clear(i);
            }
            System.out.println("Received: " + receivedMessage.get(i));
            Thread.sleep(500);
        }


        if(receivedMessage.previousSetBit(startBitLength+startBitLength+controllerLength+taskLength) == 0 ) {
            receiving = false;
            return;
        }
//        if(receivedMessage.isEmpty()) { //Indicator
//
//        }

        if(receivedMessage.get(0)) {
            if(convertReceived(receivedMessage) == 0) {
                //sensors
                /*if( xx && state == State.WAITING) {
                    trainCounter++;
                }*/

                /*if( xx && state == State.SORTING) {
                    sendMessage(); // NA OTSCEP
                    trainCounter--;
                    if(trainCounter == 0) {
                        state = State.WAITING;
                    }
                }*/
                //Start sorting if button pressed
                /*if() {
                    state = State.SORTING;
                }*/
            }
            if(getControl()==Control.FIELD) {
                if (convertReceived(receivedMessage) > 448) { //stand buttons
                    switch (convertReceived(receivedMessage)) {
                        case 450 -> {
                            sendMessage(258); //semaphore way 1
                            sendMessage(322); //rails way 1
                        }
                        case 452 -> {
                            sendMessage(260); //semaphore way 2
                            sendMessage(324); //rails way 2
                        }
                        case 454 -> {
                            sendMessage(262); //semaphore way 3
                            sendMessage(326); //rails way 3
                        }
                        case 456 -> {
                            sendMessage(264); //semaphore way 4
                            sendMessage(328); //rails way 4
                        }
                        case 458 -> {
                            sendMessage(266); //semaphore way 5
                            sendMessage(330); //rails way 5
                        }
                        case 460 -> {
                            sendMessage(268); //semaphore way 6
                            sendMessage(332); //rails way 6
                        }
                        case 462 -> {
                            sendMessage(270);
                        }
                    }
                }
            }
        }
//        receivedMessage.clear();
        receiving = false;

    }
///////////////////////////////////////
    public void sendMessage(Integer message) throws InterruptedException {
        if (!receiving) {
            //removeListener();
            setOutput();
            sending = true;
//            for(int i=0; i != Integer.toBinaryString(message).toCharArray().length - 8; i++) {
//                pin.low();
//                Thread.sleep(500);
//            }
            pin.low();
            Thread.sleep(1000);
            for (char bit : Integer.toBinaryString(message).toCharArray()) {
                if (bit == '1') {
                    pin.high();
                    System.out.println("Sent: " + bit);
                    Thread.sleep(1000);
                    continue;
                }
                pin.low();
                System.out.println("Sent: " + bit);
                Thread.sleep(1000);
            }
            pin.high();
            sending = false;
            setInput();
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
        System.out.println("set output");
    }

    public void setListener() {
        setInput();
        gpioController.addListener(listener, pin);
    }

    public void removeListener() {
        gpioController.removeListener(listener, pin);
        setOutput();
    }


    StationController(State state, Control control, int trainCounter, String name) {
        setOutput();
        this.state = state;
        this.control = control;
        this.trainCounter = trainCounter;
        this.nameOfStation = name;
        System.out.println("Construcrot station");
    }
}
