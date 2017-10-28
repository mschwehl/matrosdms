package net.schwehla.matrosdms.adapter.mapper;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoKategorySetMapper {
	
	List <InfoKategoryMapper> list;

	public List<InfoKategoryMapper> getList() {
		return list;
	}

	public void setList(List<InfoKategoryMapper> list) {
		this.list = list;
	}

}
