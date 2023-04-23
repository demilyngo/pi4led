package com.rapberry.pi4led.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class WebController {

    final StationController stationController = new StationController(State.WAITING, Control.SERVER, 0, "Сургутская");

    @RequestMapping("/")
    public String greeting(Model model) throws InterruptedException {
        //Integer msg = 13;
        ArrayList<StationController> station = new ArrayList<StationController>();
        station.add(stationController);
        model.addAttribute("station", station);

        ArrayList<String> cities = new ArrayList<String>(Arrays.asList("Москва", "Казань", "Нижневартовск", "Воркута"));
        model.addAttribute("cities", cities);

        ArrayList<wagonModel> wagonList = new ArrayList<wagonModel>();
        for(int i=1; i!=stationController.getTrainCounter(); i++) {
            wagonList.add(new wagonModel(i, cities.get((int) (Math.random() * 4))));
        }
        model.addAttribute("wagonList", wagonList);

//        System.out.println("before sending");
//        stationController.sendMessage(msg);
//        System.out.println("after sending");
//        stationController.sendMessage(0);

        return "index";
    }


    @ResponseBody
    @PostMapping("/")
    public String startSorting(@RequestBody String data) {
        Pattern pattern = Pattern.compile("[0-6]");
        String[] text = pattern.split(data);
        Matcher matcher = pattern.matcher(data);
        String res = "";
        while (matcher.find()) {
            res = res + (data.substring(matcher.start(), matcher.end()));
        }
        return res;
    }
    }

}
