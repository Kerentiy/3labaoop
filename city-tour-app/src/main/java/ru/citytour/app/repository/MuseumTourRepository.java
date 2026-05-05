package ru.citytour.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.citytour.app.model.MuseumTour;

public interface MuseumTourRepository extends JpaRepository<MuseumTour, Long> {
}