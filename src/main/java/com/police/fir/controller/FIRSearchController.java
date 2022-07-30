package com.police.fir.controller;

import com.police.fir.bean.FIRSearchBean;
import com.police.fir.service.FIRSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FIRSearchController {

    private final FIRSearchService firSearchService;

    @Autowired
    public FIRSearchController(FIRSearchService firSearchService) {
        this.firSearchService = firSearchService;
    }



    @GetMapping("/fir/{districtId}/{policeStationId}/{year}")
    public ResponseEntity<FIRSearchBean> getFIR(@PathVariable("districtId") int districtId,@PathVariable("policeStationId") int policeStationId, @PathVariable("year") int year) throws IOException {
        System.out.println("hi");
       return new ResponseEntity<>(firSearchService.searchAPIConsume(districtId,policeStationId, year), HttpStatus.OK);
    }

}
