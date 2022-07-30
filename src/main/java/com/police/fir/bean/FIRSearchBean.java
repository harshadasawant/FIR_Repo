package com.police.fir.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FIRSearchBean {
    private CitizenFirSearchBean citizenFirSearchBean;
    private List<CitizenFirSearchBean> list;
}
