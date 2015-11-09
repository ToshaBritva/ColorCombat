/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import com.opris.colorcombat.classes.Bonus;
import com.opris.colorcombat.classes.MapObject;
import com.opris.colorcombat.classes.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author boris
 */
@ServerEndpoint("/MainPage/Game/server")
public class SocketController {

    //Список игроков по умолчанию
    public static ArrayList<String> defaultPlayers = new ArrayList<String>() {
        {
            add("anton");
            add("boris");
            add("nizy");
            add("pukan");
        }
    };

    //Создаем игру
    public static com.opris.colorcombat.classes.Game currentGame = new com.opris.colorcombat.classes.Game(defaultPlayers);

    @OnMessage
    public void onMessage(String message, Session session) {

        try {
            //Парсим сообщение
            JSONParser parser = new JSONParser();
            JSONObject jsonMessage = (JSONObject) parser.parse(message);

            String target = (String) jsonMessage.get("target");
            //Проверяем цель сообщения
            switch (target) {
                case "movePlayer":
                    if (currentGame.isStarted()) {
                        //Двигаем игрока и получаем все изменения
                        List<MapObject> changes = currentGame.movePlayer(session.getUserPrincipal().getName(), (String) jsonMessage.get("value"));

                        //Рассылаем их
                        currentGame.sendChanges(changes);
                    }
                    break;
                case "startGame":
                    currentGame.end();
                    ArrayList<Session> listeners = currentGame.getListeners();
                    currentGame = new Game(defaultPlayers);
                    currentGame.setListeners(listeners);
                    List<MapObject> changes = currentGame.getWholeField();
                    currentGame.start();
                    currentGame.sendChanges(changes);
                    break;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        //Добавляем новго листенера
        currentGame.listeners.add(session);

        //Получем текущее состояние игры
        List<MapObject> changes = currentGame.getWholeField();

        //Отправляем его 
        currentGame.sendChanges(changes);
        
        for (Bonus b: currentGame.bonusesList){
            currentGame.sendSpawnBonus(b);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        currentGame.listeners.remove(session);
    }

}
