package com.police.fir.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "District")
public class District {
    @Id
    private Integer DistrictId;
    private String DistrictName;
}
