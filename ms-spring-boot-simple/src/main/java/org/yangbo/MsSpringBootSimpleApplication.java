package org.yangbo;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsSpringBootSimpleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSpringBootSimpleApplication.class, args);
	}
	
	//@Bean
	public CommandLineRunner runner(ApplicationContext context){
		return args -> {
			System.out.println(args);
			System.out.println("Let's inspect spring boot beans.");
			String[] beanNames = context.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String bean : beanNames) {
				System.out.println(bean);
			}
		};
	}
}
