package com.police.fir.scheduler;

import com.police.fir.entity.District;
import com.police.fir.entity.PoliceStation;
import com.police.fir.repository.PoliceStationRepository;
import com.police.fir.service.DistrictService;
import com.police.fir.service.FIRSearchService;
import com.police.fir.service.PoliceStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Component
public class APIScheduler {

    @Autowired
    DistrictService districtService;

    @Autowired
    PoliceStationService policeStationService;

    @Autowired
    FIRSearchService firSearchService;

    @Autowired
    PoliceStationRepository policeStationRepository;

    @Value("${fir.year}")
    int year;

    @Value("${fir.dateFrom}")
    String dateFrom;

    @Value("${fir.dateTo}")
    String dateTo;

//    @Scheduled(cron = "1 * * * * *")
    public void fetchPoliceStationSchedulerJob() {
//        System.out.println("=============policeStation==============="+year);
        List<District> districtList =  districtService.getDistrict();
        for(District district : districtList){
            try {
                policeStationService.savePoliceStationCode(district.getDistrictId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    @Scheduled(cron = "1 * * * * *")
    public void fetchFirDetailsJob() {
        System.out.println("===============Fir Details============="+year);
       int year = 2000;
       while(year <= LocalDate.now().getYear()) {
           List<District> districtList = districtService.getDistrict();
           districtList.forEach(district -> {
               int districtId = district.getDistrictId();
               Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
               policeStationCollection.forEach(policeStation -> {
                   int policeStationId = policeStation.getPolicestationId();
                   try {
                       firSearchService.searchAPIConsume(districtId, policeStationId, year);
                   } catch (Exception e) {
                       try {
                           Thread.sleep(1000*10);
                       } catch (InterruptedException ex) {
                           throw new RuntimeException(ex);
                       }
                   }

               });
           });
       }
    }

    @Scheduled(cron = "1 *  * * *")
    public void fetchFirDetailsJobOnDaily() {
        boolean flag = true;
        while(flag) {
            System.out.println("===============Fir Details i am inside=============" + dateFrom);
            List<District> districtList = districtService.getDistrict();
            LocalDate dateTo = LocalDate.now();
            LocalDate dateFrom = LocalDate.now().minus(1, ChronoUnit.MONTHS);
            System.out.println("====================" + dateFrom);
            districtList.forEach(district -> {
                int districtId = district.getDistrictId();
                Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
                policeStationCollection.forEach(policeStation -> {
                    int policeStationId = policeStation.getPolicestationId();
                    try {
                        DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd/mm/yyyy");
                        String sDateFrom = dateFrom.format(newPattern);
                        String sDateTo = dateTo.format(newPattern);
                        firSearchService.searchAPIConsumeDate(districtId, policeStationId, sDateFrom, sDateTo);
                    } catch (Exception e) {
                        try {
                            Thread.sleep(60000*10);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            });
            flag=false;
        }
        System.out.println("==================================completed=============================");
    }

//    @Scheduled(cron = "* * * 31 * *")
    public void fetchFirDetailsJobOnDateMonth() {
        System.out.println("===============Fir Details============="+dateFrom);
        List<District> districtList =  districtService.getDistrict();
        LocalDate dateTo = LocalDate.now();
        LocalDate dateFrom = LocalDate.now().minus(1, ChronoUnit.MONTHS);
        System.out.println("===================="+dateFrom);
        districtList.forEach(district -> {
            int districtId = district.getDistrictId();
            Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
            policeStationCollection.forEach(policeStation -> {
                int policeStationId = policeStation.getPolicestationId();
                try {
                    DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd/mm/yyyy");
                    String sDateFrom = dateFrom.format(newPattern);
                    String sDateTo = dateTo.format(newPattern);
                    firSearchService.getFir(districtId, policeStationId, sDateFrom, sDateTo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("==================================completed=============================");
    }

}
