/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.controller.ajax;

import com.opris.colorcombat.entities.User;
import com.opris.colorcombat.repository.RolelistRepository;
import com.opris.colorcombat.repository.UserRepository;
import com.opris.colorcombat.repository.UserroleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author boris
 */
@Controller
@RequestMapping(value = {"/checkname"})
public class CheckNameNewUser {

    private UserRepository userRepository;
    private RolelistRepository roleRepository;
    private UserroleRepository userRoleRepository;

    @Autowired
    public CheckNameNewUser(UserRepository userRepository, UserroleRepository uresRoleRepository, RolelistRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = uresRoleRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String check(@RequestParam(value = "nickname", required = false) String nickname) {
        String b = "0";
        try {
            User u = userRepository.findByName(nickname);
            if(u!= null){
                b = "1";
            }
        } catch (Exception e) {

            }
        return b;
    }
}
