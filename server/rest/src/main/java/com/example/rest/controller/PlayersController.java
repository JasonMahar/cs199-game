package com.example.rest.controller;

import com.example.model.*;
<<<<<<< HEAD
import com.example.rest.exceptions.*;
import org.springframework.http.*;
import org.springframework.web.server.*;

import java.util.*;
import java.util.concurrent.*;

public class PlayersController {
    private static ConcurrentHashMap<Integer, PlayerData> players;

    private static volatile PlayersController instance;
    private static final Object mutex = new Object();
    private PlayersController() {
        players = new ConcurrentHashMap<>();
    }

    public static PlayersController getInstance() {
        PlayersController result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new PlayersController();
            }
        }
        return result;
    }

    public Integer createPlayer(String name) throws InvalidPlayerDataException {
        if(name == null || name.equals("")){
            throw new InvalidPlayerDataException("Player name is null or blank");
        }
        int publicKey = createPublicKey();
=======
import com.example.util.*;
import org.slf4j.*;

import java.util.*;

public class PlayersController {
    private static HashMap<Integer, PlayerData> players;
    private static final Random rand;

    protected static int gameOwnerPublicId;


    static {
        players = new HashMap<>();
        rand = new Random();
    }

    public static Integer createPlayer(String name) {

        int publicKey = createKey();
>>>>>>> develop
        PlayerData player = new TemplePlayerData(name, publicKey);

        UUID privateKey = UUID.randomUUID();
        player.setPrivateID(privateKey);

		players.put(publicKey, player);
        return publicKey;
    }

<<<<<<< HEAD
    private static Integer createPublicKey() {
        Random rand = new Random();
=======
    private static Integer createKey() {

>>>>>>> develop
        int key = rand.nextInt();
        while(players.containsKey(key) ) {
            key = rand.nextInt();
        }
        return key;
    }

    private static UUID createPrivateKey() {
        return UUID.randomUUID();
    }

<<<<<<< HEAD
    public PlayerData getPlayer(Integer ID) {
=======
    public static PlayerData getPlayer(Integer ID) {
>>>>>>> develop

        return players.get(ID);
    }

<<<<<<< HEAD
    public int addPlayer(PlayerData player) throws InvalidPlayerDataException {
        int publicID = player.getPublicID();

        if(players.put(publicID, player) == null){
            throw new InvalidPlayerDataException("Player not found in game");
        }
        return publicID;
    }

    public int updatePlayer(PlayerData player) throws InvalidPlayerDataException {
        int publicID = player.getPublicID();

        if(players.put(publicID, player) == null){
            throw new InvalidPlayerDataException("Player not found in game");
        }
        return publicID;
    }

    public int removePlayer(Integer playerPublicID) {
        if (players.remove(playerPublicID) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player not found in game");
        }
        return playerPublicID;
    }

    public Collection<PlayerData> getAllPlayers() {
=======
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
>>>>>>> develop
        return players.values();
    }

}
