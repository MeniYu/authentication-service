package io.incondensable.application.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.incondensable.application.web.dto.OtpGeneratedPayload;
import io.incondensable.application.web.dto.req.OtpGenerateRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author abbas
 */
@RestController("/otp")
public class OtpController {
    static final String ROUTING_KEY = "notification_key";
    static final String EXCHANGE = "meniyu_exchange_fanout";

    private final RabbitTemplate rabbitTemplate;

    public OtpController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestBody OtpGenerateRequestDTO dto) throws JsonProcessingException {
        Random random = new Random();
        int otpCode = random.nextInt(900000) + 100000;
        OtpGeneratedPayload payload = new OtpGeneratedPayload(
                dto.getUserId(), dto.getEmailAddress(), otpCode
        );

        ObjectMapper mapper = new ObjectMapper();
        String queryPayloadString = mapper.writeValueAsString(payload);

        rabbitTemplate.convertAndSend(EXCHANGE, "", queryPayloadString);

        System.out.println("OTP Object is sent: " + queryPayloadString);

        return ResponseEntity.ok(String.format("OTP Code successfully generated and sent to the User Email Address. OTP is: %d", otpCode));
    }

}
