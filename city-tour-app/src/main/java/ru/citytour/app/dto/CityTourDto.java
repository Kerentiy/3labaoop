package ru.citytour.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityTourDto {

    private Long id;

    @NotBlank(message = "Название обязательно")
    private String name;

    @Min(100) @Max(100000)
    private int basePrice;

    @Min(1) @Max(24)
    private int baseDuration;

    private String transport = "пешая";
    private boolean meal = false;
    private boolean hasGuide = false;
}