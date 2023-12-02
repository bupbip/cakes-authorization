package ru.kustikov.cakes.auth.requests;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * Класс для запроса аутентификации
 */
public record AuthenticationRequest(
        @Email(message = "Почта должна быть корректной")
        String email,
        @Length(min = 8, message = "Пароль должен быть не менее 8 символов")
        String password
) {
}
