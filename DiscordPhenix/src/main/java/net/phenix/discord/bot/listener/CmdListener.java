package net.phenix.discord.bot.listener;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.NumberManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.SheetManager;
import net.phenix.discord.bot.manager.UnitManager;

public class CmdListener extends ListenerAdapter {

	Logger log = Logger.getLogger(getClass());
	
	private UnitManager unitManager;
	
	private SheetManager sheetManager;
	
	private BattleManager battleManager;
	
	private PetManager petManager;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;

		MessageChannel channel = event.getChannel();

		// We don't want to respond to other bot accounts, including ourself
		Message message = event.getMessage();
		String content = message.getContentRaw();
		if (content.startsWith("!")) {
			log.info(event.getAuthor().getName() + " : " + content);
			// getRawContent() is an atomic getter
			// getContent() is a lazy getter which modifies the content for e.g.
			// console view (strip discord formatting)
			if (content.equals("!help")) {
				channel.sendMessage("!ping - ...\n" + "!petfc <nb fragment> <lvl pet> <nb fragment/jour> - Vous donne le planning d'obtention.\n"
						+ "!revive <etage> <bonus> - Vous donne le nombre de médaille pour un étage donné et un bonus donné\n"
						+ "!revive <etage> <bonus> <temps> - Vous donne le repos pour un étage donné, un bonus donné et le temps de run\n"
						+ "!unit list <Nombre d'étoile> - Vous donne la liste des unités avec leur identifiant\n"
						+ "!unit goldlevel <goldLevel> <id de l'unite> <bonus de réduction> - Vous donne le total d'or pour maxer une unité\n"
						+ "\t exemple : !unit goldlevel 3122 159 423d\n"
						+ "\t résultat : 2,48at\n").queue();

			} else if (content.equals("!ping")) {
				// Important to call .queue() on the RestAction returned by
				// sendMessage(...)
				channel.sendMessage("Pong!").queue();

			} else if (content.startsWith("!petfc")) {
				petManager.petForecast(channel, content);
				
			} else if (content.startsWith("!revive")) {
				try {
					String[] args = content.split(" ");
					Integer floor = Integer.parseInt(args[1]);
					if(floor > 30000){
						channel.sendMessage("L'étage maximun est 30000").queue();
						return;
					}
					
					BigDecimal bonus = new BigDecimal(Integer.parseInt(args[2]));

					String response = "";

					if (args.length == 4) {
						Integer time = Integer.parseInt(args[3]);
						BigDecimal total = battleManager.getRevivalSpiritRest(floor, bonus, time);

						response = NumberManager.getEFFormat(total) + "/min";
					} else {
						BigDecimal total = battleManager.getRevivalMedalsQuantity(floor, bonus);

						response = NumberManager.getEFFormat(total);
					}

					channel.sendMessage(response).queue();

				} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (content.startsWith("!excel")) {
				String[] args = content.split(" ");

				try {
					channel.sendMessage(sheetManager.getUserProgression(args[1])).queue();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (content.startsWith("!unit")) {
				String[] args = content.split(" ");
				
				try {
					if(args[1].toLowerCase().equals("list")){
						channel.sendMessage(unitManager.getUnitsList(args[2])).queue();
					} else if(args[1].toLowerCase().equals("goldlevel")){
						
						if(args.length != 5){
							channel.sendMessage("Nombre de paramétre incorrect").queue();
							return;
						}
						Integer goldLevel = Integer.parseInt(args[2]);
						if(goldLevel > 3400){
							channel.sendMessage("Le gold level maximun est de 3400").queue();;
							return;
						}
						
						Integer unitID = Integer.parseInt(args[3]);
						String bonusReduction = args[4];
						
						channel.sendMessage(unitManager.getTotalUpgradeGoldLevel(goldLevel, unitID, bonusReduction)).queue();
					}
				} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				Guild guild = event.getGuild();
//				
//				String[] args = content.split(" ");
//				String id = args[1].substring(2, args[1].length() - 1);
//				Member member = guild.getMemberById(id);
//				User user = member.getUser();
//				if (user != null) {
//					user.openPrivateChannel().queue((pvChannel) -> {
//						pvChannel.sendMessage(embed).queue();
//					});
//				}
			}
		}
	}


	public UnitManager getUnitManager() {
		return unitManager;
	}


	public void setUnitManager(UnitManager unitManager) {
		this.unitManager = unitManager;
	}


	public SheetManager getSheetManager() {
		return sheetManager;
	}


	public void setSheetManager(SheetManager sheetManager) {
		this.sheetManager = sheetManager;
	}


	public BattleManager getBattleManager() {
		return battleManager;
	}


	public void setBattleManager(BattleManager battleManager) {
		this.battleManager = battleManager;
	}


	public PetManager getPetManager() {
		return petManager;
	}


	public void setPetManager(PetManager petManager) {
		this.petManager = petManager;
	}

}