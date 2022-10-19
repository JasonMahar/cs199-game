package com.example.transport;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//




import com.example.model.*;

import java.util.*;

public class STUB_GameSession implements GameSessionInterface {
    public static final int DEFAULT_GAME_ID = 1;
    public static final int STUB_DEFAULT_PLAYER_ID = 1;
    private static final float INITIAL_MOVEMENT = 2.0F;
    private static final float INITIAL_FACING = 40.0F;
    private static GameInstance game = null;
    private float[][] startingLocations = new float[][]{{64.0F, 896.0F}, {536.0F, 64.0F}, {64.0F, 64.0F}, {536.0F, 896.0F}};

    public STUB_GameSession() {
    }

    public void STUB_add3DummyPlayers() {
        for(int playerID = 2; playerID < 5; ++playerID) {
            PlayerData.Facing direction = PlayerData.Facing.LEFT;
            direction.setDirection(40.0F);
            PlayerData dummyPlayer = new TemplePlayerData(playerID, "Player " + playerID,
                    PlayerData.State.RUNNING, this.startingLocations[playerID - 1][0], this.startingLocations[playerID - 1][1], direction, 2.0F * (float)playerID);
            game.addPlayer(dummyPlayer);
        }

    }

    public PlayerData createNewPlayer(String playerName) {
        return playerName != null && !playerName.isBlank() ? new TemplePlayerData(playerName) : null;
    }

    public GameInstance joinGame(Integer gameId, PlayerData userPlayer) {
        if (game == null) {
            this.createNewGame(userPlayer);
        } else {
            game.addPlayer(userPlayer);
        }

        return game;
    }

    public GameInstance updatePlayerData(Integer gameID, PlayerData userPlayer) {
        return game;
    }

    public boolean quitGame(PlayerData userPlayer) {
        return false;
    }

    public Collection<GameInstance> getAllGames() {
        Collection<GameInstance> list = new ArrayList();
        list.add(game);
        return list;
    }

    public GameInstance getGameData(Integer gameID) {
        return game;
    }

    public GameInstance createNewGame(PlayerData userPlayer) {
        if (game == null) {
            game = new GameInstance(GameInstance.GameState.GAME_LOBBY, 1, (HashMap)null);
        }

        userPlayer.setPublicID(1);
        userPlayer.setPrivateID(1);
        userPlayer.setX(this.startingLocations[0][0]);
        userPlayer.setY(this.startingLocations[0][1]);
        game.addPlayer(userPlayer);
        this.STUB_add3DummyPlayers();
        return game;
    }

    public PlayerData getPlayerData(int ID) {
        return null;
    }

    public HashMap<Integer, PlayerData> getAllPlayersData() {
        return null;
    }

    public GameInstance startGame(Integer gameID) {
        return game;
    }
}
