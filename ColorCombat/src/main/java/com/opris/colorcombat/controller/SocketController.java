/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            for (Session peer : sessions) {
                if (!peer.equals(session)) {
                    peer.getBasicRemote().sendText(message);
                }
            }
        } catch (Exception e) {
        }

    }

    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        sessions.remove(session);
    }
}
