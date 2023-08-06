package io.incondensable.application.business.service.otp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.incondensable.application.business.domain.entity.Otp;
import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.exceptions.auth.GivenOtpExpiredException;
import io.incondensable.application.business.exceptions.auth.GivenOtpMismatchException;
import io.incondensable.application.business.exceptions.auth.OtpTriesFinishedException;
import io.incondensable.application.business.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * @author abbas
 */
@Service
@PropertySource({"classpath:rabbitmq-info.properties"})
public class OtpService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${notifications.routing.key}")
    private String ROUTING_KEY;
    @Value("${exchange.name}")
    private String EXCHANGE_NAME;

    public OtpService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOtpCode(User user) {
        user.setOtp(new Otp(
                generateOtpCode(),
                Instant.now(),
                3
        ));
        userRepository.save(user);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String otpPayloadString = mapper.writeValueAsString(user.getOtp());
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, otpPayloadString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void validateOtpCode(User user, int enteredOtpCode) {
        if (user.getOtp().getRemainingTries() == 0)
            throw new OtpTriesFinishedException();

        if (user.getOtp().getCode() != enteredOtpCode) {
            user.getOtp().setRemainingTries(user.getOtp().getRemainingTries() - 1);
            userRepository.save(user);
            throw new GivenOtpMismatchException(enteredOtpCode, user.getOtp().getRemainingTries());
        }

        final Instant activatedTime = Instant.now().plus(10, ChronoUnit.MINUTES);
        if (user.getOtp().getCreatedAt().isAfter(activatedTime)) {
            user.setOtp(null);
            userRepository.save(user);
            throw new GivenOtpExpiredException(enteredOtpCode);
        }
    }

    private int generateOtpCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

}
