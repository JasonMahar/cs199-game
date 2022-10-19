package com.example.server.model;

import com.example.server.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PlayerService {
    List<String> players = Arrays.asList("Jack Nicholson", "Marlon Brando", "Robert De Niro", "Al Pacino", "Tom Hanks");

    public String getPlayer(int index) throws PlayerNotFoundException {
        if (index >= players.size()) {
            throw new PlayerNotFoundException("Actor Not Found in Repsoitory");
        }
        return players.get(index);
    }

    public String updateActor(int index, String actorName) throws PlayerNotFoundException {
        if (index >= players.size()) {
            throw new PlayerNotFoundException("Actor Not Found in Repsoitory");
        }
        players.set(index, actorName);
        return actorName;
    }

    public String removeActor(int index) {
        if (index >= players.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Actor Not Found in Repsoitory");
        }
        return players.remove(index);
    }
}