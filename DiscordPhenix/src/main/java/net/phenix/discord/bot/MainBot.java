package net.phenix.discord.bot;

import java.io.IOException;
import java.math.BigDecimal;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import net.phenix.discord.bot.listener.CmdListener;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.BuildManager;
import net.phenix.discord.bot.manager.BundleManager;
import net.phenix.discord.bot.manager.NumberManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.SheetManager;
import net.phenix.discord.bot.manager.TreasureManager;
import net.phenix.discord.bot.manager.UnitManager;

public class MainBot {

	static Logger log = Logger.getLogger(MainBot.class);
	
	public static String DATE_FORMATTER = "dd/MM/yyyy";


	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Veuillez indiquer le token du bot");
		}

		if (args[0] == null || ((String) args[0]).isEmpty()) {
			System.out.println("Veuillez renseigné le token.");
		}
		JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(args[0]);
		 builder.setSessionController(new SessionControllerAdapter() {
		     @Override
		     public void appendSession(SessionConnectNode node) {
		         System.out.println("[SessionController] Adding SessionConnectNode to Queue!");
		         super.appendSession(node);
		     }
		 });
		 
		
		log.info("Connecté.");
		
		BundleManager bundleManager = BundleManager.getInstance();
		CmdListener listener = new CmdListener();
		UnitManager unitManager = UnitManager.getInstance();
		SheetManager sheetManager = SheetManager.getInstance();
		PetManager petManager = PetManager.getInstance();
		BattleManager battleManager = BattleManager.getInstance();
		TreasureManager treasureManager = TreasureManager.getInstance();
		BuildManager buildManager = BuildManager.getInstance();
		try {
			bundleManager.init();
			unitManager.init(bundleManager);
			sheetManager.init();
			treasureManager.init();
			petManager.init(bundleManager);
			buildManager.init(bundleManager, unitManager, petManager, treasureManager);
			
			listener.setUnitManager(unitManager);
			listener.setSheetManager(sheetManager);
			listener.setBattleManager(battleManager);
			listener.setPetManager(petManager);
			listener.setTreasureManager(treasureManager);
			listener.setBundleManager(bundleManager);
			listener.setBuildManager(buildManager);
			
			 builder.addEventListener(listener);
			 for (int i = 0; i < 5; i++) {
			     builder.useSharding(i, 5).buildAsync();
			 }
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException | JAXBException | LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
}