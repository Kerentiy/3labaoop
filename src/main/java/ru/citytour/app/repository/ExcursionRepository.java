package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.Excursion;

public interface ExcursionRepository extends JpaRepository<Excursion, Long> {
}