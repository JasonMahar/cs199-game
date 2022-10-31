package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.example.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;

//@JsonDeserialize(using = TemplePlayerData.TemplePlayerDataDeserializer.class)
public class TemplePlayerData implements PlayerData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private int publicID;
    private int privateID;
    public String name;
    private PlayerData.State state;

    public String gameName;
    public boolean isGameOwner;

    public TemplePlayerData() {}

    public TemplePlayerData(String name) {
        PrintUtils.green("construtor " + name);
        this.name = name;
        this.publicID = 1;
    }

    public TemplePlayerData(int gameId, String name) {
        this(gameId, name, State.IN_LOBBY);
    }

    public TemplePlayerData(int publicID, String name, PlayerData.State state) {
        this(publicID,  name, state, 1);
    }

    public TemplePlayerData(int publicID, String name, PlayerData.State state, int privateID) {
        PrintUtils.green("construtor");
        this.publicID = publicID;
        this.privateID = privateID;
        this.name = name;
        this.state = state;
    }

    public int getPublicID() {
        return this.publicID;
    }

    public void setPublicID(int publicID) {
        this.publicID = publicID;
    }

    public int getPrivateID() {
        return this.privateID;
    }

    public void setPrivateID(int privateID) {
        this.privateID = privateID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerData.State getState() {
        return this.state;
    }

    public void setState(PlayerData.State state) {
        this.state = state;
    }


    public boolean getIsGameOwner() {
        return isGameOwner;
    }

    public void setIsGameOwner(boolean gameOwner) {
        isGameOwner = gameOwner;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public static class TemplePlayerDataDeserializer extends JsonDeserializer<TemplePlayerData> {

        @Override
        public TemplePlayerData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            PrintUtils.cyan("Deserializing: ");
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            ObjectNode root = mapper.readTree(p);
            return mapper.readValue(root.toString(), TemplePlayerData.class);
        }
    }
}
