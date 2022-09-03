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
import org.springframework.beans.factory.annotation.Value;
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
//    @Value("${cron.time}")
//    final String time;
//
//    public APIScheduler(String time) {
//        this.time = time;
//    }

//    public APIScheduler() {
//        time = "* 16 20 * * * ";
//    }


//    PropertiesConfiguration config= new PropertiesConfiguration("config.properties");
//    String time1 = config.getProperty("config.fir.time").toString();
//
//     final String  var1= config.getProperty("config.fir.time").toString();
//
//    public APIScheduler() throws ConfigurationException {
//         config = new PropertiesConfiguration("config.properties");
//        String time1 = config.getProperty("config.fir.time").toString();
//
//        String[] arr = time1.split(":");
//        int hh = Integer.parseInt(arr[0]);
//        int mm = Integer.parseInt(arr[1]);

//    }


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

    @Scheduled(cron = "* 32 6 * * *") // run on 01:57 AM everyday
    public void fetchFirDetailsJob() throws IOException, ConfigurationException {
//        System.out.println("time = " + time);
        LocalDate date = LocalDate.now().minus(1, ChronoUnit.DAYS);
        int yearTo = date.getYear();
        PropertiesConfiguration config= new PropertiesConfiguration("config.properties");
        int yearFrom = Integer.parseInt(config.getProperty("fir.year").toString());
        System.out.println(yearFrom);
        List<District> districtList = districtService.getDistrict();
        CopyOnWriteArrayList<District> districtArrayList = new CopyOnWriteArrayList(districtList);
        while (yearTo >= yearFrom) {
            for(int i=0;i<districtArrayList.size();i++){
                logger.info("i = " + i +"  "+districtArrayList.size());
                District district = districtArrayList.get(i);
                try {
                    int districtId = district.getDistrictId();
                    policeStationService.savePoliceStationCode(districtId);
                    Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
                    System.out.println(policeStationCollection);
                    CopyOnWriteArrayList<PoliceStation> policeStationList = new CopyOnWriteArrayList(policeStationCollection);
                    for(int j=0;j<policeStationList.size();j++){
                        logger.info("j = " + j+" "+policeStationList.size());
                        PoliceStation policeStation = policeStationList.get(j);
                        int policeStationId = policeStation.getPolicestationId();
                        try {
                            firSearchService.searchAPIConsume(districtId, policeStationId, yearTo);
                            policeStationList.forEach(policeStation1 -> {
                                logger.info("policestation = "+policeStation1.getPolicestationId()+" District Id = "+policeStation1.getDistrictId());
                            });
                        } catch (Exception e) {
                           logger.error(e.getMessage(), e);
                            try {
                                logger.info("================API is down==waiting for 10 min================"+LocalDateTime.now().format(printFormatter));
                                policeStationList.add(policeStation);
                                policeStationList.forEach(policeStation1 -> {
                                    logger.info("policestation = "+policeStation1.getPolicestationId()+" District Id = "+policeStation1.getDistrictId());
                                });
                                Thread.sleep(60000 * 5);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    try {
                        logger.info("================API is down==waiting for 5 min================"+LocalDateTime.now().format(printFormatter));
                        districtArrayList.add(district);
                        districtArrayList.forEach(district1 -> {
                            logger.info("DistrictId = "+district1.getDistrictId()+" policestation Id = "+district1.getDistrictName());
                        });
                        Thread.sleep(60000 * 5);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            yearTo--;
            System.out.println("year to == "+yearTo);
        }
    }

    //@Scheduled(cron = "* 31 18 * * *") // run on 11:54 AM everyday
    public void fetchFirDetailsJobOnDaily() throws ConfigurationException, IOException {
        boolean flag = true;
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        LocalDate dateTo = LocalDate.now();
        logger.info(" Today's date  : "+dateTo);
        System.out.println(dateTo);
        String dateFromDaily = (String) config.getProperty("fir.dateFromDaily");
        logger.info("Config Property Date :"+ dateFromDaily);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateFrom = LocalDate.parse(dateFromDaily, formatter);
        String sDateFrom = dateFrom.format(formatter);
        String sDateTo = dateTo.format(formatter);
        int districtCount = 0;
        while (flag && (dateFrom.getDayOfMonth() != dateTo.getDayOfMonth())) {
            List<District> districtList = districtService.getDistrict();
            logger.info("District List Size : "+districtList.size());
            CopyOnWriteArrayList<District> districts = new CopyOnWriteArrayList<>(districtList);
//            for (District district : districts) {
                for(int i=0;i<districts.size();i++){
                    logger.info("i = " + i +"  "+districts.size());
                    District district = districts.get(i);
                int districtId = district.getDistrictId();
                try {
                    policeStationService.savePoliceStationCode(districtId);
                    Collection<PoliceStation> policeStationCollection = policeStationRepository.findAllByDistrictId(districtId);
                    CopyOnWriteArrayList<PoliceStation> policeStations = new CopyOnWriteArrayList<>(policeStationCollection);
//                    for (PoliceStation policeStation : policeStations) {
                        for(int j=0;j<policeStations.size();j++){
                            logger.info("j = " + j+" "+policeStations.size());
                            PoliceStation policeStation = policeStations.get(j);
                        int policeStationId = policeStation.getPolicestationId();
                        try {
                            logger.info(" districtId : "+districtId + " policeStationId : " + policeStationId+ " sDateFrom : " +  sDateFrom+ " sDateTo : " +  sDateTo);
                            firSearchService.searchAPIConsumeDate(districtId, policeStationId, sDateFrom, sDateTo);
                            policeStations.forEach(policeStation1 -> {
                                logger.info("policestation = "+policeStation1.getPolicestationId()+" District Id = "+policeStation1.getDistrictId());
                            });
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);

                            try {
                                logger.info("================API is down==waiting for 5 min================"+LocalDateTime.now().format(printFormatter)+" ===  "+policeStations.size());
                                policeStations.add(policeStation);
                                policeStations.forEach(policeStation1 -> {
                                    logger.info("policestation in catch = "+policeStation1.getPolicestationId()+" District Id = "+policeStation1.getDistrictId());
                                });
                                Thread.sleep(60000 * 5);
                            } catch (InterruptedException ex) {
                                logger.error(e.getMessage(), e);
                                 throw new RuntimeException(ex);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        logger.info("================API is down==waiting for 5 min================"+LocalDateTime.now().format(printFormatter)+"district size ="+districts.size());
                        districts.add(district);
                        districts.forEach(district1 -> {
                            logger.info("district Id : "+district1.getDistrictId()+" "+district1.getDistrictName());
                        });
                        Thread.sleep(60000 * 5);
                    } catch (InterruptedException ex) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(ex);
                    }
                }
                logger.info("Completed District size : "+ ++districtCount);
            }
            flag = false;
            logger.info(" Date Saved into Config file as : "+dateTo.format(formatter));
            config.setProperty("fir.dateFromDaily", dateTo.format(formatter));
            config.save();
        }

        logger.info("==================================completed============================="+LocalDateTime.now().format(printFormatter));
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
