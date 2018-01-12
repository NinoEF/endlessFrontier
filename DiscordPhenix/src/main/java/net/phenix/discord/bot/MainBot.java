package net.phenix.discord.bot;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.requests.SessionReconnectQueue;
import net.phenix.discord.bot.listener.CmdListener;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.SheetManager;
import net.phenix.discord.bot.manager.UnitManager;

public class MainBot {

	Logger log = Logger.getLogger(getClass());
	
	private static JDABuilder shardBuilder;

	public static String DATE_FORMATTER = "dd/MM/yyyy";

	public MainBot(String token) {
		try {
			shardBuilder = new JDABuilder(AccountType.BOT).setToken(token).setReconnectQueue(new SessionReconnectQueue());

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
			return;
		}
		
		log.info("Connecté.");
		
		CmdListener listener = new CmdListener();
		UnitManager unitManager = UnitManager.getInstance();
		SheetManager sheetManager = SheetManager.getInstance();
		PetManager petManager = PetManager.getInstance();
		BattleManager battleManager = BattleManager.getInstance();
		try {
			unitManager.init();
			sheetManager.init();
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listener.setUnitManager(unitManager);
		listener.setSheetManager(sheetManager);
		listener.setBattleManager(battleManager);
		listener.setPetManager(petManager);
		shardBuilder.addEventListener(listener);
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Veuillez indiquer le token du bot");
		}

		if (args[0] == null || ((String) args[0]).isEmpty()) {
			System.out.println("Veuillez renseigné le token.");
		}
		new MainBot(args[0]);
		for (int i = 0; i < 2; i++) {
			// using buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION)
			// makes sure we start to delay the next shard once the current one
			// actually
			// sent the login information, otherwise we might hit nasty race
			// conditions
			shardBuilder.useSharding(i, 2).buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION);
			Thread.sleep(5000); // sleep 5 seconds between each login
		}
	}
}