package com.example.rest.controller;

import com.example.model.*;
import com.example.rest.exceptions.*;
import org.springframework.http.*;
import org.springframework.web.server.*;

import java.util.*;
import java.util.concurrent.*;

public class GamesController {
    private static ConcurrentHashMap<Integer, GameInstance> games;
    private static volatile GamesController instance;
    private static final Object mutex = new Object();

    private GamesController() {
        games = new ConcurrentHashMap<>();
    }
    public static GamesController getInstance() {
        GamesController result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new GamesController();
            }
        }
        return result;
    }

    /*
     * normally createGame() should be used, which creates a unique gameID/key
     * but this can be used to create a specific static "game" to hold players, e.g. the game lobby
     */
    private Integer createGame(Integer gameID) throws Exception {
        // NOTE: there is only 1 game for this first iteration so could
        //		potentially re-use key/gameID == GameDesignVars.GAME_LOBBY_ID
        if(gameID == null) {
            throw new InvalidGameInstanceException("Cannot find Game ID");
        }

        GameInstance game = new GameInstance(GameInstance.GameState.GAME_LOBBY, gameID , null);
        games.put(gameID, game);
        return gameID;
    }

    private Integer createGame(Integer gameID, TemplePlayerData gameOwner) throws Exception {
        if(gameID == null) {
            throw new InvalidGameInstanceException("Game ID is null");
        }
        if(gameOwner == null) {
            throw new InvalidPlayerDataException("PlayerData is null");
        }
        Map<Integer, PlayerData> map = new HashMap<>();
        map.put(gameOwner.getPublicID(), gameOwner);
        gameOwner.setIsGameOwner(true);
        GameInstance game = new GameInstance(GameInstance.GameState.GAME_LOBBY, gameID ,  map, gameOwner);
        games.put(gameID, game);
        return gameID;
    }

    public Integer createGame(TemplePlayerData gameOwner) throws Exception {
        Integer key = createNewKey();

        return createGame(key, gameOwner);
    }

    public Integer createGame() throws Exception {
        Integer key = createNewKey();

        return createGame(key);
    }

    private Integer createNewKey() {
        int key = 1;
        while(games.containsKey(key) ) {
            key = new Random().nextInt();
        }
        return key;
    }

    public Collection<GameInstance> getAllGames() {
        if(games == null || games.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No game instances found");
        }

        return games.values();
    }

    public GameInstance getGame(Integer gameID) throws InvalidGameInstanceException {

        if(gameID == null || !games.containsKey(gameID)) {
            throw new InvalidGameInstanceException("Game ID is null");
        }

        return games.get(gameID);
    }

    public int removeGame(Integer gameID) throws InvalidGameInstanceException {

        if(gameID == null) {
            throw new InvalidGameInstanceException("Game ID is null");
        }

        GameInstance game = games.remove(gameID);

        return gameID;
    }

    // Add/Update/Remove Players:

    public boolean addPlayer(Integer gameID, PlayerData player) throws Exception {
        if(gameID == null) {
            throw new InvalidGameInstanceException("Game ID is null");
        }
        // enforce maximum players allowed into a game or lobby at a time
        GameInstance game = getGame(gameID);
        if(game == null) {
            throw new InvalidGameInstanceException("Cannot find Game ID");
        }

        if(game.getPlayers().size() >= GameDesignVars.MAX_PLAYERS_PER_GAME ) {
            return false;
        }

        return game.addPlayer(player);
    }

    public boolean updatePlayer(Integer gameID, PlayerData player) throws Exception {
        if(gameID == null) {
            throw new InvalidGameInstanceException("Game ID is null");
        }
        GameInstance game = getGame(gameID);

        if(game == null) {
            throw new InvalidGameInstanceException("Cannot find Game ID");
        }
        return game.updatePlayer((TemplePlayerData) player);
    }

    public boolean removePlayer(Integer gameID, int publicPlayerId) throws InvalidGameInstanceException {

        if(gameID == null) {
            throw new InvalidGameInstanceException("Cannot find Game ID");
        }
        GameInstance game = getGame(gameID);
        return game.removePlayer(publicPlayerId);
    }

}
