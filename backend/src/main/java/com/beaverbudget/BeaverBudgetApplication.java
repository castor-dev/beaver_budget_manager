package com.beaverbudget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class BeaverBudgetApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BeaverBudgetApplication.class, args);
        System.out.println("🟢 Flyway bean: " + ctx.containsBean("flyway"));
    }
}

