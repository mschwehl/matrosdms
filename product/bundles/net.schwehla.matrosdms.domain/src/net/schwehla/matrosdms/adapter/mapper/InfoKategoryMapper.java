package net.schwehla.matrosdms.adapter.mapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import net.schwehla.matrosdms.domain.core.Identifier;

@XmlAccessorType(XmlAccessType.FIELD)
public class InfoKategoryMapper {
	
	Identifier type;
	public List<Identifier> entry ;
	public Identifier getType() {
		return type;
	}
	public void setType(Identifier type) {
		this.type = type;
	}
	public List<Identifier> getEntry() {
		return entry;
	}
	public void setEntry(List<Identifier> entry) {
		this.entry = entry;
	}

	
}
