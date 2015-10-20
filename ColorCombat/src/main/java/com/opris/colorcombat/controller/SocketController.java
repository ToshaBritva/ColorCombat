/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import com.opris.colorcombat.classes.MapObject;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
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
@ServerEndpoint("/game/server")
public class SocketController {

    
    public static com.opris.colorcombat.classes.Game currentGame = new com.opris.colorcombat.classes.Game();

    @OnMessage
    public void onMessage(String message, Session session) {

        //Двигаем игрока и получаем все изменения
        List<MapObject> changes = currentGame.movePlayer(session, message);

        // Преобразуем их в JSON и отправляем
        Gson gson = new Gson();
        String json = gson.toJson(changes);

        //Рассылаем всем клиентам игроков
        try {
            for (Session playerSession : currentGame.players.keySet()) {
                playerSession.getBasicRemote().sendText(json);
            }
        } catch (Exception e) {

        }

    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {

        //Добавляем новго игрока
        currentGame.addPlayer(session);

        //Получаем список игроков на поле
//        List<MapObject> changes = currentGame.getPlayers();
        List<MapObject> changes = currentGame.getWholeField();
        // Преобразуем его в JSON и отправляем
        Gson gson = new Gson();
        String json = gson.toJson(changes);

        //Рассылаем всем клиентам игроков
        try {
            for (Session playerSession : currentGame.players.keySet()) {
                playerSession.getBasicRemote().sendText(json);
            }
        } catch (Exception e) {

        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

        currentGame.removePlayer(session);
        if (currentGame.players.size() >= 1) {
            List<MapObject> changes = currentGame.getWholeField();
            // Преобразуем его в JSON и отправляем
            Gson gson = new Gson();
            String json = gson.toJson(changes);

            //Рассылаем всем клиентам игроков
            try {
                for (Session playerSession : currentGame.players.keySet()) {
                    playerSession.getBasicRemote().sendText(json);
                }
            } catch (Exception e) {

            }
        }
        else
        {
            currentGame = new com.opris.colorcombat.classes.Game();
        }

    }
}
