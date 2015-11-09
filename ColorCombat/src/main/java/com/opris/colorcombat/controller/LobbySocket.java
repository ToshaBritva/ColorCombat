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
    static LinkedHashMap<String, Lobby> listeners = new LinkedHashMap<>(); //для навигации по клиентам
    static LinkedHashMap<String, Lobby> hosts = new LinkedHashMap<>(); //для навигации только по хостам
    
    void createLobby(Session session)
    {
        String username = session.getUserPrincipal().getName();
        Lobby lobby = new Lobby(session);
        hosts.put(username, lobby);
    }
    
    void destroyLobby(Session session)
    {
        String username = session.getUserPrincipal().getName();
        Lobby lobby = hosts.get(username);
        if(lobby!=null)
        {
            lobby.remove(session); //Удаляем хоста, чтобы не слать ему сообщение

            JsonObject lobbyMessage = new JsonObject();
            lobbyMessage.addProperty("target", "kicked");
            sendToLobby(lobby, lobbyMessage.toString());

            for(Session s : lobby.getLobbyListeners())
            {
                listeners.remove(s.getUserPrincipal().getName());
            }
            hosts.remove(username);
        }
        else
        {
            sendError(session, "Произошла неизвестная ошибка");
        }
    }
    
    void sendError(Session session, String message)
    {
        JsonObject JSONMEssage = new JsonObject();
        JSONMEssage.addProperty("target", "errorMessage");
        JSONMEssage.addProperty("message", message);
        try
        {
            session.getBasicRemote().sendText(JSONMEssage.toString());
        }
        catch(Exception ex)
        {
            Logger.getLogger(LobbySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void joinLobby(String host, Session session)
    {
        Lobby lobby = hosts.get(host);
        String username = session.getUserPrincipal().getName();
        
        if(lobby != null && !host.equals(username))
        {   
            if(lobby.getBusySlotNum() < 4)
            {
                JsonObject lobbyMessage = new JsonObject();
                lobbyMessage.addProperty("target", "joinPlayer");
                lobbyMessage.addProperty("nickname", username);

                sendToLobby(lobby, lobbyMessage.toString());
                lobby.join(session);
                listeners.put(username, lobby);
            }
            else
            {
                sendError(session, "В лобби нет свободных мест");
            }
        }
        else
        {
            sendError(session, "Произошла неизвестная ошибка");
        }

    }
    
    void setStatus(Session session, boolean status)
    {
        String username = session.getUserPrincipal().getName();
        Lobby lobby = listeners.get(username);

        if(lobby != null)
        {
            JsonObject lobbyMessage = new JsonObject();
            lobbyMessage.addProperty("target", "setStatus");
            lobbyMessage.addProperty("nickname", username);

            lobby.setStatus(session, status);
            if (status) 
            {
                lobbyMessage.addProperty("status", "ready");
            }
            else
            {
                lobbyMessage.addProperty("status", "notReady");
            }
            sendToLobby(lobby, lobbyMessage.toString(), session);
        }
        else
        {
            sendError(session, "Произошла неизвестная ошибка");
        }

    }
    
    void kick(Session session, String username)
    {
        String host = session.getUserPrincipal().getName();
        Lobby lobby = hosts.get(host);
        
        if(lobby != null)
        {
            try
            {
                JsonObject kickMessage = new JsonObject();
                kickMessage.addProperty("target", "kicked");
                lobby.getSession(username).getBasicRemote().sendText(kickMessage.toString());
                
                lobby.remove(username);
                listeners.remove(username);
                
                JsonObject lobbyMessage = new JsonObject();
                lobbyMessage.addProperty("target", "removePlayer");
                lobbyMessage.addProperty("nickname", username);
                sendToLobby(lobby, lobbyMessage.toString());
                
            }
            catch(Exception ex)
            {
                Logger.getLogger(LobbySocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            sendError(session, "Произошла неизвестная ошибка");
        }
    }
    
    void leaveLobby(Session session)
    {
        String username = session.getUserPrincipal().getName();
        Lobby lobby = listeners.get(username);
        
        if(lobby != null)
        {
            listeners.remove(username);
            lobby.remove(session);

            JsonObject lobbyMessage = new JsonObject();
            lobbyMessage.addProperty("target", "removePlayer");
            lobbyMessage.addProperty("nickname", username);
            sendToLobby(lobby, lobbyMessage.toString());
        }
        else
        {
            sendError(session, "Произошла неизвестная ошибка");
        }

    }
    
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
            
            switch (target) 
            {
                case "createLobby":
                    createLobby(session);
                    break;
                case "destroyLobby":
                    destroyLobby(session);
                    break;
                case "joinLobby":
                    String host = (String) jsonMessage.get("userHost");
                    joinLobby(host, session);
                    break;
                case "setStatus":
                    boolean status = jsonMessage.get("status").equals("ready");
                    setStatus(session, status);
                    break;
                case "leaveLobby":
                    leaveLobby(session);
                    break;
                case "kickPlayer":
                    String kickedUsername = (String) jsonMessage.get("username");
                    kick(session, kickedUsername);
                    break;
            }
            
            System.out.println("Цель:" + target);
            System.out.println("Количество хостов:" + hosts.size());
            System.out.println("Количество слушателей:" + listeners.size());
        }
        catch(Exception ex)
        {
            Logger.getLogger(LobbySocket.class.getName()).log(Level.SEVERE, null, ex);
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
            lobby.remove(session); //Удаляем хоста, чтобы не слать ему сообщение
            lobbyMessage.addProperty("target", "kicked");
            sendToLobby(lobby, lobbyMessage.toString());
            for(Session s : lobby.getLobbyListeners())
            {
                listeners.remove(s.getUserPrincipal().getName());
            }
            hosts.remove(username);
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
        
        System.out.println("Цель: onClose");
        System.out.println("Количество хостов:" + hosts.size());
        System.out.println("Количество слушателей:" + listeners.size());
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
        lobbyListeners.remove(exclude);
        
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
    
    public static ArrayList<Lobby> getLobbyList()
    {
        return new ArrayList<>(hosts.values());
    }
    
    public static Lobby getLobby(String nickname)
    {
        return hosts.get(nickname);
    }
        
}
