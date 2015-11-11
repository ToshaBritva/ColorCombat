/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import javax.websocket.Session;
import com.opris.colorcombat.classes.timers.*;
import com.opris.colorcombat.controller.SocketController;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author user
 */
public class Game {

    //Статусы игры
    public enum GameStatus {

        WAITING("Ожидание игроков"),
        COUNTDOWN("Обратный отсчет"),
        IN_PROGRESS("В процессе"),
        ENDED("Игра окончена");

        private final String name;

        private GameStatus(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    private Random gameRandom = new Random(); //Генератор случайных чисел для игры

    private static int bonusSpawnProbability = 20; //Вероятность спавна бонуса

    private static int fieldSize = 10; //Размер поля

    private int[][] fieldMatrix = new int[fieldSize][fieldSize]; //Текущая матрица игрового поля

    public ArrayList<Player> players = new ArrayList<>(); //Соответсвие ников и игроков

    public ArrayList<Bonus> bonusesList = new ArrayList<>(); //Бонусы на карте

    private ArrayList<Session> listeners = new ArrayList<>(); //Список сессий прослушивающих эту игру

    private ArrayList<Integer> cellsNumbers = new ArrayList<>(); //Цифры соответсвующие клеткам

    private ArrayList<Integer> playersNumbers = new ArrayList<>(); //Цифры соответсвующие игрокам

    private Timer timer = new Timer(); //Таймер игры

    public GameStatus Status = GameStatus.WAITING; //Статус игры - ожидание, начата, закончена

    //**********************************************************************
    //************************СОЗДАНИЕ ИГРЫ*********************************
    //**********************************************************************
    //Конструктор игры, создает игру с указанными игроками
    public Game(ArrayList<String> nicknames) {

        //Добавляем игроков
        cellsNumbers.add(0);
        nicknames.forEach((nickname) -> addPlayer(nickname));

        //Создаем и запускаем задачу таймера
        timer.schedule(new GameSecondsTimer(this), 0, 1000);
    }

    //Добавляем нового игрока в игру
    private void addPlayer(String nickname) {

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
        players.add(newPlayer);

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

    //Отрисовываем клетку в цвет игрока на матирце
    private MapObject drawCellMatrix(int i, int j, Player player) {
        fieldMatrix[i][j] = cellsNumbers.get(player.paintingNumber);
        return new MapObject(cellsNumbers.get(player.paintingNumber), i, j);
    }

    //**********************************************************************
    //***********************РАССЫЛКА СООБЩЕНИЙ*****************************
    //**********************************************************************
    //Отправляем листенеру текущее состояние игры
    public void SendCurrentGameState(Session session) {

        //Отправляем теукщее состояние поля
        SendMessage("movePlayer", getWholeField(), new TypeToken<ArrayList<MapObject>>() {
        }.getType(), session);

        //Отправляем всех игроков на поле
        SendMessage("movePlayer", players, new TypeToken<ArrayList<Player>>() {
        }.getType(), session);

        //Отправляем все бонусы на поле
        SendMessage("spawnBonus", bonusesList, new TypeToken<ArrayList<Bonus>>() {
        }.getType());

        //Отправляем статус игры
        SendMessage("gameStatus", Status.toString(), new TypeToken<String>() {
        }.getType());

    }

    //Рассылка всем клиентам сообщения с целью target и содержимым obj
    public void SendMessage(String target, Object obj, Type objType) {

        // Преобразуем его в JSON и отправляем
        Gson gson = new Gson();
        String json = gson.toJson(obj, objType);

        JsonObject timeMessage = new JsonObject();
        timeMessage.addProperty("target", target);
        timeMessage.addProperty("value", json);
        listeners.forEach((playerSession) -> {
            try {
                playerSession.getBasicRemote().sendText(timeMessage.toString());
            } catch (Exception ex) {
            }
        });
    }

    //Отправляем листенеру сообщение с целью target и содержимым obj
    public void SendMessage(String target, Object obj, Type objType, Session session) {
        // Преобразуем его в JSON и отправляем
        Gson gson = new Gson();
        String json = gson.toJson(obj, objType);

        JsonObject timeMessage = new JsonObject();
        timeMessage.addProperty("target", target);
        timeMessage.addProperty("value", json);
        try {
            session.getBasicRemote().sendText(timeMessage.toString());
        } catch (Exception ex) {
        }
    }

    //**********************************************************************
    //***********************ДВИЖЕНИЕ ИГРОКА********************************
    //**********************************************************************
    //Двигаем игрока, возвращаем изменения
    public void movePlayer(String nickname, String direction) {

        //Получаем объект сходившего игрока
        Player player = getPlayerByNickname(nickname);

        //Получаем игрока очки которого будем менять
        Player changindScorePlayer = getPlayerByNumber(player.paintingNumber);

        //Изменения произведенные игроком
        ArrayList<MapObject> changes = new ArrayList<>();

        //Изменения игроков
        ArrayList<Player> playersChanges = new ArrayList<>();

        //Количество движений игрока
        int moves = 0;

        //Количество очков заработанных игроком за ход
        int score = 0;

        int iTargetCell = 0, jTargetCell = 0;

        switch (direction) {
            case "up":
                iTargetCell = player.i - 1;
                jTargetCell = player.j;
                break;
            case "down":
                iTargetCell = player.i + 1;
                jTargetCell = player.j;
                break;
            case "right":
                iTargetCell = player.i;
                jTargetCell = player.j + 1;
                break;
            case "left":
                iTargetCell = player.i;
                jTargetCell = player.j - 1;
                break;
        }

        while (canMove(player, direction) && moves < player.speed) {

            //Если игрок хочет сходить на клетку
            if (cellsNumbers.contains(fieldMatrix[iTargetCell][jTargetCell])) {

                //Получаем владельца той клетки на которую хочет сходить игрок
                Player cellOwner = getPlayerByNumber(fieldMatrix[iTargetCell][jTargetCell] - 4);

                //Если клетка пустая
                if (cellOwner == null) {
                    score++;
                } else {
                    //Уменьшаем очки владельца и увеличиваем очки ходившего
                    if (cellOwner != player) {
                        cellOwner.score--;
                        playersChanges.add(cellOwner);
                        score++;
                    }
                }

            } else {
                //Если эта клетка - бонус
                if (BonusesCollection.Contains(fieldMatrix[iTargetCell][jTargetCell])) {

                    //Получаем бонус на этой клетке
                    Bonus bonus = getBonusByCoordinates(iTargetCell, jTargetCell);

                    //Увеличиваем очки игрока
                    player.score++;

                    //Применяем его к указанному игроку
                    applyBonus(player, bonus);
                }
            }

            //Красим клетку и добавляем покрашенную клетку к изменениям
            changes.add(drawCellMatrix(player.i, player.j, player));

            //Двигаем игрока
            player.move(direction);
            moves++;
        }

        changindScorePlayer.score += score;

        //Отрисовываем изменения
        drawPlayerMatrix(player);

        playersChanges.add(player);
        playersChanges.add(changindScorePlayer);

        if (moves > 0) {
            //Отправляем все изменения игроков
            SendMessage("movePlayer", playersChanges, new TypeToken<ArrayList<Player>>() {
            }.getType());

            //Отправляем все изменения игроков
            SendMessage("movePlayer", changes, new TypeToken<ArrayList<MapObject>>() {
            }.getType());
        }

    }

    //Проверяет может ли игрок двигаться в указанном направлении
    private boolean canMove(Player player, String direction) {
        switch (direction) {
            case "up":
                return player.i >= 1 && !playersNumbers.contains(fieldMatrix[player.i - 1][player.j]);
            case "down":
                return player.i <= fieldSize - 2 && !playersNumbers.contains(fieldMatrix[player.i + 1][player.j]);
            case "left":
                return player.j >= 1 && !playersNumbers.contains(fieldMatrix[player.i][player.j - 1]);
            case "right":
                return player.j <= fieldSize - 2 && !playersNumbers.contains(fieldMatrix[player.i][player.j + 1]);
        }
        return false;
    }

    //**********************************************************************
    //****************************БОНУСЫ************************************
    //**********************************************************************
    //Спавнит случайны бонус на поле
    public void spawnRandomBonus() {

        //Спавним с определенной вероятностью бонус
        if (gameRandom.nextInt(100) < bonusSpawnProbability) {
            if (getOnFieldBonusesCount() < BonusesCollection.bonusesList.size()) {

                //Генерируем случайное число - индекс бонуса
                int bonusNumber = gameRandom.nextInt(BonusesCollection.bonusesList.size());
                while (isAlreadySpawned(bonusNumber)) {
                    bonusNumber = gameRandom.nextInt(BonusesCollection.bonusesList.size());
                }

                //Генерируем координаты, пока не получим свободное место
                int iBonus, jBonus;
                do {
                    iBonus = gameRandom.nextInt(fieldSize);
                    jBonus = gameRandom.nextInt(fieldSize);
                } while (!isCell(iBonus, jBonus));

                //Создаем объект типа бонус
                Bonus bonus = BonusesCollection.getBonusCopy(bonusNumber);
                bonus.i = iBonus;
                bonus.j = jBonus;

                //Получаем владельца той клетки на которую хочет сходить игрок
                Player cellOwner = getPlayerByNumber(fieldMatrix[bonus.i][bonus.j] - 4);

                if (cellOwner != null) {
                    cellOwner.score--;
                    ArrayList<Player> playerList = new ArrayList<Player>() {
                        {
                            add(cellOwner);
                        }
                    };

                    SendMessage("movePlayer", playerList, new TypeToken<ArrayList<Player>>() {
                    }.getType());
                }

                //Отрисовываем его на поле
                fieldMatrix[bonus.i][bonus.j] = bonus.number;

                //Добавляем его в коллекцию бонусов
                bonusesList.add(bonus);

                ArrayList<Bonus> bonusList = new ArrayList<Bonus>() {
                    {
                        add(bonus);
                    }
                };

                SendMessage("spawnBonus", bonusList, new TypeToken<ArrayList<Bonus>>() {
                }.getType());

            }
        }
    }

    //Применяем изменения бонуса
    public void applyBonus(Player player, Bonus bonus) {

        //Изменения на поле произошедшие в реузльтате подбора бонуса
        ArrayList<MapObject> changes = new ArrayList<>();

        //Изменения игроков
        ArrayList<MapObject> playerChanges = new ArrayList<>();

        //Отмчеаем что бонус подобран
        bonus.picked = true;

        //Выполняем действие взависимости от вида бонуса
        switch (bonus.name) {
            case "Cross": //Бонус крест - красит вертикаль и горизонталь

                ArrayList<Player> changedPlayers = new ArrayList<>();

                //Красим горизонталь
                for (int i = 0; i < fieldSize; i++) {

                    if (cellsNumbers.contains(fieldMatrix[i][bonus.j])) {
                        Player cellOwner = getPlayerByNumber(fieldMatrix[i][bonus.j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            player.score++;
                            changes.add(drawCellMatrix(i, bonus.j, player));
                        } else {
                            //Уменьшаем очки владельца и увеличиваем очки ходившего
                            if (cellOwner != player) {
                                cellOwner.score--;
                                player.score++;
                                changes.add(drawCellMatrix(i, bonus.j, player));

                                //Если игрок уже есть в списке изменений, то не добавляем его
                                if (!changes.stream().anyMatch((ch) -> ch.number == cellOwner.number)) {
                                    playerChanges.add(cellOwner);
                                }
                            }
                        }
                    }
                }

                //Красим горизонталь
                for (int j = 0; j < fieldSize; j++) {
                    if (cellsNumbers.contains(fieldMatrix[bonus.i][j])) {
                        Player cellOwner = getPlayerByNumber(fieldMatrix[bonus.i][j] - 4);

                        //Если клетка пустая
                        if (cellOwner == null) {
                            player.score++;
                            changes.add(drawCellMatrix(bonus.i, j, player));
                        } else {
                            if (cellOwner != player) {
                                //Уменьшаем очки владельца и увеличиваем очки ходившего
                                cellOwner.score--;
                                player.score++;
                                changes.add(drawCellMatrix(bonus.i, j, player));

                                if (!changes.stream().anyMatch((ch) -> ch.number == cellOwner.number)) {
                                    playerChanges.add(cellOwner);
                                }
                            }
                        }
                    }
                }
                changes.addAll(changedPlayers);
                bonusesList.remove(bonus);
                break;
            case "SpeedUp":
                bonus.affectedPlayers.add(player);
                player.speed = player.speed * 2;
                break;
            case "Freeze":
                players.stream().map((p) -> {
                    if (p != player) {
                        p.speed = 0;
                    }
                    return p;
                }).forEach((p) -> {
                    bonus.affectedPlayers.add(p);
                });
                break;
            case "ReversePainting":
                players.stream().map((p) -> {
                    if (p != player) {
                        p.paintingNumber = player.paintingNumber;
                    }
                    return p;
                }).forEach((p) -> {
                    bonus.affectedPlayers.add(p);
                });
                playerChanges.addAll(bonus.affectedPlayers);
                break;
        }

        //Убираем бонус с карты
        fieldMatrix[bonus.i][bonus.j] = 0;
        SendMessage("removeBonus", bonus, new TypeToken<Bonus>() {
        }.getType());

        //Отправляем все изменения на карте
        if (changes.size() > 0) {
            SendMessage("movePlayer", changes, new TypeToken<ArrayList<MapObject>>() {
            }.getType());
        }

        //Отправляем все изменения игроков
        if (playerChanges.size() > 0) {
            SendMessage("movePlayer", playerChanges, new TypeToken<ArrayList<Player>>() {
            }.getType());
        }
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
                    SendMessage("removeBonus", bonus, new TypeToken<Bonus>() {
                    }.getType());

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

                    //Изменяем состояние игроков
                    SendMessage("movePlayer", bonus.affectedPlayers, new TypeToken<ArrayList<Player>>() {
                    }.getType());

                    iterator.remove();
                } else {
                    bonus.effectTime--;
                }

            }
        }
    }

    //Получаем бонус который находиться в указанной точке
    public Bonus getBonusByCoordinates(int i, int j) {
        for (Bonus b : bonusesList) {
            if (b.i == i && b.j == j && b.number == fieldMatrix[i][j]) {
                return b;
            }
        }
        return null;
    }

    //Проверяет свободна ли клетка i, j
    private boolean isCell(int i, int j) {
        return cellsNumbers.contains(fieldMatrix[i][j]);
    }

    //Проверяет лежит ли на поле указанный бонус
    private boolean isAlreadySpawned(int number) {
        return bonusesList.stream().anyMatch((b) -> (b.number == BonusesCollection.bonusesList.get(number).number && !b.picked));
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

    //**********************************************************************
    //**********************ИЗМЕНЕНИЕ СТАТУСЫ ИГРЫ**************************
    //**********************************************************************
    //Заканчиваем игру и определяем победителя
    public void End() {

        //Останавливаем таймер игры
        timer.cancel();

        //Изменяем статус игры
        Status = GameStatus.ENDED;

        //Рассыалем изменение статуса
        SendMessage("gameStatus", Status.toString(), new TypeToken<String>() {
        }.getType());

        //Заносим результаты в БД
        resultInDB();

        Player winner = getWinner();
        SendMessage("winner", winner, new TypeToken<Player>() {
        }.getType());

        SocketController.destroyGame(this);

    }

    //Заносим результаты в БД
    private void resultInDB() {
        try {
            InitialContext co = new InitialContext();
            DataSource ds = (DataSource) co.lookup("jdbc/ColComDS");
            Connection conn = ds.getConnection();
            PreparedStatement psUp = conn.prepareStatement("UPDATE ColorCombatDB.USER SET RATING=? WHERE NICKNAME=?");
            PreparedStatement psSel = conn.prepareStatement("SELECT RATING, ID FROM ColorCombatDB.USER WHERE NICKNAME=?");
            PreparedStatement psHis = conn.prepareStatement("INSERT INTO ColorCombatDB.GAMEHISTORY (DATE, ID_USER, SCORE, RESULT) VALUES (?, ?, ?, ?)");
            for (Player p : players) {
                psSel.setString(1, p.getNickname());
                ResultSet rs = psSel.executeQuery();
                int olaRating = 0;
                int idUser = 0;
                if (rs.next()) {
                    olaRating = rs.getInt("RATING");
                    idUser = rs.getInt("ID");
                }
                psUp.setInt(1, olaRating + p.getScore());
                psUp.setString(2, p.getNickname());
                psUp.executeUpdate();

                psHis.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
                psHis.setInt(2, idUser);
                psHis.setInt(3, p.score);
                psHis.setBoolean(4, isWiner(p));
                psHis.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Определяем помебедителя поэкземпляру класс игрока
    private boolean isWiner(Player p) {
        return p.equals(getWinner());
    }

    //Определяем победителя в игре
    public Player getWinner() {

        Player winer = new Player(0, 0, 0, null);
        for (Player p : players) {
            if (p.getScore() > winer.getScore()) {
                winer = p;
            }
        }
        return winer;
    }

    //Возвращает статус игры
    public GameStatus getStatus() {
        return Status;
    }

    //Начинаем новую игру(обнуляем таймер и очищаем поле)
    public void Start() {

        //Переводим игру в статус 
        Status = GameStatus.IN_PROGRESS;

        //Отправляем статус игрокам
        SendMessage("gameStatus", Status.toString(), new TypeToken<String>() {
        }.getType());

    }

    //Обратный отсчет
    public void Countdown() {

        //Переводим игру в статус 
        Status = GameStatus.COUNTDOWN;

        //Отправляем статус игрокам
        SendMessage("gameStatus", Status.toString(), new TypeToken<String>() {
        }.getType());
    }

    //Возвращает начата ли игра
    public boolean IsStarted() {
        return Status.equals(GameStatus.IN_PROGRESS);
    }

    //**********************************************************************
    //************************ВСЯКИЕ ПРОЧИЕ ШТУКИ***************************
    //**********************************************************************
    //Возрващает всех слушателей игры
    public ArrayList<Session> getListeners() {
        return listeners;
    }

    //Устанавливает слушателей игры
    public void setListeners(ArrayList<Session> listeners) {
        this.listeners = listeners;
    }

    //Возвращаем игрока по его номеру
    public Player getPlayerByNumber(int number) {
        for (Player p : players) {
            if (p.number == number) {
                return p;
            }
        }
        return null;
    }

    //Возвращаем игро по его никнейму
    public Player getPlayerByNickname(String nickname) {
        for (Player p : players) {
            if (p.nickname.equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    //Получаем все объекты на поле (бонусы и клетки)
    public ArrayList<MapObject> getWholeField() {
        ArrayList<MapObject> res = new ArrayList<>();
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (cellsNumbers.contains(fieldMatrix[i][j]) || BonusesCollection.Contains(fieldMatrix[i][j])) {
                    res.add(new MapObject(fieldMatrix[i][j], i, j));
                }
            }
        }
        return res;
    }

    //Добавляет нового листенера
    public void AddListener(Session session) {
        listeners.add(session);
    }

    //Убирает листенера
    public void RemoveListener(Session session) {
        listeners.remove(session);
    }

    //Возвращает количество листенеров
    public int ListenersCount() {
        return listeners.size();
    }

    //Возвращает количество реально слушающих игроков
    public int GetListenPlayersCount() {

        HashSet<String> nicknames = new HashSet<>();

        listeners.stream().forEach((s) -> {
            nicknames.add(s.getUserPrincipal().getName());
        });

        return nicknames.size();

    }

    //Возвращаем список имен игроков
    public ArrayList<String> GetPlayersNicknames() {
        ArrayList<String> playersNicknames = new ArrayList<>();
        players.stream().forEach((p) -> {
            playersNicknames.add(p.nickname);
        });
        return playersNicknames;

    }
}
