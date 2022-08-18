package com.police.fir.repository;

import com.police.fir.entity.FirDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface FirDetailRepository  extends CrudRepository<FirDetail, String > {

    @Query("SELECT f FROM FirDetail f WHERE f.districtId = ?1 and f.policeStationId = ?2 and f.firYear=?3")
    Collection<FirDetail> findAllFIRByPolicestation(int districtId, int policeStationId, String firYear);

    @Query("SELECT f FROM FirDetail f WHERE f.districtId = ?1 and f.policeStationId = ?2  and f.firRegDate BETWEEN ?3 AND ?4")
    Collection<FirDetail> findAllFIRByPolicestationByDate(int districtId, int policeStationId, LocalDate dateFrom, LocalDate dateTo);

    @Query("SELECT f FROM FirDetail f WHERE f.districtId = ?1 and f.policeStationId = ?2  and f.firRegDate BETWEEN ?3 AND ?4 and f.firYear=?5")
    Collection<FirDetail> findAllFIRByPolicestationByDate(int districtId, int policeStationId, LocalDate dateFrom, LocalDate dateTo, String year);


}
