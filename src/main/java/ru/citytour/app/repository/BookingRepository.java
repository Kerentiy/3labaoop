package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.Booking;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer_CustomerId(Long customerId);
}