package com.meclist.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meclist.persistence.entity.ChecklistEntity;

public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
}



