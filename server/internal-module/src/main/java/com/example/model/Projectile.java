package com.example.model;

public interface Projectile {
    int getID();

    void setID(int var1);

    float getxPosition();

    void setxPosition(float var1);

    float getyPosition();

    void setyPosition(float var1);

    PlayerData.Facing getFacing();

    void setFacing(PlayerData.Facing var1);

    Float getSpeed();

    void setSpeed(Float var1);
}
