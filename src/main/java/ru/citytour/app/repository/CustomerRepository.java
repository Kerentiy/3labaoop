package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // ПУСТОЙ
}