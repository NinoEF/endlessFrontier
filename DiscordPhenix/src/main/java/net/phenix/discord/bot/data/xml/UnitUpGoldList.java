	package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "unitUpGoldList")
public class UnitUpGoldList implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314979068602262237L;

	List<UnitUpGold> unitUpGolds;
	
	@XmlElementRef()
	public List<UnitUpGold> getUnitUpGolds() {
		return unitUpGolds;
	}


	public void setUnitUpGolds(List<UnitUpGold> unitUpGolds) {
		this.unitUpGolds = unitUpGolds;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@XmlRootElement(name = "unitUpGold")
	public static class UnitUpGold implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3476163478578802204L;
		
		Integer level;
		String rare1;
		String rare2;
		String rare3;
		String rare4;
		String rare5;
		String rare6;
		String rare7;
		String rare8;
		String rare9;
		String rare10;
		
		public Integer getLevel() {
			return level;
		}
		@XmlElement
		public void setLevel(Integer level) {
			this.level = level;
		}
		public String getRare1() {
			return rare1;
		}
		@XmlElement
		public void setRare1(String rare1) {
			this.rare1 = rare1;
		}
		public String getRare2() {
			return rare2;
		}
		@XmlElement
		public void setRare2(String rare2) {
			this.rare2 = rare2;
		}
		public String getRare3() {
			return rare3;
		}
		@XmlElement
		public void setRare3(String rare3) {
			this.rare3 = rare3;
		}
		public String getRare4() {
			return rare4;
		}
		@XmlElement
		public void setRare4(String rare4) {
			this.rare4 = rare4;
		}
		public String getRare5() {
			return rare5;
		}
		@XmlElement
		public void setRare5(String rare5) {
			this.rare5 = rare5;
		}
		public String getRare6() {
			return rare6;
		}
		@XmlElement
		public void setRare6(String rare6) {
			this.rare6 = rare6;
		}
		public String getRare7() {
			return rare7;
		}
		@XmlElement
		public void setRare7(String rare7) {
			this.rare7 = rare7;
		}
		public String getRare8() {
			return rare8;
		}
		@XmlElement
		public void setRare8(String rare8) {
			this.rare8 = rare8;
		}
		public String getRare9() {
			return rare9;
		}
		@XmlElement
		public void setRare9(String rare9) {
			this.rare9 = rare9;
		}
		public String getRare10() {
			return rare10;
		}
		@XmlElement
		public void setRare10(String rare10) {
			this.rare10 = rare10;
		}
	}
}
