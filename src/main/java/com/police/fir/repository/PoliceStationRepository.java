package com.police.fir.repository;

import com.police.fir.entity.PoliceStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
public interface PoliceStationRepository extends CrudRepository<PoliceStation, Long > {

}
