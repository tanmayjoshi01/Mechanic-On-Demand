package com.mechanicondemand.backend.repository;

import com.mechanicondemand.backend.model.MechanicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicProfileRepository extends JpaRepository<MechanicProfile, Long> {
}
