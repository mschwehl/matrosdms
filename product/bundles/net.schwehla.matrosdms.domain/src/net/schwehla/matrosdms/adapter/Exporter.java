package net.schwehla.matrosdms.adapter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import net.schwehla.matrosdms.domain.api.E_ATTRIBUTETYPE;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.core.InfoContext;
import net.schwehla.matrosdms.domain.core.InfoItem;
import net.schwehla.matrosdms.domain.core.InfoItemListProxy;
import net.schwehla.matrosdms.domain.core.InfoOrginalstore;
import net.schwehla.matrosdms.domain.core.attribute.AbstractInfoAttribute;
import net.schwehla.matrosdms.domain.core.attribute.AttributeType;
import net.schwehla.matrosdms.domain.core.attribute.InfoTextAttribute;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;
import net.schwehla.matrosdms.domain.metadata.MatrosMetadata;
import net.schwehla.matrosdms.rcp.MyGlobalConstants;


public class Exporter {
	
	public static void main(String[] args) throws Exception {

		new Exporter().xpath();
		
	}


	private void xpath() throws Exception {
		
		// others with seeAlso
        JAXBContext jc = JAXBContext.newInstance(InfoContext.class , InfoItem.class, 
        		InfoKategory.class, AttributeType.class, AbstractInfoAttribute.class); 
        Marshaller marshaller = jc.createMarshaller(); 
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        
        
		
		// parse the XML as a W3C Document
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new File("C:\\Users\\Martin\\Desktop\\export.xml"));

		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "//InfoItemListElement";
		NodeList infoKategoryNodes = (NodeList) xpath.evaluate(expression,document, XPathConstants.NODESET);
		
		for (int i=0; i < infoKategoryNodes.getLength(); i++) {

			Node root = (Node) xpath.evaluate("ROOT",  infoKategoryNodes.item(i), XPathConstants.NODE);
			
		
			List<Field> infoFields = getAllFields(new ArrayList<Field>(),InfoKategory.class);
			
			Map <Field,String> valMap = new HashMap<>();
			
			for (Field f : infoFields) {
				
				String val = (String) xpath.evaluate("./" + f.getName(), root, XPathConstants.STRING);
				valMap.put(f, val);
			}
		
			String uuid = (String) xpath.evaluate("./identifier/uuid", root, XPathConstants.STRING);
			
			InfoKategory k = new InfoKategory(Identifier.createImport( uuid  ), "xx");
			
			for (Field x : infoFields) {
				x.setAccessible(true);
				
				String val = valMap.get(x);
				
				if (val == null || val.trim().length() == 0) {
					continue;
				}
				
				String type = x.getType().getSimpleName();
				
				
				System.out.println(type);
				
				
				switch(type) {
				
					case "boolean":
						
						x.set(k, Boolean.parseBoolean(val) );
						
						break;
						
					case "int":
						
						x.set(k, Integer.parseInt(val) );
						
						break;
						
					case "Date":
						
						// xxx
						x.set(k, new Date() );
						
						break;
						
						
						
						
						
					default:
						
						x.set(k,val);
				}
			
				
				
			}
			
 			System.out.println(k);

			
			
			
		/*	
			String pk = (String) xpath.evaluate(".//pk", root, XPathConstants.STRING);
			String uuid = (String) xpath.evaluate("./identifier/uuid", root, XPathConstants.STRING);
			String uuid = (String) xpath.evaluate(".//dateCreated", root, XPathConstants.STRING);
			String uuid = (String) xpath.evaluate(".//uuid", root, XPathConstants.STRING);
			String uuid = (String) xpath.evaluate(".//uuid", root, XPathConstants.STRING);
			String uuid = (String) xpath.evaluate(".//uuid", root, XPathConstants.STRING);
		*/
		
	
			
			
		}
		
		
		
		

		
		
	}

	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		
		for (Field f : type.getDeclaredFields()) {
			if (f.getType().getName().equals(Identifier.class.getName())) {
				continue;
			}
			
		    if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
		        continue;
		    }
			
			fields.add(f);
			
		}
			
	    if (type.getSuperclass() != null) {
	    	getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
	

	static Map<InfoContext,List<InfoItem>> service = new HashMap<>();
	
	private void execute() throws Exception {
			
		
		
		InfoContextBuilder b = new InfoContextBuilder();
		
	
		InfoContext root = b.nextContext();
		b.nextItem(3);
		b.nextContext();
		b.nextItem(2);
		
		List <InfoKategory> allKategory = new ArrayList<>();
		
		InfoKategory k = b.nextKategory();
		
		InfoKategory k1= b.nextKategory();
		InfoKategory k2= b.nextKategory();
		
		k.connectWithChild(k1);
		k.connectWithChild(k2);
		
		allKategory.add(k);
		allKategory.add(k1);
		allKategory.add(k2);
		
		
		root.getTagList(MyGlobalConstants.ROOT_WER).add(k);
		root.getTagList(MyGlobalConstants.ROOT_WER).add(k2);
		
		
		AttributeType a1 = b.nextAttributeType();
		AttributeType a2 = b.nextAttributeType();
		

		InfoOrginalstore o = b.getNextStore();
		
		
		service.get(root).get(0).setStoreIdentifier(o.getIdentifier());
		service.get(root).get(0).setStoreItemNumber("" + o.getIdentifier().getPk());
		service.get(root).get(0).getAttributeList().add(b.nextInfoTextAttribute());
		service.get(root).get(0).getAttributeList().add(b.nextInfoTextAttribute());
		

		// others with seeAlso
        JAXBContext jc = JAXBContext.newInstance(InfoContext.class , InfoItem.class, 
        		InfoKategory.class, AttributeType.class, AbstractInfoAttribute.class); 
        Marshaller marshaller = jc.createMarshaller(); 
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        
        OutputStream metadataOutputStream = new ByteArrayOutputStream();

		metadataOutputStream.write("<Matros>".getBytes());
		
		metadataOutputStream.write("<InfoKategoryList>".getBytes());
        // kategorie
		allKategory.stream().forEach( c -> {
			
			JAXBElement<InfoKategory> jaxbElement = new JAXBElement<InfoKategory>(
					new QName("InfoKategory"), InfoKategory.class, c);   
			try {
				marshaller.marshal(jaxbElement, metadataOutputStream);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		metadataOutputStream.write("</InfoKategoryList>".getBytes());
		
		metadataOutputStream.write("<AttributeTypeList>".getBytes());
		
        // AttributType
		b.attList.stream().forEach( c -> {
			
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
		service.keySet().stream().forEach( c -> {
			
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

		
		List <InfoItem> allElements = new ArrayList<>();
		service.values().stream().forEach( e -> allElements.addAll(e));
		
		allElements.stream().sorted().forEach( c -> {
			
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
		
	//	System.out.println(metadataOutputStream.toString());	
		System.out.println(prettyFormat(metadataOutputStream.toString(),2));
		
		
		
	}

	class InfoContextBuilder {
		
		long nextIdent = 1L;
		
		List <InfoContext> cList = new ArrayList<>();
		List <AttributeType> attList = new ArrayList<>();
		
		
		InfoContext c ;
		
		public InfoOrginalstore getNextStore() {
			
			InfoOrginalstore i = new InfoOrginalstore(next(), "STORE_" + nextIdent);
			init(i);
			i.setShortname("SN");
			i.setOrdinal((int)nextIdent);
	
			nextIdent ++;
			return i;
	
		}
		
		public AttributeType nextAttributeType() {
	
			
			AttributeType i = new AttributeType(next(), "ATTRIBUTETYPE_" + nextIdent);
			init(i);
			i.setOrdinal((int)nextIdent);
			
			i.setKey("KEY_" + nextIdent);
			i.setType(E_ATTRIBUTETYPE.DATE);
			// todo
//			i.set
			
			
			attList.add(i);
	
			nextIdent ++;
			return i;
	
		}
		
		
		public InfoKategory nextKategory() {
			
	
			
			InfoKategory i = new InfoKategory(next(), "KATEGORY_" + nextIdent);
			init(i);
			i.setObject(true);
			i.setOrdinal((int)nextIdent);
	
			nextIdent ++;
			return i;
	
		}
		
		public InfoTextAttribute nextInfoTextAttribute() {
			
	
			
			InfoTextAttribute i = new InfoTextAttribute(next(), "InfoTextAttribute_" + nextIdent);
			init(i);
			i.setStrValue("VALUE_"+ nextIdent);
	
			nextIdent ++;
			return i;
	
		}
		
		
		public InfoContext nextContext() {
			
			c = new InfoContext(next(), "CONTEXT_" + nextIdent);
			init(c);
			
			c.setStorableInfoItemContainerListProxy( new InfoItemListProxy(c));
			nextIdent ++;
			
			cList.add(c);
			service.put(c, new ArrayList<>());
			return c;
		}
		
		public InfoItem nextItem() {
			
			InfoItem i = new InfoItem(c, next(), "INFOITEM_" + nextIdent);
			init(i);
			
			MatrosMetadata m = new MatrosMetadata();
			m.setFilename("setFilename_" + i.getIdentifier().getPk());
			m.setFilesize( i.getIdentifier().getPk());
			m.setMimetype("setMimetype_" + i.getIdentifier().getPk());
			m.setSha256("setSha256_" + i.getIdentifier().getPk());
			m.setTextLayer("setTextLayer_" + i.getIdentifier().getPk());
		
			i.setMetadata(m);
			
			service.get(c).add(i);
			
			nextIdent ++;
			
			return i;
			
		}
		
		public void nextItem(int i) {
			
			for (int x =0 ; x <i; x++) {
				nextItem();
			}
			
		}
		
		private Identifier next() {
			
			Identifier i = Identifier.create(nextIdent, "UUID_" + nextIdent );
			return i;
		}
		
		void init(InfoBaseElement b) {
			b.setName("NAME_" + b);
			b.setIcon("ICON_" + b);
			b.setDescription("DECRIPTION_" + b);
			b.setDateCreated((new Date(nextIdent*96400000  )));
			b.setDateArchived((new Date(nextIdent*96400000  + (1000*60*2) )));
		}
		
		
	}
	
	
	 
	public static String prettyFormat(String unformattedXml, int indent) {
	
		
	    try {
	        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
	        DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
	        LSSerializer writer = impl.createLSSerializer();
	        writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
	        writer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
	        LSOutput output = impl.createLSOutput();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        output.setByteStream(out);
	        writer.write(parseXmlFile(unformattedXml), output);
	        String xmlStr = new String(out.toByteArray(), "UTF-8"); // Charset is extremely important
	        return xmlStr;
	    } catch (Exception e) {
	        return unformattedXml;
	}
	    
	}
	
	
	private static Document parseXmlFile(String in) {
	    try {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(in));
	        return db.parse(is);
	    } catch (ParserConfigurationException e) {
	        throw new RuntimeException(e);
	    } catch (SAXException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

}
