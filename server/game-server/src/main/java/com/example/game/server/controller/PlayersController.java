package com.example.game.server.controller;

import com.example.model.*;
import com.example.util.*;
import org.slf4j.*;

import java.util.*;

public class PlayersController {
    private static final Logger logger;

    private static HashMap<Integer, PlayerData> players;
    private static final Random rand;

    static {
        logger = LoggerFactory.getLogger(PlayersController.class);
        PrintUtils.cyan(String.format("static block in class/logger = %s", PlayersController.class.toString()));
        players = new HashMap<>();
        PrintUtils.cyan("players == null " + players);
        rand = new Random();
    }

    public static Integer createPlayer() {
        PlayerData player = new TemplePlayerData();
        int publicKey = createNewKey();
        player.setPublicID(publicKey);
        int privateKey = createNewKey();
        player.setPrivateID(privateKey);

		players.put(privateKey, player);
        return publicKey;
    }

    private static Integer createNewKey() {

        int key = rand.nextInt();
        while(players.containsKey(key) ) {
            key = rand.nextInt();
        }

        PrintUtils.titleCyan("returning public key: " + key);
        return key;
    }

    private static Integer createNewPrivateKey() {
        int key = rand.nextInt();
        while( players.containsKey(key) ) {
            key = rand.nextInt();
        }
        PrintUtils.titleCyan("returning private key: " + key);
        return key;
    }

    public static PlayerData getPlayer(Integer ID) {

        PlayerData playerData = players.get(ID);

        PrintUtils.red(String.format(" PlayerData.players.get(%s) = %s", ID, playerData));

        return playerData;
    }

    // Add/Update/Remove Players:

    public static boolean addPlayer(PlayerData player) {
        if(player == null) {
            return false;
        }

        return players.put(player.getPublicID(), player) == null;
    }

    public static boolean updatePlayer(PlayerData player) {

        if( player == null )	return false;

        return players.put(player.getPublicID(), player) != null;
    }

    public static boolean removePlayer(Integer playerPublicID ) {

        return players.remove(playerPublicID) != null;
    }

    public static Collection<PlayerData> getAllPlayers() {
        return players.values();
    }

    public static PlayerData addTestPlayer(PlayerData player) {
        if(player == null) {
            return null;
        }

        return players.put(player.getPublicID(), player);
    }

    public PlayerData getTestPlayer(Integer ID) {

        PlayerData playerData = players.get(ID);

        PrintUtils.red(String.format(" PlayerData.players.get(%s) = %s", ID, playerData));

        return players.get(ID);

    }
}
