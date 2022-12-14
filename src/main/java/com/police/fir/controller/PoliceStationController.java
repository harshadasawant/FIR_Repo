package com.police.fir.controller;

import com.police.fir.bean.PoliceStationResponseBean;
import com.police.fir.entity.PoliceStation;
import com.police.fir.service.PoliceStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class PoliceStationController {

    private final PoliceStationService poiliceStationService;

    @Autowired
    public PoliceStationController(PoliceStationService poiliceStationService) {
        this.poiliceStationService = poiliceStationService;
    }


    @PostMapping("/getpolicestationid/{districtId}")
    public ResponseEntity<PoliceStationResponseBean> postPoliceStationId(@PathVariable("districtId") int districtId) throws IOException {
        System.out.println("hi");
        return new ResponseEntity<>(poiliceStationService.savePoliceStationCode(districtId), HttpStatus.OK);
    }

    @GetMapping("/getpolicestationid")
    public List<PoliceStation> getPoliceStationId(@RequestParam(value = "districtId", required = true) int districtId) throws IOException {
        System.out.println("hi");
        return poiliceStationService.getPoliceStationCode(districtId);
    }

}
