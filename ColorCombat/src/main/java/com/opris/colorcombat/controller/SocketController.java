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
    
    //Список игр
    static HashMap<String, Game> games = new HashMap<>();

    @OnMessage
    public void onMessage(String message, Session session) {
        
        //Получаем имя пользователя
        String nickname = session.getUserPrincipal().getName();
        
        try {

            //Получаем игру
            Game game = games.get(nickname);

            //Парсим сообщение
            JSONParser parser = new JSONParser();
            JSONObject jsonMessage = (JSONObject) parser.parse(message);

            //Проверяем цель сообщения
            String target = (String) jsonMessage.get("target");
            switch (target) {
                case "movePlayer":
                    //Если игра начата
                    if (game.IsStarted()) {
                        
                        //Двигаем игрока и получаем все изменения
                        List<MapObject> changes = game.movePlayer(nickname, (String) jsonMessage.get("value"));

                        //Рассылаем их
                        game.sendChanges(changes);
                    }
                    break;
            }

        } catch (Exception e) {
            System.err.println(e);
        }

    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {

        //Получаем никнейм из сессии
        String nickname = session.getUserPrincipal().getName();

        //Если имеется игра с таким игроком
        if (games.containsKey(nickname)) {

            //Получаем эту игру
            Game game = games.get(nickname);

            //Добавляем новго листенера
            game.AddListener(session);

            //Отправляем текущее состояние игры
            game.SendCurrentGameState(session);
            
        }

    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        Game game = games.get(session.getUserPrincipal().getName());
        if (game != null) {
            game.RemoveListener(session);
        }
    }

    //Создание новой игры
    public static void addGame(Lobby lobby) {

        //Список имен игроков
        ArrayList<String> playersNicknames = new ArrayList<>();
        lobby.getLobbyListeners().forEach(x -> playersNicknames.add(x.getUserPrincipal().getName()));

        //Создаем новую игру
        Game game = new Game(playersNicknames);

        //Добавляем для каждого игрока пару - ник-игра.
        playersNicknames.forEach(x -> games.put(x, game));
    }
    
    public static void destroyGame(Game game)
    {
        ArrayList<String> playersNicknames = game.GetPlayersNicknames();
        for(String p: playersNicknames)
        {
            games.remove(p);
        }
    }

}
