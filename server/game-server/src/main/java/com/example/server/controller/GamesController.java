package com.example.server.controller;


import com.example.model.*;

import org.slf4j.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;


public class GamesController {

    private static HashMap<Integer, GameInstance> games = new HashMap<Integer,GameInstance>();
    private static Random rand = new Random();

    /*
     * normally createGame() should be used, which creates a unique ID/key
     * but this can be used to create a specific static "game" to hold players, e.g. the game lobby
     */
    public static Integer createGame(Integer key) {

        // NOTE: there is only 1 game for this first iteration so could
        //		potentially re-use key/ID == GameDesignVars.GAME_LOBBY_ID
        GameInstance game = new GameInstance(GameInstance.GameState.GAME_LOBBY, key , null);
        games.put(key, game);
        return key;
    }

    public static Integer createGame() {
        Integer key = createNewKey();
        return createGame(key);
    }

    private static Integer createNewKey() {

        Integer key = rand.nextInt();
        while( games.containsKey(key) ) {
            key = rand.nextInt();
        }
        return key;
    }

    public Collection<GameInstance> getAllGames() {

        return games.values();
    }

    public GameInstance getGame(Integer ID) {

        return games.get(ID);
    }

    public GameInstance removeGame(Integer ID) {

        return games.remove(ID);
    }

    // Add/Update/Remove Players:

    public boolean addPlayer(Integer gameID, PlayerData player) {

        // enforce maximum players allowed into a game or lobby at a time
        GameInstance game = getGame(gameID);
        if( game.getAllPlayers().size() >= GameDesignVars.MAX_PLAYERS_PER_GAME ) {
            return false;
        }

        return game.addPlayer(player);
    }

    public boolean updatePlayer(Integer gameID, PlayerData player) {

        GameInstance game = getGame(gameID);
        return game.updatePlayer(player);
    }

    public boolean removePlayer(Integer gameID, Integer playerPublicID) {

        GameInstance game = getGame(gameID);
        return game.removePlayer(playerPublicID);
    }

}
