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
 * @author Pushkin
 */
@Controller
@RequestMapping(value = {"/","MainPage"})
public class MainAnimController {
    @RequestMapping(value = {"MainPage"})
    public String game() 
    {
        return "MainAnim";
    }
    
    @RequestMapping(value = {"/"})
    public String reMainPage() 
    {
        return "redirect:/MainPage";
    }
}
