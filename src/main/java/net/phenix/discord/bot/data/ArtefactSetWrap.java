package net.phenix.discord.bot.data;

public class ArtefactSetWrap {

	String id;
	String trans;
	Integer level;
	
	public ArtefactSetWrap(String id, String trans, Integer level) {
		super();
		this.id = id;
		this.trans = trans;
		this.level = level;
	}
	
	public ArtefactSetWrap() {
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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ArtefactSetWrap)){
			return false;
		}
		ArtefactSetWrap u = (ArtefactSetWrap) obj;
		return this.getId().equals(u.getId());
	}
}
