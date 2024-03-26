package com.yyi.projectStudy;

import jakarta.servlet.http.HttpSessionListener;
import org.apache.catalina.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectStudyApplication.class, args);
	}


}
