/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.websocket.Session;
import com.opris.colorcombat.classes.timers.*;
import static com.opris.colorcombat.classes.timers.GameCurrentTime.getCurrentTime;
import com.opris.colorcombat.controller.SocketController;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Game {

    final static int fieldSize = 10; //Размер поля

    private final int[][] fieldMatrix = new int[fieldSize][fieldSize]; //Текущая матрица игрового поля

    public final HashMap<String, Player> players = new LinkedHashMap<>(); //Соответсвие ников и игроков

    public ArrayList<Session> listeners = new ArrayList<>(); //Список сессий прослушивающих эту игру

    private final ArrayList<Integer> cellsNumbers = new ArrayList<>(); //Цифры соответсвующие клеткам

    private final ArrayList<Integer> playersNumbers = new ArrayList<>(); //Цифры соответсвующие игрокам

    private Timer timer = new Timer(); //Таймер игры

    private String status = "wait"; //Статус игры - ожидание, начата, закончена

    public ArrayList<Session> getListeners() {
        return listeners;
    }

    public void setListeners(ArrayList<Session> listeners) {
        this.listeners = listeners;
    }
    
    public Game(ArrayList<String> nicknames) {
        cellsNumbers.add(0);
        nicknames.forEach((nickname) -> addPlayer(nickname));
    }

    public String getStatus() {
        return status;
    }

    public boolean isStarted() {
        return status.equals("started");
    }

    //Начинаем новую игру(обнуляем таймер и очищаем поле)
    public void start() {
        //Переводим игру в статус 
        status = "started";
        
        //Создаем и запускаем задачу таймера
        timer.schedule(new GameSecondsTimer(this), 0, 1000);
    }

    //Заканчиваем игру и определяем победителя
    public void end() {
        
        status = "ended";
        timer.cancel();
        
        //Определяем победителя       
        Player winer = getWinner();

        //Формируем сообщения о завершении игры и победителе
        JsonObject endMessage = new JsonObject();
        JsonObject winerData = new JsonObject();
        winerData.addProperty("nickname", winer.getNickname());
        winerData.addProperty("score", winer.getScore());
        endMessage.addProperty("target", "endGame");
        endMessage.add("value", winerData);

        //Рассылаем победителей
        listeners.forEach((playerSession) -> {
            try {
                playerSession.getBasicRemote().sendText(endMessage.toString());
            } catch (Exception ex) {
            }
        });

    }

    //Определяем победителя в игре
    public Player getWinner(){
        
        Player winer = new Player(0, 0, 0, null);
        for (Player p : players.values()){
            if (p.getScore() > winer.getScore()) {
                winer = p;
            }
        }
        return winer;
    }
    
    //Посылаем время
    public void sendTime(String time) {
        JsonObject timeMessage = new JsonObject();
        timeMessage.addProperty("target", "time");
        timeMessage.addProperty("value", time);
        listeners.forEach((playerSession) -> {
            try {
                playerSession.getBasicRemote().sendText(timeMessage.toString());
            } catch (Exception ex) {
            }
        });
    }

    //Добавляем нового игрока в игру
    public void addPlayer(String nickname) {

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
        Player newPlayer = new Player(players.size() + 1, i, j, nickname);

        //Добавляем его в список игроков
        players.put(nickname, newPlayer);

        //Добавляем цифры которые будут соответсвовать игрокам на матрице
        playersNumbers.add(newPlayer.number);
        cellsNumbers.add(newPlayer.number + 4);

        //Отрисовываем игрока на матирце
        drawPlayerMatrix(newPlayer);

    }

    //Отрисовываем игрока на матрице
    private void drawPlayerMatrix(Player player) {
        fieldMatrix[player.i][player.j] = player.number;
    }

    //Отрисовываем клетку на матирце
    private MapObject drawCellMatrix(int i, int j, int playerNumber) {
        fieldMatrix[i][j] = cellsNumbers.get(playerNumber);
        return new MapObject(cellsNumbers.get(playerNumber), i, j);
    }

    //Получаем все объекты на поле
    public ArrayList<MapObject> getWholeField() {
        ArrayList<MapObject> res = new ArrayList<>();
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (playersNumbers.contains(fieldMatrix[i][j])) {
                    res.add(getPlayer(fieldMatrix[i][j]));
                } else {
                    res.add(new MapObject(fieldMatrix[i][j], i, j));
                }
            }
        }
        return res;
    }

    //Рассылаем изменения игрокам
    public void sendChanges(List<MapObject> changes) {
        try {
            
            // Преобразуем его в JSON и отправляем
            Gson gson = new Gson();
            String json = gson.toJson(changes);
            JsonObject moveMessage = new JsonObject();
            moveMessage.addProperty("target", "movePlayer");
            moveMessage.addProperty("value", json);

            //Рассылаем всем клиентам игроков
            listeners.forEach((playerSession) -> {
                try {
                    playerSession.getBasicRemote().sendText(moveMessage.toString());
                } catch (IOException ex) {
                    Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (Exception e) {
        }

    }

    //Возвращаем игрока по его номеру
    public Player getPlayer(int number) {
        for (Player p : players.values()) {
            if (p.number == number) {
                return p;
            }
        }
        return null;
    }

    //Двигаем игрока, возвращаем изменения
    public ArrayList<MapObject> movePlayer(String nickname, String direction) {

        //Получаем объект сходившего игрока
        Player player = players.get(nickname);

        //Изменения произведенные игроком
        ArrayList<MapObject> changes = new ArrayList<>();

        //Количество движений игрока
        int moves = 0;

        //Количество очков заработанных игроком за ход
        int score = 0;

        //Определяем в каком направлении идет игрок
        switch (direction) {
            case "up":
                while (canMoveUp(player) && moves < player.speed) {

                    //Если игрок хочет сходить на клетку
                    if (cellsNumbers.contains(fieldMatrix[player.i - 1][player.j])) {

                        //Получаем владельца той клетки на которую хочет сходить игрок
                        Player cellOwner = getPlayer(fieldMatrix[player.i - 1][player.j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            score++;
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                changes.add(cellOwner);
                                score++;
                            }
                        }

                        //Красим клетку и добавляем покрашенную клетку к изменениям
                        changes.add(drawCellMatrix(player.i, player.j, player.number));

                        //Двигаем игрока
                        player.moveUp();

                    }

                    moves++;
                }
                break;

            case "down":
                while (canMoveDown(player) && moves < player.speed) {

                    //Если игрок хочет сходить на клетку
                    if (cellsNumbers.contains(fieldMatrix[player.i + 1][player.j])) {

                        //Получаем владельца той клетки на которую хочет сходить игрок
                        Player cellOwner = getPlayer(fieldMatrix[player.i + 1][player.j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            score++;
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                changes.add(cellOwner);
                                score++;
                            }
                        }

                        //Красим клетку и добавляем покрашенную клетку к изменениям
                        changes.add(drawCellMatrix(player.i, player.j, player.number));

                        //Двигаем игрока
                        player.moveDown();

                    }

                    moves++;
                }
                break;
            case "left":
                while (canMoveLeft(player) && moves < player.speed) {

                    //Если игрок хочет сходить на клетку
                    if (cellsNumbers.contains(fieldMatrix[player.i][player.j - 1])) {

                        //Получаем владельца той клетки на которую хочет сходить игрок
                        Player cellOwner = getPlayer(fieldMatrix[player.i][player.j - 1] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            score++;
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                changes.add(cellOwner);
                                score++;
                            }
                        }

                        //Красим клетку и добавляем покрашенную клетку к изменениям
                        changes.add(drawCellMatrix(player.i, player.j, player.number));

                        //Двигаем игрока
                        player.moveLeft();

                    }

                    moves++;
                }
                break;
            case "right":
                while (canMoveRight(player) && moves < player.speed) {

                    //Если игрок хочет сходить на клетку
                    if (cellsNumbers.contains(fieldMatrix[player.i][player.j + 1])) {

                        //Получаем владельца той клетки на которую хочет сходить игрок
                        Player cellOwner = getPlayer(fieldMatrix[player.i][player.j + 1] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            score++;
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                changes.add(cellOwner);
                                score++;
                            }
                        }

                        //Красим клетку и добавляем покрашенную клетку к изменениям
                        changes.add(drawCellMatrix(player.i, player.j, player.number));

                        //Двигаем игрока
                        player.moveRight();

                    }
                    moves++;
                }
        }

        player.score += score;

        //Отрисовываем изменения
        drawPlayerMatrix(player);
        changes.add(player);

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

}
