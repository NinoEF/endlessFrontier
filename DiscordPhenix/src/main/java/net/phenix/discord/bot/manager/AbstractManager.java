package net.phenix.discord.bot.manager;

import net.dv8tion.jda.core.events.message.GenericMessageEvent;

public class AbstractManager {

	GenericMessageEvent event;

	public GenericMessageEvent getEvent() {
		return event;
	}

	public void setEvent(GenericMessageEvent event) {
		this.event = event;
	}
	
	
}
