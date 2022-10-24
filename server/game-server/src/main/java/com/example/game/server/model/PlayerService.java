package com.example.game.server.model;


import com.example.game.server.exceptions.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Service
public class PlayerService {
    private static List<Integer> players;

    static {
        players = new ArrayList<>();
    }
    public int getPlayer(int playerId) throws InvalidPlayerFieldException {
        if (!players.contains(playerId)) {
            throw new InvalidPlayerFieldException("Player ID Not Found");
        }
        return players.get(playerId);
    }

    public int updatePlayer(int playerId) throws InvalidPlayerFieldException {
        if (!players.contains(playerId)) {
            throw new InvalidPlayerFieldException("Player ID Not Found");
        }
        int index = players.indexOf(playerId);
        players.set(index, playerId);
        return playerId;
    }

    public int removePlayer(int playerId) {
        if (!players.contains(playerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player ID not found");
        }
        return players.remove(playerId);
    }
}