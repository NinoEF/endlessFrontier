package net.phenix.discord.bot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import net.phenix.discord.bot.data.xml.Config;
import net.phenix.discord.bot.data.xml.Config.Webhooks.Webhook;
import net.phenix.discord.bot.listener.CmdListener;
import net.phenix.discord.bot.manager.BattleManager;
import net.phenix.discord.bot.manager.BuildManager;
import net.phenix.discord.bot.manager.BundleManager;
import net.phenix.discord.bot.manager.PetManager;
import net.phenix.discord.bot.manager.RaidManager;
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
		     private SessionConnectNode node;

			@Override
		     public void appendSession(SessionConnectNode node) {
		         System.out.println("[SessionController] Adding SessionConnectNode to Queue!");
		         this.node = node;
		         super.appendSession(node);
		     }

			public SessionConnectNode getNode() {
				return node;
			}

			public void setNode(SessionConnectNode node) {
				this.node = node;
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
		
		RaidManager raidManager = RaidManager.getInstance();
		try {
			unitManager.init(bundleManager);
			sheetManager.init();
			treasureManager.init();
			petManager.init(bundleManager);
			buildManager.init(bundleManager, unitManager, petManager, treasureManager);
			raidManager.init(bundleManager);
			
			listener.setUnitManager(unitManager);
			listener.setSheetManager(sheetManager);
			listener.setBattleManager(battleManager);
			listener.setPetManager(petManager);
			listener.setTreasureManager(treasureManager);
			listener.setBundleManager(bundleManager);
			listener.setBuildManager(buildManager);
			listener.setRaidManager(raidManager);
			
			builder.addEventListener(listener);
			for (int i = 0; i < 5; i++) {
				JDAImpl jda = (JDAImpl) builder.useSharding(i, 5).buildAsync();
			}
			
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException | JAXBException | LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}	
		
		
		
		File directory = new File("/home/pi/discord/config/");
		for (File file : directory.listFiles()) {
			JAXBContext jaxbContext = JAXBContext.newInstance(Config.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Config config = (Config) jaxbUnmarshaller.unmarshal(file);

			if(config.getWebhooks().getWebhooks() != null){
				for (Webhook webhook : config.getWebhooks().getWebhooks()) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date date = sdf.parse(webhook.getTime());
					Calendar time = Calendar.getInstance();
					time.setTime(date);
					
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
					cal.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
					cal.set(Calendar.SECOND, 0);
					
					Calendar now = Calendar.getInstance();
					if(now.after(cal)){
						cal.add(webhook.getEvery().getUnit(), webhook.getEvery().getValue());
					}
					
					MyTimeTask task = new MyTimeTask();
					task.setWebhook(webhook);
					timer.schedule(task, cal.getTime());
				}
			}
		}

	}

	private static void sendMessage(String url, String username, String content) throws IOException {
		
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("content", content); 
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost(url);
		    StringEntity params = new StringEntity(json.toString());
		    params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    request.setEntity(params);
		    CloseableHttpResponse response =  httpClient.execute(request);
		// handle response here...
		} catch (Exception ex) {
			ex.printStackTrace();
		    // handle exception here
		} finally {
		    httpClient.close();
		}
	}


	static Timer timer = new Timer();

	private static class MyTimeTask extends TimerTask {

		private Webhook webhook;

		public void run() {
			try {
				sendMessage(webhook.getUrl(), webhook.getBotname(), webhook.getContent());
			} catch (IOException e) {
			}
			
			Calendar cal = Calendar.getInstance();
			// Date and time at which you want to execute
			cal.add(webhook.getEvery().getUnit(), webhook.getEvery().getValue());
			MyTimeTask task = new MyTimeTask();
			task.setWebhook(this.webhook);
			timer.schedule(task, cal.getTime());
		}

		public void setWebhook(Webhook webhook) {
			this.webhook = webhook;
		}
	}

}