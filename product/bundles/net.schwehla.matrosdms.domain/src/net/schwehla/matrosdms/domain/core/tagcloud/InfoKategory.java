package net.schwehla.matrosdms.domain.core.tagcloud;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.schwehla.matrosdms.adapter.ParentArrayAdapter;
import net.schwehla.matrosdms.domain.api.ITagInterface;
import net.schwehla.matrosdms.domain.core.Identifier;
import net.schwehla.matrosdms.domain.core.InfoBaseElementWithOrdinal;


@XmlAccessorType(XmlAccessType.FIELD)
public class InfoKategory extends InfoBaseElementWithOrdinal implements ITagInterface {
	
	// kann ich entsorgen ? 
	public void setName(String name) {
		this.name = name;
	}

	
	@XmlJavaTypeAdapter(ParentArrayAdapter.class) 
	private List<InfoKategory> parents;
	
	@XmlTransient
	private List<InfoKategory> children;

	private boolean object;

	public boolean isObject() {
		return object;
	}

	public void setObject(boolean object) {
		this.object = object;
	}

	private void init() {
		parents = new ArrayList<InfoKategory>();
		children = new ArrayList<InfoKategory>();
	}

	public InfoKategory() {
		init();
	}


	public List<InfoKategory> getParents() {
		return parents;
	}

	public void setParents(List<InfoKategory> parents) {
		this.parents = parents;
	}

	public List<InfoKategory> getChildren() {
		return children;
	}

	public void setChildren(List<InfoKategory> children) {
		this.children = children;
	}


	public List<InfoKategory> getInfoObjectList(boolean recursive) {

		List<InfoKategory> temp = new ArrayList<InfoKategory>();

		for (InfoKategory v : children) {
			
			if (v.isObject()) {
				temp.add(v);
			}
			
			if (recursive) {
				temp.addAll(v.getInfoObjectList(recursive));				
			}

		}

		return temp;
	}



	public Identifier getInfoTyp() {
		return getRoot().getIdentifier();
	}
	

	@Override
	public InfoKategory clone() {

		InfoKategory clone = new InfoKategory(identifier, name);
		return clone;
	
	}

	public InfoKategory(Identifier key, String name) {
		super(key, name);
		init();

	}

	public List<InfoKategory> GetRootElements() {
		List<InfoKategory> result = new ArrayList<InfoKategory>();

		if (parents.size() == 0) {
			result.add(this);
			return result;
		}

		Iterator<InfoKategory> enumerator = parents.iterator();

		while (enumerator.hasNext()) {
			InfoKategory item = enumerator.next();
			result.addAll(item.GetRootElements());
		}

		return result;

	}

	public void connectWithChild(InfoKategory child) {
		child.parents.add(this);
		// child.ParentIdList.Add(this.Id);
		children.add(child);
	}

	/**
	 * Liefert alle InfoKategorien als Set
	 */
	@Override
	public HashSet<InfoKategory> getSelfAndAllTransitiveChildren() {
		HashSet<InfoKategory> temp = new HashSet<InfoKategory>();

		temp.add(this);

		for (InfoKategory v : children) {
			temp.addAll(v.getSelfAndAllTransitiveChildren());
		}

		return temp;
	}


	/**
	 * Liefert alle InfoKategorien als Set
	 */
	public HashSet<InfoKategory> getSelfAndTransitiveParents() {
		HashSet<InfoKategory> temp = new HashSet<InfoKategory>();

		temp.add(this);
		for (InfoKategory v : parents) {
			temp.addAll(v.getSelfAndTransitiveParents());
		}

		return temp;
	}

	/**
	 * Per definition gibt es einen rootknoten welcher die Kategorie ausmacht
	 * @return
	 */
	public InfoKategory getRoot() {
	
		InfoKategory root = this;
		
		List <InfoKategory>  p = parents;
		while (p.size() > 0) {
			root = p.get(0);
			p = root.getParents();
		}
		
		return root;
			
	}
	
	// was ist das ?
	boolean dropfieldKategory;

	public boolean isDropfieldKategory() {
		return dropfieldKategory;
	}
	
	public void setDropfieldKategory(boolean dropfieldKategory) {
		this.dropfieldKategory = dropfieldKategory;
	}




}
