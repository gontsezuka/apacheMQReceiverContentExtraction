package com.zukalover.demo.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zukalover.demo.MessageConsumer;
import com.zukalover.demo.entity.DocumentEntity;

@Service
public class SolrService {
	
	@Autowired
	MessageConsumer messageConsumer;	
	
	@Autowired
	DocumentService documentService;

	public SolrService()
	{
		/**
		 * 
		 */
		
	}
	
	public void sendToSorl(String messageFromActiveMQ, String extractedText)
	{
		String documentID = messageConsumer.getDocumentID(messageFromActiveMQ);
		String messageWithoutPath = messageConsumer.getSubstringWithRemovedPathFromMessage(messageFromActiveMQ);
		int indexOfDash = messageConsumer.getIndexOfSpecifiedCharacter('-', messageWithoutPath);
		String pageNumberAndJPGExtension= removeCharsUntilPositionOfDash(indexOfDash, messageWithoutPath);
		int indexOfFullStop = messageConsumer.getIndexOfSpecifiedCharacter('.', pageNumberAndJPGExtension);
		String pageNumberString = pageNumberAndJPGExtension.substring(0, indexOfFullStop);
		
		int documentIDInteger = Integer.parseInt(documentID);
		int pageNumberInteger = Integer.parseInt(pageNumberString);
			
		DocumentEntity documentToIndex = documentService.findDocumentById(documentIDInteger);
		
		SolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/Solr/Opti_core").build();
		
		SolrInputDocument document = new SolrInputDocument();
		
		document.addField("id", documentToIndex.getId());
		document.addField("alfrescoID", documentToIndex.getAlfrescoid());
		document.addField("name", documentToIndex.getDocumentname());
		document.addField("folder", documentToIndex.getFile().getFilename());
		document.addField("extractedContent", "{page-"+pageNumberInteger+":"+extractedText+"}");
		
	}
	
	public String removeCharsUntilPositionOfDash(int indexOfDash,String messageWithoutPath)
	{
		return messageWithoutPath.substring(indexOfDash+1);
	}
	
}
