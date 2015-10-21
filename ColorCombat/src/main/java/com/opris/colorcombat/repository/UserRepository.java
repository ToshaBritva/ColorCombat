/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.repository;

import com.opris.colorcombat.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author boris
 */
public interface UserRepository extends JpaRepository<User, Integer> {

}
