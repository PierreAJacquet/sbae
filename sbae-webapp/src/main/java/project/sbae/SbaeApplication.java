package project.sbae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "project.sbae")
@EnableJpaRepositories(basePackages = "project.sbae.repository")
@EntityScan(basePackages = "project.sbae.entity")
public class SbaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbaeApplication.class, args);
    }
}
