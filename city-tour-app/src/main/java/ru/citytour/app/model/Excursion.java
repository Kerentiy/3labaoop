package ru.citytour.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "excursions")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Excursion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    private int basePrice;
    private int baseDuration;

    // JOIN с бронированиями - один ко многим
    @OneToMany(mappedBy = "excursion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    public abstract int calculateFinalPrice(int people);
    public abstract String getType();
    public abstract void updatePriceAndDuration();
    
    public boolean canBook(int people) {
        return people > 0 && people <= 50;
    }
    
    // ПОЛУЧЕНИЕ ВСЕХ БРОНИРОВАНИЙ ЭКСКУРСИИ ЧЕРЕЗ JOIN
    public List<Booking> getBookings() {
        return bookings;
    }
    
    // ПОДСЧЁТ ПРОДАННЫХ БИЛЕТОВ НА ЭКСКУРСИЮ ЧЕРЕЗ JOIN
    public int getTotalTicketsSold() {
        return bookings.stream().mapToInt(Booking::getPeopleCount).sum();
    }
    
    // ПОДСЧЁТ ВЫРУЧКИ ПО ЭКСКУРСИИ ЧЕРЕЗ JOIN
    public int getTotalRevenue() {
        return bookings.stream().mapToInt(Booking::getTotalPrice).sum();
    }
}