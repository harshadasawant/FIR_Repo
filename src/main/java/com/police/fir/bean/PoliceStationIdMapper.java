package com.police.fir.bean;

import com.police.fir.entity.FirDetail;
import com.police.fir.entity.PoliceStation;
import com.police.fir.repository.FirDetailRepository;
import com.police.fir.repository.PoliceStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PoliceStationIdMapper {
    @Autowired
    PoliceStationRepository policeStationRepository;

    @Autowired
    FirDetailRepository firDetailRepository;

    public void beanToDBMapper(PoliceStationResponseBean policeStationResponseBean, int districtId) {
        List<List<String>> rows = policeStationResponseBean.getRows();
        for (List<String> station : rows) {
            PoliceStation policeStation = new PoliceStation();
            System.out.println(station);
            policeStation.setPolicestationId(Long.parseLong(station.get(0)));
            policeStation.setDistrictId(districtId);
            policeStation.setPolicestationName(station.get(1).toString());
            policeStationRepository.save(policeStation);
        }
    }

    public void beanToFIrDetailsDBMapper(FIRSearchBean firSeachBean) {
        CitizenFirSearchBean citizenFirSearchBean = firSeachBean.getCitizenFirSearchBean();
        System.out.println(citizenFirSearchBean);
        List<CitizenFirSearchBean> citizenFirSearchBeans = firSeachBean.getList();
        System.out.println(citizenFirSearchBeans);
        for (CitizenFirSearchBean bean : citizenFirSearchBeans) {
            FirDetail firDetail = new FirDetail();
            firDetail.setDistrictId(citizenFirSearchBean.getDistrictId());
            firDetail.setDistrictId(citizenFirSearchBean.getDistrictId());
            firDetail.setActId(bean.getActId());
            firDetail.setActName(bean.getActName());
            firDetail.setComplainantFirstName(bean.getComplainantFirstName());
            firDetail.setComplainantMiddleName(bean.getComplainantMiddleName());
            firDetail.setComplainantLastName(bean.getComplainantLastName());
            firDetail.setComplainantName(bean.getComplainantName());
            firDetail.setDistrictCd(bean.getDistrictCd());
            firDetail.setDistrictName(bean.getDistrictName());
            firDetail.setFirBrief(bean.getFirBrief());
            firDetail.setFirFromDate(bean.getFirFromDate());
            firDetail.setFirFromDateStr(bean.getFirFromDateStr());
            firDetail.setFirNumDisplay(bean.getFirNumDisplay());
            firDetail.setRegFirNo(bean.getRegFirNo());
            firDetail.setFirRegDate(bean.getFirRegDate());
            firDetail.setFirStatus(bean.getFirStatus());
            firDetail.setFirToDate(bean.getFirToDate());
            firDetail.setFirToDateStr(bean.getFirToDateStr());
            firDetail.setFirYear(bean.getFirYear());
            firDetail.setIsFirstSyncDone(bean.getIsFirstSyncDone());
            firDetail.setLangCd(bean.getLangCd());
            firDetail.setLinkedFirAction(bean.getLinkedFirAction());
            firDetail.setLinkingFir(bean.getLinkingFir());
            firDetail.setLinkingReason(bean.getLinkingReason());
            firDetail.setLinkingFir(bean.getLinkingFir());
            firDetail.setMisc1(bean.getMisc1());
            firDetail.setMisc2(bean.getMisc2());
            firDetail.setNameType(bean.getNameType());
            firDetail.setOrginalRecord(bean.getOrginalRecord());
            firDetail.setPageCacheRows(bean.getPageCacheRows());
            firDetail.setPageStartNo(bean.getPageStartNo());
            firDetail.setPageTotalCount(bean.getPageTotalCount());
            firDetail.setPoliceStationId(citizenFirSearchBean.getPoliceStationId());
            firDetail.setPoliceStationName(bean.getPoliceStationName());
            firDetail.setPsRecordSyncOn(bean.getPsRecordSyncOn());
            firDetail.setQueryKey(bean.getQueryKey());
            firDetail.setRecordCreatedBy(bean.getRecordCreatedBy());
            firDetail.setRecordCreatedOn(bean.getRecordCreatedOn());
            firDetail.setRecordStatus(bean.getRecordStatus());
            firDetail.setRecordSyncFrom(bean.getRecordSyncFrom());
            firDetail.setRecordSyncOn(bean.getRecordSyncOn());
            firDetail.setRecordSyncTo(bean.getRecordSyncTo());
            firDetail.setRecordUpStringdBy(bean.getRecordUpStringdBy());
            firDetail.setRecordUpStringdFrom(bean.getRecordUpStringdFrom());
            firDetail.setRecordUpStringdOn(bean.getRecordUpStringdOn());
            firDetail.setRecordUpdatedFrom(bean.getRecordUpdatedFrom());
            firDetail.setRecordUpdatedOn(bean.getRecordUpdatedOn());
            firDetail.setRecordUpdatedby(bean.getRecordUpdatedby());
            firDetail.setRegFirNo(bean.getRegFirNo());
            firDetail.setReturnClassType(bean.getReturnClassType());
            firDetail.setSearchCrit(bean.isSearchCrit());
            firDetail.setSearchName(bean.getSearchName());
            firDetail.setSectionId(bean.getSectionId());
            firDetail.setSearchName(bean.getSearchName());
            firDetail.setStateCd(bean.getStateCd());
            firDetail.setStateName(bean.getStateName());
            firDetail.setStatusOfFir(bean.getStatusOfFir());
            firDetail.setUserDistrictCd(bean.getUserDistrictCd());
            firDetail.setUserPsCd(bean.getUserPsCd());
            firDetail.setUserStateCd(bean.getUserStateCd());
            firDetailRepository.save(firDetail);
        }
    }
}
