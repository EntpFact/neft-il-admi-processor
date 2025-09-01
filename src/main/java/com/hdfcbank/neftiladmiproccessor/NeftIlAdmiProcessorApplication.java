package com.hdfcbank.neftiladmiproccessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.hdfcbank"
})
public class NeftIlAdmiProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeftIlAdmiProcessorApplication.class, args);
	}

}
