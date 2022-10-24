package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fasterxml.jackson.databind.annotation.*;

@JsonDeserialize(as = TemplePlayerData.class)
public class TemplePlayerData implements PlayerData {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private int publicID;
    private int privateID;
    private String name;
    private PlayerData.State state;
    protected float x;
    protected float y;
    private PlayerData.Facing facing;
    private float speed;
    private int livesRemaining;

    public TemplePlayerData() {

    }

    public TemplePlayerData(int publicID, String name, PlayerData.State state, float x, float y, PlayerData.Facing facing, float speed) {
        this(publicID, 1, name, state, x, y, facing, speed);
    }

    public TemplePlayerData(int publicID, int privateID, String name, PlayerData.State state, float x, float y, PlayerData.Facing facing, float speed) {
        this.publicID = publicID;
        this.privateID = privateID;
        this.name = name;
        this.state = state;
        this.x = x;
        this.y = y;
        this.facing = facing;
        this.speed = speed;
    }

    public TemplePlayerData(String name) {
        this(1,1, name);
    }

    public TemplePlayerData(int publicID, int privateID, String name) {
        this(publicID, privateID, name, State.IN_LOBBY, 0, 0, Facing.DOWN, 3);
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

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public PlayerData.Facing getFacing() {
        return this.facing;
    }

    public void setFacing(PlayerData.Facing facing) {
        this.facing = facing;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String toString() {
        return "{" +
                " publicID=" + this.publicID +
                ", name=" + this.name +
                ", state=" + this.state +
                ", x=" + this.x +
                ", y=" + this.y +
                ", facing=" + this.facing +
                ", speed=" + this.speed +
                "}";
    }


}
