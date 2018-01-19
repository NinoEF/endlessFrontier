package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "petList")
public class PetList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5551264487500402234L;

	private List<Pet> pets;

	@XmlElementRef()
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	@XmlRootElement(name = "pet")
	public static class Pet implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -996073801658183453L;

		String kindNum;
		String className;
		String name;
		String tribe;
		String type;
		String rank;
		String skill1;
		String value1;
		String skill2;
		String value2;
		String value3;
		String masterSkill;
		String couple;
		String incGoldLevel;
		String treasure;
		String isPercent;
		String isAlpha;
		
		public String getKindNum() {
			return kindNum;
		}
		@XmlElement
		public void setKindNum(String kindNum) {
			this.kindNum = kindNum;
		}
		public String getClassName() {
			return className;
		}
		@XmlElement
		public void setClassName(String className) {
			this.className = className;
		}
		public String getName() {
			return name;
		}
		@XmlElement
		public void setName(String name) {
			this.name = name;
		}
		public String getTribe() {
			return tribe;
		}
		@XmlElement
		public void setTribe(String tribe) {
			this.tribe = tribe;
		}
		public String getType() {
			return type;
		}
		@XmlElement
		public void setType(String type) {
			this.type = type;
		}
		public String getRank() {
			return rank;
		}
		@XmlElement
		public void setRank(String rank) {
			this.rank = rank;
		}
		public String getSkill1() {
			return skill1;
		}
		public void setSkill1(String skill1) {
			this.skill1 = skill1;
		}
		public String getValue1() {
			return value1;
		}
		public void setValue1(String value1) {
			this.value1 = value1;
		}
		public String getSkill2() {
			return skill2;
		}
		public void setSkill2(String skill2) {
			this.skill2 = skill2;
		}
		public String getValue2() {
			return value2;
		}
		public void setValue2(String value2) {
			this.value2 = value2;
		}
		public String getValue3() {
			return value3;
		}
		public void setValue3(String value3) {
			this.value3 = value3;
		}
		public String getMasterSkill() {
			return masterSkill;
		}
		public void setMasterSkill(String masterSkill) {
			this.masterSkill = masterSkill;
		}
		public String getCouple() {
			return couple;
		}
		public void setCouple(String couple) {
			this.couple = couple;
		}
		public String getIncGoldLevel() {
			return incGoldLevel;
		}
		public void setIncGoldLevel(String incGoldLevel) {
			this.incGoldLevel = incGoldLevel;
		}
		public String getTreasure() {
			return treasure;
		}
		public void setTreasure(String treasure) {
			this.treasure = treasure;
		}
		public String getIsPercent() {
			return isPercent;
		}
		public void setIsPercent(String isPercent) {
			this.isPercent = isPercent;
		}
		public String getIsAlpha() {
			return isAlpha;
		}
		public void setIsAlpha(String isAlpha) {
			this.isAlpha = isAlpha;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}

	}
}
