package com.TimeTable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TimeTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeTableApplication.class, args);
		System.out.println("hello Sarkar");
	}
}

