package edu.co.sergio.arboleda.smartparkingapi;

import java.time.Clock;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@Configuration
@OpenAPIDefinition
@SpringBootApplication
public class SmartParkingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartParkingApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	@Autowired
	public void configureEncoder(AuthenticationManagerBuilder auth,
								 PasswordEncoder passwordEncoder,
								 UserDetailsService jwtUserDetailsService) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
	}

}
