package com.example.server.controller;


import com.example.model.*;

import com.example.util.*;
import org.slf4j.*;

import java.util.*;

public class GamesController {

    private static final Logger logger;

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
    public static Integer createGame(Integer key) {

        // NOTE: there is only 1 game for this first iteration so could
        //		potentially re-use key/ID == GameDesignVars.GAME_LOBBY_ID
        GameInstance game = new GameInstance(GameInstance.GameState.GAME_LOBBY, key , null);
        games.put(key, game);
        logger.info("logging GamesController.createGame(Integer key)%n{}", PrintUtils.red(String.format("Game created. game.getId() = %s%n" +
                "game.getState() = %s" +
                        "games.keySet() = %s",
                game.getID(),
                game.getGameState(),
                games.keySet())));
        return key;
    }

    public static Integer createGame() {

        Integer key = createNewKey();
        int gameId = createGame(key);

        logger.info("logging GamesController.createGame()%n{}",PrintUtils.red(String.format("created new key. key = %s%n" +
                        "created new game with key. key = %s" +
                        "games.keySet = %s",
                key,
                gameId,
                games.keySet())));

        return gameId;
    }

    private static Integer createNewKey() {
        int key = 1;
        while(games.containsKey(key) ) {
            key = new Random().nextInt();
        }
        PrintUtils.red(String.format("created new key. key = %s%n",
                key
        ));

        return key;
    }

    public static Integer createNewGame() {
        Integer key = createNewKey();
        int gameId = createGame(key);
        PrintUtils.red(String.format("created new key. key = %s%n" +
                        "created new game with key. key = %s" +
                        "games.keySet = %s",
                key,
                gameId,
                games.keySet()));

        return gameId;
    }

    public Collection<GameInstance> getAllGames() {
        PrintUtils.red(String.format("get all games. returning Collection<GameInstance> games.values() = %s%n",
                games.values()
        ));
        logger.info("Collection<GameInstance> getAllGames returning: " + games.values());
        return games.values();
    }

    public GameInstance getGame(Integer ID) {
        PrintUtils.red(String.format("get game by ID. ID = %s%n games.keySet = %s",
                ID,
                games.keySet()
        ));
        GameInstance game = games.get(ID);
        PrintUtils.red("game = " + games.values());
        return games.get(ID);
    }

    public GameInstance removeGame(Integer ID) {
        GameInstance removed = games.remove(ID);
        PrintUtils.red(String.format("removing game by ID. ID = %s%n" +
                        "removed = %s",
                ID,
                removed
        ));

        return removed;
    }

    // Add/Update/Remove Players:

    public boolean addPlayer(Integer gameID, PlayerData player) {

        // enforce maximum players allowed into a game or lobby at a time
        GameInstance game = getGame(gameID);

        if(game.getAllPlayers().size() >= GameDesignVars.MAX_PLAYERS_PER_GAME ) {
            return false;
        }
        boolean added = game.addPlayer(player);
        PrintUtils.red(String.format("Adding player.getPublicID() = %s, added = %s",
                player.getPublicID(),
                added
        ));
        return game.addPlayer(player);
    }

    public boolean updatePlayer(Integer gameID, PlayerData player) {
        logger.info("logging GamesController.updatePlayer(Integer gameID, PlayerData player)%n{}",
                PrintUtils.red(String.format("Updating player = %s and game id = %s", player.getPublicID(), gameID)));

        GameInstance game = getGame(gameID);
        return game.updatePlayer(player);
    }

    public boolean removePlayer(Integer gameID, Integer playerPublicID) {
        logger.info("logging GamesController.removePlayer(Integer gameID, Integer playerPublicID)%n{}",
                PrintUtils.red(String.format("removing player = %s and game id = %s", playerPublicID, gameID)));
        GameInstance game = getGame(gameID);
        return game.removePlayer(playerPublicID);
    }

}
