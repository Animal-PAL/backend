package project.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BackendApplication {

    public static void main(String[] args) {
        System.out.println("🔍 DB_HOST: " + System.getenv("DB_HOST"));
        System.out.println("🔍 DB_USERNAME: " + System.getenv("DB_USERNAME"));
        SpringApplication.run(BackendApplication.class, args);
    }

}
