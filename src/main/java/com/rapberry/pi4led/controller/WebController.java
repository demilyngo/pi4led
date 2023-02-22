package com.rapberry.pi4led.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;


@Controller
public class WebController {


    @RequestMapping("/")
    public String greeting(Model model) throws InterruptedException {
        final StationController stationController = new StationController(State.SORTING, Control.FIELD, 3, "Сургутская");
        Integer msg = 13;
        System.out.println("get");
        ArrayList<StationController> station = new ArrayList<StationController>();
        station.add(stationController);
        model.addAttribute("station", station);

        ArrayList<String> cities = new ArrayList<String>(Arrays.asList("Москва", "Казань", "Нижневартовск", "Воркута"));
        model.addAttribute("cities", cities);

        ArrayList<wagonModel> counter = new ArrayList<wagonModel>();
        for(int i=1; i!=15; i++) {
            counter.add(new wagonModel(i, cities.get((int) (Math.random() * 4))));
        }
        model.addAttribute("counter", counter);

        System.out.println("before sending");
        stationController.sendMessage(msg);
        System.out.println("after sending");
        stationController.sendMessage(0);

        return "index";
    }

}
