/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes.timers;

import com.opris.colorcombat.classes.Game;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;

/**
 *
 * @author boris
 */
public class GameSecondsTimer extends TimerTask {

    //Статусы игры
    public enum GameStatus {

        WAITING("Ожидание игроков"),
        IN_PROGRESS("В процессе"),
        ENDED("Завершена");

        private final String name;

        private GameStatus(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    private int bonusSpawnInterval = 3; //Интервал спавна бонуса

    private int minPlayersToStart = 2; //Минимальное количество игроков для того чтобы начать игру

    private Game game; //Игра

    private DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);//Форматтер времени
    private LocalTime endTime = LocalTime.parse("00:00:00", sdf); //Время окончания игры
    private LocalTime startTime = LocalTime.parse("00:00:30", sdf); //Начальное время

    private LocalTime maxWaitingTime = LocalTime.parse("00:00:10", sdf);
    private LocalTime waitingTime = LocalTime.parse("00:00:00", sdf);

    public GameSecondsTimer(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        //В зависимости от статусы игры изменяем время
        switch (game.Status) {
            case WAITING:

                //Если максимальное время ожидание превышено, начинаем игру
                if (waitingTime.isAfter(maxWaitingTime) || game.GetListenPlayersCount() >= minPlayersToStart) {
                    game.Start();
                } else {
                    waitingTime = waitingTime.plusSeconds(1);
                }

                break;
            case IN_PROGRESS:

                //Если игра уже в процессе, занимаемся бонусами и прочим
                if (endTime.equals(startTime)) {
                    game.sendTime(startTime.format(sdf));
                    game.End();
                } else {

                    //Проверяем бонусы на карте, уменьшаем время существования и т.д., удаляем не подобранные
                    game.checkBonuses();

                    if (startTime.getSecond() % bonusSpawnInterval == 0) {
                        game.spawnRandomBonus();
                    }

                    //Отправляем время клиентам
                    game.sendTime(startTime.format(sdf));

                    startTime = startTime.minusSeconds(1);
                }
                break;
        }

    }

}
