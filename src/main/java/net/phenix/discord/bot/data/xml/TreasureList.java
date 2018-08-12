package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "treasureList")
public class TreasureList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7241544252238184690L;
	
	private List<Treasure> treasures;
	
	@XmlElementRef()
	public List<Treasure> getTreasures() {
		return treasures;
	}


	public void setTreasures(List<Treasure> treasures) {
		this.treasures = treasures;
	}

	@XmlRootElement(name = "treasure")
	public static class Treasure implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8895270537090320152L;
		
		String kindNum;
		String name;
		String mainCode;
		String subCode;
		String grade;
		String maxLv;
		String desc;
		String openCost;
		String skillCode1;
		String ability1;
		String ability11;
		String ability21;
		String skillCode2;
		String ability2;
		String ability12;
		String ability22;
		String skillCode3;
		String ability3;
		String upgradeCostType;
		String showDesc;
		String sortId;
		
		public String getKindNum() {
			return kindNum;
		}
		@XmlElement
		public void setKindNum(String kindNum) {
			this.kindNum = kindNum;
		}
		public String getName() {
			return name;
		}
		@XmlElement
		public void setName(String name) {
			this.name = name;
		}
		public String getMainCode() {
			return mainCode;
		}
		@XmlElement
		public void setMainCode(String mainCode) {
			this.mainCode = mainCode;
		}
		public String getSubCode() {
			return subCode;
		}
		@XmlElement
		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}
		public String getGrade() {
			return grade;
		}
		@XmlElement
		public void setGrade(String grade) {
			this.grade = grade;
		}
		public String getMaxLv() {
			return maxLv;
		}
		@XmlElement
		public void setMaxLv(String maxLv) {
			this.maxLv = maxLv;
		}
		public String getDesc() {
			return desc;
		}
		@XmlElement
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public String getOpenCost() {
			return openCost;
		}
		@XmlElement
		public void setOpenCost(String openCost) {
			this.openCost = openCost;
		}
		public String getSkillCode1() {
			return skillCode1;
		}
		@XmlElement
		public void setSkillCode1(String skillCode1) {
			this.skillCode1 = skillCode1;
		}
		public String getAbility1() {
			return ability1;
		}
		@XmlElement
		public void setAbility1(String ability1) {
			this.ability1 = ability1;
		}
		public String getAbility11() {
			return ability11;
		}
		@XmlElement
		public void setAbility11(String ability11) {
			this.ability11 = ability11;
		}
		public String getAbility21() {
			return ability21;
		}
		@XmlElement
		public void setAbility21(String ability21) {
			this.ability21 = ability21;
		}
		public String getSkillCode2() {
			return skillCode2;
		}
		@XmlElement
		public void setSkillCode2(String skillCode2) {
			this.skillCode2 = skillCode2;
		}
		public String getAbility2() {
			return ability2;
		}
		@XmlElement
		public void setAbility2(String ability2) {
			this.ability2 = ability2;
		}
		public String getAbility12() {
			return ability12;
		}
		@XmlElement
		public void setAbility12(String ability12) {
			this.ability12 = ability12;
		}
		public String getAbility22() {
			return ability22;
		}
		@XmlElement
		public void setAbility22(String ability22) {
			this.ability22 = ability22;
		}
		public String getSkillCode3() {
			return skillCode3;
		}
		@XmlElement
		public void setSkillCode3(String skillCode3) {
			this.skillCode3 = skillCode3;
		}
		public String getAbility3() {
			return ability3;
		}
		@XmlElement
		public void setAbility3(String ability3) {
			this.ability3 = ability3;
		}
		public String getUpgradeCostType() {
			return upgradeCostType;
		}
		@XmlElement
		public void setUpgradeCostType(String upgradeCostType) {
			this.upgradeCostType = upgradeCostType;
		}
		public String getShowDesc() {
			return showDesc;
		}
		@XmlElement
		public void setShowDesc(String showDesc) {
			this.showDesc = showDesc;
		}
		public String getSortId() {
			return sortId;
		}
		@XmlElement
		public void setSortId(String sortId) {
			this.sortId = sortId;
		}
		
	}

}
