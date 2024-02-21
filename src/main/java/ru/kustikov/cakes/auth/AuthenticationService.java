package ru.kustikov.cakes.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kustikov.cakes.auth.requests.AuthenticationRequest;
import ru.kustikov.cakes.auth.requests.RegistrationRequest;
import ru.kustikov.cakes.auth.responses.AuthenticationResponse;
import ru.kustikov.cakes.auth.responses.RegistrationResponse;
import ru.kustikov.cakes.config.JwtService;
import ru.kustikov.cakes.exception.UserExistException;
import ru.kustikov.cakes.user.Role;
import ru.kustikov.cakes.user.User;
import ru.kustikov.cakes.user.UserRepository;

/**
 * Сервис для аутентификации и регистрации
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request - запрос на регистрацию
     * @return - ответ с результатом регистрации
     */
    public RegistrationResponse register(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role() ? Role.ROLE_CONFECTIONER : Role.ROLE_CUSTOMER);

        try {
            LOG.info("Saving user {}", user.getEmail());
            userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("Пользователь с таким email уже существует");
        }
        return new RegistrationResponse("Успешная регистрация.");
    }

    /**
     * Аутентификация пользователя
     *
     * @param request - запрос на аутентификацию
     * @return - ответ с результатом аутентификации
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (Exception e) {
            LOG.error("Error during authentication. {}", e.getMessage());
            return new AuthenticationResponse(
                    null,
                    null,
                    "Почта или пароль не верны. Попробуйте ещё раз.");
        }

        var jwtToken = jwtService.generateToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthenticationResponse((User) authentication.getPrincipal(), jwtToken, "Успешный вход!");
    }

}
