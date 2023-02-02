package com.rapberry.pi4led.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;


@Controller
public class WebController {
//    final StationController stationController = new StationController();
//    Integer msg = 13;

    @RequestMapping("/")
    public String greeting(Model model) throws InterruptedException {
        ArrayList<StationController> station = new ArrayList<StationController>();
        station.add(new StationController(State.SORTING, Control.FIELD, 3, "Сургутская"));
        model.addAttribute("station", station);

        ArrayList<String> cities = new ArrayList<String>(Arrays.asList("Москва", "Казань", "Нижневартовск", "Воркута"));
        model.addAttribute("cities", cities);

        ArrayList<wagonModel> counter = new ArrayList<wagonModel>();
        for(int i=1; i!=15; i++) {
            counter.add(new wagonModel(i, cities.get((int) (Math.random() * 4))));
        }
        model.addAttribute("counter", counter);
        return "index";
    }

}