package ru.citytour.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long excursionId;
    private String excursionName;
    private int peopleCount;
    private LocalDateTime bookingDate;
    private int totalPrice;
}