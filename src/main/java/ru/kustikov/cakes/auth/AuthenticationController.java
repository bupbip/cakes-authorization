package ru.kustikov.cakes.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kustikov.cakes.auth.requests.AuthenticationRequest;
import ru.kustikov.cakes.auth.requests.RegistrationRequest;
import ru.kustikov.cakes.auth.responses.RegistrationResponse;
import ru.kustikov.cakes.config.JwtService;
import ru.kustikov.cakes.exception.UserExistException;
import ru.kustikov.cakes.validation.ResponseErrorValidation;

/**
 * Контроллер для аутентификации
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ResponseErrorValidation responseErrorValidation;
    private final JwtService jwtService;

    /**
     * Регистрация пользователя
     *
     * @param request       - запрос на регистрацию
     * @param bindingResult - результат валидации
     * @return - ответ с результатом регистрации
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegistrationRequest request,
            BindingResult bindingResult
    ) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        try {
            RegistrationResponse response = authenticationService.register(request);
            return ResponseEntity.ok(response);
        } catch (UserExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistrationResponse("Пользователь с таким email уже существует"));
        }
    }

    /**
     * Аутентификация пользователя
     *
     * @param request       - запрос на аутентификацию
     * @param bindingResult - результат валидации
     * @return - ответ с результатом аутентификации
     */
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {
//        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
//        if (!ObjectUtils.isEmpty(errors)) return errors;
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
