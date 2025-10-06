package com.mechanicondemand.repository;

import com.mechanicondemand.model.Booking;
import com.mechanicondemand.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(User customer);
    List<Booking> findByMechanic(User mechanic);
}
