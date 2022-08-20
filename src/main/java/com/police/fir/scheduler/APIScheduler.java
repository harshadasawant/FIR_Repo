package com.police.fir.scheduler;

import com.police.fir.entity.District;
import com.police.fir.entity.PoliceStation;
import com.police.fir.repository.PoliceStationRepository;
import com.police.fir.service.DistrictService;
import com.police.fir.service.FIRSearchService;
import com.police.fir.service.PoliceStationService;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    DateTimeFormatter printFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

    Logger logger = LoggerFactory.getLogger(APIScheduler.class);


    //    @Scheduled(cron = "1 * * * * *")
    public void fetchPoliceStationSchedulerJob() {
//        System.out.println("=============policeStation==============="+year);
        List<District> districtList = districtService.getDistrict();
        for (District district : districtList) {
            try {
                policeStationService.savePoliceStationCode(district.getDistrictId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(cron = "* 06 16 * * *") // run on 01:57 AM everyday
    public void fetchFirDetailsJob() throws IOException, ConfigurationException {
        LocalDate date = LocalDate.now().minus(1, ChronoUnit.DAYS);
        int yearTo = date.getYear();
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        int yearFrom = Integer.parseInt(config.getProperty("fir.year").toString());
        System.out.println(yearFrom);
        List<District> districtList = districtService.getDistrict();
        CopyOnWriteArrayList<District> districtArrayList = new CopyOnWriteArrayList(districtList);
        while (yearTo >= yearFrom) {
            for (District district : districtArrayList) {
                try {
                    int districtId = district.getDistrictId();
                    policeStationService.savePoliceStationCode(districtId);
                    Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
                    System.out.println(policeStationCollection);
                    CopyOnWriteArrayList<PoliceStation> policeStationList = new CopyOnWriteArrayList(policeStationCollection);
                    for (PoliceStation policeStation : policeStationList) {
                        int policeStationId = policeStation.getPolicestationId();
                        try {
                            firSearchService.searchAPIConsume(districtId, policeStationId, yearTo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                System.out.println("================API is down==waiting for 10 min================"+LocalDateTime.now().format(printFormatter));
                                policeStationList.add(policeStation);
                                Thread.sleep(60000 * 10);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        System.out.println("================API is down==waiting for 1 min================"+LocalDateTime.now().format(printFormatter));
                        districtArrayList.add(district);
                        Thread.sleep(60000 * 10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            yearTo--;
            System.out.println("year to == "+yearTo);
        }
    }

    @Scheduled(cron = "* 35 09 * * *") // run on 11:54 AM everyday
    public void fetchFirDetailsJobOnDaily() throws ConfigurationException, IOException {
        boolean flag = true;
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        LocalDate dateTo = LocalDate.now().minus(1, ChronoUnit.DAYS);
        logger.info(" Previous date  : "+dateTo);
        System.out.println(dateTo);
        String dateFromDaily = (String) config.getProperty("fir.dateFromDaily");
        logger.info("Config Property Date :"+ dateFromDaily);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateFrom = LocalDate.parse(dateFromDaily, formatter);
        String sDateFrom = dateFrom.format(formatter);
        String sDateTo = dateTo.format(formatter);

        while (flag && (dateFrom.getDayOfMonth() != dateTo.getDayOfMonth())) {
            List<District> districtList = districtService.getDistrict();
            logger.info("District List Size : "+districtList.size());
            CopyOnWriteArrayList<District> districts = new CopyOnWriteArrayList<>(districtList);
            for (District district : districts) {
//                while (dateFrom.getDayOfMonth() != dateTo.getDayOfMonth()) {
                int districtId = district.getDistrictId();
                try {
                    policeStationService.savePoliceStationCode(districtId);
                    Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
                    CopyOnWriteArrayList<PoliceStation> policeStations = new CopyOnWriteArrayList<>(policeStationCollection);
                    for (PoliceStation policeStation : policeStations) {
                        int policeStationId = policeStation.getPolicestationId();
                        try {
                            firSearchService.searchAPIConsumeDate(districtId, policeStationId, sDateFrom, sDateTo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            config.setProperty("fir.dateFromDaily", dateFrom.format(formatter));
                            config.save();
                            try {
                                System.out.println("================API is down==waiting for 10 min================"+LocalDateTime.now().format(printFormatter));
                                policeStations.add(policeStation);
                                Thread.sleep(60000 * 10);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        System.out.println("================API is down==waiting for 10 min================"+LocalDateTime.now().format(printFormatter));
                        districts.add(district);
                        Thread.sleep(60000 * 10);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            flag = false;
            config.setProperty("fir.dateFromDaily", dateTo.format(formatter));
            config.save();
        }

        System.out.println("==================================completed============================="+LocalDateTime.now().format(printFormatter));
    }

    //        @Scheduled(cron = "1 * * * * *")
    public void fetchFirDetailsJobOnDateMonth() {
        List<District> districtList = districtService.getDistrict();
        LocalDateTime dateTo = LocalDateTime.now();
        LocalDateTime dateFrom = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        System.out.println("====================" + dateFrom);
        districtList.forEach(district -> {
            int districtId = district.getDistrictId();
            Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
            policeStationCollection.forEach(policeStation -> {
                int policeStationId = policeStation.getPolicestationId();
                try {
                    DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String sDateFrom = dateFrom.format(newPattern);
                    String sDateTo = dateTo.format(newPattern);
                    firSearchService.getFir(districtId, policeStationId, sDateFrom, sDateTo, "");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("==================================completed============================="+LocalDateTime.now().format(printFormatter));
    }

}
