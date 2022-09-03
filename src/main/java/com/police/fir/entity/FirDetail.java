package com.police.fir.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Component
@Entity
public class FirDetail {

    private String firNumDisplay;
    private int districtId;
    private String actId;
    private String actName;
    private String complainantFirstName;
    private String complainantLastName;
    private String complainantMiddleName;
    private String complainantName;
    private int districtCd;
    private String districtName;

    @Column(columnDefinition = "longtext")
    private String firBrief;
    private String firDate;
    private String firFromDate;
    private String firFromDateStr;
    private LocalDate firRegDate;
    @Id
    private String firRegNum;
    private String firStatus;
    private String firToDate;
    private String firToDateStr;
    private String firYear;
    private String isFirstSyncDone;
    private int langCd;
    private String linkedFirAction;
    private String linkingFir;
    private String linkingReason;
    private String misc1;
    private String misc2;
    private String nameType;
    private int orginalRecord;
    private int pageCacheRows;
    private int pageStartNo;
    private int pageTotalCount;
    private int policeStationId;
    private String policeStationName;
    private String psRecordSyncOn;
    private String queryDynamicPart;
    private String queryKey;
    private String recordCreatedBy;
    private LocalDate recordCreatedOn;
    private String recordStatus;
    private String recordSyncFrom;
    private String recordSyncOn;
    private String recordSyncTo;
    private String recordUpStringdBy;
    private String recordUpStringdFrom;
    private String recordUpStringdOn;
    private String recordUpdatedFrom;
    private String recordUpdatedOn;
    private String recordUpdatedby;
    private String regFirNo;
    private String returnClassType;
    private boolean searchCrit;
    private String searchName;
    private String sectionId;
    private String sectionName;
    private int stateCd;
    private String stateName;
    private String statusOfFir;
    private int userDistrictCd;
    private int userPsCd;
    private int userStateCd;
    private String address;
    private String yearOfBirth;
    private String passportNo;
}