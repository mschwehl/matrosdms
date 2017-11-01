package net.schwehla.matrosdms.persistenceservice.internal.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.search.SearchItemInput;
import net.schwehla.matrosdms.domain.search.SearchedInfoItemElement;
import net.schwehla.matrosdms.persistenceservice.IMatrosServiceService;
import net.schwehla.matrosdms.rcp.MatrosServiceException;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;

public class MatrosMetadataGenerator {
	
	ByteArrayOutputStream metadataOutputStream = new ByteArrayOutputStream();
	IMatrosServiceService service;
	Marshaller marshaller;
	
	public MatrosMetadataGenerator(IMatrosServiceService service) throws Exception {
		this.service = service;
		

		// others with seeAlso
        JAXBContext jc = JAXBContext.newInstance(InfoContext.class , InfoItem.class, 
        		InfoKategory.class, AttributeType.class, AbstractInfoAttribute.class); 
        marshaller = jc.createMarshaller(); 
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
        
        
	}

	public String generate() throws Exception {
		
	metadataOutputStream.write("<Matros>".getBytes());
		
		metadataOutputStream.write("<InfoKategoryList>".getBytes());
		
		appendKategory(MyGlobalConstants.ROOT_WER);
		appendKategory(MyGlobalConstants.ROOT_WAS);
		appendKategory(MyGlobalConstants.ROOT_WO);
		appendKategory(MyGlobalConstants.ROOT_ART);
		
		metadataOutputStream.write("</InfoKategoryList>".getBytes());
			
		metadataOutputStream.write("<AttributeTypeList>".getBytes());
		
        // AttributType
		service.loadAttributeTypeList().stream().forEach( c -> {
			
			JAXBElement<AttributeType> jaxbElement = new JAXBElement<AttributeType>(
					new QName("AttributeType"), AttributeType.class, c);   
			try {
				marshaller.marshal(jaxbElement, metadataOutputStream);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		
		metadataOutputStream.write("</AttributeTypeList>".getBytes());
		
		
		metadataOutputStream.write("<InfoContextList>".getBytes());
		
		// Contextliste
		service.loadInfoContextList(true).stream().forEach( c -> {
			
			JAXBElement<InfoContext> jaxbElement = new JAXBElement<InfoContext>(
					new QName("InfoContext"), InfoContext.class, c);   
			try {
				marshaller.marshal(jaxbElement, metadataOutputStream);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		metadataOutputStream.write("</InfoContextList>".getBytes());
		
		
		metadataOutputStream.write("<InfoItemList>".getBytes());

	
		List <SearchedInfoItemElement> allElements = service.searchInfoContextItems( new SearchItemInput());
		
//
//		service.sear.stream().forEach( e -> allElements.addAll(e));
		
		allElements.stream().sorted().forEachOrdered( c -> {
			
			JAXBElement<InfoItem> jaxbElement = new JAXBElement<InfoItem>(
					new QName("InfoItem"), InfoItem.class, c);   
			try {
				marshaller.marshal(jaxbElement, metadataOutputStream);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		metadataOutputStream.write("</InfoItemList>".getBytes());
		
		metadataOutputStream.write("</Matros>".getBytes());
		
		return metadataOutputStream.toString();
//		return metadataOutputStream.toString();


	}

	
	private void appendKategory( Identifier rootIdentifier) throws Exception {

		metadataOutputStream.write("<InfoItemListElement>".getBytes());
		
		InfoKategory kat = service.getInfoKategoryByIdentifier(rootIdentifier);

		
		JAXBElement<InfoKategory> jaxbElement = new JAXBElement<InfoKategory>(
				new QName("ROOT"), InfoKategory.class, kat);   

			marshaller.marshal(jaxbElement, metadataOutputStream);
	
			metadataOutputStream.write("<Children>".getBytes());
		
			List <InfoKategory> streamingOrderdList = new ArrayList<>();
			kat.appendOrderedTransitiveChildren(streamingOrderdList);
			
			
			for (InfoKategory element : streamingOrderdList) {
				JAXBElement<InfoKategory> kid = new JAXBElement<InfoKategory>(
						new QName("InfoKategory"), InfoKategory.class, element);   
					marshaller.marshal(kid, metadataOutputStream);
			}

			metadataOutputStream.write("</Children>".getBytes());
			
			metadataOutputStream.write("</InfoItemListElement>".getBytes());
	}


	
}
