package com.nova.guestApp.repository;

import com.nova.guestApp.enums.Status;
import com.nova.guestApp.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    boolean existsByTagId(String tagId);

    Guest findByTagId(String tagId);

    Guest findFirstByTagIdAndStatusOrderByCheckInTimeDesc(String tagId, Status status);
}
