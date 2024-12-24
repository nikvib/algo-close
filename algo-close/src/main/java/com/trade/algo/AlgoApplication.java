package com.trade.algo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan({ "com.trade.algo" })
@EnableMongoRepositories({ "com.trade.algo.repo" })
@SpringBootApplication
@EnableScheduling
public class AlgoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgoApplication.class, args);
	}

}
