package com.rapberry.pi4led.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Console;
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

    private static final String[] WORDS = "Screenshot_1 Screenshot_2 Screenshot_3 Screenshot_4 Screenshot_5 Screenshot_6".split(" ");
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @GetMapping(path = "/words", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter getWords(@RequestParam(value = "order", defaultValue = "0") String order) {
        SseEmitter emitter = new SseEmitter();
        cachedThreadPool.execute(() -> {
            System.out.println("This is SSE " + Thread.currentThread().getId());
            try {
                emitter.send(order);
                int i = (int) (Math.random() * 5);
                stationController.setTrainCounter(stationController.getTrainCounter()+1);
                emitter.send(WORDS[i]);
                TimeUnit.SECONDS.sleep(1);
                emitter.complete();emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }


    @ResponseBody
    @PostMapping("/")
    public String startSorting(@RequestBody String data)  {
        cachedThreadPool.execute(() -> {
            System.out.println("Post mapping " + Thread.currentThread().getId());
            Pattern pattern = Pattern.compile("[0-6]");
            Matcher matcher = pattern.matcher(data);
            String res = "";
            while (matcher.find()) {
                res = res + (data.substring(matcher.start(), matcher.end()));
            }
//            for (char way : res.toCharArray()) {
//                stationController.setCurrentWay(way);
//                try {
//                    stationController.sendMessage(256+2*way);
//                    stationController.sendMessage(320+2*way); //message to change way
//                    while(StationController.convertReceived(stationController.getReceivedMessage()) < 386) {
//                        Thread.onSpinWait();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        });

        return "index";
    }


}
