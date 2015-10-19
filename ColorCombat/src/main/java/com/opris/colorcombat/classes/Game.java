/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.websocket.Session;

/**
 *
 * @author user
 */
public class Game {

    final static int fieldSize = 10; //Размер поля

    private final int[][] fieldMatrix = new int[fieldSize][fieldSize]; //Текущая матрица игрового поля

    public final HashMap<Session, Player> players = new LinkedHashMap<>(); //Соответсвие сессий и игроков

    private final ArrayList<Integer> cellsNumbers = new ArrayList<>(); //Цифры соответсвующие клеткам

    private final ArrayList<Integer> playersNumbers = new ArrayList<>(); //Цифры соответсвующие игрокам

    public Game() {
        cellsNumbers.add(0);
    }

    //Добавляем нового игрока в игру
    public void addPlayer(Session session) {

        //Определяем его место на карте
        int i = 0, j = 0;
        switch (players.size() + 1) {
            case 1:
                i = 0;
                j = 0;
                break;
            case 2:
                i = 0;
                j = fieldSize - 1;
                break;
            case 3:
                i = fieldSize - 1;
                j = 0;
                break;
            case 4:
                i = fieldSize - 1;
                j = fieldSize - 1;
                break;
        }

        //Создаем объект соответсвующий новому игроку
        Player newPlayer = new Player(players.size() + 1, i, j);

        //Добавляем его в список игроков
        players.put(session, newPlayer);

        //Добавляем цифры которые будут соответсвовать игрокам на матрице
        playersNumbers.add(newPlayer.number);
        cellsNumbers.add(newPlayer.number + 4);

        //Отрисовываем игрока на матирце
        drawPlayerMatrix(newPlayer);

    }

    //Отрисовываем игрока на матрице
    private MapObject drawPlayerMatrix(Player player) {
        fieldMatrix[player.i][player.j] = player.number;
        return new MapObject(player.number, player.i, player.j);
    }

    //Отрисовываем клетку на матирце
    private MapObject drawCellMatrix(int i, int j, int playerNumber) {
        fieldMatrix[i][j] = cellsNumbers.get(playerNumber);
        return new MapObject(cellsNumbers.get(playerNumber), i, j);
    }

    //Возвращаем список игроков на поле
    public ArrayList<MapObject> getPlayers() {
        ArrayList<MapObject> arr = new ArrayList<MapObject>();
        for (Player p : players.values()) {
            arr.add(p);
        }
        return arr;

    }

    public ArrayList<MapObject> movePlayer(Session session, String direction) {

        //Получаем объект сходившего игрока
        Player player = players.get(session);

        //Изменения произведенные игроком
        ArrayList<MapObject> changes = new ArrayList<MapObject>();

        //Количество движений игрока
        int moves = 0;

        //Определяем в каком направлении идет игрок
        switch (direction) {
            case "up":
                while (canMoveUp(player) && moves < player.speed) {

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveUp();

                    //Отрисовываем изменения
                    changes.add(drawPlayerMatrix(player));

                    moves++;
                }
                break;
            case "down":
                while (canMoveDown(player) && moves < player.speed) {

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveDown();

                    //Отрисовываем изменения
                    changes.add(drawPlayerMatrix(player));

                    moves++;
                }
                break;
            case "left":
                while (canMoveLeft(player) && moves < player.speed) {

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveLeft();

                    //Отрисовываем изменения
                    changes.add(drawPlayerMatrix(player));

                    moves++;
                }
                break;
            case "right":
                while (canMoveRight(player) && moves < player.speed) {

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveRight();

                    //Отрисовываем изменения
                    changes.add(drawPlayerMatrix(player));

                    moves++;
                }
        }

        return changes;
    }

    //Проверяет может ли игрок двигаться
    private boolean canMoveUp(Player player) {
        return player.i >= 1 && !playersNumbers.contains(fieldMatrix[player.i - 1][player.j]);
    }

    private boolean canMoveDown(Player player) {
        return player.i <= fieldSize - 2 && !playersNumbers.contains(fieldMatrix[player.i + 1][player.j]);
    }

    private boolean canMoveLeft(Player player) {
        return player.j >= 1 && !playersNumbers.contains(fieldMatrix[player.i][player.j - 1]);
    }

    private boolean canMoveRight(Player player) {
        return player.j <= fieldSize - 2 && !playersNumbers.contains(fieldMatrix[player.i][player.j + 1]);
    }

    //Удаляем игрока из игры (для отладки)
    public void removePlayer(Session session) {
        Player player = players.get(session);
        cellsNumbers.remove(playersNumbers.indexOf(player.number) + 1);
        playersNumbers.remove(playersNumbers.indexOf(player.number));
        fieldMatrix[player.i][player.j] = 0;
        players.remove(session);
    }

    public ArrayList<MapObject> getWholeField() {
        ArrayList<MapObject> res = new ArrayList<MapObject>();
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                res.add(new MapObject(fieldMatrix[i][j], i, j));
            }
        }
        return res;
    }

}
