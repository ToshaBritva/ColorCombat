/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opris.colorcombat.entities.User;
import com.opris.colorcombat.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author HP
 */

@Controller
@RequestMapping(value = {"/MainPage/getLeaders"})
public class GetLeaders
{
    private UserRepository userRepository;

    @Autowired
    public GetLeaders(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getLeaders()
    {
        Sort sort = new Sort(Sort.Direction.DESC, "rating");
        List<User> users = userRepository.findAll(sort);
        
        JsonArray JSONLeaders = new JsonArray();
        
        int leaderNum = 10;
        for(int i=0; i<leaderNum && i<users.size(); i++)
        {
             JsonObject JSONleader = new JsonObject();
             JSONleader.addProperty("place", i + 1);
             JSONleader.addProperty("nickname", users.get(i).getNickname());
             JSONleader.addProperty("score", users.get(i).getRating());
             JSONLeaders.add(JSONleader);
        }
        
        return JSONLeaders.toString();
    }
}
