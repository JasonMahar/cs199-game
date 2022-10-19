package com.example.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.text.*;

public interface PlayerData {
    int INVALID_PLAYER_ID = 0;
    float MAX_SPEED = 2.0F;
    float STOPPED = 0.0F;
    int MAX_PROJECTILES = 1;

    int getPublicID();

    void setPublicID(int var1);

    void setPrivateID(int var1);

    String getName();

    void setName(String var1);

    State getState();

    void setState(State var1);

    float getX();

    void setX(float var1);

    float getY();

    void setY(float var1);

    Facing getFacing();

    void setFacing(Facing var1);

    float getSpeed();

    void setSpeed(float var1);

    public static enum Facing {
        RIGHT(0.0F),
        UP(90.0F),
        LEFT(180.0F),
        DOWN(270.0F);

        private static final DecimalFormat DF = new DecimalFormat("0.00");
        private float direction;

        private Facing() {
            this.direction = 0.0F;
        }

        private Facing(float direction) {
            this.direction = direction;
        }

        public float getDirection() {
            return this.direction;
        }

        public void setDirection(float direction) {
            this.direction = direction;
        }

        public String toString() {
            return DF.format((double)this.direction);
        }
    }

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
