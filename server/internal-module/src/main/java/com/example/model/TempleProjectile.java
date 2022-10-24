package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public class TempleProjectile implements Projectile {
    private int ID;
    protected float xPosition;
    protected float yPosition;
    private PlayerData.Facing facing;
    private Float speed;

    public TempleProjectile() {
        this.ID = 0;
        this.xPosition = 0.0F;
        this.yPosition = 0.0F;
        this.facing = PlayerData.Facing.RIGHT;
        this.speed = 0.0F;
    }

    public TempleProjectile(int iD, float xPosition, float yPosition, PlayerData.Facing facing, Float speed) {
        this.ID = iD;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.facing = facing;
        this.speed = speed;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int iD) {
        this.ID = iD;
    }

    public float getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return this.yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public PlayerData.Facing getFacing() {
        return this.facing;
    }

    public void setFacing(PlayerData.Facing facing) {
        this.facing = facing;
    }

    public Float getSpeed() {
        return this.speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}
