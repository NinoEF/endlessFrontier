package net.phenix.discord.bot.data.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "questList")
public class QuestList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3030788387794946393L;
	List<Quest> quests = new ArrayList<>();

	@XmlElementRef()
	public List<Quest> getQuests() {
		return quests;
	}

	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}

	@XmlRootElement(name = "quest")
	public static class Quest implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3272829869687219415L;
		
		String kindNum;
		String name;
		String duration;
		String openGold;
		String initGold;
		String incGold;
		String upgradeGold;
		String maxLv;
		String autoGold;
		String autoGem;
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
		public String getDuration() {
			return duration;
		}
		@XmlElement
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public String getOpenGold() {
			return openGold;
		}
		@XmlElement
		public void setOpenGold(String openGold) {
			this.openGold = openGold;
		}
		public String getInitGold() {
			return initGold;
		}
		@XmlElement
		public void setInitGold(String initGold) {
			this.initGold = initGold;
		}
		public String getIncGold() {
			return incGold;
		}
		@XmlElement
		public void setIncGold(String incGold) {
			this.incGold = incGold;
		}
		public String getUpgradeGold() {
			return upgradeGold;
		}
		@XmlElement
		public void setUpgradeGold(String upgradeGold) {
			this.upgradeGold = upgradeGold;
		}
		public String getMaxLv() {
			return maxLv;
		}
		@XmlElement
		public void setMaxLv(String maxLv) {
			this.maxLv = maxLv;
		}
		public String getAutoGold() {
			return autoGold;
		}
		@XmlElement
		public void setAutoGold(String autoGold) {
			this.autoGold = autoGold;
		}
		public String getAutoGem() {
			return autoGem;
		}
		@XmlElement
		public void setAutoGem(String autoGem) {
			this.autoGem = autoGem;
		}
	}
}
