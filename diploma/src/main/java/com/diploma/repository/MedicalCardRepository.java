package com.diploma.repository;

import com.diploma.model.MedicalCard;
import com.diploma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, Long> {
    Optional<MedicalCard> findByUser(User user);
}