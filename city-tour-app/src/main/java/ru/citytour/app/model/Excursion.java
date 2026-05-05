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

    @OneToMany(mappedBy = "excursion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    public abstract int calculateFinalPrice(int people);
    public abstract String getType();
    public abstract void updatePriceAndDuration();
    
    // Добавьте этот метод
    public boolean canBook(int people) {
        return people > 0 && people <= 50;
    }
}