package ru.citytour.app.mapper;

import org.springframework.stereotype.Component;
import ru.citytour.app.dto.CityTourDto;
import ru.citytour.app.dto.MuseumTourDto;
import ru.citytour.app.model.CityTour;
import ru.citytour.app.model.MuseumTour;

@Component
public class TourMapper {

    public CityTour toEntity(CityTourDto dto) {
        CityTour tour = new CityTour(
                dto.getName(),
                dto.getBasePrice(),
                dto.getBaseDuration(),
                dto.getTransport(),
                dto.isMeal(),
                dto.isHasGuide()
        );
        tour.setId(dto.getId());
        // Принудительно обновляем расчеты
        tour.updatePriceAndDuration();
        return tour;
    }

    public MuseumTour toEntity(MuseumTourDto dto) {
        MuseumTour tour = new MuseumTour(
                dto.getName(),
                dto.getBasePrice(),
                dto.getBaseDuration(),
                dto.getMuseum(),
                dto.getGuide(),
                dto.isAudio(),
                dto.isExpertGuide()
        );
        tour.setId(dto.getId());
        // Принудительно обновляем расчеты
        tour.updatePriceAndDuration();
        return tour;
    }

    public CityTourDto toDto(CityTour tour) {
        CityTourDto dto = new CityTourDto();
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        dto.setBasePrice(tour.getBasePrice());
        dto.setBaseDuration(tour.getBaseDuration());
        dto.setTransport(tour.getTransport());
        dto.setMeal(tour.isMeal());
        dto.setHasGuide(tour.isHasGuide());
        return dto;
    }

    public MuseumTourDto toDto(MuseumTour tour) {
        MuseumTourDto dto = new MuseumTourDto();
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        dto.setBasePrice(tour.getBasePrice());
        dto.setBaseDuration(tour.getBaseDuration());
        dto.setMuseum(tour.getMuseum());
        dto.setGuide(tour.getGuide());
        dto.setAudio(tour.isAudio());
        dto.setExpertGuide(tour.isExpertGuide());
        return dto;
    }
}