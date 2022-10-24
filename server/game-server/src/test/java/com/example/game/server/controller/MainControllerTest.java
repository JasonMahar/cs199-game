package com.example.game.server.controller;

import com.example.model.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainControllerTest {

    @MockBean
    private GamesController gamesController;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void createGameTest() throws Exception {
        TemplePlayerData playerData =  new TemplePlayerData(1, 1, "Stew");

        ResponseEntity<GameInstance> response = restTemplate.postForEntity("/game/", playerData,
                GameInstance.class);


        GameInstance expectedGameInstance = new GameInstance(
                GameInstance.GameState.GAME_LOBBY,
                1,
                Map.of(1, new TemplePlayerData(1, 1, "Stew")
                ));


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);

        assertThat(response.getBody()).isNotNull();


        String json = new ObjectMapper().writer().writeValueAsString(expectedGameInstance);
        String actual = new ObjectMapper().writer().writeValueAsString(response.getBody());

        assertThat(json).isEqualTo(actual);

        assertThat(response.getBody().getAllPlayers().stream().toList().get(0))
                .isEqualTo(expectedGameInstance.getAllPlayers().stream().toList().get(0));
    }

    @Test
    public void createPlayerTest() throws Exception {
        TemplePlayerData playerData =  new TemplePlayerData(1, 1, "Stew");

        // when
        ResponseEntity<TemplePlayerData> response = restTemplate.postForEntity("/player?name=Stew", playerData,
                TemplePlayerData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(playerData.getName()).isEqualTo(response.getBody().getName());
        assertThat(playerData.getPrivateID()).isEqualTo(response.getBody().getPrivateID());
    }


    @Test
    public void shouldNotJoinTest() throws Exception {

        // given
        given(gamesController.getGame(2))
                .willReturn(new GameInstance(
                        GameInstance.GameState.GAME_LOBBY,
                        2,
                        Map.of(2, new TemplePlayerData("Stew")
                        )
                ));

        // when
        ResponseEntity<GameInstance> response = restTemplate.getForEntity("/game/2", GameInstance.class);


        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isNull();

    }
}
