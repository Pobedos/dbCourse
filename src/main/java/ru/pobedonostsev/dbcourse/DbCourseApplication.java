package ru.pobedonostsev.dbcourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class DbCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbCourseApplication.class, args);
    }

}
