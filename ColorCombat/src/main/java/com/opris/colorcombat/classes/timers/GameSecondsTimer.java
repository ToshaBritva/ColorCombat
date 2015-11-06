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

    
    private Game game; //Игра
    private DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
    private LocalTime endTime = LocalTime.parse("00:00:00", sdf); //Время окончания игры
    private LocalTime startTime = LocalTime.parse("00:00:30", sdf); //Начальное время

    public GameSecondsTimer(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        if (endTime.equals(startTime)) {  
            game.sendTime(startTime.format(sdf));
            game.end();
        } else {
            game.sendTime(startTime.format(sdf));
            startTime = startTime.minusSeconds(1);
        }
    }

}
