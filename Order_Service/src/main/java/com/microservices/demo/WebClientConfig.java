package com.microservices.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

@Configuration
public class WebClientConfig {

	  @Bean
	  
	    public WebClient.Builder webClientBuilder() {
	        return WebClient.builder(); // Specify the base URL here
	    }

	public static UriSpec<?> get() {
		// TODO Auto-generated method stub
		return null;
	}
}


