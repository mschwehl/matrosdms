package net.schwehla.matrosdms.domain.api;

import java.util.HashSet;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

public interface ITagInterface extends IIdentifiable {

//	public String getInfoTyp();
//
//	public void setInfoTyp(String typ);

	HashSet<InfoKategory> getSelfAndAllTransitiveChildren();

}
