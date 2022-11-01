package com.example.game.server.controller;

import com.example.model.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.http.*;

import java.net.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

public class GameInstanceTest {
    static HttpHeaders headers = new HttpHeaders();
    static HttpEntity<PlayerData> entity = new HttpEntity<PlayerData>(new TemplePlayerData(), headers);
    static int port;
    static TestRestTemplate restTemplate;

    @BeforeAll
    static void setup() {
        port = 8080;
        headers = new HttpHeaders();
        entity = new HttpEntity<PlayerData>(new TemplePlayerData(), headers);
        restTemplate = new TestRestTemplate();
    }

    private GamesController gamesController;


    @Test
    public void createGameTest() {

        PlayerData playerData = new TemplePlayerData("Stew");

        HttpEntity<PlayerData> entity = new HttpEntity<PlayerData>(playerData, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/game"), HttpMethod.POST, entity, String.class);

        String actual = Objects.requireNonNull(
                response.getHeaders().get(HttpHeaders.LOCATION)).get(0);

        assertTrue(actual.contains("/game"));
        assertEquals("Hello Controller", response.getBody());

    }

    @Test
    public void addPlayerTest() {

        PlayerData playerData = new TemplePlayerData("Stew");

        HttpEntity<PlayerData> entity = new HttpEntity<PlayerData>(playerData, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/player"), HttpMethod.POST, entity, String.class);

        String actual = Objects.requireNonNull(
                response.getHeaders().get(HttpHeaders.LOCATION)).get(0);

        assertTrue(actual.contains("/game"));
        assertEquals("Hello Controller", response.getBody());

    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void getHello() throws Exception {

        ResponseEntity<String> response = restTemplate.getForEntity(new URL("http://localhost:" + port + "/game").toString(), String.class);
        assertEquals("Hello Controller", response.getBody());

    }

}
