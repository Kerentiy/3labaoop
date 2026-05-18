package ru.citytour.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.citytour.app.model.Booking;
import ru.citytour.app.model.Customer;
import ru.citytour.app.model.Excursion;
import ru.citytour.app.repository.BookingRepository;
import ru.citytour.app.repository.CustomerRepository;
import ru.citytour.app.repository.ExcursionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ExcursionRepository excursionRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Booking createBooking(Long excursionId, Long customerId, int peopleCount) {
        Excursion excursion = excursionRepository.findById(excursionId)
                .orElseThrow(() -> new RuntimeException("Экскурсия не найдена"));
        
        if (!excursion.canBook(peopleCount)) {
            throw new RuntimeException("Нельзя забронировать более 50 человек");
        }
        
        // Находим или создаём покупателя
        List<Customer> allCustomers = customerRepository.findAll();
        Customer customer = null;
        for (Customer c : allCustomers) {
            if (c.getCustomerId().equals(customerId)) {
                customer = c;
                break;
            }
        }
        
        if (customer == null) {
            customer = Customer.builder()
                    .customerId(customerId)
                    .build();
            customer = customerRepository.save(customer);
        }
        
        int totalPrice = excursion.calculateFinalPrice(peopleCount);
        
        Booking booking = Booking.builder()
                .excursion(excursion)
                .customer(customer)
                .peopleCount(peopleCount)
                .bookingDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .build();
        
        return bookingRepository.save(booking);
    }
    
    // ВСЕ БРОНИРОВАНИЯ
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    // ВСЕ ПОКУПАТЕЛИ
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        // джойн и вытаскивание определенного айди
        return bookingRepository.findByCustomer_CustomerId(customerId);
    }
}