/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.repository;

import com.opris.colorcombat.entities.Gamehistory;
import com.opris.colorcombat.entities.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author boris
 */
public interface GamehistoryRepository extends JpaRepository<Gamehistory, Integer>{
    
    @Query("select g from Gamehistory g where g.idUser = :USER")
    List<Gamehistory> findByUserId(@Param("USER") User user);
    
}
