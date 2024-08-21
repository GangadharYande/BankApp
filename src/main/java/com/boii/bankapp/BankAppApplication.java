package com.boii.bankapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "The Banking_App",
                description = "Backend Rest APIs for Banking App",
                version = "v1.0",
                contact = @Contact(
                        name = "Gangadhar yande",
                        email = "yande.gangadhar@gmail.com",
                        url="http://github.com/gangahar"
                )
        )
)
public class BankAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(BankAppApplication.class, args);
    }

}
