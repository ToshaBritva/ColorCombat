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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boris
 */
@Entity
@Table(name = "LOBBY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lobby.findAll", query = "SELECT l FROM Lobby l"),
    @NamedQuery(name = "Lobby.findById", query = "SELECT l FROM Lobby l WHERE l.id = :id"),
    @NamedQuery(name = "Lobby.findBySlotstatus1", query = "SELECT l FROM Lobby l WHERE l.slotstatus1 = :slotstatus1"),
    @NamedQuery(name = "Lobby.findBySlotstatus2", query = "SELECT l FROM Lobby l WHERE l.slotstatus2 = :slotstatus2"),
    @NamedQuery(name = "Lobby.findBySlotstatus3", query = "SELECT l FROM Lobby l WHERE l.slotstatus3 = :slotstatus3"),
    @NamedQuery(name = "Lobby.findByStatuslobby", query = "SELECT l FROM Lobby l WHERE l.statuslobby = :statuslobby")})
public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 45)
    @Column(name = "SLOTSTATUS1")
    private String slotstatus1;
    @Size(max = 45)
    @Column(name = "SLOTSTATUS2")
    private String slotstatus2;
    @Size(max = 45)
    @Column(name = "SLOTSTATUS3")
    private String slotstatus3;
    @Size(max = 45)
    @Column(name = "STATUSLOBBY")
    private String statuslobby;
    @JoinColumn(name = "ID_USERHOST", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private User idUserhost;
    @JoinColumn(name = "ID_USER1", referencedColumnName = "ID")
    @ManyToOne
    private User idUser1;
    @JoinColumn(name = "ID_USER2", referencedColumnName = "ID")
    @ManyToOne
    private User idUser2;
    @JoinColumn(name = "ID_USER3", referencedColumnName = "ID")
    @ManyToOne
    private User idUser3;

    public Lobby() {
    }

    public Lobby(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlotstatus1() {
        return slotstatus1;
    }

    public void setSlotstatus1(String slotstatus1) {
        this.slotstatus1 = slotstatus1;
    }

    public String getSlotstatus2() {
        return slotstatus2;
    }

    public void setSlotstatus2(String slotstatus2) {
        this.slotstatus2 = slotstatus2;
    }

    public String getSlotstatus3() {
        return slotstatus3;
    }

    public void setSlotstatus3(String slotstatus3) {
        this.slotstatus3 = slotstatus3;
    }

    public String getStatuslobby() {
        return statuslobby;
    }

    public void setStatuslobby(String statuslobby) {
        this.statuslobby = statuslobby;
    }

    public User getIdUserhost() {
        return idUserhost;
    }

    public void setIdUserhost(User idUserhost) {
        this.idUserhost = idUserhost;
    }

    public User getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(User idUser1) {
        this.idUser1 = idUser1;
    }

    public User getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(User idUser2) {
        this.idUser2 = idUser2;
    }

    public User getIdUser3() {
        return idUser3;
    }

    public void setIdUser3(User idUser3) {
        this.idUser3 = idUser3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lobby)) {
            return false;
        }
        Lobby other = (Lobby) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.opris.colorcombat.entities.Lobby[ id=" + id + " ]";
    }
    
}
