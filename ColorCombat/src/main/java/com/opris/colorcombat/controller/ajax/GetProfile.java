/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opris.colorcombat.entities.Gamehistory;
import com.opris.colorcombat.entities.User;
import com.opris.colorcombat.repository.GamehistoryRepository;
import com.opris.colorcombat.repository.UserRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = {"/MainPage/getProfile"})
public class GetProfile
{
    private UserRepository userRepository;
    private GamehistoryRepository gameRepository;

    @Autowired
    public GetProfile(UserRepository userRepository, GamehistoryRepository gameRepository) 
    {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getProfile(@RequestParam(value = "nickname", required = true) String nickname)
    {
        User user = userRepository.findByName(nickname);
        
        JsonObject JSONprofile = new JsonObject();
        JSONprofile.addProperty("rating", user.getRating());
        JSONprofile.addProperty("about", user.getDescription());
        
        JsonArray JSONgames = new JsonArray();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
        
        ArrayList<Gamehistory> games = new ArrayList<>(user.getGamehistoryCollection());
        for(Gamehistory game : games)
        {
            JsonObject JSONGame = new JsonObject();
            
            JSONGame.addProperty("date", formatter.format(game.getDate()));
            JSONGame.addProperty("score", game.getScore());
            JSONGame.addProperty("result", game.getResult());
            
            JSONgames.add(JSONGame);
        }
        
        JSONprofile.add("games", JSONgames);
        
        return JSONprofile.toString();
    }
}
