package io.incondensable.application.business.messaging.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.domain.jms.AuthUserToOwnerServiceDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author abbas
 */
@Component
public class UserPublisher {

    @Value("${exchange.name}")
    private String exchange;
    @Value("${owner.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public UserPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUser(User user) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AuthUserToOwnerServiceDTO dto = new AuthUserToOwnerServiceDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getToken().getJwtString(),
                    user.getToken().getActiveTime()
            );
            mapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend(exchange, routingKey, dto);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
}
