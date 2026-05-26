package com.retail.rewards.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/rewards";
    }
 
    @Test
    void shouldFetchCustomerRewards() {
        var response = restTemplate.getForEntity(baseUrl() + "/101", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundForInvalidCustomer() {
        var response = restTemplate.getForEntity(baseUrl() + "/999", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
 
    @Test
    void shouldReturnResponseBodyForValidCustomer() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/101", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnJsonContentType() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/101", String.class);

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }

    @Test
    void shouldContainTotalRewardsInResponse() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/101", String.class);

        String body = response.getBody();

        assertNotNull(body);
        assertTrue(body.contains("totalRewards"));
    }

    @Test
    void shouldHandleInvalidPathVariableFormat() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/abc", String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnNotAllowedForPost() {
        ResponseEntity<String> response =
                restTemplate.postForEntity(baseUrl() + "/101", null, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void shouldReturnNotAllowedForPut() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl() + "/101",
                        HttpMethod.PUT,
                        entity,
                        String.class
                );

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void shouldReturnNotAllowedForDelete() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl() + "/101",
                        HttpMethod.DELETE,
                        entity,
                        String.class
                );

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void shouldHandleLargeCustomerId() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/999999999999", String.class);
        assertTrue(
                response.getStatusCode() == HttpStatus.NOT_FOUND
                        || response.getStatusCode() == HttpStatus.BAD_REQUEST
        );
    }

    @Test
    void shouldReturnConsistentResponseStructure() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl() + "/101", String.class);

        String body = response.getBody();

        assertNotNull(body);
        assertTrue(body.contains("{"));
        assertTrue(body.contains("}"));
    }
}
