package com.daria.javatemplate.admin;

import com.daria.javatemplate.BasePackageLocation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackageClasses = BasePackageLocation.class)
@EntityScan(basePackageClasses = BasePackageLocation.class)
@EnableJpaRepositories(basePackageClasses = BasePackageLocation.class)
@EnableScheduling
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }
}