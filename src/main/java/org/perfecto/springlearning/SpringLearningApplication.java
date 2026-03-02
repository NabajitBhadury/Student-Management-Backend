package org.perfecto.springlearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearningApplication.class, args);
    }

}
