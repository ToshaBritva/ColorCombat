package com.opris.colorcombat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/"})
public class ProfilePageController 
{
    @RequestMapping(value = {"Profile"})
    public String profilePage() {
        return "ProfilePage";
    }
}
