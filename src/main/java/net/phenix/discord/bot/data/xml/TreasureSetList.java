package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "treasureSetList")
public class TreasureSetList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7241544252238184690L;
	
	private List<TreasureSet> treasureSets;
	
	@XmlElementRef()
	public List<TreasureSet> getTreasureSets() {
		return treasureSets;
	}

	public void setTreasureSets(List<TreasureSet> treasureSets) {
		this.treasureSets = treasureSets;
	}

	@XmlRootElement(name = "treasureSet")
	public static class TreasureSet implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8895270537090320152L;
		
		String kindNum;
		String title; 
		String itemList;
		String numSetList;
		String skillList;
		String valueList;
		String valueList1;
		String valueList2;
		String desc;
		String history;
		String showDesc;

		public String getKindNum() {
			return kindNum;
		}
		@XmlElement
		public void setKindNum(String kindNum) {
			this.kindNum = kindNum;
		}
		public String getTitle() {
			return title;
		}
		@XmlElement
		public void setTitle(String title) {
			this.title = title;
		}
		public String getItemList() {
			return itemList;
		}
		@XmlElement
		public void setItemList(String itemList) {
			this.itemList = itemList;
		}
		public String getNumSetList() {
			return numSetList;
		}
		@XmlElement
		public void setNumSetList(String numSetList) {
			this.numSetList = numSetList;
		}
		public String getSkillList() {
			return skillList;
		}
		@XmlElement
		public void setSkillList(String skillList) {
			this.skillList = skillList;
		}
		public String getValueList() {
			return valueList;
		}
		@XmlElement
		public void setValueList(String valueList) {
			this.valueList = valueList;
		}
		public String getValueList1() {
			return valueList1;
		}
		@XmlElement
		public void setValueList1(String valueList1) {
			this.valueList1 = valueList1;
		}
		public String getValueList2() {
			return valueList2;
		}
		@XmlElement
		public void setValueList2(String valueList2) {
			this.valueList2 = valueList2;
		}
		public String getDesc() {
			return desc;
		}
		@XmlElement
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getHistory() {
			return history;
		}
		@XmlElement
		public void setHistory(String history) {
			this.history = history;
		}
		public String getShowDesc() {
			return showDesc;
		}
		@XmlElement
		public void setShowDesc(String showDesc) {
			this.showDesc = showDesc;
		}
	}
}
