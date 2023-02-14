package ru.spring.restfull.config;

import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.spring.restfull.model.Authorities;
import ru.spring.restfull.model.User;
import ru.spring.restfull.repository.UserRepository;

import java.util.List;

/**
 * Класс для:<br>
 * а) предварительной загрузки данных, при хранении данных in MEMORY:H2 Hibernate<br>
 * б) 1-й загрузки в mySQL (создание таблицы),
 * после создания таблицы переключить userTable.data.preload=false  в application.properties
 */
@Log
@Configuration
public class LoadDataBase {

    @Bean
    @ConditionalOnProperty(name = "userTable.data.preload", havingValue = "true")
    CommandLineRunner initDatabase(UserRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new User("user1", "admin",
                    List.of(Authorities.READ, Authorities.WRITE, Authorities.DELETE))));
            log.info("Preloading " + repository.save(new User("user1", "password2",
                    List.of(Authorities.WRITE))));
            log.info("Preloading " + repository.save(new User("user1", "password3",
                    List.of(Authorities.READ,Authorities.DELETE))));
            log.info("Preloading " + repository.save(new User("user2", "admin",
                    List.of(Authorities.READ, Authorities.DELETE, Authorities.WRITE))));
            log.info("Preloading " + repository.save(new User("user2", "password2",
                    List.of(Authorities.READ, Authorities.DELETE))));
            log.info("Preloading " + repository.save(new User("user2", "password3",
                    List.of(Authorities.READ))));

        };
    }

    ;
}

