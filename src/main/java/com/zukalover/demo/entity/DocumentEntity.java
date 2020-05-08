package com.zukalover.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name="documents")
public class DocumentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@Column(unique=true)
	private String documentname;
	@CreationTimestamp
	private LocalDateTime createddate;
	private String alfrescoid;
	
	@ManyToOne
	@JoinColumn
	private FileEntity file;
	public DocumentEntity()
	{
		
	}
	
	public DocumentEntity(String documentname,FileEntity file)
	{
		this.documentname=documentname;
		this.file=file;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getDocumentname() {
		return documentname;
	}

	public void setDocumentname(String documentname) {
		this.documentname = documentname;
	}

	public String getAlfrescoid() {
		return alfrescoid;
	}

	public void setAlfrescoid(String alfrescoid) {
		this.alfrescoid = alfrescoid;
	}

	
	public LocalDateTime getCreateddate() {
		return createddate;
	}

	public void setCreateddate(LocalDateTime createddate) {
		this.createddate = createddate;
	}

	public FileEntity getFile() {
		return file;
	}

	public void setFile(FileEntity file) {
		this.file = file;
	}
}
