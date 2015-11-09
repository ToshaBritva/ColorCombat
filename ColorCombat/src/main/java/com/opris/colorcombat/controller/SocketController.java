/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import com.opris.colorcombat.classes.Bonus;
import com.opris.colorcombat.classes.MapObject;
import com.opris.colorcombat.classes.Game;
import com.opris.colorcombat.classes.Lobby;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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

    static HashMap<String, Game> games = new HashMap<>();
    //Список игроков по умолчанию
//    public static ArrayList<String> defaultPlayers = new ArrayList<String>() {
//        {
//            add("anton");
//            add("boris");
//            add("nizy");
//            add("pukan");
//        }
//    };

    //Создаем игру
    //public static com.opris.colorcombat.classes.Game currentGame = new com.opris.colorcombat.classes.Game(defaultPlayers);

    @OnMessage
    public void onMessage(String message, Session session) {
        String username = session.getUserPrincipal().getName();
        try {
            Game game = games.get(username);
            //Парсим сообщение
            JSONParser parser = new JSONParser();
            JSONObject jsonMessage = (JSONObject) parser.parse(message);

            String target = (String) jsonMessage.get("target");
            //Проверяем цель сообщения
            switch (target) {
                case "movePlayer":
                    if (game.isStarted()) {
                        //Двигаем игрока и получаем все изменения
                        List<MapObject> changes = game.movePlayer(session.getUserPrincipal().getName(), (String) jsonMessage.get("value"));

                        //Рассылаем их
                        game.sendChanges(changes);
                    }
                    break;
//                case "startGame":
//                    game.end();
//                    ArrayList<Session> listeners = game.getListeners();
//                    game = new Game(defaultPlayers);
//                    game.setListeners(listeners);
//                    List<MapObject> changes = game.getWholeField();
//                    game.start();
//                    game.sendChanges(changes);
//                    break;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        
        String username = session.getUserPrincipal().getName();
        if(games.containsKey(username))
        {
            Game game = games.get(username);
            //Добавляем новго листенера
            game.listeners.add(session);
            //Получем текущее состояние игры
            List<MapObject> changes = game.getWholeField();
            //Отправляем его 
            game.sendChanges(changes);
            
            for (Bonus b : game.bonusesList)
            {
                game.sendSpawnBonus(b);
            }
            
            if(!game.isStarted() && game.listeners.size()>=2)
            {
                game.start();
            }
        }


    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        Game game = games.get(session.getUserPrincipal().getName());
        if(game != null)
        {
            game.listeners.remove(session);
        }
    }
    
    public static void addGame(Lobby lobby)
    {
        String lobbyName = lobby.getName();
        ArrayList<String> players = new ArrayList<>();
        lobby.getLobbyListeners().forEach(x -> players.add(x.getUserPrincipal().getName()));
        Game game = new Game(players);
        
        players.forEach(x -> games.put(x, game));
    }

}
