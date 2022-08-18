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

    @PostMapping("/fir/{districtId}/{policeStationId}")
    public ResponseEntity<FIRSearchBean> postFIRWithDate(@PathVariable("districtId") int districtId,
                                                         @PathVariable("policeStationId") int policeStationId,
                                                         @RequestParam(value="dateFrom") String dateFrom,
                                                         @RequestParam(value="dateTo") String dateTo) throws Exception {
        System.out.println("hi");
        return new ResponseEntity<>(firSearchService.searchAPIConsumeDate(districtId,policeStationId, dateFrom, dateTo), HttpStatus.OK);
    }

    @GetMapping("/fir")
    public List<FirDetail> getFIR(@RequestParam(value = "districtId", required = true) int districtId,
                                  @RequestParam(value = "policestationId", required = true) int policestationId,
                                  @RequestParam(value = "year", required = true) String year
                                 ) throws IOException {
        return firSearchService.getFir(districtId,policestationId, year);
    }

    @GetMapping("/firdate")
    public List<FirDetail> getFIR(@RequestParam(value = "districtId", required = true) int districtId,
                                  @RequestParam(value = "policestationId", required = true) int policestationId,
                                  @RequestParam(value = "datefrom", required = true) String datefrom,
                                  @RequestParam(value = "dateto", required = true) String dateto,
                                  @RequestParam(value = "year", defaultValue = "", required = false) String year
    ) throws IOException {
        System.out.println("===========year====="+year);
        return firSearchService.getFir(districtId,policestationId,datefrom, dateto, year);
    }
    @GetMapping("/download")
    public String getFIRDate(@RequestParam(value = "regfirno", required = true) String regfirno ) throws Exception {
        System.out.println("inside========");
        return firSearchService.downloadPDF(regfirno);
    }


}
