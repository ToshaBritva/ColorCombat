package com.opris.colorcombat.controller;

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
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author Niyaz
 */

@ServerEndpoint("/MainPage/Animate/socialsocket")
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
            
            switch (target) 
            {
                case "createLobby":
                    lobby = new Lobby(session);
                    hosts.put(username, lobby);
                    listeners.put(username, lobby);
                    break;
                case "destroyLobby":
                    lobby = hosts.get(username);
                    for(Session s : lobby.getLobbyListeners())
                    {
                        listeners.remove(s.getUserPrincipal().getName());
                        
                    }
                    hosts.remove(username);
                    break;
                case "joinLobby":
                    String host = (String) jsonMessage.get("userHost");
                    lobby = hosts.get(host); 
                    lobby.join(session);
                    listeners.put(username, lobby);
                    sendToLobby(lobby, "{\"target\":\"joinPlayer\",\"nickname\":\"" + username + "\"}");
                    break;
                case "setStatus":
                    lobby = listeners.get(username); 
                    if((String) jsonMessage.get("status") == "ready")
                    {
                        lobby.setStatus(session, true);
                    }
                    else
                    {
                        lobby.setStatus(session, false);
                    }
                    sendToLobby(lobby, "{\"target\":\"setStatus\",\"nickname\":\"" + username + "\"}");
                    break;
                case "leaveLobby":
                    lobby = listeners.get(username);
                    listeners.remove(username);
                    sendToLobby(lobby, "{\"target\":\"removePlayer\",\"nickname\":\"" + username + "\"}");
                    break;
            }
        }
        catch(Exception ex)
        {
            
        }
    }
    
    @OnClose
    public void onClose(Session session)
    {
        String username = session.getUserPrincipal().getName();
        listeners.remove(username);
        if(hosts.containsKey(username))
        {
            for(Session s : hosts.get(username).getLobbyListeners())
            {
                listeners.remove(s.getUserPrincipal().getName());
            }
        }
    }
    
    public void sendToLobby(Lobby lobby, String message)
    {
        ArrayList<Session> listeners = lobby.getLobbyListeners();
        
        listeners.forEach(x -> 
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
}
