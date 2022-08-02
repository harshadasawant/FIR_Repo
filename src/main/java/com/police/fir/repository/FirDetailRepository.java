package com.police.fir.repository;

import com.police.fir.entity.FirDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FirDetailRepository  extends CrudRepository<FirDetail, String > {

    @Query("SELECT f FROM FirDetail f WHERE f.districtId = ?1 and f.policeStationId = ?2 and f.firYear=?3")
    Collection<FirDetail> findAllFIRByPolicestation(int districtId, int policeStationId, String firYear);
}
