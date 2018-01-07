package net.phenix.discord.bot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CmdListener extends ListenerAdapter {
	
	String DATE_FORMATTER = "dd/MM/yyyy";
	
	@Override	
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageChannel channel = event.getChannel();
		
		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		System.out.println(event.getAuthor().getName() + " : " + content);
		// getRawContent() is an atomic getter
		// getContent() is a lazy getter which modifies the content for e.g.
		// console view (strip discord formatting)
		if (content.equals("!ping")) {
			channel.sendMessage("Pong!").queue(); // Important to call .queue()
													// on the RestAction
													// returned by
													// sendMessage(...)
		} else if (content.startsWith("!petfc")) {
			String[] args = content.split(" ");
			
			String exemple = "exemple : !petfc 32 3 7";
			if (args.length != 4) {
				channel.sendMessage("La commande est erronée.\n" 
				+ "Veuillez saisir : !petfc <nb fragment> <lvl pet> <nb fragment/jour>\n" 
				+ exemple).queue();
				return;
			}

			Double curFrag = null;
			Double petLevel = null;
			Double nbFragPerDay = null;
			try {
				curFrag = Double.parseDouble(args[1]);
			} catch (NumberFormatException e) {
				channel.sendMessage("La commande est erronée.\n"
						+ args[1] +" n'est pas un nombre\n"
						+ exemple).queue();
				return;
			}
			
			try{
				petLevel = Double.parseDouble(args[2]);
			} catch (NumberFormatException e) {
				channel.sendMessage("La commande est erronée.\n"
						+ args[2] +" n'est pas un nombre\n"
						+ exemple).queue();
				return;
			}
			if(petLevel < 0 || petLevel > 5){
				channel.sendMessage("La commande est erronée.\n"
						+ "<<lvl pet> n'est pas correct, devrait être 0,1,2,3,4 ou 5").queue();
				return;
			}			
			
			try{
				nbFragPerDay = Double.parseDouble(args[3]);
				if(nbFragPerDay == 0){
					channel.sendMessage("La commande est erronée.\n"
							+ "<nb fragment/jour> ne peut pas être 0.").queue();
					return;
				}
			} catch (NumberFormatException e) {
				channel.sendMessage("La commande est erronée.\n"
						+ args[3] +" n'est pas un nombre\n"
						+ exemple).queue();
				return;
			}
			
			String forecast5 = petForecast(curFrag, nbFragPerDay, 330.0, petLevel);
			String forecast4 = petForecast(curFrag, nbFragPerDay, 180.0, petLevel);
			String forecast3 = petForecast(curFrag, nbFragPerDay, 80.0, petLevel);
			String forecast2 = petForecast(curFrag, nbFragPerDay, 30.0, petLevel);
			String forecast1 = petForecast(curFrag, nbFragPerDay, 10.0, petLevel);
			
			
			if(petLevel == 0){				
				channel.sendMessage("★ : "+ forecast1+"\n"
						+ "★★: "+ forecast2+"\n"
						+ "★★★: "+ forecast3+"\n"
						+ "★★★★ : "+ forecast4+"\n"
						+ "★★★★★ : "+ forecast5+"\n").queue();
				return;
				
			} else if(petLevel == 1){
				channel.sendMessage( "★★: "+ forecast2+"\n"
						+ "★★★: "+ forecast3+"\n"
						+ "★★★★ : "+ forecast4+"\n"
						+ "★★★★★ : "+ forecast5+"\n").queue();
				return;
			} else if(petLevel == 2){
				channel.sendMessage( "★★★: "+ forecast3+"\n"
						+ "★★★★ : "+ forecast4+"\n"
						+ "★★★★★ : "+ forecast5+"\n").queue();
				return;
				
			} else if(petLevel == 3){
				channel.sendMessage( "★★★★ : "+ forecast4+"\n"
						+ "★★★★★ : "+ forecast5+"\n").queue();
				return;
			} else if(petLevel == 4){
				channel.sendMessage( "★★★★★ : "+ forecast5+"\n").queue();
				return;
			} else if(petLevel == 5){
				channel.sendMessage("Tu peux pas avoir mieux :)").queue();
				return;
			}  
		}
	}

	private String petForecast(Double curFrag, Double nbFragPerDay, Double maxFrag, Double petLevel) {
		Double totalFrag = curFrag;
		if(petLevel == 1){
			totalFrag += 10;
		} else if(petLevel == 2){
			totalFrag += 10;
			totalFrag += 20;
		} else if(petLevel == 3){
			totalFrag += 10;
			totalFrag += 20;
			totalFrag += 50;
		} else if(petLevel == 4){
			totalFrag += 10;
			totalFrag += 20;
			totalFrag += 50;
			totalFrag += 100;
		}
		
		Double missingFrag = maxFrag - totalFrag;
		Integer nbDay = ((Double)Math.ceil(missingFrag/nbFragPerDay)).intValue();
		Calendar forecast = Calendar.getInstance();
		forecast.add(Calendar.DAY_OF_YEAR, nbDay);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER);
		return sdf.format(forecast.getTime());
	}
}