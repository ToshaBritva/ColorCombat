/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author boris
 */
@Entity
@Table(name = "USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByNickname", query = "SELECT u FROM User u WHERE u.nickname = :nickname"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByRating", query = "SELECT u FROM User u WHERE u.rating = :rating"),
    @NamedQuery(name = "User.findByDescription", query = "SELECT u FROM User u WHERE u.description = :description"),
    @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull(message = "Необходимо указать никнейм")
    @Size(min = 1, max = 45, message = "Никнейм должен содержать от 1 до 45 символов")
    @Column(name = "NICKNAME")
    private String nickname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Недопустимый адрес электронной почты")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull(message = "Необходимо указать e-mail")
    @Size(min = 1, max = 45, message = "e-mail должен содержать от 1 до 45 символов")
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull(message = "Необходимо указать пароль")
    @Size(min = 1, max = 45, message = "Пароль должен содержать от 1 до 45 символов")
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATING")
    private int rating;
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 45)
    @Column(name = "STATUS")
    private String status;
    @OneToMany(mappedBy = "idUser")
    private Collection<Userrole> userroleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUserhost")
    private Collection<Lobby> lobbyCollection;
    @OneToMany(mappedBy = "idUser1")
    private Collection<Lobby> lobbyCollection1;
    @OneToMany(mappedBy = "idUser2")
    private Collection<Lobby> lobbyCollection2;
    @OneToMany(mappedBy = "idUser3")
    private Collection<Lobby> lobbyCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private Collection<Gamehistory> gamehistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser1")
    private Collection<Friendlist> friendlistCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser2")
    private Collection<Friendlist> friendlistCollection1;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String nickname, String email, String password, int rating) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Userrole> getUserroleCollection() {
        return userroleCollection;
    }

    public void setUserroleCollection(Collection<Userrole> userroleCollection) {
        this.userroleCollection = userroleCollection;
    }

    @XmlTransient
    public Collection<Lobby> getLobbyCollection() {
        return lobbyCollection;
    }

    public void setLobbyCollection(Collection<Lobby> lobbyCollection) {
        this.lobbyCollection = lobbyCollection;
    }

    @XmlTransient
    public Collection<Lobby> getLobbyCollection1() {
        return lobbyCollection1;
    }

    public void setLobbyCollection1(Collection<Lobby> lobbyCollection1) {
        this.lobbyCollection1 = lobbyCollection1;
    }

    @XmlTransient
    public Collection<Lobby> getLobbyCollection2() {
        return lobbyCollection2;
    }

    public void setLobbyCollection2(Collection<Lobby> lobbyCollection2) {
        this.lobbyCollection2 = lobbyCollection2;
    }

    @XmlTransient
    public Collection<Lobby> getLobbyCollection3() {
        return lobbyCollection3;
    }

    public void setLobbyCollection3(Collection<Lobby> lobbyCollection3) {
        this.lobbyCollection3 = lobbyCollection3;
    }

    @XmlTransient
    public Collection<Gamehistory> getGamehistoryCollection() {
        return gamehistoryCollection;
    }

    public void setGamehistoryCollection(Collection<Gamehistory> gamehistoryCollection) {
        this.gamehistoryCollection = gamehistoryCollection;
    }

    @XmlTransient
    public Collection<Friendlist> getFriendlistCollection() {
        return friendlistCollection;
    }

    public void setFriendlistCollection(Collection<Friendlist> friendlistCollection) {
        this.friendlistCollection = friendlistCollection;
    }

    @XmlTransient
    public Collection<Friendlist> getFriendlistCollection1() {
        return friendlistCollection1;
    }

    public void setFriendlistCollection1(Collection<Friendlist> friendlistCollection1) {
        this.friendlistCollection1 = friendlistCollection1;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.opris.colorcombat.entities.User[ id=" + id + " ]";
    }
    
}
