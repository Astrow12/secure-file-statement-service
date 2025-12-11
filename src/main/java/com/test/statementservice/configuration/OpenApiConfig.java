package com.test.statementservice.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Statement Api", description = "Demo account statement api to manage pdf account statements", contact =
@Contact(name = "Afonso"), version = "1"))
public class OpenApiConfig {
}
