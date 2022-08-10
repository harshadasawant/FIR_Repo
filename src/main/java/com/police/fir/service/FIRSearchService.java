package com.police.fir.service;

import com.police.fir.bean.FIRSearchBean;
import com.police.fir.bean.PoliceStationIdMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.police.fir.entity.FirDetail;
import com.police.fir.repository.FirDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
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
    public FIRSearchBean searchAPIConsume(int districtId, int policestationId, int year) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String data = "sdistrict=8162&spolicestation=8162038&firFromDateStr=23/03/2022&firToDateStr=06/06/2022&regFirNo=0&radioValue=None&firYear=2022";
        String data = "sdistrict=" + districtId + "&spolicestation=" + policestationId + "&firFromDateStr=01/01/" + year + "&firToDateStr=31/12/" + year + "&regFirNo=0&radioValue=None&firYear=" + year;
        HttpEntity<String> request = new HttpEntity<>(data, headers);

        FIRSearchBean firSearchBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/regfirsearchpage.htm", request, FIRSearchBean.class);
//        FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
        policeStationIdMapper.beanToFIrDetailsDBMapper(firSearchBean);
        return firSearchBean;
    }
    public FIRSearchBean searchAPIConsumeDate(int districtId, int policestationId, String dateFrom, String dateTo) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateTime = LocalDate.parse(dateFrom, dateTimeFormatter);
        int year = dateTime.getYear();
        System.out.println(dateTime.getYear());  // 2012
//        String data = "sdistrict=8162&spolicestation=8162038&firFromDateStr=23/03/2022&firToDateStr=06/06/2022&regFirNo=0&radioValue=None&firYear=2022";
        String data = "sdistrict=" + districtId + "&spolicestation=" + policestationId + "&firFromDateStr"+dateFrom + "&firToDateStr=" + dateTo + "&regFirNo=0&radioValue=None&firYear=" + year;
        HttpEntity<String> request = new HttpEntity<>(data, headers);
        FIRSearchBean firSearchBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/regfirsearchpage.htm", request, FIRSearchBean.class);
//        FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
        policeStationIdMapper.beanToFIrDetailsDBMapper(firSearchBean);
        return firSearchBean;
    }

    public List<FirDetail> getFir(int districtId, int policestationId, int year) throws IOException {
        List<FirDetail> firDetailList = new ArrayList<FirDetail>();
        Iterable<FirDetail>   firDetailItr = firDetailRepository.findAllFIRByPolicestation(districtId, policestationId, String.valueOf(year));
        firDetailItr.forEach(firDetailList::add);
        System.out.println("==========="+firDetailList);
        return firDetailList;
    }
    public List<FirDetail> getFir(int districtId, int policestationId, String dateFrom, String dateTo) throws IOException {
        List<FirDetail> firDetailList = new ArrayList<FirDetail>();
        Iterable<FirDetail>   firDetailItr = firDetailRepository.findAllFIRByPolicestationByDate(districtId, policestationId,dateFrom, dateTo);
        firDetailItr.forEach(firDetailList::add);
        System.out.println("==========="+firDetailList);
        return firDetailList;
    }

}
