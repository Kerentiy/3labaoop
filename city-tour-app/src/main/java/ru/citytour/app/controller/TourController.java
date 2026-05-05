package ru.citytour.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.citytour.app.dto.CityTourDto;
import ru.citytour.app.dto.MuseumTourDto;
import ru.citytour.app.model.CityTour;
import ru.citytour.app.model.Excursion;
import ru.citytour.app.model.MuseumTour;
import ru.citytour.app.service.TourService;

@Controller
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tours", tourService.getAllTours());
        return "index";
    }

    // ====================== CITY TOUR ======================
    @GetMapping("/add-city")
    public String addCityForm(Model model) {
        model.addAttribute("cityTour", new CityTourDto());
        return "tours/add-city";  // Ищем в папке tours
    }

    @PostMapping("/add-city")
    public String addCity(@ModelAttribute CityTourDto cityTour) {
        tourService.saveCityTour(cityTour);
        return "redirect:/";
    }

    // ====================== MUSEUM TOUR ======================
    @GetMapping("/add-museum")
    public String addMuseumForm(Model model) {
        model.addAttribute("museumTour", new MuseumTourDto());
        return "tours/add-museum";  // Ищем в папке tours
    }

    @PostMapping("/add-museum")
    public String addMuseum(@ModelAttribute MuseumTourDto museumTour) {
        tourService.saveMuseumTour(museumTour);
        return "redirect:/";
    }

    // ====================== EDIT ======================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Excursion tour = tourService.findById(id);

        if (tour instanceof CityTour) {
            model.addAttribute("cityTour", tourService.getCityTourDto(id));
            return "tours/edit-city";  // Ищем в папке tours
        } else if (tour instanceof MuseumTour) {
            model.addAttribute("museumTour", tourService.getMuseumTourDto(id));
            return "tours/edit-museum";  // Ищем в папке tours
        }
        return "redirect:/";
    }

    @PostMapping("/edit-city")
    public String updateCity(@ModelAttribute CityTourDto cityTour) {
        tourService.saveCityTour(cityTour);
        return "redirect:/";
    }

    @PostMapping("/edit-museum")
    public String updateMuseum(@ModelAttribute MuseumTourDto museumTour) {
        tourService.saveMuseumTour(museumTour);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tourService.deleteById(id);
        return "redirect:/";
    }
}