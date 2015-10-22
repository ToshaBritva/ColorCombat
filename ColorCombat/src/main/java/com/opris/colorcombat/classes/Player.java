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
    
    public String nickname;
    
    public int speed = 1; //Скорость игрока
    
    public int score = 0; //Очки игрока

    public Player(int playerNumber, int i, int j, String nickname) {
        super(playerNumber, i, j);
        this.nickname = nickname;
    }
    
    //Двигаем игрока на клекту
    public void moveUp(){
        this.i--;
    }    
    public void moveDown(){
        this.i++;
    }
    public void moveLeft(){
        this.j--;
    } 
    public void moveRight(){
        this.j++;
    }

}
