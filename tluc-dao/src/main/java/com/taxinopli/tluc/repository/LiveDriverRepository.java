package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveDriverEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveDriverRepository extends CrudRepository<LiveDriverEntity, Long> {

}