/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import java.util.ArrayList;
import javax.websocket.Session;

/**
 *
 * @author HP
 */


public class Lobby
{

    enum Status {ready, notReady};
    class Member
    {
        Session user;
        Status status;
        
        public Member(Session user)
        {
            this.user = user;
            status = Status.notReady;
        }
        
        public void setStatus(Status s)
        {
            status = s;
        }
        
        public boolean isReady()
        {
            return (status == Status.ready);
        }
        
        public String getUserNickname()
        {
            return user.getUserPrincipal().getName();
        }
        
        @Override
        public boolean equals(Object other) // Работает только со string (ником пользователя)
        {
            String nickname = (String) other;
            return getUserNickname().equals(nickname);
        }

        @Override
        public int hashCode()
        {
            return getUserNickname().hashCode();
        }
    }
    
    
    Session host;
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Session> listeners = new ArrayList<>();
    
    public Lobby(Session host)
    {
        this.host = host;
        listeners.add(host);
    }
    
    public void join(Session member)
    {
        if(members.size()<4)
        {
            members.add(new Member(member));
            listeners.add(member);
        }
    }
    
    public void remove(Session member)
    {
        String nickname = member.getUserPrincipal().getName();
        Member user = getMember(nickname);
        members.remove(user);
        listeners.remove(member);
    }
    
    public void setStatus(Session session, boolean ready)
    {
        String nickname = session.getUserPrincipal().getName();
        Member user = getMember(nickname);
        if(ready)
        {
            user.setStatus(Status.ready);
        }
        else
        {
            user.setStatus(Status.notReady);
        }
    }
    
    Member getMember(String nickname)
    {
        int ind = members.indexOf(nickname);
        return members.get(ind);
    }
    
    public ArrayList<Session> getLobbyListeners()
    {
        return listeners;
    }
    
}
