package net.phenix.discord.bot.utils;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;

import net.phenix.discord.bot.MainBot;

public class TestJSON {

	public static void main(String[] args) {
		
		try {
			MainBot.sendMessage("https://discordapp.com/api/webhooks/425017085991714817/_ZgQilOwk8vOmSiE3pQdhe4mp3UWvNjd8GTlD6Kk6EVPmqc9d7OQWD9W4TUAhM2-ral3", "Test", "Hey @Serenity!\n"+ 
"Today is day 10.\n"+
"We open 2 raid 1-8 and 2 raid 2-7.\n"+
"__Rules for today: __\n"+
"```Raid 1: First Belial killed 12 hours after reset. Second Belial killed after reset. \n"+
"\n"+
"Raid 2 : First Bahamut to be killed 12 hours after reset. Second one killed after next reset. \n"+
"\n"+
"Raid 4: Kill Mamoon when raid timer is less than 1 hour.```\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
