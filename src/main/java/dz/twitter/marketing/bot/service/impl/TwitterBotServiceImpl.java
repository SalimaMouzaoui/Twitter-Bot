package dz.twitter.marketing.bot.service.impl;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import dz.twitter.marketing.bot.component.TwitterOAuth1;
import dz.twitter.marketing.bot.service.TwitterBotService;
import dz.twitter.marketing.bot.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
@Service
public class TwitterBotServiceImpl implements TwitterBotService {

	private static final Logger LOGGER = LogManager.getLogger(TwitterBotServiceImpl.class);
	@Autowired
	private TwitterOAuth1 twitterOAuth1;
	@Override
	public void postTweet() throws URISyntaxException {
		// TODO Auto-generated method stub
		LOGGER.info("Post Tweet");
		String tweetStatus = this.computeTweetContent();
		LOGGER.info("Tweet Status => " + tweetStatus);
		this.computeTweetContent(tweetStatus);
	}
	
	private void computeTweetContent(String tweetStatus) throws URISyntaxException {
		// TODO Auto-generated method stub
		URI resource = new URI(Constants.POST_TWEET_URL);
		
		// create an instance of RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		
		// request body params
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add(Constants.STATUS, tweetStatus);
		
		// create headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set(Constants.AUTHORIZATION, twitterOAuth1.generateOauth1Header(resource, HttpMethod.POST, map));
		
		// build the request 
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String,String>>(map, headers);
		
		// send POST request to Twitter
		ResponseEntity<String> response = restTemplate.postForEntity(resource.toString(),
				entity, 
				String.class);
		
		// check response
		if (response.getStatusCode() == HttpStatus.OK)
			LOGGER.info("Tweet posted successfully !! ");
		else
			LOGGER.info("Error on posting tweet. StatusCode : " + response.getStatusCode());
	} 

	private String computeTweetContent() {
		int tweetSize = 0;
		StringBuilder tweetStatus = new StringBuilder("");
		
		// get a random course link
		int courseListSize = Constants.courseList.size();
		String courseUrl = Constants.courseList.get(this.getRandomNumberUsingNextInt(courseListSize));
		
		// add to tweetSize
		tweetSize = tweetSize + courseUrl.length() + 1;
		
		// get a random message as an additional information
		int courseMessageSize = Constants.courseMessagesList.size();
		String courseMessage = Constants.courseMessagesList.get(this.getRandomNumberUsingNextInt(courseMessageSize));
		
		// add to tweetSize
		tweetSize = tweetSize + courseMessage.length() + 1;
		
		List<String> validHashtagList = new ArrayList<String>();
		
		while (tweetSize < Constants.MAX_TWEET_LENGTH - 10) {
			int hashtagListSize = Constants.commonHashtagsList.size();
			String hashtag = Constants.commonHashtagsList.get(this.getRandomNumberUsingNextInt(hashtagListSize));
			
			if (validHashtagList.contains(hashtag)) {
				continue;
			}
			validHashtagList.add(hashtag);
			tweetSize = tweetSize + hashtag.length() + 1;
			
			tweetStatus.append(hashtag);
			tweetStatus.append("  ");
		}
		tweetStatus.append(" \n \n ");
		tweetStatus.append(courseMessage);
		tweetStatus.append(" \n \n ");
		tweetStatus.append(courseUrl);
		
		return tweetStatus.toString();
	}

	private int getRandomNumberUsingNextInt(int max) {
		// TODO Auto-generated method stub
		Random random = new Random();
		return random.nextInt(max);
	}

}
