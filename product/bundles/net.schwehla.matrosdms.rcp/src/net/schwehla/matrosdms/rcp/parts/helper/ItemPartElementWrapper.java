package net.schwehla.matrosdms.rcp.parts.helper;

import java.io.File;

import net.schwehla.matrosdms.domain.core.InfoItem;

public class ItemPartElementWrapper {
	
	private ItemPartElementWrapper() {}
	
	public ItemPartElementWrapper(Type type) {
		this.type = type;
	}

	Type type;
	
	public enum Type {
		NEW, EXISTING
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public InfoItem getInfoItem() {
		return infoItem;
	}

	public void setInfoItem(InfoItem infoItem) {
		this.infoItem = infoItem;
	}

	public File getInboxFile() {
		return inboxFile;
	}

	public void setInboxFile(File inboxFile) {
		this.inboxFile = inboxFile;
	}

	InfoItem infoItem;
	
	File inboxFile;
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof ItemPartElementWrapper) {
			
			if (infoItem != null) {
				return infoItem.equals(((ItemPartElementWrapper) obj).getInfoItem());
			}
			
			if (inboxFile != null) {
				return inboxFile.equals(((ItemPartElementWrapper) obj).getInboxFile());
			}
			

		}
		
		return this.hashCode() == obj.hashCode();
		
	}

	@Override
	public int hashCode() {

		if (infoItem != null) {
			return infoItem.hashCode();
		}
		
		if (inboxFile != null) {
			return inboxFile.hashCode();
		}
		
		return this.hashCode();
	}
	
}
