package com.police.fir.service;

import com.police.fir.bean.PoliceStationIdMapper;
import com.police.fir.bean.PoliceStationResponseBean;
import com.police.fir.repository.PoliceStationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class PoliceStationService {
    private final RestTemplate restTemplate;
    @Autowired
    public PoliceStationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    PoliceStationIdMapper policeStationIdMapper;

    public PoliceStationResponseBean searchPoliceStationCodeConsumer(int districtId) throws IOException {
        System.out.println("from police station code");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String data = "districtCd=" + districtId;
//        String data = "districtCd=8162";
        HttpEntity<String> request = new HttpEntity<>(data, headers);
//        String policeStationResponseBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/getfirsearchpolicestations.htm", request, String.class);
//        PoliceStationResponseBean policeStationResponseBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/getfirsearchpolicestations.htm", request, PoliceStationResponseBean.class);
        ObjectMapper objectMapper = new ObjectMapper();
        PoliceStationResponseBean policeStationResponseBean = objectMapper.readValue(new File("data/stationcode.json"), PoliceStationResponseBean.class);
        policeStationIdMapper.beanToDBMapper(policeStationResponseBean, districtId);
        return policeStationResponseBean;
    }
}