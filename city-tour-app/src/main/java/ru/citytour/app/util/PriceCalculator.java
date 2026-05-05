package ru.citytour.app.util;

import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    public int calculateCityTourPrice(int basePrice, String transport, boolean meal, boolean hasGuide) {
        int price = basePrice;
        if ("автобус".equals(transport)) price += 100;
        if (meal) price += 150;
        if (hasGuide) price += price * 20 / 100;
        return price;
    }
}