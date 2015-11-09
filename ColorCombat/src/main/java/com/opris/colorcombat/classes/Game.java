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
import com.opris.colorcombat.controller.SocketController;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Game {

    private static int fieldSize = 10; //Размер поля

    private int[][] fieldMatrix = new int[fieldSize][fieldSize]; //Текущая матрица игрового поля

    public HashMap<String, Player> players = new LinkedHashMap<>(); //Соответсвие ников и игроков

    public ArrayList<Session> listeners = new ArrayList<>(); //Список сессий прослушивающих эту игру

    private ArrayList<Integer> cellsNumbers = new ArrayList<>(); //Цифры соответсвующие клеткам

    private ArrayList<Integer> playersNumbers = new ArrayList<>(); //Цифры соответсвующие игрокам

    private Timer timer = new Timer(); //Таймер игры

    private String status = "wait"; //Статус игры - ожидание, начата, закончена

    public ArrayList<Bonus> bonusesList = new ArrayList<>(); //Бонусы на карте

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
        //Сигнализируем очистить поля на клиенте
        JsonObject clearMessage = new JsonObject();
        clearMessage.addProperty("target", "clear");
        listeners.forEach((playerSession) -> {
            try {
                playerSession.getBasicRemote().sendText(clearMessage.toString());
            } catch (Exception ex) {
            }
        });
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
    public Player getWinner() {

        Player winer = new Player(0, 0, 0, null);
        for (Player p : players.values()) {
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

                    } else {
                        //Если эта клетка - бонус
                        if (BonusesCollection.Contains(fieldMatrix[player.i - 1][player.j])) {

                            //Получаем бонус на этой клетке
                            Bonus bonus = getBonusByCoordinates(player.i - 1, player.j);

                            //Применяем его к указанному игроку
                            changes.addAll(applyBonus(player, bonus));
                        }
                    }

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveUp();
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

                    } else {
                        //Если эта клетка - бонус
                        if (BonusesCollection.Contains(fieldMatrix[player.i + 1][player.j])) {

                            //Получаем бонус на этой клетке
                            Bonus bonus = getBonusByCoordinates(player.i + 1, player.j);

                            //Применяем его к указанному игроку
                            changes.addAll(applyBonus(player, bonus));
                        }
                    }

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveDown();
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

                    } else {
                        //Если эта клетка - бонус
                        if (BonusesCollection.Contains(fieldMatrix[player.i][player.j - 1])) {

                            //Получаем бонус на этой клетке
                            Bonus bonus = getBonusByCoordinates(player.i, player.j - 1);

                            //Применяем его к указанному игроку
                            changes.addAll(applyBonus(player, bonus));

                        }
                    }

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveLeft();
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

                    } else {
                        //Если эта клетка - бонус
                        if (BonusesCollection.Contains(fieldMatrix[player.i][player.j + 1])) {

                            //Получаем бонус на этой клетке
                            Bonus bonus = getBonusByCoordinates(player.i, player.j + 1);

                            //Применяем его к указанному игроку
                            changes.addAll(applyBonus(player, bonus));
                        }
                    }

                    //Красим клетку и добавляем покрашенную клетку к изменениям
                    changes.add(drawCellMatrix(player.i, player.j, player.number));

                    //Двигаем игрока
                    player.moveRight();
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

    //Спавнит случайны бонус на поле
    public void spawnRandomBonus() {

        if (getOnFieldBonusesCount() < BonusesCollection.bonusesList.size()) {
            //Инициализируем рандомайзер
            Random random = new Random();

            //Генерируем случайное число - индекс бонуса
            int bonusNumber = random.nextInt(BonusesCollection.bonusesList.size());
            while (isAlreadySpawned(bonusNumber)) {
                bonusNumber = random.nextInt(BonusesCollection.bonusesList.size());
            }

            //Генерируем координаты, пока не получим свободное место
            int iBonus, jBonus;
            do {
                iBonus = random.nextInt(fieldSize);
                jBonus = random.nextInt(fieldSize);
            } while (!isFree(iBonus, jBonus));

            //Создаем объект типа бонус
            Bonus bonus = BonusesCollection.getBonusCopy(bonusNumber);
            bonus.i = iBonus;
            bonus.j = jBonus;

            //Получаем владельца той клетки на которую хочет сходить игрок
            Player cellOwner = getPlayer(fieldMatrix[bonus.i][bonus.j] - 4);

            if (cellOwner != null) {
                cellOwner.score--;
                sendChanges(new ArrayList<MapObject>() {
                    {
                        add(cellOwner);
                    }
                });
            }

            //Отрисовываем его на поле
            fieldMatrix[bonus.i][bonus.j] = bonus.number;

            //Добавляем его в коллекцию бонусов
            bonusesList.add(bonus);

            //Отправляем бонус клиентам
            sendSpawnBonus(bonus);
        }

    }

    //Рассылает игрокам сообщение о новом бонусе
    public void sendSpawnBonus(Bonus bonus) {
        try {

            // Преобразуем его в JSON и отправляем
            Gson gson = new Gson();
            String json = gson.toJson(bonus);
            JsonObject moveMessage = new JsonObject();
            moveMessage.addProperty("target", "spawnBonus");
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

    //Рассылает игрокам сообщение об удалении бонуса
    private void sendRemoveBonus(Bonus bonus) {
        try {

            // Преобразуем его в JSON и отправляем
            Gson gson = new Gson();
            String json = gson.toJson(bonus);
            JsonObject moveMessage = new JsonObject();
            moveMessage.addProperty("target", "removeBonus");
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

    //Проверяет свободна ли клетка i, j
    private boolean isFree(int i, int j) {
        return cellsNumbers.contains(fieldMatrix[i][j]);
    }

    //Проверяет лежит ли на поле указанный бонус
    private boolean isAlreadySpawned(int number) {
        for (Bonus b : bonusesList) {
            if (b.number == BonusesCollection.bonusesList.get(number).number && !b.picked) {
                return true;
            }
        }
        return false;
    }

    //Получает количество бонусов на поле
    private int getOnFieldBonusesCount() {
        int count = 0;
        for (Bonus b : bonusesList) {
            if (!b.picked) {
                count++;
            }
        }
        return count;
    }

    //Изменяет время существования бонусов, удаляет те которые не подобрали
    public void checkBonuses() {

        //Пробегаем коллецию с помощью итератора (для безопасного удаления элементов)
        for (Iterator<Bonus> iterator = bonusesList.iterator(); iterator.hasNext();) {

            //Получаем сделующий элемент коллекции
            Bonus bonus = iterator.next();

            if (!bonus.picked) {
                if (bonus.existTime <= 0) {
                    //Убираем его с карты
                    fieldMatrix[bonus.i][bonus.j] = 0;

                    //Отправляем сообщение клиентам
                    sendRemoveBonus(bonus);
                    iterator.remove();
                } else {
                    //Если бонус не подобран, уменьшаем его время существования
                    bonus.existTime--;
                }
            } else {
                if (bonus.effectTime <= 0) {
                    //Восстанавливаем состояние игрока до бонуса
                    bonus.affectedPlayers.forEach((player) -> {
                        player.restoreDefaultState();
                    });

                    //Отправляем сообщение клиентам
                    sendRemoveBonus(bonus);
                    iterator.remove();
                } else {
                    bonus.effectTime--;
                }

            }
            //Если бонус не подобран слишком долгое время, удаляем его

        }
    }

    public Bonus getBonusByCoordinates(int i, int j) {
        for (Bonus b : bonusesList) {
            if (b.i == i && b.j == j && b.number == fieldMatrix[i][j]) {
                return b;
            }
        }
        return null;
    }

    public List<MapObject> applyBonus(Player player, Bonus bonus) {

        //Изменения на поле произошедшие в реузльтате подбора бонуса
        List<MapObject> changes = new LinkedList<>();

        //Отмчеаем что бонус подобран
        bonus.picked = true;

        //Выполняем действие взависимости от вида бонуса
        switch (bonus.name) {
            case "Cross": //Бонус крест - красит вертикаль и горизонталь

                ArrayList<Player> changedPlayers = new ArrayList<>();

                //Красим горизонталь
                for (int i = 0; i < fieldSize; i++) {

                    if (cellsNumbers.contains(fieldMatrix[i][bonus.j])) {
                        Player cellOwner = getPlayer(fieldMatrix[i][bonus.j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            player.score++;
                            changes.add(drawCellMatrix(i, bonus.j, player.number));
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                player.score++;
                                changes.add(drawCellMatrix(i, bonus.j, player.number));

                                //Если игрок уже есть в списке изменений, то не добавляем его
                                if (!changes.stream().anyMatch((ch) -> ch.number == cellOwner.number)) {
                                    changedPlayers.add(cellOwner);
                                }
                            }
                        }
                    }
                }

                //Красим горизонталь
                for (int j = 0; j < fieldSize; j++) {
                    if (cellsNumbers.contains(fieldMatrix[bonus.i][j])) {
                        Player cellOwner = getPlayer(fieldMatrix[bonus.i][j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            player.score++;
                            changes.add(drawCellMatrix(bonus.i, j, player.number));
                        } else {
                            if (cellOwner != player) {
                                //Уменьшаем очки владельца и увеличиваем очки ходившего
                                cellOwner.score--;
                                player.score++;
                                changes.add(drawCellMatrix(bonus.i, j, player.number));

                                if (!changes.stream().anyMatch((ch) -> ch.number == cellOwner.number)) {
                                    changedPlayers.add(cellOwner);
                                }
                            }
                        }
                    }
                }
                changes.addAll(changedPlayers);

                break;
            case "SpeedUp":
                player.score++;
                bonus.affectedPlayers.add(player);
                player.speed = player.speed * 2;
                break;
            case "Freeze":
                player.score++;
                for (Player p : players.values()) {
                    if (p != player) {
                        p.speed = 0;
                    }
                    bonus.affectedPlayers.add(p);
                }
                break;
        }
        //Убираем бонус с карты
        fieldMatrix[bonus.i][bonus.j] = 0;
        sendRemoveBonus(bonus);

        return changes;
    }

}
