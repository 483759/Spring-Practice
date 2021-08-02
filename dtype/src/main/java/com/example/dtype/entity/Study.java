package com.example.dtype.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@DiscriminatorValue("S")
public class Study extends File{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String studyName;

	@Builder
	public Study(String studyName) {
		super();
		this.studyName = studyName;
	}

	@Builder
	public Study(String name, String path, String studyName) {
		super(name, path);
		this.studyName = studyName;
	}
	
	
}
