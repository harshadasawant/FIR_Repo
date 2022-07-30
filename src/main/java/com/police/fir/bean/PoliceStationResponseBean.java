package com.police.fir.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
public class PoliceStationResponseBean {
    private List<List<String>> rows;
}
