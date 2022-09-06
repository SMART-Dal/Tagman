package com.smartlab.tagman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smartlab.tagman.model.Repository;

@Component
public interface RepoRepository extends JpaRepository<Repository, Long> {

}
