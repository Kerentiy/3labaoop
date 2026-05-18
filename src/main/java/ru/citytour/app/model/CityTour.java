package ru.citytour.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "city_tours")
@PrimaryKeyJoinColumn(name = "id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CityTour extends Excursion {

    private String transport = "пешая";
    private boolean meal = false;
    private boolean hasGuide = false;

    public CityTour(String name, int basePrice, int baseDuration, String transport, boolean meal, boolean hasGuide) {
        this.setName(name);
        this.setBasePrice(basePrice);
        this.setBaseDuration(baseDuration);
        this.transport = transport != null ? transport : "пешая";
        this.meal = meal;
        this.hasGuide = hasGuide;
    }

    @Override
    public void updatePriceAndDuration() {
        // Метод не используется, расчёт происходит в calculateFinalPrice
    }

    @Override
    public int calculateFinalPrice(int people) {
        // БАЗОВАЯ ЦЕНА
        int price = this.getBasePrice();
        
        // ТРАНСПОРТ: автобус +100 руб
        if ("автобус".equals(this.transport)) {
            price += 100;
        }
        
        // ОБЕД: +150 руб
        if (this.meal) {
            price += 150;
        }
        
        // ГИД: +20% к цене
        if (this.hasGuide) {
            price = price + (price * 20 / 100);
        }
        
        // УМНОЖАЕМ НА КОЛИЧЕСТВО ЧЕЛОВЕК
        return price * people;
    }
    
    // МЕТОД ДЛЯ ПОЛУЧЕНИЯ ИТОГОВОЙ ЦЕНЫ (БЕЗ УМНОЖЕНИЯ НА КОЛИЧЕСТВО)
    public int getFinalPrice() {
        int price = this.getBasePrice();
        
        if ("автобус".equals(this.transport)) {
            price += 100;
        }
        if (this.meal) {
            price += 150;
        }
        if (this.hasGuide) {
            price = price + (price * 20 / 100);
        }
        return price;
    }
    
    // МЕТОД ДЛЯ ПОЛУЧЕНИЯ ИТОГОВОЙ ДЛИТЕЛЬНОСТИ
    public int getFinalDuration() {
        int duration = this.getBaseDuration();
        
        if ("автобус".equals(this.transport)) {
            duration += 1;
        }
        if (this.meal) {
            duration += 1;
        }
        if (this.hasGuide) {
            duration += 2;
        }
        return duration;
    }

    @Override
    public String getType() {
        return "CityTour";
    }
}