/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import java.util.ArrayList;
import javax.websocket.Session;
import com.opris.colorcombat.classes.Member;

/**
 *
 * @author HP
 */


public class Lobby
{
    Session host;
    ArrayList<Member> members = new ArrayList<>();
    ArrayList<Session> listeners = new ArrayList<>();
    
    public Lobby(Session host)
    {
        this.host = host;
        listeners.add(host);
    }
    
    public void join(Session member) throws Exception
    {
        if(members.size()<4)
        {
            members.add(new Member(member));
            listeners.add(member);
        }
        else
        {
            throw new Exception("В лобби нет свободных мест");
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
            user.setStatus(Member.Status.ready);
        }
        else
        {
            user.setStatus(Member.Status.notReady);
        }
    }
    
    Member getMember(String nickname)
    {
        for(Member mem: members)
        {
            if(mem.getUserNickname().equals(nickname))
            {
                return mem;
            }
        }
        return null;
    }
    
    public ArrayList<Session> getLobbyListeners()
    {
        return listeners;
    }
    
    public String getName()
    {
        return host.getUserPrincipal().getName();
    }
    
    public int getBusySlotNum()
    {
        return members.size() + 1;
    }
    
    public ArrayList<Member> getMembers()
    {
        return members;
    }
    
}
