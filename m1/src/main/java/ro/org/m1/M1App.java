package ro.org.m1;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class M1App {

	public static void main(String[] args) {
		SpringApplication.run(M1App.class, args);
	}

}
