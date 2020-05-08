package com.zukalover.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zukalover.demo.entity.DocumentEntity;
@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

	//TO RETRIEVE DOCUMENT VIA THE ID
	@Query(value="SELECT d.alfrescoid FROM documents d WHERE d.id=?2 AND d.file_id=?1",nativeQuery=true)
	public String findAlfrescoidByDocumentIdAndfileId(Integer fid, Integer did);
	
	@Query(value="SELECT d.alfrescoid FROM documents d WHERE d.id=?1", nativeQuery=true)
	public String findAlfrescoID(Integer did);

	@Query(value="SELECT * FROM documents d WHERE d.documentname=?1",nativeQuery=true)
	DocumentEntity findByDocumentname(String documentname);
}
