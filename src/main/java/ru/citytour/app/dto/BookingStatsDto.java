package ru.citytour.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatsDto {
    private String excursionType;      // тип экскурсии или название
    private long bookingsCount;        // количество бронирований
    private int totalTicketsSold;      // всего продано билетов
    private int totalRevenue;          // общая выручка
}