package ru.kustikov.cakes.auth.requests;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import ru.kustikov.cakes.user.Role;

/**
 * Класс для запроса регистрации
 */
public record RegistrationRequest(
        @Email(message = "Почта должна быть корректной")
        String email,
        @Length(min = 5, message = "Никнейм должен быть не менее 5 символов")
        String username,
        @Length(min = 8, message = "Пароль должен быть не менее 8 символов")
        String password
) {
}
