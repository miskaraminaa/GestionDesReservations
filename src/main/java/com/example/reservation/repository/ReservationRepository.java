package com.example.reservation.repository;

import com.example.reservation.models.Client;
import com.example.reservation.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
