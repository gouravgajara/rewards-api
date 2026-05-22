package com.retail.rewards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardIntegrationTest {
 @LocalServerPort int port;
 @Autowired TestRestTemplate restTemplate;
 @Test void shouldFetchCustomerRewards(){ var response=restTemplate.getForEntity("http://localhost:"+port+"/api/rewards/101",String.class); assertEquals(HttpStatus.OK,response.getStatusCode());}
 @Test void shouldReturnNotFoundForInvalidCustomer(){ var response=restTemplate.getForEntity("http://localhost:"+port+"/api/rewards/999",String.class); assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());}
}
