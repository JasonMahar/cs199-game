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

    @BeforeAll
    static void setup() {

        headers = new HttpHeaders();
        entity = new HttpEntity<PlayerData>(new TemplePlayerData(), headers);

    }



}
