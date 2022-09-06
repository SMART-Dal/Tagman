package com.smartlab.tagman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.smartlab.tagman.model.DesigniteCSV;
import com.smartlab.tagman.model.DesigniteCSVMethod;
import com.smartlab.tagman.model.Sample;

@Component
public interface DesigniteCSVMethodRepository extends JpaRepository<DesigniteCSVMethod, Long> {
    
}
