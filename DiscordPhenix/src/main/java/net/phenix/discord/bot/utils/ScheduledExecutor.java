package net.phenix.discord.bot.utils;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;

public class ScheduledExecutor extends AbstractScheduledService {

	@Override
	protected void runOneIteration() throws Exception {
		System.out.println("Executing....");
	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(0, 3, TimeUnit.SECONDS);
	}

	@Override
	protected void startUp() {
		System.out.println("StartUp Activity....");
	}

	@Override
	protected void shutDown() {
		System.out.println("Shutdown Activity...");
	}

}
