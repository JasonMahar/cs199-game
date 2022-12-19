package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fasterxml.jackson.annotation.*;

import java.text.*;
import java.util.*;

public interface PlayerData {

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
        PLAYER_CREATED("PLAYER_CREATED"),
        MAIN_MENU("MAIN_MENU"),
        IN_LOBBY("IN_LOBBY"),
        JOINING_GAME("JOINING_GAME"),
        ENTERING_GAME("ENTERING_GAME"),
        IN_GAME("IN_GAME"),
        LEAVING_GAME("LEAVING_GAME");

        public final String state;
        private State(String state) {
            this.state = state;
        }
    }
}
