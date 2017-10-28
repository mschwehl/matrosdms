package net.schwehla.matrosdms.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

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

		new Exporter().execute();
		
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
		

//		System.out.println(root);
		// others with seeAlso
        JAXBContext jc = JAXBContext.newInstance(InfoContext.class , InfoItem.class, 
        		InfoKategory.class, AttributeType.class, AbstractInfoAttribute.class); 
        Marshaller marshaller = jc.createMarshaller(); 
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        
		System.out.println("\nKategorie\n");
		
        // kategorie
		allKategory.stream().forEach( c -> {
			
			JAXBElement<InfoKategory> jaxbElement = new JAXBElement<InfoKategory>(
					new QName("InfoKategory"), InfoKategory.class, c);   
			try {
				marshaller.marshal(jaxbElement, System.out);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		
        // kategorie
		b.attList.stream().forEach( c -> {
			
			JAXBElement<AttributeType> jaxbElement = new JAXBElement<AttributeType>(
					new QName("AttributeType"), AttributeType.class, c);   
			try {
				marshaller.marshal(jaxbElement, System.out);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		
		
		
		
		// Contextliste
		service.keySet().stream().forEach( c -> {
			
			JAXBElement<InfoContext> jaxbElement = new JAXBElement<InfoContext>(
					new QName("InfoContext"), InfoContext.class, c);   
			try {
				marshaller.marshal(jaxbElement, System.out);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		List <InfoItem> allElements = new ArrayList<>();
		service.values().forEach( e -> allElements.addAll(e));
		
		System.out.println("\nItemlist\n");
		
		allElements.stream().forEach( c -> {
			
			JAXBElement<InfoItem> jaxbElement = new JAXBElement<InfoItem>(
					new QName("InfoItem"), InfoItem.class, c);   
			try {
				marshaller.marshal(jaxbElement, System.out);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
		});
		
		
		
		
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
	

}
