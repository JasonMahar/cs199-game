package com.example.model;

import java.util.*;

public class GameInstance {
    private GameState gameState;
    private Integer ID;
    private HashMap<Integer, PlayerData> players = new HashMap();

    public GameInstance(GameState gameState, Integer ID, HashMap<Integer, PlayerData> players) {
        this.gameState = gameState;
        this.ID = ID;
        if (players == null) {
            players = new HashMap();
        }

        this.players = players;
    }

    public boolean addPlayer(PlayerData player) {
        if (player != null && !this.players.containsKey(player.getPublicID())) {
            return this.players.put(player.getPublicID(), player) == null;
        } else {
            return false;
        }
    }

    public boolean updatePlayer(PlayerData player) {
        if (player != null && this.players.containsKey(player.getPublicID())) {
            return this.players.replace(player.getPublicID(), player) != null;
        } else {
            return false;
        }
    }

    public boolean removePlayer(Integer playerPublicID) {
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
        System.out.println("\nGameInstance.join() called ");
        this.setGameState(GameState.GAME_LOBBY);
        return true;
    }

    public boolean start() {
        System.out.println("\nGameInstance.start() called ");
        this.setGameState(GameState.GAME_STARTING);
        return true;
    }

    public boolean enter() {
        System.out.println("\nGameInstance.entering() called ");
        this.setGameState(GameState.ENTERING_GAME);
        return true;
    }

    public boolean run() {
        System.out.println("\nGameInstance.run() called ");
        this.setGameState(GameState.IN_GAME);
        return true;
    }

    public boolean close() {
        System.out.println("\nGameInstance.close() called ");
        this.setGameState(GameState.CLOSING);
        return true;
    }

    public Integer getID() {
        return this.ID;
    }

    protected void setID(Integer iD) {
        this.ID = iD;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState newState) {
        System.out.println("GameInstance.setGameState() newState = " + newState);
        this.gameState = newState;
    }

    public void setPlayers(HashMap<Integer, PlayerData> players) {
        this.players = players;
    }

    public Collection<PlayerData> getAllPlayers() {
        return this.players.values();
    }

    public PlayerData getPlayer(Integer playerID) {
        return (PlayerData)this.players.get(playerID);
    }

    public PlayerData getPlayer(String playerName) {
        Iterator var3 = this.players.values().iterator();

        while(var3.hasNext()) {
            PlayerData player = (PlayerData)var3.next();
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

    public String toString() {
        return "\n{ '_commnent' : 'GAME','ID' : " + this.ID + ", " + "'gameState' : " + this.gameState + ", " + this.getAllPlayers() + " } ";
    }

    public static void main(String[] args) {
        System.out.println("Running GameInstance.main()");
        System.out.println("End GameInstance.main()");
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

