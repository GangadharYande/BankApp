package com.boii.bankapp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Banking App",
                description = "Backend Rest APIs for Banking App",
                version = "v1.0",
                contact = @Contact(
                        name = "Gangadhar Yande",
                        email = "yande.gangadhar@gmail.com",
                        url="https://github.com/GangadharYande/BankApp"
                ),
                license = @License(
                        name = "Banking_App",
                        url="https://github.com/GangadharYande/BankApp"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Bank_App Documentation",
                url="https://github.com/GangadharYande/BankApp"
        )
)
public class BankAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(BankAppApplication.class, args);
    }

}
