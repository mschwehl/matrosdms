package net.schwehla.matrosdms.adapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.schwehla.matrosdms.adapter.mapper.InfoKategoryMapper;
import net.schwehla.matrosdms.adapter.mapper.InfoKategorySetMapper;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategoryList;


// http://blog.bdoughan.com/2010/07/xmladapter-jaxbs-secret-weapon.html
public class LinkingInfoBaseElementAdapter extends XmlAdapter <InfoKategorySetMapper , Map <Identifier,InfoKategoryList>> {

	@Override
	public InfoKategorySetMapper marshal(Map<Identifier, InfoKategoryList> map) throws Exception {
	
		InfoKategorySetMapper list = new InfoKategorySetMapper();
		list.setList(new ArrayList<>());
		
	
		map.keySet().stream().forEach( e -> {
			
			InfoKategoryMapper result = new InfoKategoryMapper();
			
			result.setType( e );
			result.entry = map.get(e).stream().map( x -> x.getIdentifier()).collect(Collectors.toList());
			
			list.getList().add(result);
		});
		
		return list;
	}

	@Override
	public Map<Identifier, InfoKategoryList> unmarshal(InfoKategorySetMapper arg0) throws Exception {
		throw new IllegalStateException("not implemented");
	}





}
