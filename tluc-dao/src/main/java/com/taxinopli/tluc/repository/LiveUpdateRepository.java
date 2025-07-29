package com.taxinopli.tluc.repository;

import com.taxinopli.tluc.entity.LiveUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveUpdateRepository extends JpaRepository<LiveUpdateEntity, Long> {

}
