package com.police.fir.repository;

import com.police.fir.entity.FirDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirDetailRepository  extends CrudRepository<FirDetail, String > {
}
