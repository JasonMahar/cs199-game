package com.example.model;

import com.example.util.*;
import org.slf4j.*;

import java.util.*;
import java.util.stream.*;

public class GameInstance {
    private static final Logger logger;
    private GameState gameState;
    private int ID;

    private String gameName; // create game will provide an option to set a custom name

    private PlayerData gameOwner; // owner of game, so there cannot be a game without an owner
    private Map<Integer, PlayerData> players;

    static {
        logger = LoggerFactory.getLogger(GameInstance.class);
        logger.trace("static initialization block: {}",
                PrintUtils.cyan(String.format("class/logger = %s", GameInstance.class.toString())));
    }

    public GameInstance(GameState gameState, Integer ID, Map<Integer, PlayerData> players) throws Exception {
        this.gameState = gameState;
        this.ID = ID;
        this.players = Objects.requireNonNullElseGet(players, HashMap::new);
    }

    private GameInstance(){

    }

    public boolean addPlayer(PlayerData player) throws Exception {
        if(player == null){
            throw new Exception("Null Input Exception");
        }
        if(player instanceof TemplePlayerData templePlayerData) {

            if (players.containsKey(templePlayerData.getPublicID())) {
                return false;
            }

            PrintUtils.green("public id = " + templePlayerData.getPublicID() + " private id = " + templePlayerData.getPrivateID());
            return this.players.put(templePlayerData.getPrivateID(), templePlayerData) == null;
        }
        return false;
    }

    public boolean updatePlayer(PlayerData player) throws Exception {
        if(player == null){
            throw new Exception("Null Input Exception");
        }
        if(!players.containsKey(player.getPublicID())){
            return false;
        }

        return this.players.replace(player.getPublicID(), player) != null;
    }

    public boolean removePlayer(int playerPublicID) {
        if (!this.players.containsKey(playerPublicID)) {
            return false;
        } else {
            return this.players.remove(playerPublicID) != null;
        }
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }

    public boolean join() {
        this.setGameState(GameState.GAME_LOBBY);
        return true;
    }

    public boolean start() {
        this.setGameState(GameState.GAME_STARTING);
        return true;
    }

    public boolean enter() {
        this.setGameState(GameState.ENTERING_GAME);
        return true;
    }

    public boolean run() {
        this.setGameState(GameState.IN_GAME);
        return true;
    }

    public boolean close() {
        this.setGameState(GameState.CLOSING);
        return true;
    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public PlayerData gameOwner() {
        return gameOwner;
    }

    public void setGameOwner(PlayerData gameOwner) {
        if(gameOwner instanceof TemplePlayerData player) {
            player.setIsGameOwner(true);
            this.gameOwner = gameOwner;
        }
    }

    public int getID() {
        return this.ID;
    }

    protected void setID(int iD) {
        this.ID = iD;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    public void setPlayers(Map<Integer, PlayerData> players) {
        this.players = players;
    }

    public Collection<PlayerData> getAllPlayers() {
        if(players.size() == 0) {
            players = new HashMap<>();
        }

        return players.values().stream()
                .filter(playerData -> playerData instanceof TemplePlayerData)
                .map(playerData -> (TemplePlayerData) playerData)
                .collect(Collectors.toSet());
    }

    public PlayerData getPlayer(int playerID) {

        return (TemplePlayerData)this.players.get(playerID);
    }

    public PlayerData getPlayer(String playerName) {

        for (PlayerData player : this.players.values()) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    public Map<Integer, PlayerData> getPlayers() {
        return players;
    }

    @Override
    public String toString() {

        Set<TemplePlayerData> templePlayers = players.values().stream()
                .filter(sc -> sc instanceof TemplePlayerData)
                .map(sc -> (TemplePlayerData) sc)
                .collect(Collectors.toSet());

        return "\n{ '_comment' : 'GAME','ID' : " + this.ID + ", " + "'gameState' : " + this.gameState + ", " + templePlayers + " } ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameInstance that)) return false;

        if (ID != that.ID) return false;
        if (gameState != that.gameState) return false;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        int result = gameState != null ? gameState.hashCode() : 0;
        result = 31 * result + ID;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }

    private void setCustomGameName(String gameName) {
        this.gameName = gameName;
    }

    public static enum GameState {
        GAME_LOBBY,
        GAME_STARTING,
        ENTERING_GAME,
        IN_GAME,
        CLOSING,
        UNKNOWN;

        private GameState() {
        }
    }
}

