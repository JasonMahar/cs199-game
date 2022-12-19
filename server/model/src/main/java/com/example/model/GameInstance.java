package com.example.model;

import com.fasterxml.jackson.annotation.*;


import java.util.*;
import java.util.stream.*;


public class GameInstance {

    private int gameID;

    private GameState gameState;

    public String gameName;

    @JsonIgnore
    private PlayerData gameOwner;

    private Map<Integer, PlayerData> players;

    public GameInstance(){}
    
    public GameInstance(GameState gameState, Integer ID, Map<Integer, PlayerData> players) throws Exception {
        this.gameState = gameState;
        this.gameID = ID;

        if(players == null) {
            this.players = new HashMap<>();
        } else {
            this.players = players;
            this.gameOwner = new ArrayList<>(players.values()).get(0);
        }
    }

    public GameInstance(GameState gameState, Integer ID, Map<Integer, PlayerData> players, PlayerData gameOwner) throws Exception {
        this.gameState = gameState;
        this.gameID = ID;

        if(gameOwner == null) {
            throw new Exception("players map cannot be null");
        }
        // concrete class in GameInstance?
        this.gameOwner = (TemplePlayerData) gameOwner;

        // set game name
        if(((TemplePlayerData) gameOwner).gameName != null) {
            this.gameName = ((TemplePlayerData) gameOwner).gameName;
        }
        else {
            this.gameName = "TempleRun-" + ID;
        }

        // create new players map or set players map
        if(players == null) {
            this.players = new HashMap<>();
        }
        else {
            this.players = players;
        }
        this.players.put(gameOwner.getPublicID(), gameOwner);

    }
    
    public String getGameName() {
        return gameName;
    }

    public GameInstance setGameName(String gameName) {
        this.gameName = gameName;
        return this;
    }

    public PlayerData getGameOwner() {
        return gameOwner;
    }

    private void setGameOwner(PlayerData gameOwner) throws Exception {
        this.gameOwner = gameOwner;
    }

    public boolean addPlayer(PlayerData player) throws Exception {
        if(player == null){
            throw new Exception("Null Input Exception");
        }
        if(player instanceof TemplePlayerData) {
            TemplePlayerData other = (TemplePlayerData)  player;

            if(players.isEmpty()){
                setGameOwner(player);
            }
            if (players.containsKey(other.getPublicID())) {
                return false;
            }

            return this.players.put(other.getPublicID(), other) == null;
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
    
    @JsonIgnore
    public boolean isEmpty() {
        return this.players.isEmpty();
    }
    @JsonIgnore
    public boolean isFull() {
        return this.players.size() >= 4;
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


    public int getGameID() {
        return this.gameID;
    }

    public void setGameID(int iD) {
        this.gameID = iD;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    public PlayerData getPlayer(int playerID) {
        return (TemplePlayerData)this.players.get(playerID);
    }

    public Map<Integer, PlayerData> getPlayers() {

        return players;
    }

    @Override
    public String toString() {
        return
                "gameID=" + gameID +
                        ", gameName=" + gameName +
                        ", gameOwner=" + gameOwner.getName() +
                        ", gameState=" + gameState;
    
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameInstance)) return false;
        GameInstance game = (GameInstance) o;
        if (gameID != game.gameID) return false;
        if (gameState != game.gameState) return false;
        return Objects.equals(players, game.players);
    }

    @Override
    public int hashCode() {
        int result = gameState != null ? gameState.hashCode() : 0;
        result = 31 * result + gameID;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }

    private void setCustomGameName(String gameName) {
        this.gameName = gameName;
    }

    public static enum GameState {
        GAME_LOBBY("GAME_LOBBY"),
        GAME_STARTING("GAME_STARTING"),
        ENTERING_GAME("ENTERING_GAME"),
        IN_GAME("IN_GAME"),

        GAME_ENDING("GAME_ENDING"),
        CLOSING("CLOSING"),

        UNKNOWN("GAME_STARTING");
        public final String state;
        private GameState(String state) {
            this.state = state;
        }
    }
}

