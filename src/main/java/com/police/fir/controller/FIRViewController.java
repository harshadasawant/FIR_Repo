package com.police.fir.controller;

import com.police.fir.entity.District;
import com.police.fir.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FIRViewController {
    @Autowired
    DistrictService districtService;


    @GetMapping(value = "/firsearch")
    public String firSearch(Model model){

        List<District> districtList =  districtService.getDistrict();
        model.addAttribute("districtList", districtList);

        System.out.println("Fir Search");
        return "firsearch";
    }
}
