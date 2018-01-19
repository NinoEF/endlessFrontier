package net.phenix.discord.bot.data;

public class PetWrap {

	String id;
	String level;
	String fragment;
	boolean principale = false;
	boolean resu = false;
	
	public PetWrap(String id, String level, String fragment) {
		super();
		this.id = id;
		this.level = level;
		this.fragment = fragment;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getFragment() {
		return fragment;
	}
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public boolean isPrincipale() {
		return principale;
	}

	public void setPrincipale(boolean principale) {
		this.principale = principale;
	}

	public boolean isResu() {
		return resu;
	}

	public void setResu(boolean resu) {
		this.resu = resu;
	}
	
}
