package net.phenix.discord.bot.manager;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import net.dv8tion.jda.core.entities.impl.PrivateChannelImpl;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.phenix.discord.bot.data.xml.Config;

public class ConfigManager extends AbstractManager {

	Logger log = Logger.getLogger(getClass());

	public static ConfigManager getInstance() {
		return new ConfigManager();
	}

	public static Config getConfig(GenericMessageEvent event){
		
		try{
			if(!(event.getChannel() instanceof PrivateChannelImpl)){
				File file = new File("/home/pi/discord/config/"+((MessageReceivedEvent)event).getGuild().getId()+".xml");
				JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				return (Config) jaxbUnmarshaller.unmarshal(file);
			}	
			
		} catch (Exception e){
			return new Config();
		}
		return new Config();
	}
}
