package rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.RateLimitExceededException;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Trend;
import org.springframework.social.twitter.api.Trends;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This is the controller class used to handle all the Twitter related REST API's
 * @author Sudhindra
 *
 */
@Controller
@RequestMapping("/")
public class TwitterController {

	private Twitter twitter;

	private ConnectionRepository connectionRepository;

	@Inject
	public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	/**
	 * This method is used to get friends list
	 * @param model - {@link Model}
	 * @return HTML page name
	 */
	@RequestMapping(value = "/getFriends", method = RequestMethod.GET)
	public String findTwitterConnection(Model model) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			model.addAttribute(twitter.userOperations().getUserProfile());
			CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
			model.addAttribute("friends", friends);
			return "friends";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage().toString());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}

	/**
	 * This function used to post max 160 characters tweet
	 * @param model - {@link Model}
	 * @param tweet - Tweet to post(Max 160 characters)
	 * @return HTML page name
	 */
	@RequestMapping(value = "/sendTweet", method = RequestMethod.POST)
	public String sendTweet(Model model, @RequestParam("tweet") String tweet) {
		tweet = tweet.substring(1, tweet.length());
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			model.addAttribute(twitter.userOperations().getUserProfile());
			twitter.timelineOperations().updateStatus(tweet);
			model.addAttribute("tweets", twitter.timelineOperations().getHomeTimeline());
			return "tweets";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}

	/**
	 * This function is used to search for a hashtag
	 * @param model - {@link Model}
	 * @param hashtag - HashTag to search 
	 * @return HTML page name
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchHashTags(Model model, @RequestParam("hashtag") String hashtag) {
		hashtag = hashtag.substring(1, hashtag.length());
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			SearchResults searchResults = twitter.searchOperations().search(hashtag, 20);
			List<Tweet> tweets = searchResults.getTweets();
			model.addAttribute("hashtag", hashtag);
			model.addAttribute("tweets", tweets);
			return "searchhashtag";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}

	/**
	 * This function is used to search for treending tweets
	 * @param model - {@link Model}
	 * @return HTML page name
	 */
	@RequestMapping(value = "/trends", method = RequestMethod.GET)
	public String getLocalTrends(Model model) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			// location = location.substring(1, location.length());
			Trends trends = twitter.searchOperations().getLocalTrends(2379574l);
			List<Trend> trendList = trends.getTrends();
			List<String> hashtags = new ArrayList<String>();
			for (Trend trend : trendList) {
				hashtags.add(trend.getName());
			}
			model.addAttribute(twitter.userOperations().getUserProfile());
			model.addAttribute("trends", hashtags);
			return "trending";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}
	
	/**
	 * This function is used to follow a user
	 * @param model - {@link Model}
	 * @param username - Twitter username
	 * @return HTML page name
	 */
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public String followConnection(Model model, @RequestParam("follow") String username) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			twitter.friendOperations().follow(username);
			model.addAttribute(twitter.userOperations().getUserProfile());
			CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
			model.addAttribute("friends", friends);
			return "friends";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage().toString());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}
	
	/**
	 * This function is used to unfollow a user
	 * @param model - {@link Model}
	 * @param username - Twitter username
	 * @return HTML page name
	 */
	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public String unFollowConnection(Model model, @RequestParam("unfollow") String username) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}

		try {
			twitter.friendOperations().unfollow(username);
			model.addAttribute(twitter.userOperations().getUserProfile());
			CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
			model.addAttribute("friends", friends);
			return "friends";
		} catch (RateLimitExceededException rle) {
			rle.printStackTrace();
			model.addAttribute("error", rle.getMessage().toString());
			return "error";
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("error", ex.getMessage());
			return "error";
		}
	}
	
}
