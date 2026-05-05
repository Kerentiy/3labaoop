package ru.citytour.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MuseumTourDto {

    private Long id;

    @NotBlank(message = "Название обязательно")
    private String name;

    @Min(100) @Max(100000)
    private int basePrice;

    @Min(1) @Max(24)
    private int baseDuration;

    @NotBlank
    private String museum;

    private String guide = "русский";
    private boolean audio = false;
    private boolean expertGuide = false;
}