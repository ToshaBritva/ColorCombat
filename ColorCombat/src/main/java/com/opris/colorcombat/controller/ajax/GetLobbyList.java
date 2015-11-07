/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.opris.colorcombat.controller.LobbySocket;
import com.opris.colorcombat.classes.Lobby;
import java.util.ArrayList;

/**
 *
 * @author HP
 */

@Controller
@RequestMapping(value = {"/MainPage/getLobbyList"})
public class GetLobbyList
{
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getLobbyList()
    {
        ArrayList<Lobby> lobbies = LobbySocket.getLobbyList();
        
        System.out.println("Кол-во лобби:" + lobbies.size());
        
        JsonArray JSONLobbies = new JsonArray();
        
        for(Lobby lobby : lobbies)
        {
             JsonObject JSONlobby = new JsonObject();
             JSONlobby.addProperty("lobbyName", lobby.getName());
             JSONlobby.addProperty("busySlotNum", lobby.getBusySlotNum());
             System.out.println(JSONlobby.toString());
             JSONLobbies.add(JSONlobby);
        }
        
        return JSONLobbies.toString();
    }
}
