package com.police.fir.controller;

import com.police.fir.bean.PoliceStationResponseBean;
import com.police.fir.service.PoliceStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PoliceStationController {

    private final PoliceStationService districtService;

    @Autowired
    public PoliceStationController(PoliceStationService districtService) {
        this.districtService = districtService;
    }


    @GetMapping("/getpolicestationid/{districtId}")
    public ResponseEntity<PoliceStationResponseBean> getPoliceStationId(@PathVariable("districtId") int districtId) throws IOException {
        System.out.println("hi");
        return new ResponseEntity<>(districtService.searchPoliceStationCodeConsumer(districtId), HttpStatus.OK);
    }
}
