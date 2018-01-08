package net.phenix.discord.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.requests.SessionReconnectQueue;
import net.phenix.discord.bot.listener.CmdListener;

public class MainBot {

	private static JDABuilder shardBuilder;

	public static String DATE_FORMATTER = "dd/MM/yyyy";

	MainBot(String token) {
		try {
			shardBuilder = new JDABuilder(AccountType.BOT).setToken(token).setReconnectQueue(new SessionReconnectQueue());

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
			return;
		}
		System.out.println("Connecté.");
		shardBuilder.addEventListener(new CmdListener());
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Veuillez indiquer le token du bot");
		}

		if (args[0] == null || ((String) args[0]).isEmpty()) {
			System.out.println("Veuillez renseigné le token.");
		}
		new MainBot(args[0]);
		for (int i = 0; i < 10; i++) {
			// using buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION)
			// makes sure we start to delay the next shard once the current one
			// actually
			// sent the login information, otherwise we might hit nasty race
			// conditions
			shardBuilder.useSharding(i, 10).buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION);
			Thread.sleep(5000); // sleep 5 seconds between each login
		}
	}
}