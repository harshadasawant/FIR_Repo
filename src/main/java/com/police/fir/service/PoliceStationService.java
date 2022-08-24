package com.police.fir.service;

import com.police.fir.bean.PoliceStationIdMapper;
import com.police.fir.bean.PoliceStationResponseBean;
import com.police.fir.entity.PoliceStation;
import com.police.fir.repository.PoliceStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PoliceStationService {
    private final RestTemplate restTemplate;
    @Autowired
    public PoliceStationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    PoliceStationIdMapper policeStationIdMapper;

    @Autowired
    PoliceStationRepository policeStationRepository;

    Logger logger = LoggerFactory.getLogger(PoliceStationService.class);

    public PoliceStationResponseBean savePoliceStationCode(int districtId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String data = "districtCd=" + districtId;
//        String data = "districtCd=8162";
        HttpEntity<String> request = new HttpEntity<>(data, headers);
//        String policeStationResponseBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/getfirsearchpolicestations.htm", request, String.class);
        logger.info("Data : "+data+ "  "+ LocalDateTime.now());
        PoliceStationResponseBean policeStationResponseBean = restTemplate.postForObject("https://cctns.delhipolice.gov.in/citizen/getfirsearchpolicestations.htm", request, PoliceStationResponseBean.class);
        logger.info("policeStationResponseBean :  "+policeStationResponseBean +"  "+LocalDateTime.now());
//        ObjectMapper objectMapper = new ObjectMapper();
//        PoliceStationResponseBean policeStationResponseBean = objectMapper.readValue(new File("data/stationcode.json"), PoliceStationResponseBean.class);
        policeStationIdMapper.beanToDBMapper(policeStationResponseBean, districtId);
        return policeStationResponseBean;
    }
    public List<PoliceStation> getPoliceStationCode(int districtId) throws IOException {
            List<PoliceStation> policeStationList = new ArrayList<PoliceStation>();
            Iterable<PoliceStation> districtItr = policeStationRepository.findAllByDistrictId(districtId);
            districtItr.forEach(policeStationList::add);
            return policeStationList;
        }
}
