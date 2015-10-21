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
@RequestMapping(value = {"/MainPage"})
public class GameController {
    
    @RequestMapping(value = {"/Game"})
    public String Game() {
        return "Game";
    }
}
