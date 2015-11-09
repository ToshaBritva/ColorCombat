package com.opris.colorcombat.controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.opris.colorcombat.classes.Lobby;
import java.util.ArrayList;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author Niyaz
 */

@ServerEndpoint("/MainPage/socialsocket")
public class LobbySocket
{
    static LinkedHashMap<String, Lobby> listeners = new LinkedHashMap<>(); //для навигации по всем пользователям лобби
    static LinkedHashMap<String, Lobby> hosts = new LinkedHashMap<>(); //для навигации только по хостам
    
    @OnMessage
    public void onMessage(String message, Session session) throws IOException
    {   
        String username = session.getUserPrincipal().getName();
        Lobby lobby;
        
        JSONParser parser = new JSONParser();
        try
        {
            JSONObject jsonMessage = (JSONObject) parser.parse(message);
            String target = (String) jsonMessage.get("target");
            
            JsonObject lobbyMessage = new JsonObject();
            
            switch (target) 
            {
                case "createLobby":
                    lobby = new Lobby(session);
                    hosts.put(username, lobby);
                    //listeners.put(username, lobby);
                    System.out.println("createLobby");
                    break;
                case "destroyLobby":
                    lobby = hosts.get(username);
                    lobby.remove(session); //Удаляем хоста, чтобы не слать ему сообщение
                    lobbyMessage.addProperty("target", "kicked");
                    sendToLobby(lobby, lobbyMessage.toString());
                    for(Session s : lobby.getLobbyListeners())
                    {
                        listeners.remove(s.getUserPrincipal().getName());
                    }
                    hosts.remove(username);
                    break;
                case "joinLobby":
                    String host = (String) jsonMessage.get("userHost");
                    lobby = hosts.get(host);
                    if(lobby.getBusySlotNum() < 4)
                    {
                        lobbyMessage.addProperty("target", "joinPlayer");
                        lobbyMessage.addProperty("nickname", username);
                        sendToLobby(lobby, lobbyMessage.toString());
                        lobby.join(session);
                        listeners.put(username, lobby);
                    }
                    break;
                case "setStatus":
                    lobby = listeners.get(username);
                    System.out.println("1");
                    lobbyMessage.addProperty("target", "setStatus");
                    lobbyMessage.addProperty("nickname", username);
                    if (jsonMessage.get("status").equals("ready")) 
                    {
                        lobby.setStatus(session, true);
                        lobbyMessage.addProperty("status", "ready");
                        System.out.println("ready");
                    }
                    else
                    {
                        lobby.setStatus(session, false);
                        lobbyMessage.addProperty("status", "notReady");
                        System.out.println("notReady");
                    }
                    System.out.println(lobbyMessage.toString());
                    sendToLobby(lobby, lobbyMessage.toString(), session);
                    break;
                case "leaveLobby":
                    lobby = listeners.get(username);
                    listeners.remove(username);
                    lobby.remove(session);
                    lobbyMessage.addProperty("target", "removePlayer");
                    lobbyMessage.addProperty("nickname", username);
                    sendToLobby(lobby, lobbyMessage.toString());
                    break;
            }
            
            System.out.println("Количество хостов:" + hosts.size());
            System.out.println("Количество слушателей:" + listeners.size());
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    @OnClose
    public void onClose(Session session)
    {
        String username = session.getUserPrincipal().getName();
        JsonObject lobbyMessage = new JsonObject();
        
        if(hosts.containsKey(username))
        {
            Lobby lobby = hosts.get(username);
            
            lobbyMessage.addProperty("target", "kicked");
            sendToLobby(lobby, lobbyMessage.toString());
            for(Session s : hosts.get(username).getLobbyListeners())
            {
                listeners.remove(s.getUserPrincipal().getName());
            }
        }
        else
        {
            Lobby lobby = listeners.get(username);
            lobby.remove(session);
            lobbyMessage.addProperty("target", "removePlayer");
            lobbyMessage.addProperty("nickname", username);
            sendToLobby(lobby, lobbyMessage.toString());
        }
        listeners.remove(username);
    }
    
    public void sendToLobby(Lobby lobby, String message)
    {
        ArrayList<Session> lobbyListeners = lobby.getLobbyListeners();
        
        lobbyListeners.forEach(x -> 
            {
                try
                {
                    x.getBasicRemote().sendText(message);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(LobbySocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        );          
    }
    
    public void sendToLobby(Lobby lobby, String message, Session exclude)
    {
        ArrayList<Session> lobbyListeners = new ArrayList<>(lobby.getLobbyListeners());
        
        lobbyListeners.forEach(x -> 
            {
                try
                {
                    if(x!=exclude)
                        x.getBasicRemote().sendText(message);
                }
                catch(IOException ex)
                {
                    Logger.getLogger(LobbySocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        );          
    }
    
    public static ArrayList<Lobby> getLobbyList()
    {
        return new ArrayList<>(hosts.values());
    }
    
    public static Lobby getLobby(String nickname)
    {
        return hosts.get(nickname);
    }
        
}
