/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author boris
 */
@Controller
@RequestMapping(value = {"/", "game"})
public class Game {

    @RequestMapping(value = {"game"})
    public String game() {
        return "game";
    }

    @RequestMapping(value = {"/"})
    public String home() {
        return "MainPage";
    }
    
    @RequestMapping(value = {"MyLobby"})
    public String CreateLobby() {
        return "CreateLobbyPage";
    }
    
    @RequestMapping(value = {"Lobbies"})
    public String FindLobbies() {
        return "FindLobbiesPage";
    }
    @RequestMapping(value = {"/1"})
        public String MainAnim() {
            return "MainAnim";
        }
}


