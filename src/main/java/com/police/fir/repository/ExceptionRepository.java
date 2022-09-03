package com.police.fir.repository;

import com.police.fir.entity.ExceptionDBConnect;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionRepository  extends CrudRepository<ExceptionDBConnect, Integer> {
}
