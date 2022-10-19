package com.example.transport;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//




import com.example.model.*;

import java.util.*;

public interface GameSessionInterface {
    Collection<GameInstance> getAllGames();

    GameInstance getGameData(Integer var1);

    GameInstance createNewGame(PlayerData var1);

    GameInstance startGame(Integer var1);

    PlayerData createNewPlayer(String var1);

    GameInstance joinGame(Integer var1, PlayerData var2);

    GameInstance updatePlayerData(Integer var1, PlayerData var2);

    boolean quitGame(PlayerData var1);

    HashMap<Integer, PlayerData> getAllPlayersData();

    PlayerData getPlayerData(int var1);
}
