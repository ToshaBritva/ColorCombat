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
//Статичная коллекция бонусов, содержащая в себе все бонусы игры и их характеристики
public class BonusesCollection {

    public static ArrayList<Integer> bonusNumbers = new ArrayList<>();

    public static ArrayList<Bonus> bonusesList = new ArrayList<Bonus>() {
        {
            add(new Bonus(10, -1, -1, "Cross", 3, 0));
            add(new Bonus(11, -1, -1, "SpeedUp", 3, 5));
            add(new Bonus(12, -1, -1, "Freeze", 3, 5));
        }
    };

    public static Bonus getBonusCopy(int index) {
        Bonus originBonus = bonusesList.get(index);
        return new Bonus(originBonus.number, originBonus.i, originBonus.j,
                originBonus.name, originBonus.existTime, originBonus.effectTime);
    }

    public static boolean Contains(int number) {
        if (bonusesList.stream().anyMatch((b) -> (b.number == number))) {
            return true;
        }
        return false;
    }

}
