/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.classes;

import javax.websocket.Session;

/**
 *
 * @author HP
 */
public class Member
{
    public enum Status {ready, notReady};
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
}
