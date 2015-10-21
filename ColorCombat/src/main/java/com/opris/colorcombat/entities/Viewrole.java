/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boris
 */
@Entity
@Table(name = "VIEWROLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viewrole.findAll", query = "SELECT v FROM Viewrole v"),
    @NamedQuery(name = "Viewrole.findByNickname", query = "SELECT v FROM Viewrole v WHERE v.nickname = :nickname"),
    @NamedQuery(name = "Viewrole.findByNameRole", query = "SELECT v FROM Viewrole v WHERE v.nameRole = :nameRole"),
    @NamedQuery(name = "Viewrole.findByPassword", query = "SELECT v FROM Viewrole v WHERE v.password = :password")})
public class Viewrole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NICKNAME")
    private String nickname;
    @Size(max = 45)
    @Column(name = "NAME_ROLE")
    private String nameRole;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PASSWORD")
    private String password;

    public Viewrole() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
