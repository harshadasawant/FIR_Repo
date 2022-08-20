package com.police.fir.controller;

import com.police.fir.entity.District;
import com.police.fir.service.DistrictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FIRViewController {
    @Autowired
    DistrictService districtService;
    Logger log = LoggerFactory.getLogger(FIRViewController.class);

    @GetMapping(value = "/firsearch")
    public String firSearch(Model model){
        log.trace("helooooooooooooooooo");
        List<District> districtList =  districtService.getDistrict();
        model.addAttribute("districtList", districtList);

        System.out.println("Fir Search");
        return "firsearch";
    }
}
