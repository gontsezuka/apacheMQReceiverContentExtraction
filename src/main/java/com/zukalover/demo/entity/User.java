package com.zukalover.demo.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="users")
public class User {

	@NotNull
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@NotNull
	private String username;
	@CreationTimestamp
	private LocalDateTime createddate;
	private String password;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private Set<FileEntity> files;
	
	public Set<FileEntity> getFiles() {
		return files;
	}

	public void setFiles(Set<FileEntity> files) {
		this.files = files;
	}

	public User()
	{
		/**
		 * This is a default constructor
		 */
				
	}
	
	@CheckForNull
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

	public LocalDateTime getCreateddate() {
		return createddate;
	}
	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
