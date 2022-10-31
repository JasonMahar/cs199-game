package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.fasterxml.jackson.databind.annotation.*;

import java.text.*;

//@JsonDeserialize(using = PlayerDataDeserializer.class)
public interface PlayerData {
    
    int INVALID_PLAYER_ID = 0;
    int getPublicID();

    void setPublicID(int var1);

    void setPrivateID(int var1);

    String getName();

    void setName(String var1);

    State getState();

    void setState(State var1);

    public static enum State {
        MAIN_MENU,
        IN_LOBBY,
        RUNNING,
        CROUCHING,
        DEAD;

        private State() {
        }
    }
}
