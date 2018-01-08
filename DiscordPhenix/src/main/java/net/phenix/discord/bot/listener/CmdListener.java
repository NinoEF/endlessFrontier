package net.phenix.discord.bot.listener;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.phenix.discord.bot.manager.PetManager;

public class CmdListener extends ListenerAdapter {
	
	
	@Override	
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageChannel channel = event.getChannel();
		
//		if (event.isFromType(ChannelType.PRIVATE))
//        {
//            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
//                                    event.getMessage().getContentDisplay());
//        }
//        else
//        {
//            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
//                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
//                        event.getMessage().getContentDisplay());
//        }
		
		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		if(content.startsWith("!")){
			System.out.println(event.getAuthor().getName() + " : " + content);
			// getRawContent() is an atomic getter
			// getContent() is a lazy getter which modifies the content for e.g.
			// console view (strip discord formatting)
			if (content.equals("!ping")) {
				//Important to call .queue() on the RestAction returned by sendMessage(...)
				channel.sendMessage("Pong!").queue(); 
				
			} else if (content.startsWith("!petfc")) {
				PetManager.petForecast(channel, content);  
			}
		}
	}
	
}