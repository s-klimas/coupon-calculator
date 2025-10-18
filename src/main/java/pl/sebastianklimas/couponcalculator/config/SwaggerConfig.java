package pl.sebastianklimas.couponcalculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Coupon calculator REST API")
                                .description("Here you can try out and better prepare to integrate with frontend.")
                                .version("1.0").contact(
                                        new Contact()
                                                .name("Sebastian Klimas")
                                                .email("sebastianklimas@op.pl")
                                                .url("https://sebastianklimas.pl")));
    }
}
