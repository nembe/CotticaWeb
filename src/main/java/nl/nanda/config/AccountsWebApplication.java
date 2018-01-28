package nl.nanda.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@SpringBootApplication
@ComponentScan(basePackages = { "nl.nanda.web" })
@EntityScan("nl.nanda")
@Import({ AccountsConfig.class, SystemConfig.class })
public class AccountsWebApplication {

    public static void main(final String[] args) {
        SpringApplication.run(AccountsWebApplication.class, args);
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        final SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return multicaster;
    }

}
