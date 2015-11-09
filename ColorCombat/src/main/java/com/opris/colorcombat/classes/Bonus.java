/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import java.util.ArrayList;

/**
 *
 * @author AntonBritva
 */
public class Bonus extends MapObject{
    
    //Название бонуса
    public String name;
    
    //Время существования бонуса
    public int existTime;
    
    //Время действия бонуса
    public int effectTime;
    
    //Флаг подобран ли бонус игроком
    public Boolean picked = false;
    
    public ArrayList<Player> affectedPlayers = new ArrayList<>();
    
    //Конструкторы класса
    public Bonus(int bonusNumber, int i, int j, String bonusName, int existTime, int effectTime){
        super(bonusNumber, i, j);
        this.name = bonusName;
        this.existTime = existTime;
        this.effectTime = effectTime;
    }
    
}
