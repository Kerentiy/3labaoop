package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}