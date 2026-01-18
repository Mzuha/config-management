package com.mzuha.configservice.repository;

import com.mzuha.configservice.model.ConfigEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, Long> {
    Set<ConfigEntity> findByApplicationAndProfile(String application, String profile);
}
