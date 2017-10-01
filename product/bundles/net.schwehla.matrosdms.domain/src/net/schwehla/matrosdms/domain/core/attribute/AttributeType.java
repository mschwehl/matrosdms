package net.schwehla.matrosdms.domain.core.attribute;

import net.schwehla.matrosdms.domain.api.E_ATTRIBUTETYPE;
import net.schwehla.matrosdms.domain.core.InfoBaseElement;
import net.schwehla.matrosdms.domain.util.Identifier;

public class AttributeType extends InfoBaseElement {
	
	String key;
	E_ATTRIBUTETYPE type;
	
	String defaultValueScript;
	String validateScript;
	String pattern;
	String unit;
	
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValidateScript() {
		return validateScript;
	}

	public void setValidateScript(String validateScript) {
		this.validateScript = validateScript;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDefaultValueScript() {
		return defaultValueScript;
	}

	public void setDefaultValueScript(String defaultValueScript) {
		this.defaultValueScript = defaultValueScript;
	}

	public AttributeType(Identifier primaryKey,  String name) {
		super(primaryKey,name);
	}

	public E_ATTRIBUTETYPE getType() {
		return type;
	}

	public void setType(E_ATTRIBUTETYPE type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
