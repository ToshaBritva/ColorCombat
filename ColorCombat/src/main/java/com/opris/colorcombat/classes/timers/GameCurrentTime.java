/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes.timers;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author boris
 */
public class GameCurrentTime {

    public static LocalTime getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String strDate = simpleDateFormat.format(calendar.getTime());
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
        LocalTime currentTime = LocalTime.parse(strDate, sdf);
        return currentTime;
    }
}
