package com.nexora.repository;

import com.nexora.entity.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    Optional<Startup> findByName(String name);
    boolean existsByName(String name);
    List<Startup> findByFounderId(Long founderId);
    List<Startup> findByStatus(Startup.StartupStatus status);
    List<Startup> findByIndustry(String industry);
}
