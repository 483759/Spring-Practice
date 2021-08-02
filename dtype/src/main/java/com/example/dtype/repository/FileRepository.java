package com.example.dtype.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dtype.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{
	File getByName(String name);
}
