/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opris.colorcombat.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author boris
 */
@Entity
@Table(name = "ROLELIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rolelist.findAll", query = "SELECT r FROM Rolelist r"),
    @NamedQuery(name = "Rolelist.findByIdRole", query = "SELECT r FROM Rolelist r WHERE r.idRole = :idRole"),
    @NamedQuery(name = "Rolelist.findByNameRole", query = "SELECT r FROM Rolelist r WHERE r.nameRole = :nameRole")})
public class Rolelist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ROLE")
    private Integer idRole;
    @Size(max = 45)
    @Column(name = "NAME_ROLE")
    private String nameRole;
    @OneToMany(mappedBy = "idRole")
    private Collection<Userrole> userroleCollection;

    public Rolelist() {
    }

    public Rolelist(Integer idRole) {
        this.idRole = idRole;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    @XmlTransient
    public Collection<Userrole> getUserroleCollection() {
        return userroleCollection;
    }

    public void setUserroleCollection(Collection<Userrole> userroleCollection) {
        this.userroleCollection = userroleCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRole != null ? idRole.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rolelist)) {
            return false;
        }
        Rolelist other = (Rolelist) object;
        if ((this.idRole == null && other.idRole != null) || (this.idRole != null && !this.idRole.equals(other.idRole))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.opris.colorcombat.entities.Rolelist[ idRole=" + idRole + " ]";
    }
    
}
