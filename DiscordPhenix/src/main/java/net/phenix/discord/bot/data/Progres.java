package net.phenix.discord.bot.data;

public class Progres implements Comparable<Progres>{

	String name;
	Integer note;
	
	
	public Progres(String name, Integer note) {
		super();
		this.name = name;
		this.note = note;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getNote() {
		return note;
	}


	public void setNote(Integer note) {
		this.note = note;
	}


	@Override
	public int compareTo(Progres o) {
		return o.getNote().compareTo(this.getNote());
	}
}
