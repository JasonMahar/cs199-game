package com.example.server.controller;

import com.example.model.*;
import com.example.server.exceptions.*;
import com.example.server.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import java.util.*;

@RestController
public class PlayersController {

    private static HashMap<Integer, PlayerData> players;
    private static Random rand = new Random();

    @Autowired
    PlayerService playerService;


    @GetMapping("/actor/{id}")
    public String getActorName(@PathVariable("id") int id) {
        try {
            return playerService.getPlayer(id);
        } catch (PlayerNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor Not Found", ex);
        }
    }


    public static Integer createPlayer() {
        PlayerData player = new TemplePlayerData();
        int publicKey = createNewKey();
        player.setPublicID(publicKey);
        int privateKey = createNewKey();
        player.setPrivateID(privateKey);

// NOTE: in future this should be by private key which only the player it belongs to
//		will know. But for simplicity we're just using publicKey for everything.
//		players.put(privateKey, player);
        players.put(publicKey, player);

        return publicKey;
    }

    private static Integer createNewKey() {

        int key = rand.nextInt();
        while( players.containsKey(key) ) {
            key = rand.nextInt();
        }
        return key;
    }

    public static PlayerData getPlayer(Integer ID) {

        System.out.println("PlayersController.getPlayer() called. id = " + ID);

        return players.get(ID);
    }

    // Add/Update/Remove Players:

    public static boolean addPlayer(PlayerData player) {

        if( player == null )	return false;

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


}
