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
public class MapObject {

    public int number; //Номер
    public int i; //Координата в матрице (Y)
    public int j; //Координата в матрице (X)

    public MapObject(int number, int i, int j) {
        this.number = number;
        this.i = i;
        this.j = j;
    }

}
