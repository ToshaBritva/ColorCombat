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
@RequestMapping(value = {"/MainPage/setLobbyDescr"})
public class SetProfileDescr
{
    private UserRepository userRepository;

    @Autowired
    public SetProfileDescr(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public void getProfile(@RequestParam(value = "nickname", required = true) String nickname, @RequestParam(value = "description", required = true) String descr)
    {
        User user = userRepository.findByName(nickname);
        user.setDescription(descr);
        userRepository.save(user);
    }
}
