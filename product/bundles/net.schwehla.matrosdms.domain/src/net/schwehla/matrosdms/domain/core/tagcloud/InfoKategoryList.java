package net.schwehla.matrosdms.domain.core.tagcloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.util.Identifier;



public class InfoKategoryList extends InfoBaseElement implements List <InfoKategory> {
	
	
	private InfoKategoryList() {
		
	}
	

	public InfoKategoryList(Identifier key, String name) {
		super(key, name);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	List <InfoKategory> elements = new ArrayList <InfoKategory> ();
	
	@Override
	public boolean add(InfoKategory e) {
		return elements.add(e);
	}

	@Override
	public void add(int index, InfoKategory element) {
		 elements.add(index,element);	
	}

	@Override
	public boolean addAll(Collection<? extends InfoKategory> c) {
		return elements.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends InfoKategory> c) {
		return elements.addAll(index,c);
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean contains(Object o) {
		return elements.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return elements.contains(c);
	}

	@Override
	public InfoKategory get(int index) {
		return elements.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<InfoKategory> iterator() {
		return elements.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOf(o);
	}

	@Override
	public ListIterator<InfoKategory> listIterator() {
		return listIterator();
	}

	@Override
	public ListIterator<InfoKategory> listIterator(int index) {
		return elements.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public InfoKategory remove(int index) {
		return elements.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	@Override
	public InfoKategory set(int index, InfoKategory element) {
		return elements.set(index, element);
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public List<InfoKategory> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return elements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	/**
	 * Hier k�nnte eine Optimierung gemacht werden
	 * 
	 * @param currentList
	 * @return
	 */
	
	public boolean checkRekursivKategoriePasstRekursiv(InfoKategoryList currentList) {
		
		if (currentList == null || currentList.isEmpty()) {
			return false;
		}
		
		// Hier platz f�r caching falls die performance einmal zu W�nschen �brig l�sst
		
		Set<InfoKategory> imTagGraph = getAllTransitiveChildren();
        return imTagGraph.containsAll(currentList);
         
	}
	
	// todo: in die Liste auslagern
	
	public HashSet<InfoKategory> getAllTransitiveChildren() {

		HashSet<InfoKategory> tmp;

		// HashSet<ITagInterface>
		tmp = new HashSet<InfoKategory>();

		for (InfoKategory e : elements) {
			tmp.addAll(e.getSelfAndAllTransitiveChildren());
		}

		return tmp;

	}


	
	
	// todo: in die Liste auslagern
	
	public HashSet<InfoKategory> getSelfAndTransitiveParents() {

		HashSet<InfoKategory> tmp;

		// HashSet<ITagInterface>
		tmp = new HashSet<InfoKategory>();

		for (InfoKategory e : elements) {
			tmp.addAll(e.getSelfAndTransitiveParents());
		}

		return tmp;

	}


	
	
	
}
