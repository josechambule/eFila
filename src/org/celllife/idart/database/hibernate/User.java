/*
 * iDART: The Intelligent Dispensing of Antiretroviral Treatment
 * Copyright (C) 2006 Cell-Life 
 * 
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License version 2 as published by 
 * the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT  
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or  
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version  
 * 2 for more details. 
 * 
 * You should have received a copy of the GNU General Public License version 2 
 * along with this program; if not, write to the Free Software Foundation, 
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 * 
 */

/*
 * Created on 2005/03/24
 *
 */
package org.celllife.idart.database.hibernate;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import model.nonPersistent.Autenticacao;
import org.hibernate.annotations.Cascade;

/**
 */
@Entity
@Table(name = "users")
public class User {

	private static final int ACTIVO = 1;
	private static final int NOT_ACTIVO = -1;

	@Id
	@GeneratedValue
	private Integer id;

	private char modified;

	@Column(name = "cl_password")
	private String password;



	@Column(name = "cl_username")
	private String username;
	
	@Column(name = "permission")
	private char permission;

	@Column(name = "state")
	private int state;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "ClinicUser", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "clinicId") })
	private Set<Clinic> clinics;

	@ManyToMany(cascade = { CascadeType.PERSIST })
	@JoinTable(
			name = "user_role",
			joinColumns = { @JoinColumn(name = "userid") },
			inverseJoinColumns = { @JoinColumn(name = "roleid") }
	)
	private Set<Role> roles;


	public User() {
		super();
	}

	/**
	 * @param username
	 * @param password
	 * @param modified
	 * @param clinics Set<Clinic>
	 * @param permission
	 */
	public User(String username, String password, char modified, Set<Role> roles, Set<Clinic> clinics, char permission) {
		super();
		this.username = username;
		this.password = Autenticacao.converteMD5(password);
		this.modified = modified;
		this.roles = roles;
		this.clinics=clinics;
		this.permission=permission;
	}

	public void addRole(Role role){
		if (this.roles == null) this.roles = new HashSet<>();

		this.roles.add(role);
	}
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Method getModified.
	 * @return char
	 */
	public char getModified() {
		return modified;
	}

	/**
	 * Method getPassword.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Method getUsername.
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Method setId.
	 * @param id int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method setModified.
	 * @param modified char
	 */
	public void setModified(char modified) {
		this.modified = modified;
	}

	/**
	 * Method setPassword.
	 * @param password String
	 */
	public void setPassword(String password) {
		
		this.password = Autenticacao.converteMD5(password);
	}


	/**
	 * Method setUsername.
	 * @param username String
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Method getClinics.
	 * @return Set<Clinic>
	 */
	public Set<Clinic> getClinics() {
		return clinics;
	}

	/**
	 * Method setClinics.
	 * @param clinics Set<Clinic>
	 */
	public void setClinics(Set<Clinic> clinics) {
		this.clinics = clinics;
	}

	/**
	 * Metodo getPermission 
	 * @return char permission
	 */
	public char getPermission() {
		return permission;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isPermitedTo(String functionalityCode){
		if (this.roles == null || this.roles.isEmpty()) return false;

		for (Role role : this.roles) {
			if (role.getSysFunctions() == null || role.getSysFunctions().isEmpty()) return false;

			for (SystemFunctionality functionality : role.getSysFunctions()) {
				if (functionality.getCode().equalsIgnoreCase(functionalityCode)) return true;
			}
		}
		return false;
	}

	public boolean hasRole(String roleCode){
		if (this.roles == null || this.roles.isEmpty()) return false;

		for (Role role : this.roles) {
			if (role.getCode().equalsIgnoreCase(roleCode)) return true;
		}
		return false;
	}

	/**
	 * Metodo setPermission
	 * @param permission char
	 */

	public void setPermission(char permission) {
		this.permission = permission;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

    public boolean isActive() {
		return this.state == User.ACTIVO;
    }

	public void changeStateToActive() {
		this.state = ACTIVO;
	}

	public void changeStateToNotActive() {
		this.state = NOT_ACTIVO;
	}
}
