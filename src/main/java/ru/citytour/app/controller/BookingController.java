package ru.citytour.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.citytour.app.model.Booking;
import ru.citytour.app.model.Customer;
import ru.citytour.app.service.BookingService;

import java.util.List;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(@RequestParam(required = false) Long customerId, Model model) {
        
        // Получаем всех покупателей для выпадающего списка
        List<Customer> allCustomers = bookingService.getAllCustomers();
        model.addAttribute("allCustomers", allCustomers);

        if (customerId != null && customerId > 0) {
            // БД ВОЗВРАЩАЕТ ТОЛЬКО БРОНИРОВАНИЯ ЭТОГО ПОКУПАТЕЛЯ
            List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
            
            model.addAttribute("bookings", bookings);
            model.addAttribute("selectedCustomerId", customerId);
            model.addAttribute("filtered", true);
        } else {
            // ВСЕ БРОНИРОВАНИЯ
            List<Booking> allBookings = bookingService.getAllBookings();
            
            model.addAttribute("bookings", allBookings);
            model.addAttribute("filtered", false);
        }
        
        return "bookings/list";
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam Long excursionId,
                                @RequestParam Long customerId,
                                @RequestParam int peopleCount) {
        try {
            bookingService.createBooking(excursionId, customerId, peopleCount);
        } catch (RuntimeException e) {
            return "redirect:/?error=" + e.getMessage();
        }
        return "redirect:/bookings";
    }
}