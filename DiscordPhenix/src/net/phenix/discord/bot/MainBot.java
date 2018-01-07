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

		//Prod : Mzk5NTk5MDYyODA4MjY0NzA0.DTPcqw.YrtSHrU7mITE2Z95dJ2E5tbpyd4
		//Dev : Mzk5NjcxNjE4MTE0Mjg5Njc4.DTQhfw.3M1c4CE6Tniz7w0rDQ9QWbRv48Y
		if(args[0] == null || ((String)args[0]).isEmpty()){
			System.out.println("Veuillez renseigné le token.");
		}
		new MainBot(args[0]);
	}
}