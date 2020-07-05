package ro.org.m2;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class M2App {

	public static void main(String[] args) {
		SpringApplication.run(M2App.class, args);
	}

}
