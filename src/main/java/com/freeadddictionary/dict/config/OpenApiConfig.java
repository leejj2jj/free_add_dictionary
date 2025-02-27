package com.freeadddictionary.dict.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI dictionaryOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Free Add Dictionary API")
                .description("API documentation for the Free Add Dictionary application")
                .version("v1.0.0")
                .contact(
                    new Contact().name("Dictionary Team").email("support@freeadddictionary.com")));
  }
}
