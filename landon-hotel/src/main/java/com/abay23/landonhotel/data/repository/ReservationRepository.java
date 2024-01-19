package com.abay23.landonhotel.data.repository;

import com.abay23.landonhotel.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findAllByReservationDate(Date reservationDate);
}
