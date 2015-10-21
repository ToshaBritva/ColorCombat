/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.repository;

import com.opris.colorcombat.entities.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author boris
 */
public interface LobbyRepository extends JpaRepository<Lobby, Integer>{
    
}
