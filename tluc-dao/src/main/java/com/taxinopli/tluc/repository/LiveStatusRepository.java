package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveStatusRepository extends JpaRepository<LiveStatusEntity, Long> {

}