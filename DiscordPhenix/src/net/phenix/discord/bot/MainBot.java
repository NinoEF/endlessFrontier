package net.phenix.discord.bot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class MainBot {

	private JDA jda;


	MainBot(String token) {
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
		} catch (LoginException | InterruptedException | IllegalArgumentException | RateLimitedException e) {
			e.printStackTrace();
			System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
			return;
		}
		System.out.println("Connecte avec: " + jda.getSelfUser());
		int i;
		System.out.println("Le bot est autorisé sur " + (i = jda.getGuilds().size()) + " serveur" + (i > 1 ? "s" : ""));
		jda.addEventListener(new CmdListener());
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Veuillez indiquer le token du bot");
		}

		if(args[0] == null || ((String)args[0]).isEmpty()){
			System.out.println("Veuillez renseigné le token.");
		}
		new MainBot(args[0]);
	}
}