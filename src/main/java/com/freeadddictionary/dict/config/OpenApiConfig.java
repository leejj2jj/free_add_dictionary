package com.freeadddictionary.dict.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${app.url:http://localhost:8888}")
  private String appUrl;

  @Bean
  public OpenAPI dictionaryOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl(appUrl);
    localServer.setDescription("로컬 서버");

    Contact contact = new Contact().name("개발팀").email("support@freeadddictionary.com");

    License license = new License().name("MIT License").url("https://opensource.org/licenses/MIT");

    Info info =
        new Info()
            .title("Free Add Dictionary API")
            .description("오픈 사전 서비스의 API 문서입니다.")
            .version("1.0.0")
            .contact(contact)
            .license(license);

    return new OpenAPI().info(info).servers(List.of(localServer));
  }
}
