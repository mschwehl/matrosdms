package net.schwehla.matrosdms.domain.api;

import java.util.Set;

import net.schwehla.matrosdms.domain.core.tagcloud.InfoKategory;

public interface ITagInterface extends IIdentifiable {


	Set<InfoKategory> getSelfAndAllTransitiveChildren();

}
