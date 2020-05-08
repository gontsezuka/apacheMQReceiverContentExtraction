package com.zukalover.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zukalover.demo.entity.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {

	@Query(value="SELECT f.alfrescoid FROM files f WHERE f.id= ?2 AND f.user_id=?1",nativeQuery = true)
	public String findFileAlfrescoid(Integer userid,Integer fid); 
	
	
	FileEntity findByFilename(String filename);
	List<FileEntity> findByUserId( Integer userid);
}
