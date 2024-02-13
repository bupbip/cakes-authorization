package ru.kustikov.cakes.auth.responses;

import ru.kustikov.cakes.user.User;

/**
 * Класс для ответа аутентификации
 */
public record AuthenticationResponse (
        User user,
        String token,
        String message
) {
}