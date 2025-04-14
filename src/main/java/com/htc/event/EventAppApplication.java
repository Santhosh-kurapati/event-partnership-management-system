package com.htc.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@EnableAspectJAutoProxy
@PropertySource("classpath:config.properties")
@SpringBootApplication(scanBasePackages = "com.htc.event")
public class EventAppApplication implements CommandLineRunner {
 
	public static void main(String[] args) {
		SpringApplication.run(EventAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
 
	}

}
