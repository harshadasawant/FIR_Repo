package com.police.fir.controller;

import com.police.fir.bean.FIRSearchBean;
import com.police.fir.entity.FirDetail;
import com.police.fir.service.FIRSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class FIRSearchController {

    private final FIRSearchService firSearchService;

    @Autowired
    public FIRSearchController(FIRSearchService firSearchService) {
        this.firSearchService = firSearchService;
    }



    @PostMapping("/fir/{districtId}/{policeStationId}/{year}")
    public ResponseEntity<FIRSearchBean> postFIR(@PathVariable("districtId") int districtId,@PathVariable("policeStationId") int policeStationId, @PathVariable("year") int year) throws Exception {
        System.out.println("hi");
       return new ResponseEntity<>(firSearchService.searchAPIConsume(districtId,policeStationId, year), HttpStatus.OK);
    }

    @GetMapping("/fir")
    public List<FirDetail> getFIR(@RequestParam(value = "districtId", required = true) int districtId,
                                  @RequestParam(value = "policestationId", required = true) int policestationId,
                                  @RequestParam(value = "year", required = true) int year
                                 ) throws IOException {
        return firSearchService.getFir(districtId,policestationId, year);
    }
    @GetMapping("/firdate")
    public List<FirDetail> getFIRDate(@RequestParam(value = "districtId", required = true) int districtId,
                                  @RequestParam(value = "policestationId", required = true) int policestationId,
                                  @RequestParam(value = "datefrom", required = true) String dateFrom,
                                  @RequestParam(value = "dateto", required = true) String dateTo
    ) throws IOException {
        return firSearchService.getFir(districtId,policestationId, dateFrom, dateTo);
    }

}
