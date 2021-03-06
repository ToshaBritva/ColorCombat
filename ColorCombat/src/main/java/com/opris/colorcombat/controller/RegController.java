/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.opris.colorcombat.entities.*;
import com.opris.colorcombat.repository.*;

@Controller
@RequestMapping("/register")
public class RegController {

    private UserRepository userRepository;
    private RolelistRepository roleRepository;
    private UserroleRepository userRoleRepository;

    @Autowired
    public RegController(UserRepository studentRepository, UserroleRepository uresRoleRepository,RolelistRepository roleRepository) {
        this.userRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository =  uresRoleRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute(new User());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String checkuser(Model model, @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        userRepository.save(user);
        Rolelist role =  roleRepository.getOne(1);
        Userrole a = new Userrole();
        a.setIdRole(role);
        a.setIdUser(user);
        userRoleRepository.save(a);
        
        return "login";
    }

}
