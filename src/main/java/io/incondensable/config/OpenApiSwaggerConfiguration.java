package io.incondensable.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author abbas
 */
@Configuration
public class OpenApiSwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new io.swagger.v3.oas.models.info.Info()
                                .title("MeniYu Auth Service APIs")
                                .version("0.0.1")
                                .description("REST APIs for MeniYu Auth Project.")
                );
    }

}
