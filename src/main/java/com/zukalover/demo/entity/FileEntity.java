package com.zukalover.demo.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name="files")
public class FileEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String filename;
	@CreationTimestamp
	private LocalDateTime createddate;
	private String alfrescoid;
	
	@OneToMany(mappedBy = "file",cascade = CascadeType.ALL)
	private Set<DocumentEntity> documents;
//ADDED
	@ManyToOne
	@JoinColumn
	User user;
	
	public FileEntity()
	{
		/**
		 * This is a constructor
		 */
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
		
	public String getAlfrescoid() {
		return alfrescoid;
	}

	public void setAlfrescoid(String alfrescoid) {
		this.alfrescoid = alfrescoid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public LocalDateTime getCreateddate() {
		return createddate;
	}

	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}

	public Set<DocumentEntity> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<DocumentEntity> documents) {
		this.documents = documents;
	}
}
