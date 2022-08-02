package com.police.fir.service;

import com.police.fir.entity.District;
import com.police.fir.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictService {

    @Autowired
    DistrictRepository districtRepository;
    public List<District> getDistrict(){
        List<District> districtList = new ArrayList<District>();
        Iterable<District> districtItr = districtRepository.findAll();
        districtItr.forEach(districtList::add);
        return districtList;
    }
}
