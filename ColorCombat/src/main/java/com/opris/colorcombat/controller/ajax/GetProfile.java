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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */
@Controller
@RequestMapping(value = {"/MainPage/getProfile"})
public class GetProfile {

    private int gamesToShow = 5;

    private UserRepository userRepository;
    private GamehistoryRepository gameRepository;

    @Autowired
    public GetProfile(UserRepository userRepository, GamehistoryRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getProfile(HttpServletRequest request) {
        User user = userRepository.findByName(request.getUserPrincipal().getName());

        JsonObject JSONprofile = new JsonObject();
        JSONprofile.addProperty("rating", user.getRating());
        JSONprofile.addProperty("about", user.getDescription());

        JsonArray JSONgames = new JsonArray();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");

        List<Gamehistory> games = gameRepository.findByUserId(user);

        games.sort((Gamehistory game1, Gamehistory game2) -> game1.getDate().compareTo(game2.getDate()));

        for (int i = 0; i < games.size() && i < gamesToShow; i++) {
            JsonObject JSONGame = new JsonObject();

            JSONGame.addProperty("date", formatter.format(games.get(i).getDate()));
            JSONGame.addProperty("score", games.get(i).getScore());

            String resultStr = "";
            if (games.get(i).getResult()) {
                resultStr = "Победа";
            } else {
                resultStr = "Поражение";
            }
            
            JSONGame.addProperty("result", resultStr);

            JSONgames.add(JSONGame);
        }

        JSONprofile.add("games", JSONgames);

        return JSONprofile.toString();
    }
}
