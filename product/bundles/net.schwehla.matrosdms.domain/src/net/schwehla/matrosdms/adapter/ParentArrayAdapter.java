package net.schwehla.matrosdms.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.schwehla.matrosdms.domain.api.IIdentifiable;

public class ParentArrayAdapter extends XmlAdapter <String, List<IIdentifiable>>{

	@Override
	public String marshal(List<IIdentifiable> list) throws Exception {
		return list.stream().map( e -> e.getIdentifier().getUuid()).collect(Collectors.joining(","));
	}

	@Override
	public List<IIdentifiable> unmarshal(String list) throws Exception {
		throw new IllegalStateException("not implemented");
	}


}
