package com.example.game.server.controller;


import com.example.game.server.exceptions.*;
import com.example.model.*;

import com.example.util.*;
import org.slf4j.*;

import java.util.*;

public class GamesController {

    private static final Logger logger;

    protected static GameInstance GAME_INSTANCE;
    private static HashMap<Integer, GameInstance> games;

    static {
        PrintUtils.cyan(String.format("class/logger = %s", GamesController.class.toString()));

        logger = LoggerFactory.getLogger(GamesController.class);

        if(games == null){
            games = new HashMap<>();
            PrintUtils.red("GamesController.static: Initializing new Map<Integer, GameInstance> ");
        }
    }

    /*
     * normally createGame() should be used, which creates a unique ID/key
     * but this can be used to create a specific static "game" to hold players, e.g. the game lobby
     */
    public static Integer createGame(Integer key) throws Exception {

        // NOTE: there is only 1 game for this first iteration so could
        //		potentially re-use key/ID == GameDesignVars.GAME_LOBBY_ID

        GameInstance game = new GameInstance(GameInstance.GameState.GAME_LOBBY, key , null);
        games.put(key, game);
        return key;
    }

    public static Integer createGame() throws Exception {
        Integer key = createNewKey();

        return createGame(key);
    }

    private static Integer createNewKey() {
        int key = 1;
        while(games.containsKey(key) ) {
            key = new Random().nextInt();
        }
        return key;
    }


    public static Integer createNewGame() throws Exception {

        Integer key = createNewKey();

        return createGame(key);
    }

    public Collection<GameInstance> getAllGames() {
        if(games == null || games.size() == 0) {
            return null;
        }
        return games.values();
    }

    public GameInstance getGame(Integer ID) {
        GameInstance game = games.get(ID);
        return games.get(ID);
    }

    public GameInstance removeGame(Integer ID) {

        return games.remove(ID);
    }

    // Add/Update/Remove Players:

    public boolean addPlayer(Integer gameID, PlayerData player) throws Exception {

        // enforce maximum players allowed into a game or lobby at a time
        GameInstance game = getGame(gameID);

        if(game.getPlayers().size() >= GameDesignVars.MAX_PLAYERS_PER_GAME ) {
            return false;
        }

        boolean added = game.addPlayer(player);

        return game.addPlayer(player);
    }

    public boolean updatePlayer(Integer gameID, PlayerData player) throws Exception {

        GameInstance game = getGame(gameID);
        return game.updatePlayer((TemplePlayerData) player);
    }

    public boolean removePlayer(Integer gameID, Integer playerPublicID) {

        GameInstance game = getGame(gameID);
        return game.removePlayer(playerPublicID);
    }

}
