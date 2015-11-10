/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes.timers;

import com.google.gson.reflect.TypeToken;
import com.opris.colorcombat.classes.Game;
import com.opris.colorcombat.classes.MapObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimerTask;

/**
 *
 * @author boris
 */
public class GameSecondsTimer extends TimerTask {

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

    private int bonusSpawnInterval = 1; //Интервал спавна бонуса

    private int minPlayersToStart = 2; //Минимальное количество игроков для того чтобы начать игру

    private Game game; //Игра

    private DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);//Форматтер времени
    private LocalTime endTime = LocalTime.parse("00:00:00", sdf); //Время окончания игры
    private LocalTime startTime = LocalTime.parse("00:00:30", sdf); //Начальное время

    private LocalTime maxWaitingTime = LocalTime.parse("00:00:10", sdf); //максимальное время ожидание игры
    private LocalTime waitingTime = LocalTime.parse("00:00:00", sdf); //текущее ожидание

    private LocalTime countdownEnd = LocalTime.parse("00:00:00", sdf); //Конец отсчета
    private LocalTime countdownStart = LocalTime.parse("00:00:05", sdf); //Начало отсчета

    public GameSecondsTimer(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        //В зависимости от статусы игры изменяем время
        switch (game.Status) {
            
            case  IN_PROGRESS:

                //Если игра уже в процессе, занимаемся бонусами и прочим
                if (endTime.equals(startTime)) {
//                    game.sendTime("time", startTime.format(sdf));
                    game.SendMessage("time", startTime.format(sdf), new TypeToken<String>(){}.getType());
                    game.End();
                } else {
                    //Проверяем бонусы на карте, уменьшаем время существования и т.д., удаляем не подобранные
                    game.checkBonuses();

                    //Спавним бонус
                    if (startTime.getSecond() % bonusSpawnInterval == 0) {
                        game.spawnRandomBonus();
                    }

                    //Отправляем время клиентам
//                    game.sendTime("time", startTime.format(sdf));
                    game.SendMessage("time", startTime.format(sdf), new TypeToken<String>(){}.getType());

                    //Уменьшаем текущее время
                    startTime = startTime.minusSeconds(1);
                }
                break;
                
            case WAITING:
                //Если максимальное время ожидание превышено, начинаем игру
                if (waitingTime.isAfter(maxWaitingTime) || game.GetListenPlayersCount() >= minPlayersToStart) {
                    game.Countdown();
                } else {
                    waitingTime = waitingTime.plusSeconds(1);
                }
                break;

            case COUNTDOWN:
                if (countdownStart.equals(countdownEnd)){
                    game.SendMessage("countdown", String.valueOf(countdownStart.getSecond()), new TypeToken<String>(){}.getType());
//                    game.sendTime("countdown", String.valueOf(countdownStart.getSecond()));
                    game.Start();
                } else {
                    game.SendMessage("countdown", String.valueOf(countdownStart.getSecond()), new TypeToken<String>(){}.getType());
//                    game.sendTime("countdown", String.valueOf(countdownStart.getSecond()));
                    countdownStart = countdownStart.minusSeconds(1);
                }
                break;

        }

    }

}
