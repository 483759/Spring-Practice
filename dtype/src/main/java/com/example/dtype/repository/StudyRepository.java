package com.example.dtype.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dtype.entity.File;
import com.example.dtype.entity.Study;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>{
	Study getByStudyName(String name);
}
