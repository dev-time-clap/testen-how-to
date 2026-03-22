package de.devtime.examples;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "de.devtime")
@EntityScan
@EnableJpaRepositories
public class TestApplication {

}
