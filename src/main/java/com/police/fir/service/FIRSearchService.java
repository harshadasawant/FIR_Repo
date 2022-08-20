package com.police.fir.service;

import com.police.fir.bean.FIRSearchBean;
import com.police.fir.bean.PoliceStationIdMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.police.fir.entity.ExceptionDBConnect;
import com.police.fir.entity.FirDetail;
import com.police.fir.repository.ExceptionRepository;
import com.police.fir.repository.FirDetailRepository;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FIRSearchService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PoliceStationIdMapper policeStationIdMapper;
    @Autowired
    private FirDetailRepository firDetailRepository;
    @Autowired
    private ExceptionRepository exceptionRepository;

    @Autowired
    private ExceptionDBConnect exceptionDBConnect;

    Logger logger = LoggerFactory.getLogger(FIRSearchService.class);

    public FIRSearchBean searchAPIConsume(int districtId, int policestationId, int year) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String data = "sdistrict=8162&spolicestation=8162038&firFromDateStr=23/03/2022&firToDateStr=06/06/2022&regFirNo=0&radioValue=None&firYear=2022";
        String data = "sdistrict=" + districtId + "&spolicestation=" + policestationId + "&firFromDateStr=01/01/" + year + "&firToDateStr=31/12/" + year + "&regFirNo=0&radioValue=None&firYear=" + year;
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        FIRSearchBean firSearchBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/regfirsearchpage.htm", request, FIRSearchBean.class);
        System.out.println("");
//        FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
        String regNo =  policeStationIdMapper.beanToFIrDetailsDBMapper(firSearchBean);
        if(regNo != null) {
            downloadPDF(regNo);
        }
        return firSearchBean;
    }
    public FIRSearchBean searchAPIConsumeDate(int districtId, int policestationId, String dateFrom, String dateTo) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTime = LocalDate.parse(dateFrom, dateTimeFormatter);
        int year = dateTime.getYear();
//        districtId = 8162;
//        policestationId = 8162001;
//        String data = "sdistrict=8162&spolicestation=8162038&firFromDateStr=23/03/2022&firToDateStr=06/06/2022&regFirNo=0&radioValue=None&firYear=2022";
        String data = "sdistrict=" + districtId + "&spolicestation=" + policestationId + "&firFromDateStr="+dateFrom + "&firToDateStr=" + dateTo + "&regFirNo=0&radioValue=None&firYear=" + year;
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        FIRSearchBean firSearchBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/regfirsearchpage.htm", request, FIRSearchBean.class);
//        FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
//        System.out.println("===============posted==========="+firSearchBean.getList().size());
        String regNo =  policeStationIdMapper.beanToFIrDetailsDBMapper(firSearchBean);
        logger.info("Register No : "+regNo);
        if(regNo != null) {
            downloadPDF(regNo);
        }
        return firSearchBean;
    }

    public String downloadPDF(String regNo) throws ConfigurationException {
        System.out.println("=====inside ============");
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        String filePath = config.getProperty("fir.report.path").toString();
        logger.info(" File Path for Report : "+filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url ="https://cctns.delhipolice.gov.in/citizen/gefirprint.htm?firRegNo="+regNo+"&stov=46XU-O3NJ-QB17-1MHM-TCDQ-391O-571E-FYEH";
        logger.info(" download pdf URL : "+url);
        File file = null;
        try {

            file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
               File ret = new File(filePath + regNo + ".pdf");
               StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
               return ret;
           });
       }catch (Exception e){
           logger.error(e.getMessage(), e);
            System.out.println("exceptionDBConnect = " + exceptionDBConnect);
            System.out.println("exceptionRepository = " + exceptionRepository);
           exceptionDBConnect.setExceptionCause(e.getCause() == null ? null : e.getCause().toString());
           exceptionDBConnect.setExceptionTime(LocalDateTime.now());
           exceptionDBConnect.setExceptionMessage(e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.out.println(exceptionAsString);
           exceptionDBConnect.setExceptionStatckTrace(exceptionAsString);
           exceptionDBConnect.setFir_reg_num(regNo);
           exceptionRepository.save(exceptionDBConnect);
       }
       if(file != null){
           logger.info("File is get Saved into : "+file.getAbsolutePath());
       }
       else{
           logger.info("File is not Created check Exception ");
       }
         return "file save on " + file.getAbsolutePath();
    }


    public List<FirDetail> getFir(int districtId, int policestationId, String year) throws IOException {
        List<FirDetail> firDetailList = new ArrayList<FirDetail>();
        Iterable<FirDetail>   firDetailItr = firDetailRepository.findAllFIRByPolicestation(districtId, policestationId, year);
        firDetailItr.forEach(firDetailList::add);
        System.out.println("==========="+firDetailList);
        return firDetailList;
    }
    public List<FirDetail> getFir(int districtId, int policestationId, String dateFrom, String dateTo,String year) throws IOException {
        List<FirDetail> firDetailList = new ArrayList<FirDetail>();
        Iterable<FirDetail> firDetailItr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateFrom1 = LocalDate.parse(dateFrom, formatter);
        LocalDate dateTo1 = LocalDate.parse(dateTo, formatter);

        System.out.println("==="+dateFrom1);
        System.out.println("dateTo= "+dateTo1);
        if(year.equals("")) {
            firDetailItr = firDetailRepository.findAllFIRByPolicestationByDate(districtId, policestationId, dateFrom1, dateTo1);
        } else{
            System.out.println("in side else");
            firDetailItr = firDetailRepository.findAllFIRByPolicestationByDate(districtId, policestationId, dateFrom1, dateTo1, year);
        }
        firDetailItr.forEach(firDetailList::add);
        System.out.println("==========="+firDetailList);
        return firDetailList;
    }

}
