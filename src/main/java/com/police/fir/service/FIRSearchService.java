package com.police.fir.service;

import com.police.fir.bean.FIRSearchBean;
import com.police.fir.bean.PoliceStationIdMapper;
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
public class FIRSearchService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PoliceStationIdMapper policeStationIdMapper;
    public FIRSearchBean searchAPIConsume(int districtId, int policestationId, int year) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String data = "sdistrict=8162&spolicestation=8162038&firFromDateStr=23/03/2022&firToDateStr=06/06/2022&regFirNo=0&radioValue=None&firYear=2022";
        String data = "sdistrict=" + districtId + "&spolicestation=" + policestationId + "&firFromDateStr=01/01/" + year + "&firToDateStr=31/12/" + year + "&regFirNo=0&radioValue=None&firYear=" + year;
        HttpEntity<String> request = new HttpEntity<>(data, headers);
//        FIRSearchBean firSearchBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/regfirsearchpage.htm", request, FIRSearchBean.class);
        FIRSearchBean firSearchBean = objectMapper.readValue(new File("data/data.json"), FIRSearchBean.class);
        policeStationIdMapper.beanToFIrDetailsDBMapper(firSearchBean);
        return firSearchBean;
    }
}
