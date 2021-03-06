/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

/**
 *
 * @author user
 */
public class Player extends MapObject {

    private static int defaultSpeed = 1;

    public String nickname;

    public int paintingNumber;

    public int speed = 1; //Скорость игрока

    public int score = 0; //Очки игрока

    public Player(int playerNumber, int i, int j, String nickname) {
        super(playerNumber, i, j);
        this.nickname = nickname;
        this.paintingNumber = playerNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void move(String direction) {
        switch (direction) {
            case "up":
                this.i--;
                break;
            case "down":
                this.i++;
                break;
            case "right":
                this.j++;
                break;
            case "left":
                this.j--;
                break;
        }
    }

    public void restoreDefaultState() {
        speed = defaultSpeed;
        paintingNumber = number;
    }

}
