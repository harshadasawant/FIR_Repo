package com.police.fir.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
public class PoliceStation {
    @Id
    private Long policestationId;
    private String policestationName;
    private int districtId;
}
