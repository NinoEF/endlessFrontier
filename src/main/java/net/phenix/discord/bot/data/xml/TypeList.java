package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "typeList", propOrder = {
    "type"
})
@XmlRootElement(name = "typeList")
public class TypeList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5924655105066775374L;
	
	private List<Type> type;

    public List<Type> getType() {
        if (type == null) {
        	type = new ArrayList<Type>();
        }
        return this.type;
    }
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "type", propOrder = {
	    "content"
	})
	@XmlRootElement(name = "type")
	public static class Type implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 6233574410011542328L;
		
		
	    @XmlMixed
	    @XmlAnyElement(lax = true)
	    protected List<Object> content;
	    
	    public List<Object> getContent() {
	        if (content == null) {
	            content = new ArrayList<Object>();
	        }
	        return this.content;
	    }
	    
	}

}
