package net.phenix.discord.bot.data;

public class ArtefactWrap {

	String id;
	String trans;
	String level;
	
	public ArtefactWrap(String id, String trans, String level) {
		super();
		this.id = id;
		this.trans = trans;
		this.level = level;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrans() {
		return trans;
	}
	public void setTrans(String trans) {
		this.trans = trans;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
