package api.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TwitterHelper {
	@Autowired
	public Environment env;

	public TwitterTemplate twitterTemplate() {
		String consumerKey = env.getProperty("spring.social.twitter.app-id");
		System.out.println(consumerKey);
		String consumerSecret = env.getProperty("spring.social.twitter.app-secret");
		String accessToken = env.getProperty("spring.social.twitter.access-token");
		String accessTokenSecret = env.getProperty("spring.social.twitter.access-token-secret");
		return new TwitterTemplate(consumerKey,
				consumerSecret, accessToken, accessTokenSecret);
	}

	public TwitterTemplate twitterTemplate(String consumerKey, String consumerSecret,
										   String accessToken, String accessTokenSecret) {
		return new TwitterTemplate(consumerKey,
				consumerSecret, accessToken, accessTokenSecret);
	}

	public static int getFollowersCount(Twitter twitter, String userName)
	{
		return twitter.friendOperations().getFollowers(userName).size();
	}

	 public static List<Tweet> getHashTagTweets(Twitter twitter, String hashTag)
	 {
		return twitter.searchOperations().search(hashTag, 20).getTweets();
	 }
	    
	 public static List<Tweet> getMyTimeLine(Twitter twitter, String name)
	    {
	        return twitter.timelineOperations().getUserTimeline(name);

	    }
	 
	    public static TwitterProfile getUserProfile(Twitter twitter)
	    {
	        TwitterProfile profile = twitter.userOperations().getUserProfile();
	        return profile;

	    }

	    public static String getScreenName(Twitter twitter)
	    {
	        String profileId = twitter.userOperations().getScreenName();
	        return profileId;
	    }

	    public static void updateStatus(Twitter twitter, String status)
	    {
	        twitter.timelineOperations().updateStatus("Spring Social is awesome!");
	    }

	    public static List<Tweet> getHomeTimeLine(Twitter twitter)
	    {
	        List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
	        return tweets;
	    }

	    public static CursoredList<TwitterProfile> getFriends(Twitter twitter)
	    {
	        CursoredList<TwitterProfile> friends = twitter.friendOperations().getFriends();
	        return friends;
	    }

	public Status postContent(String consumerKey, String consumerSecret,
							  String accessToken, String accessTokenSecret,
							  MultipartFile file,
							  DataInputStream inputStream) throws IOException, TwitterException {

		TwitterFactory twitterFactory = new TwitterFactory();

		twitter4j.Twitter twitter = twitterFactory.getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

//        StatusUpdate statusUpdate = new StatusUpdate(
//                //your tweet or status message
//                "H-1B Transfer Jobs | Java Developer | Harrison, NY | 2 Years" +
//                        " - http://h1b-work-visa-usa.blogspot.com/2013/07/h-1b-transfer-jobs-java-developer_19.html");
		//attach any media, if you want to
//        statusUpdate.setMedia(
//                //title of media
//                "http://h1b-work-visa-usa.blogspot.com"
//                , new URL("http://lh6.ggpht.com/-NiYLR6SkOmc/Uen_M8CpB7I/AAAAAAAAEQ8/tO7fufmK0Zg/h-1b%252520transfer%252520jobs%25255B4%25255D.png?imgmax=800").openStream());

		//tweet or update status

		StatusUpdate statusUpdate = new StatusUpdate(file.getOriginalFilename());
		statusUpdate.setMedia(file.getOriginalFilename(), inputStream);
		Status status = twitter.updateStatus(statusUpdate);

		//response from twitter server
		System.out.println("status.toString() = " + status.toString());
		System.out.println("status.getInReplyToScreenName() = " + status.getInReplyToScreenName());
		System.out.println("status.getSource() = " + status.getSource());
		System.out.println("status.getText() = " + status.getText());
		System.out.println("status.getContributors() = " + Arrays.toString(status.getContributors()));
		System.out.println("status.getCreatedAt() = " + status.getCreatedAt());
		System.out.println("status.getCurrentUserRetweetId() = " + status.getCurrentUserRetweetId());
		System.out.println("status.getGeoLocation() = " + status.getGeoLocation());
		System.out.println("status.getId() = " + status.getId());
		System.out.println("status.getInReplyToStatusId() = " + status.getInReplyToStatusId());
		System.out.println("status.getInReplyToUserId() = " + status.getInReplyToUserId());
		System.out.println("status.getPlace() = " + status.getPlace());
		System.out.println("status.getRetweetCount() = " + status.getRetweetCount());
		System.out.println("status.getRetweetedStatus() = " + status.getRetweetedStatus());
		System.out.println("status.getUser() = " + status.getUser());
		System.out.println("status.getAccessLevel() = " + status.getAccessLevel());
		System.out.println("status.getHashtagEntities() = " + Arrays.toString(status.getHashtagEntities()));
		System.out.println("status.getMediaEntities() = " + Arrays.toString(status.getMediaEntities()));
		if (status.getRateLimitStatus() != null) {
			System.out.println("status.getRateLimitStatus().getLimit() = " + status.getRateLimitStatus().getLimit());
			System.out.println("status.getRateLimitStatus().getRemaining() = " +
					status.getRateLimitStatus().getRemaining());
			System.out.println("status.getRateLimitStatus().getResetTimeInSeconds() = " +
					status.getRateLimitStatus().getResetTimeInSeconds());
			System.out.println("status.getRateLimitStatus().getSecondsUntilReset() = " +
					status.getRateLimitStatus().getSecondsUntilReset());
		}
		System.out.println("status.getURLEntities() = " + Arrays.toString(status.getURLEntities()));
		System.out.println("status.getUserMentionEntities() = " + Arrays.toString(status.getUserMentionEntities()));
		return status;
	}
}
