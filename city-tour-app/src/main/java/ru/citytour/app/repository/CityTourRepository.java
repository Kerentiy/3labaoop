package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.CityTour;

public interface CityTourRepository extends JpaRepository<CityTour, Long> {
}