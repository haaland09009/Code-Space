package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
}
