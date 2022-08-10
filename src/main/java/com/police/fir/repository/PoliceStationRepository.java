package com.police.fir.repository;

import com.police.fir.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public interface PoliceStationRepository extends JpaRepository<PoliceStation, Integer > {

    @Query("SELECT p FROM PoliceStation p WHERE p.districtId = ?1")
    Collection<PoliceStation> findAllByDistrictId(int districtId);

}
