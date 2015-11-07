/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opris.colorcombat.classes.Lobby;
import com.opris.colorcombat.classes.Member;
import com.opris.colorcombat.controller.LobbySocket;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */

@Controller
@RequestMapping(value = {"/MainPage/Animate/getLobby"})
public class GetLobby
{
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getLobbyList(@RequestParam(value = "host", required = true) String hostname)
    {
        Lobby lobby = LobbySocket.getLobby(hostname);
        JsonObject JSONlobby = new JsonObject();
        JSONlobby.addProperty("master", hostname);
        
        ArrayList<Member> members = lobby.getMembers();
        
        JsonArray JSONmembers = new JsonArray();
        for(Member member : members)
        {
             JsonObject JSONmember  = new JsonObject();
             JSONmember.addProperty("nickname", member.getUserNickname());
             if(member.isReady())
             {
                 JSONmember.addProperty("status", "ready");
             }
             else
             {
                 JSONmember.addProperty("status", "notReady");
             }
             
             JSONmembers.add(JSONmember);
        }
        
        JSONlobby.add("members", JSONmembers);
        
        return JSONlobby.toString();
    }
}
