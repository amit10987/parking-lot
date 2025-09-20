package com.assessment.parkinglot;

import org.springframework.boot.SpringApplication;

public class TestParkingLotApplication {

	public static void main(String[] args) {
		SpringApplication.from(ParkingLotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
