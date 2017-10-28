package net.schwehla.matrosdms.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.schwehla.matrosdms.domain.api.IIdentifiable;
import net.schwehla.matrosdms.domain.core.Identifier;

public class IDAdapter extends XmlAdapter <Identifier, IIdentifiable>{

	@Override
	public Identifier marshal(IIdentifiable ident) throws Exception {
		return ident.getIdentifier();
	}

	@Override
	public IIdentifiable unmarshal(Identifier ident
			) throws Exception {
		throw new IllegalStateException("Not Implemented");
	}



}
