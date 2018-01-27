package nl.nanda.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
// @ComponentScan("nl.nanda")
// @EnableJpaRepositories("nl.nanda.*")
@ComponentScan(basePackages = { "nl.nanda.*" })
@EntityScan("nl.nanda.*")
// @EnableAutoConfiguration
@Import({ AccountsConfig.class, SystemConfig.class })
public class AccountsWebApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AccountsWebApplication.class, args);
    }

}
