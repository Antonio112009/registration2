package com.registaration2.registration2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:database.properties")
public class Registration2Application {

	public static void main(String[] args) {
		SpringApplication.run(Registration2Application.class, args);
	}


}
