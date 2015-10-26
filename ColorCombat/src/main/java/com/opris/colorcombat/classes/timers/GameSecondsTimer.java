/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes.timers;

import com.opris.colorcombat.classes.Game;
import static com.opris.colorcombat.classes.timers.GameCurrentTime.getCurrentTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimerTask;

/**
 *
 * @author boris
 */
public class GameSecondsTimer extends TimerTask {

    private LocalTime startTime;
    private Game game = null;

    public GameSecondsTimer(LocalTime startTime, Game game) {
        this.startTime = startTime;
        this.game = game;
    }

    @Override
    public void run() {
        //Получаем тукущее время
        LocalTime currentTime = getCurrentTime();
        currentTime = currentTime.minusMinutes(startTime.getMinute()).minusSeconds(startTime.getSecond()).minusHours(startTime.getHour());
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
        LocalTime gameLongest = LocalTime.parse("00:00:30", sdf);
        if (currentTime.isAfter(gameLongest)) {
            game.end();
        } else {
            game.sendTime(currentTime.format(sdf));
        }
    }

}
