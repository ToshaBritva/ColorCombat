/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import com.opris.colorcombat.classes.MapObject;
import com.google.gson.Gson;
import com.opris.colorcombat.classes.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

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

        //Двигаем игрока и получаем все изменения
        List<MapObject> changes = currentGame.movePlayer(session.getUserPrincipal().getName(), message);
        
        //Рассылаем их
        sendChanges(currentGame, changes);  
    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {

        //Добавляем новго листенера
        currentGame.listeners.add(session);
        
        //Получем текущее состояние игры
        List<MapObject> changes = currentGame.getWholeField();
        
        //Отправляем его 
        sendChanges(currentGame, changes);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        currentGame.listeners.remove(session);
    }
    
    //Рассылаем изменения игрокам
    private void sendChanges(Game game, List<MapObject> changes){
        
        // Преобразуем его в JSON и отправляем
        Gson gson = new Gson();
        String json = gson.toJson(changes);
        
        //Рассылаем всем клиентам игроков
        game.listeners.forEach((playerSession) -> {
            try {
                playerSession.getBasicRemote().sendText(json);
            } catch (IOException ex) {
                Logger.getLogger(SocketController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
