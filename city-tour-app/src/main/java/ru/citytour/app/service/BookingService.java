package ru.citytour.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.citytour.app.dto.BookingStatsDto;
import ru.citytour.app.model.Booking;
import ru.citytour.app.model.CityTour;
import ru.citytour.app.model.Excursion;
import ru.citytour.app.model.MuseumTour;
import ru.citytour.app.repository.BookingRepository;
import ru.citytour.app.repository.ExcursionRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ExcursionRepository excursionRepository;

    // Создание бронирования
    @Transactional
    public Booking createBooking(Long excursionId, int peopleCount) {
        Excursion excursion = excursionRepository.findById(excursionId)
                .orElseThrow(() -> new RuntimeException("Экскурсия не найдена"));
        
        // Проверка возможности бронирования (макс 50 человек)
        if (!excursion.canBook(peopleCount)) {
            throw new RuntimeException("Нельзя забронировать более 50 человек");
        }
        
        // Расчёт итоговой цены
        int totalPrice = excursion.calculateFinalPrice(peopleCount);
        
        Booking booking = new Booking();
        booking.setExcursion(excursion);
        booking.setPeopleCount(peopleCount);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        
        return bookingRepository.save(booking);
    }
    
    // Получение всех бронирований
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    // Получение бронирований по экскурсии
    public List<Booking> getBookingsByExcursion(Long excursionId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getExcursion().getId().equals(excursionId))
                .collect(Collectors.toList());
    }
    
    // Подсчёт количества проданных билетов на конкретную экскурсию
    public int getTicketsSoldForExcursion(Long excursionId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getExcursion().getId().equals(excursionId))
                .mapToInt(Booking::getPeopleCount)
                .sum();
    }
    
    // Подсчёт выручки по конкретной экскурсии
    public int getRevenueForExcursion(Long excursionId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getExcursion().getId().equals(excursionId))
                .mapToInt(Booking::getTotalPrice)
                .sum();
    }
    
    // Подсчёт общего количества проданных билетов
    public int getTotalTicketsSoldAll() {
        return bookingRepository.findAll().stream()
                .mapToInt(Booking::getPeopleCount)
                .sum();
    }
    
    // Подсчёт общей выручки
    public int getTotalRevenueAll() {
        return bookingRepository.findAll().stream()
                .mapToInt(Booking::getTotalPrice)
                .sum();
    }
    
    // Статистика по типам экскурсий (CityTour и MuseumTour)
    public List<BookingStatsDto> getBookingStatsByType() {
        List<Booking> allBookings = bookingRepository.findAll();
        
        // Группировка по типу экскурсии
        Map<String, List<Booking>> bookingsByType = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getExcursion().getType()));
        
        List<BookingStatsDto> stats = new ArrayList<>();
        
        for (Map.Entry<String, List<Booking>> entry : bookingsByType.entrySet()) {
            String type = entry.getKey();
            List<Booking> bookings = entry.getValue();
            
            long bookingsCount = bookings.size();
            int totalTickets = bookings.stream().mapToInt(Booking::getPeopleCount).sum();
            int totalRevenue = bookings.stream().mapToInt(Booking::getTotalPrice).sum();
            
            stats.add(new BookingStatsDto(type, bookingsCount, totalTickets, totalRevenue));
        }
        
        // Добавляем статистику для экскурсий, у которых нет бронирований
        List<Excursion> allExcursions = excursionRepository.findAll();
        for (Excursion excursion : allExcursions) {
            String type = excursion.getType();
            boolean exists = stats.stream().anyMatch(s -> s.getExcursionType().equals(type));
            if (!exists) {
                stats.add(new BookingStatsDto(type, 0, 0, 0));
            }
        }
        
        return stats;
    }
    
    // Статистика по каждой экскурсии отдельно
    public List<BookingStatsDto> getBookingStatsByExcursion() {
        List<Excursion> allExcursions = excursionRepository.findAll();
        List<BookingStatsDto> stats = new ArrayList<>();
        
        for (Excursion excursion : allExcursions) {
            List<Booking> excursionBookings = getBookingsByExcursion(excursion.getId());
            
            long bookingsCount = excursionBookings.size();
            int totalTickets = excursionBookings.stream().mapToInt(Booking::getPeopleCount).sum();
            int totalRevenue = excursionBookings.stream().mapToInt(Booking::getTotalPrice).sum();
            
            String displayName = excursion.getName() + " (" + excursion.getType() + ")";
            stats.add(new BookingStatsDto(displayName, bookingsCount, totalTickets, totalRevenue));
        }
        
        return stats;
    }
    
    // Получение количества бронирований по типу экскурсии (альтернативный метод)
    public Map<String, Long> getBookingsCountByType() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    b -> b.getExcursion().getType(),
                    Collectors.counting()
                ));
    }
    
    // Получение количества проданных билетов по типу экскурсии
    public Map<String, Integer> getTicketsSoldByType() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    b -> b.getExcursion().getType(),
                    Collectors.summingInt(Booking::getPeopleCount)
                ));
    }
    
    // Получение выручки по типу экскурсии
    public Map<String, Integer> getRevenueByType() {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    b -> b.getExcursion().getType(),
                    Collectors.summingInt(Booking::getTotalPrice)
                ));
    }
    
    // Получение самых популярных экскурсий (по количеству билетов)
    public List<Excursion> getMostPopularExcursions(int limit) {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    Booking::getExcursion,
                    Collectors.summingInt(Booking::getPeopleCount)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Excursion, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    // Получение самых прибыльных экскурсий
    public List<Excursion> getMostProfitableExcursions(int limit) {
        return bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    Booking::getExcursion,
                    Collectors.summingInt(Booking::getTotalPrice)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Excursion, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}