package io.incondensable.application.business.service.otp;

import io.incondensable.application.business.domain.entity.Otp;
import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.domain.jms.to_notif.GeneratedOtpDTO;
import io.incondensable.application.business.exceptions.auth.GivenOtpExpiredException;
import io.incondensable.application.business.exceptions.auth.GivenOtpMismatchException;
import io.incondensable.application.business.exceptions.auth.OtpTriesFinishedException;
import io.incondensable.application.business.messaging.publishers.OtpPublisher;
import io.incondensable.application.business.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * @author abbas
 */
@Service
public class OtpService {

    private final UserRepository userRepository;
    private final OtpPublisher otpPublisher;

    public OtpService(UserRepository userRepository, OtpPublisher otpPublisher) {
        this.userRepository = userRepository;
        this.otpPublisher = otpPublisher;
    }

    public void sendOtpCode(User user, String subject) {
        user.setOtp(new Otp(
                generateOtpCode(),
                Instant.now(),
                3
        ));
        userRepository.save(user);

        GeneratedOtpDTO dto = new GeneratedOtpDTO(subject, user.getId(), user.getEmail(), user.getOtp().getCode());
        otpPublisher.sendOtp(dto);
    }

    public void validateOtpCode(User user, int enteredOtpCode) {
        otpTryCountIsZero(user);

        if (user.getOtp().getCode() != enteredOtpCode) {
            user.getOtp().setRemainingTries(user.getOtp().getRemainingTries() - 1);
            otpTryCountIsZero(user);
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

    private void otpTryCountIsZero(User user) {
        if (user.getOtp().getRemainingTries() == 0) {
            user.setOtp(null);
            userRepository.save(user);
            throw new OtpTriesFinishedException();
        }
    }

}
