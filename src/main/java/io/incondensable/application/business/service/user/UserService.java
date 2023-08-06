package io.incondensable.application.business.service.user;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.domain.jms.AuthUserToOwnerServiceDTO;
import io.incondensable.application.business.exceptions.auth.UserNotFoundWithEmailException;
import io.incondensable.application.business.exceptions.user.UserDuplicateWithUsernameException;
import io.incondensable.application.business.exceptions.user.UserNotFoundWithIdException;
import io.incondensable.application.business.repository.UserRepository;
import io.incondensable.application.mapper.UserMapper;
import io.incondensable.application.web.dto.req.user.UserCreateRequestDTO;
import io.incondensable.application.web.dto.req.user.UserUpdateRequestDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author abbas
 */
@Service
@PropertySource({"classpath:rabbitmq-info.properties"})
public class UserService {

    @Value("${exchange.name}")
    private String exchange;

    @Value("${owner.routing.key}")
    private String routingKey;

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, RabbitTemplate rabbitTemplate) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
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

    public UserResponseDTO createUser(UserCreateRequestDTO req) {
        validateUsernameAndEmail(req.getUsername(), req.getEmail());
        User user = userMapper.mapDtoToEntity(req);

        String encodedPassword = passwordEncoder.encode(req.getPassword());
        user.setPassword(encodedPassword);

        User persistedUser = userRepository.save(user);
        rabbitTemplate.convertAndSend(exchange, routingKey, new AuthUserToOwnerServiceDTO(
                persistedUser.getId(),
                persistedUser.getEmail(),
                persistedUser.getToken().getJwtString(),
                persistedUser.getToken().getActiveTime()
        ));
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
        rabbitTemplate.convertAndSend(exchange, routingKey, new AuthUserToOwnerServiceDTO(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getToken().getJwtString(),
                updatedUser.getToken().getActiveTime()
        ));
        return userMapper.mapEntityToDto(updatedUser);
    }

    public User changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
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
