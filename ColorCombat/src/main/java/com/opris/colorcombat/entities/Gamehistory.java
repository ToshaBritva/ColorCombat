/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author boris
 */
@Entity
@Table(name = "GAMEHISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gamehistory.findAll", query = "SELECT g FROM Gamehistory g"),
    @NamedQuery(name = "Gamehistory.findById", query = "SELECT g FROM Gamehistory g WHERE g.id = :id"),
    @NamedQuery(name = "Gamehistory.findByDate", query = "SELECT g FROM Gamehistory g WHERE g.date = :date"),
    @NamedQuery(name = "Gamehistory.findByScore", query = "SELECT g FROM Gamehistory g WHERE g.score = :score"),
    @NamedQuery(name = "Gamehistory.findByResult", query = "SELECT g FROM Gamehistory g WHERE g.result = :result"),
    @NamedQuery(name = "Gamehistory.findByIdGame", query = "SELECT g FROM Gamehistory g WHERE g.idGame = :idGame")})
public class Gamehistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SCORE")
    private int score;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RESULT")
    private boolean result;
    @Column(name = "ID_GAME")
    private Integer idGame;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private User idUser;

    public Gamehistory() {
    }

    public Gamehistory(Integer id) {
        this.id = id;
    }

    public Gamehistory(Integer id, Date date, int score, boolean result) {
        this.id = id;
        this.date = date;
        this.score = score;
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Integer getIdGame() {
        return idGame;
    }

    public void setIdGame(Integer idGame) {
        this.idGame = idGame;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
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
        if (!(object instanceof Gamehistory)) {
            return false;
        }
        Gamehistory other = (Gamehistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.opris.colorcombat.entities.Gamehistory[ id=" + id + " ]";
    }
    
}
