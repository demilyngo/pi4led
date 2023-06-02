package com.rapberry.pi4led.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class WebController {
    final StationController stationController = new StationController(State.WAITING, Control.SERVER, 3, "Сургутская");

    @GetMapping("/")
    public String greeting(Model model) throws InterruptedException {
        ArrayList<StationController> station = new ArrayList<StationController>();
        station.add(stationController);
        model.addAttribute("station", station);

        ArrayList<String> cities = new ArrayList<String>(Arrays.asList("Москва", "Казань", "Нижневартовск", "Воркута"));
        model.addAttribute("cities", cities);

        ArrayList<wagonModel> wagonList = new ArrayList<wagonModel>();
        for(int i=1; i!=stationController.getTrainCounter()+1; i++) {
            int way = (int) (Math.random() * 4);
            wagonList.add(new wagonModel(i, cities.get(way), way));
        }
        model.addAttribute("wagonList", wagonList);
        return "index";
    }

    private static final String[] MAPS = "Screenshot_1 Screenshot_2 Screenshot_3 Screenshot_4 Screenshot_5 Screenshot_6".split(" ");
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @GetMapping(path = "/words", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter getWords(@RequestParam(value = "order", defaultValue = "0") String order) {
        SseEmitter emitter = new SseEmitter();
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
        if(stationController.getState() != State.SORTING) { //to stop threads from multiplying
            stationController.setState(State.SORTING);
            cachedThreadPool.execute(() -> {
                for (char way : order.toCharArray()) {
                    stationController.setCurrentWay(way);
                    try {
                        eventBuilder.id("1").data(MAPS[way - 1]).build();
                        emitter.send(eventBuilder);
                        stationController.sendMessage(256 + 2 * way); //message to change semaphores
                        stationController.sendMessage(320 + 2 * way); //message to change way
                        stationController.sendMessage(336); //start moving
                        while (StationController.convertReceived(stationController.getReceivedMessage()) != 384 + 2 * way) {
                            Thread.onSpinWait();
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    eventBuilder.id("2").data("Finished sorting").build();
                    emitter.send(eventBuilder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return emitter;
    }


    @ResponseBody
    @GetMapping("/start")
    public SseEmitter startSorting()  {
        SseEmitter emitter = new SseEmitter();
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
        if(stationController.getState() == State.WAITING) {
            stationController.setState(State.COMING);
            cachedThreadPool.execute(() -> {
                try {
                    stationController.sendMessage(334); //moving to position for sorting
                    while (StationController.convertReceived(stationController.getReceivedMessage()) != 398
                            || StationController.convertReceived(stationController.getReceivedMessage()) != 400) {
                        Thread.onSpinWait();
                    }
                    if (StationController.convertReceived(stationController.getReceivedMessage()) == 398) {
                        stationController.setTrainCounter(stationController.getTrainCounter() + 1);
                        eventBuilder.id("1").data(stationController.getTrainCounter()).build();
                        emitter.send(eventBuilder);
                    } else {
                        eventBuilder.id("2").data("Ready to sort").build();
                        emitter.send(eventBuilder);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return emitter;
    }
}
