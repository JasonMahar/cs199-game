package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fasterxml.jackson.annotation.*;

import java.text.*;
import java.util.*;

public interface PlayerData {

    int MAX_PROJECTILES = 1;

    boolean isGameOwner();

    @JsonIgnore
    UUID getPrivateID();


    int getPublicID();

    void setPublicID(int var1);

    @JsonIgnore
    void setPrivateID(UUID var1);

    String getName();

    void setName(String var1);

    State getState();

    void setState(State var1);

    public static enum State {
        PLAYER_CREATED,
        MAIN_MENU,
        IN_LOBBY,
        JOINING_GAME,
        ENTERING_GAME,
        IN_GAME,
        LEAVING_GAME;
        private State() {
        }
    }
}
