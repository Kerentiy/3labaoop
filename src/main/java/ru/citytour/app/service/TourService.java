package ru.citytour.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.citytour.app.dto.CityTourDto;
import ru.citytour.app.dto.MuseumTourDto;
import ru.citytour.app.mapper.TourMapper;
import ru.citytour.app.model.CityTour;
import ru.citytour.app.model.Excursion;
import ru.citytour.app.model.MuseumTour;
import ru.citytour.app.repository.ExcursionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {

    private final ExcursionRepository repository;
    private final TourMapper mapper;

    public List<Excursion> getAllTours() {
        return repository.findAll();
    }

    @Transactional
    public Excursion saveCityTour(CityTourDto dto) {
        CityTour tour = mapper.toEntity(dto);
        // ПРИНУДИТЕЛЬНО ОБНОВЛЯЕМ РАСЧЁТЫ (необязательно, но для уверенности)
        tour.updatePriceAndDuration();
        return repository.save(tour);
    }

    @Transactional
    public Excursion saveMuseumTour(MuseumTourDto dto) {
        MuseumTour tour = mapper.toEntity(dto);
        tour.updatePriceAndDuration();
        return repository.save(tour);
    }

    public Excursion findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Экскурсия с id " + id + " не найдена"));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public CityTourDto getCityTourDto(Long id) {
        Excursion excursion = findById(id);
        if (excursion instanceof CityTour cityTour) {
            return mapper.toDto(cityTour);
        }
        throw new RuntimeException("Это не CityTour");
    }

    public MuseumTourDto getMuseumTourDto(Long id) {
        Excursion excursion = findById(id);
        if (excursion instanceof MuseumTour museumTour) {
            return mapper.toDto(museumTour);
        }
        throw new RuntimeException("Это не MuseumTour");
    }
}