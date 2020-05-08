package com.zukalover.demo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zukalover.demo.entity.DocumentEntity;
import com.zukalover.demo.repository.DocumentRepository;

@Service
public class DocumentService {
	
	private Logger logger = LoggerFactory.getLogger(DocumentService.class);

	private String admin="admin";
	private String alfrescoURL="http://localhost:8080/alfresco/api/-default-/public/cmis/versions/1.1/atom";
	
	@Autowired
	DocumentRepository documentRepository;
	
	public DocumentEntity saveDocument(DocumentEntity documentEntity)
	{
		
		return documentRepository.save(documentEntity);
	
	}
	
	public void deleteDocument(Integer id)//DELETES FROM BOTH ALFRESCO AND DATABASE
	{
		try {
		SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<>();
 
        /**
         *  user credentials
         */
        parameter.put(SessionParameter.USER, admin);
        parameter.put(SessionParameter.PASSWORD, admin);
 
        /**
         *  connection settings
         */
        parameter.put(SessionParameter.ATOMPUB_URL, alfrescoURL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        
        
        /**
         *  create session
         */
        Session session = factory.getRepositories(parameter).get(0).createSession();
        Folder root = session.getRootFolder();
		
        String alfrescoId= documentRepository.findAlfrescoID(id);
        Document d =(Document) session.getObject(alfrescoId);
        d.delete();
		documentRepository.deleteById(id);
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	public DocumentEntity findDocumentById(Integer id)
	{
		return documentRepository.getOne(id);
	}
	
	public DocumentEntity findByDocumentname(String documentname) 
	{
		return documentRepository.findByDocumentname(documentname);
	}
	
	public List<DocumentEntity> findAllDocuments()
	{
		return documentRepository.findAll();
	}
	
	public void updateDocument(DocumentEntity documentEntity)
	{
		try {
		for(DocumentEntity doc: documentRepository.findAll())
		{
			if(doc.getId().equals(documentEntity.getId()))
			{
				doc.setDocumentname(documentEntity.getDocumentname());
			}
		}
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	
	
	/**
	 * Author: Gontse Mochoana
	 * Date: 2019 December
	 * Purpose: THe method saves the document locally
	 * FROM PREVIOUS PROJECT
	 * @param file
	 * @param filePath
	 * @throws IOException
	 */
	public void saveDocument(MultipartFile file,String filePath) throws IOException
	{
		try {
		byte[] bytes = file.getBytes();
        Path path = Paths.get("/home/gontse/UploadedDocs/" + file.getOriginalFilename());
        Files.write(path, bytes);
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
		}
	}
	
	/**
	 * Author: Gontse Mochoana
	 * Date: 2019 December
	 * Purpose: The method changes the uploaded document to the entered document
	 * @param path
	 * @param name
	 * @param newName
	 * @return
	 * @throws IOException
	 */
	public File renameDocument(String path,String name, String newName) throws IOException
	{
			File newDoc = new File(path+newName+".pdf");
			File doc = new File(path+name);
			try {
				boolean b = newDoc.createNewFile();
				
				if(b)
				{
					doc.renameTo(newDoc);
						
				}
			}
			catch(Exception e)
			{
				logger.info(e.getMessage());
			}
			
			return newDoc;
	}
	
	public File readFile()
	{
		
		return new File("/home/gontse/UploadedDocs/REPORT-V2.pdf");

	}
	
	/**
	 * INSERT SEND TO ALFRESCO HERE BASED ON A FILE
	 * @param document
	 * @param alfrescoid
	 * @return
	 * @throws IOException
	 */

	public String sendToAlfresco(File document,String alfrescoid) throws IOException
	{
	
		File file2 = document;
		
        String name = file2.getName();
        /**
         * String fileP = file2.getAbsolutePath();
         */
		
		//NEW ADDITIONS 1
		byte[] fileContent = new byte[(int) file2.length()];
		FileInputStream istream = new FileInputStream(file2);
		
		try {
			istream.read(fileContent);
		} catch (IOException e) {
			
			logger.info(e.getMessage());
		}
		istream.close();
		
		//END OF NEW ADDTIONS
		
		
		/**
		 *  default factory implementation
		 */
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<>();
 
        /**
         *  user credentials
         */
        parameter.put(SessionParameter.USER, admin);
        parameter.put(SessionParameter.PASSWORD, admin);
 
        /** connection settings
         * 
         */
        parameter.put(SessionParameter.ATOMPUB_URL, alfrescoURL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        
        
        /** create session
         * 
         */
        Session session = factory.getRepositories(parameter).get(0).createSession();
        Folder root = session.getRootFolder();
    
  /**
        // properties
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        properties.put(PropertyIds.NAME, "20-NOV"); // folder name
 
        // create the folder
        Folder parent = root.createFolder(properties);
    //NVM  
        //String parentid=parent.getId();
     */   
        Folder fol = (Folder) session.getObject(alfrescoid);
       
       /**String extention = FilenameUtils.getExtension(fileP);
        * properties
        */
        Map<String, Object> properties2 = new HashMap<>();
        properties2.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties2.put(PropertyIds.NAME, name);
        
        //NEW ADDITIONS -2
        ContentStream contentStream = new ContentStreamImpl(name,BigInteger.valueOf(fileContent.length),"binary/octet-stream", new ByteArrayInputStream(fileContent));
        
        Document newDoc = fol.createDocument(properties2, contentStream, VersioningState.MAJOR);
        
       return newDoc.getId(); 
	}
	
	
	
	
	public String getActualNameNoPDF(String name)
	{
		String file = name;
		String word = ".pdf";
		String newName="";
		
		newName = file.replaceAll(word,"");
		
		return newName.trim();		
	}
	
	
	
	public int convertToImage(File file,String actualName,String filePath,DocumentEntity savedDocument) throws IOException
	{
		
		/**
		 * File nFile = file;
		 * String name = actualName;
		 */
		
		File newFile = file;
		
		
		
		/**
		 * String bar = barcode;
		 */
		String imageFilePath=filePath;
		//convert and send to the Queue
		
		String extension ="jpg";
		/**The extension to which we convert
		this will be the file name to which we get the pdf
		String filename= file.getAbsolutePath();
		
		Create a File object which we will pass the filePAth
		File theFile = new File(filename);
		
		Create a document object
		*/
		PDDocument document = null;
		
		int count =0;	
			/**
			 * with the document object we will load the specific file
			 */
			document = PDDocument.load(newFile);
		
		/**
		 * WE will create the PDFRENDERER OBJECT for the DOCUMENT OBJECT
		 */
	    PDFRenderer pdfRenderer = new PDFRenderer(document);
	    for (int page = 0; page < document.getNumberOfPages(); ++page) {
	    	
	    		count++;
	    		
	            BufferedImage bim = null;
		
				bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				
	       
				ImageIOUtil.writeImage(
				bim, String.format(imageFilePath+savedDocument.getId()+"-%d.%s", page + 1, extension), 300);
				
				
	    }
	    
	    
			document.close();
		
		/**
		 * sendToQueue(count,imageFilePath,barcode);
		 */
		
		return count;
	}
	
	/**
	 * NEW ADDITIONS 27 JANUARY 2020 FOR GETTING PDF FROM ALFRESCO
	 * @param fid
	 * @param did
	 * @return
	 */
	public String findAlfrescoIdByDocumentIdAndfileId(Integer fid, Integer did)
	{
		return documentRepository.findAlfrescoidByDocumentIdAndfileId(fid, did);
	}
	

	public void findAtAlfresco(String fid,String did) 
	{
		SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<>();
 
        /**
         *  user credentials
         */
        parameter.put(SessionParameter.USER, admin);
        parameter.put(SessionParameter.PASSWORD, admin);
 
        /**
         *  connection settings
         */
        parameter.put(SessionParameter.ATOMPUB_URL, alfrescoURL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        
        
        /**
         *  create session
         */
        Session session = factory.getRepositories(parameter).get(0).createSession();
        Folder root = session.getRootFolder();
		
        Folder fol = (Folder) session.getObject(fid);
        
        
        Document theDocumentFromAlfresco = (Document) session.getObject(did);
        
        ContentStream cs = theDocumentFromAlfresco.getContentStream();
        
        InputStream is = cs.getStream();

        File targetFile = new File("/home/gontse/FROMALFRESCO/targetFile.pdf");
        
        try {
        	
			Files.copy(is,targetFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		
        session.clear();
	}
	
	/**
	 * 27 JAN ADDITION TO JUST CALL TO
	 * @param did
	 * @return
	 */
	
	
	/*
	 * 02 FEBRUARY 2020
	 * AUthor: Gontse Mochoana
	 */
	
	public String findAlfrescoID(Integer did)
	{
		return documentRepository.findAlfrescoID(did);
	}
	
	
	public String findDocument(String alfrescoId) 
	{
		SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<>();
 
        /**
         *  user credentials
         */
        parameter.put(SessionParameter.USER, admin);
        parameter.put(SessionParameter.PASSWORD, admin);
 
        /**
         *  connection settings
         */
        parameter.put(SessionParameter.ATOMPUB_URL, alfrescoURL);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        
        
        /**
         *  create session
         */
        Session session = factory.getRepositories(parameter).get(0).createSession();
        Folder root = session.getRootFolder();
		
        
        Document theDocumentFromAlfresco = (Document) session.getObject(alfrescoId);
        String name = theDocumentFromAlfresco.getName();
        
        ContentStream cs = theDocumentFromAlfresco.getContentStream();
        
        InputStream is = cs.getStream();

        File targetFile = new File("/srv/www/htdocs/"+name);
        
        try {
			Files.copy(is,targetFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		
        session.clear();
        return "http://localhost/"+name;
	}
	
}
