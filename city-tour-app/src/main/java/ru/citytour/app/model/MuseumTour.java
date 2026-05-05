package ru.citytour.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "museum_tours")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MuseumTour extends Excursion {

    private String museum;
    private String guide = "русский";
    private boolean audio = false;
    private boolean expertGuide = false;
    
    // Временные поля для хранения рассчитанных значений
    @Transient
    private int calculatedPrice;
    @Transient
    private int calculatedDuration;

    public MuseumTour(String name, int basePrice, int baseDuration, String museum,
                      String guide, boolean audio, boolean expertGuide) {
        this.setName(name);
        this.setBasePrice(basePrice);
        this.setBaseDuration(baseDuration);
        this.museum = museum;
        this.guide = guide != null ? guide : "русский";
        this.audio = audio;
        this.expertGuide = expertGuide;
        updatePriceAndDuration();
    }

    @Override
    public void updatePriceAndDuration() {
        int price = getBasePrice();
        int duration = getBaseDuration();
        
        // Логика как в C++
        if (!"русский".equals(guide)) {
            price += 80;
        }
        if (audio) {
            price += 60;
        }
        if (expertGuide) {
            price += 200;
            duration += 2;
        }
        
        this.calculatedPrice = price;
        this.calculatedDuration = duration;
    }

    @Override
    public int calculateFinalPrice(int people) {
        if (!canBook(people)) {
            return 0;
        }
        updatePriceAndDuration();
        return calculatedPrice * people;
    }
    
    // Методы для отображения в таблице
    public int getFinalPrice() {
        updatePriceAndDuration();
        return calculatedPrice;
    }
    
    public int getFinalDuration() {
        updatePriceAndDuration();
        return calculatedDuration;
    }
    
    public String getMuseumName() { return museum; }
    public String getGuideLanguage() { return guide; }
    public boolean hasAudio() { return audio; }
    public boolean hasExpertGuide() { 
        return museum != null && museum.contains("Эрмитаж") && !"русский".equals(guide);
    }
    public boolean recommendAudio() {
        return audio || !"русский".equals(guide);
    }

    @Override
    public String getType() {
        return "MuseumTour";
    }
}