package com.example.dtype;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.dtype.entity.File;
import com.example.dtype.entity.Study;
import com.example.dtype.repository.FileRepository;
import com.example.dtype.repository.StudyRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Rollback(false)
class DtypeApplicationTests {
	
	@Autowired private FileRepository fileRepo;
	@Autowired private StudyRepository studyRepo;
	
	@Test
	@Ignore
	@Rollback
	public void fileTest() {
		String fileName = "name";
		String filePath = "path";
		
//		fileRepo.save(
				
//					File.builder()
//					.name(fileName)
//					.path(filePath)
//					.build()
//				);
		
		File file = fileRepo.getByName(fileName);
		
		if (file!=null) {
			System.out.println(file);
			assertEquals(file.getPath(), "path");
		}else {
			assertFalse(false);
		}
	}

	@Test
	@Rollback
	public void studyTest() {
		String studyName = "ALGO";
		String fileName = "name";
		String filePath = "path";
		
		studyRepo.save(
				Study.builder()
				.studyName(studyName)
				.name(fileName)
				.path(filePath)
				.build()
				);
		
		Study study = studyRepo.getByStudyName("ALGO");
		
		if (study!=null) {
			System.out.println(study);
			assertEquals(study.getPath(), "path");
		}else {
			assertFalse(false);
		}
	}

}
