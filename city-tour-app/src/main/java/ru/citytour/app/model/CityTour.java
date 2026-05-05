package ru.citytour.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "city_tours")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CityTour extends Excursion {

    private String transport = "пешая";
    private boolean meal = false;
    private boolean hasGuide = false;
    
    // Временные поля для хранения рассчитанных значений
    @Transient
    private int calculatedPrice;
    @Transient
    private int calculatedDuration;

    public CityTour(String name, int basePrice, int baseDuration, String transport, boolean meal, boolean hasGuide) {
        this.setName(name);
        this.setBasePrice(basePrice);
        this.setBaseDuration(baseDuration);
        this.transport = transport != null ? transport : "пешая";
        this.meal = meal;
        this.hasGuide = hasGuide;
        updatePriceAndDuration();
    }

    @Override
    public void updatePriceAndDuration() {
        int price = getBasePrice();
        int duration = getBaseDuration();

        // Логика как в C++
        if ("автобус".equals(transport)) {
            price += 100;
            duration += 1;
        }
        if (meal) {
            price += 150;
            duration += 1;
        }
        if (hasGuide) {
            int guideIncrease = price * 20 / 100;
            price += guideIncrease;
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
    
    public String getRoute() {
        if (getName().contains("Москва")) {
            return "Красная площадь - Кремль - Арбат";
        } else if (getName().contains("Казань")) {
            return "Казанский Кремль - Башня Сююмбике - Улица Баумана";
        }
        return "Стандартный маршрут";
    }
    
    public String getTransportType() { return transport; }
    public boolean needsBus() { return "автобус".equals(transport); }
    public boolean hasMeal() { return meal; }
    public boolean hasGuide() { return hasGuide; }

    @Override
    public String getType() {
        return "CityTour";
    }
}