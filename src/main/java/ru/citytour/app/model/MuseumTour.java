package ru.citytour.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "museum_tours")
@PrimaryKeyJoinColumn(name = "id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MuseumTour extends Excursion {

    private String museum;
    private String guide = "русский";
    private boolean audio = false;
    private boolean expertGuide = false;

    public MuseumTour(String name, int basePrice, int baseDuration, String museum,
                      String guide, boolean audio, boolean expertGuide) {
        this.setName(name);
        this.setBasePrice(basePrice);
        this.setBaseDuration(baseDuration);
        this.museum = museum;
        this.guide = guide != null ? guide : "русский";
        this.audio = audio;
        this.expertGuide = expertGuide;
    }

    @Override
    public void updatePriceAndDuration() {
        // Метод не используется
    }

    @Override
    public int calculateFinalPrice(int people) {
        int price = this.getBasePrice();
        
        // ЯЗЫК ГИДА: не русский +80 руб
        if (!"русский".equals(this.guide)) {
            price += 80;
        }
        
        // АУДИОГИД: +60 руб
        if (this.audio) {
            price += 60;
        }
        
        // ЭКСПЕРТ-ГИД: +200 руб
        if (this.expertGuide) {
            price += 200;
        }
        
        return price * people;
    }
    
    // МЕТОД ДЛЯ ПОЛУЧЕНИЯ ИТОГОВОЙ ЦЕНЫ (БЕЗ УМНОЖЕНИЯ НА КОЛИЧЕСТВО)
    public int getFinalPrice() {
        int price = this.getBasePrice();
        
        if (!"русский".equals(this.guide)) {
            price += 80;
        }
        if (this.audio) {
            price += 60;
        }
        if (this.expertGuide) {
            price += 200;
        }
        return price;
    }
    
    // МЕТОД ДЛЯ ПОЛУЧЕНИЯ ИТОГОВОЙ ДЛИТЕЛЬНОСТИ
    public int getFinalDuration() {
        int duration = this.getBaseDuration();
        
        if (this.expertGuide) {
            duration += 2;
        }
        return duration;
    }

    @Override
    public String getType() {
        return "MuseumTour";
    }
}