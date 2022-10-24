package com.example.game.server.controller;

import com.example.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlayersController playersController;

    @MockBean
    private GamesController gamesController;

    @Autowired
    private JacksonTester<PlayerData> jsonPlayerData;


    @Autowired
    private JacksonTester<GameInstance> jsonGameInstance;

    @Test
    public void canRetrieveByIdWhenExists() throws Exception {

        Map<Integer, PlayerData> map = Map.of(2, new TemplePlayerData("Stew"));
        // given
        given(gamesController.getGame(2))
                .willReturn(new GameInstance(
                        GameInstance.GameState.GAME_LOBBY,
                        2,
                        map));

        // when
        MockHttpServletResponse response = mvc.perform(
                        get("/game/2")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonPlayerData.write(new TemplePlayerData("Stew")).getJson()
        );
    }
}
