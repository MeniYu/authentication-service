package io.incondensable.application.business.messaging.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.incondensable.application.business.domain.jms.to_notif.GeneratedOtpDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author abbas
 */
@Component
public class OtpPublisher {

    @Value("${notifications.routing.key}")
    private String ROUTING_KEY;
    @Value("${exchange.name}")
    private String EXCHANGE_NAME;

    private final RabbitTemplate rabbitTemplate;

    public OtpPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOtp(GeneratedOtpDTO otp) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String otpPayloadString = mapper.writeValueAsString(otp);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, otpPayloadString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
