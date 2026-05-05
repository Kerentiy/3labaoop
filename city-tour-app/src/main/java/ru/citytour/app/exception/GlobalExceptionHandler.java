package ru.citytour.app.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDuplicateKey(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("error", "Ошибка: Экскурсия с таким названием уже существует! Используйте другое имя.");
        model.addAttribute("tours", null);
        return "index";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("tours", null);
        return "index";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "Произошла ошибка: " + ex.getMessage());
        model.addAttribute("tours", null);
        return "index";
    }
}