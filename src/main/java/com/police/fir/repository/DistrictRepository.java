package com.police.fir.repository;

import com.police.fir.entity.District;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository  extends CrudRepository<District, Integer> {
}
