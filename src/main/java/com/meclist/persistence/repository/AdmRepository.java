package com.meclist.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meclist.persistence.entity.AdmEntity;

@Repository
public interface AdmRepository  extends JpaRepository<AdmEntity,Long>{
    Optional<AdmEntity> findByEmail(String email);

}
