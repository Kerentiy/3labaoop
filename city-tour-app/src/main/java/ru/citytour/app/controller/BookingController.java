package ru.citytour.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.citytour.app.service.BookingService;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings/list";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long excursionId,
                                @RequestParam int peopleCount) {
        bookingService.createBooking(excursionId, peopleCount);
        return "redirect:/bookings";
    }
}