package io.incondensable.application.business.service.user;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.UserNotFoundWithEmailException;
import io.incondensable.application.business.exceptions.user.UserDuplicateWithUsernameException;
import io.incondensable.application.business.exceptions.user.UserNotFoundWithIdException;
import io.incondensable.application.business.messaging.publishers.UserPublisher;
import io.incondensable.application.business.repository.UserRepository;
import io.incondensable.application.business.service.otp.OtpService;
import io.incondensable.application.mapper.UserMapper;
import io.incondensable.application.web.dto.req.user.UserCreateRequestDTO;
import io.incondensable.application.web.dto.req.user.UserUpdateRequestDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author abbas
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final OtpService otpService;
    private final UserPublisher userPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, OtpService otpService, UserPublisher userPublisher, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.otpService = otpService;
        this.userPublisher = userPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundWithIdException(userId);
                }
        );
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> {
                    throw new UserNotFoundWithEmailException(email);
                }
        );
    }

    /**
     * The User created using this for signup, must be Activated later.
     *
     * @param req the Data required for Signup flow
     * @return the INACTIVE User
     */
    public UserResponseDTO createUser(UserCreateRequestDTO req) {
        validateUsernameAndEmail(req.getUsername(), req.getEmail());
        User user = userMapper.mapDtoToEntity(req);

        String encodedPassword = passwordEncoder.encode(req.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(false); // The User is created in Signup Flow but must be Activated using the

        User persistedUser = userRepository.save(user);
        otpService.sendOtpCode(persistedUser, "Forgot Password");
//        userPublisher.sendUser(persistedUser);
        return userMapper.mapEntityToDto(persistedUser);
    }

    public UserResponseDTO updateUser(String userId, UserUpdateRequestDTO req) {
        User user = getById(userId);
        validateUsernameAndEmail(req.getUsername(), req.getEmail());
        String oldPassword = user.getPassword();

        userMapper.setMappedDtoToUser(user, req);

        if (!passwordEncoder.matches(req.getPassword(), oldPassword))
            user.setPassword(passwordEncoder.encode(req.getPassword()));

        User updatedUser = userRepository.save(user);
        userPublisher.sendUser(updatedUser);
        return userMapper.mapEntityToDto(updatedUser);
    }

    public User changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User activateUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private void validateUsernameAndEmail(String username, String email) {
        userRepository.findByUsername(username).ifPresent(
                u -> {
                    throw new UserDuplicateWithUsernameException(username);
                }
        );
        userRepository.findByEmail(email).ifPresent(
                u -> {
                    throw new UserDuplicateWithUsernameException(email);
                }
        );
    }

}
