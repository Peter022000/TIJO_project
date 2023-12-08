package com.example.TIJO_project;

import com.example.TIJO_project.config.EnableMongoTestServer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableMongoTestServer
public class TijoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TijoProjectApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper()
	{
		return new ModelMapper();
	}
}
