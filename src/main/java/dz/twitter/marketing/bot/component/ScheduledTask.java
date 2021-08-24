package dz.twitter.marketing.bot.component;

import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dz.twitter.marketing.bot.service.TwitterBotService;

@Component
public class ScheduledTask {

	private static final Logger LOGGER = LogManager.getLogger(ScheduledTask.class);
	
	@Autowired
	private TwitterBotService botService;
	
	@Scheduled(fixedRate = 1800000L)
	public void runScheduledTask() throws URISyntaxException {
		LOGGER.info("Executing Sceduler ... ");
		this.botService.postTweet();
	}
}
