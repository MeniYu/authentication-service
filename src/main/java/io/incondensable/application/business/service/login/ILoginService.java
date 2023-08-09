package io.incondensable.application.business.service.login;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.PasswordMismatchException;
import io.incondensable.application.business.exceptions.auth.UserNotFoundWithUsernameException;
import io.incondensable.application.business.repository.UserRepository;
import io.incondensable.application.business.service.jwt.TokenService;
import io.incondensable.application.web.dto.req.auth.UserLoginRequestDTO;
import io.incondensable.security.jwt.JwtUtil;
import io.incondensable.security.userdetails.MeniYuUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author abbas
 */
@Service
public final class ILoginService implements LoginService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passEnc;
    private final TokenService tokenService;
    private final AuthenticationManager authMng;

    public ILoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authMng, JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepository;
        this.passEnc = passwordEncoder;
        this.authMng = authMng;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<User> validateUser(UserLoginRequestDTO dto) {
        User user;
//        if (isUserInputEmail(loginInfo.username()))
//            user = userRepo.findByEmail(loginInfo.getUserStringIdentity())
//                    .orElseThrow(
//                            () -> {
//                                throw new UserNotFoundWithEmailException(loginInfo.getUserStringIdentity());
//                            }
//                    );
//        else
        user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(
                        () -> {
                            throw new UserNotFoundWithUsernameException(dto.getUsername());
                        }
                );

        if (!passEnc.matches(dto.getPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        return Optional.of(user);
    }

    @Override
    public User login(User user) {
        Authentication auth = authMng.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        MeniYuUserDetails userDetails = (MeniYuUserDetails) auth.getPrincipal();
        String jwt = jwtUtil.generateJwtToken(userDetails);

        if (user.getToken() == null) {
            tokenService.setUserTokenToNull(user);
        } else {
            tokenService.increaseTokenActivationTime(user);
        }
        userRepo.save(user);

        return user;
    }

    /**
     * @param in this is the incoming value from DTO whether it is a Username or Passowrd
     * @return if the value is determined to be a Username, the return value is false, otherwise it is true
     */
    private boolean isUserInputEmail(String in) {
        return in.contains("@"); //it contains @ so it is Email address so the return value is TRUE.
    }

}
