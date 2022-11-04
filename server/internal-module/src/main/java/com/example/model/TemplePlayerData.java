package com.example.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class TemplePlayerData implements PlayerData {

    protected int publicID;
    @JsonIgnore
    private UUID privateID;

    protected String name;
    protected PlayerData.State state;

    @JsonIgnore
    protected String gameName;

    private boolean isGameOwner;

    public TemplePlayerData(){}
    public TemplePlayerData(String name, int publicId) {
        this(name, publicId, false);
    }

    public TemplePlayerData(String name, int publicId, boolean isGameOwner) {
        this.name = name;
        this.publicID = publicId;
        this.isGameOwner = isGameOwner;
    }

    @Override
    public boolean isGameOwner() {
        return isGameOwner;
    }
    
    public void setIsGameOwner(boolean isGameOwner) {
        this.isGameOwner = isGameOwner;

    }
    public int getPublicID() {
        return this.publicID;
    }

    public void setPublicID(int publicID) {
        this.publicID = publicID;
    }

    public UUID getPrivateID() {
        return this.privateID;
    }

    public void setPrivateID(UUID privateID) {
        this.privateID = privateID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public PlayerData.State getState() {
        return this.state;
    }

    public void setState(PlayerData.State state) {
        this.state = state;
    }
    public String toString() {

        return "{" +
                " publicID=" + this.publicID +
                ", name=" + this.name +
                ", state=" + this.state +
                "}";
    }

}
