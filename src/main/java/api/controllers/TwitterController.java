package api.controllers;

import api.utilities.TwitterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/twitter")
public class TwitterController {
    public static final String TWITTER_BASE_URI = "/twitter";

    private static final long WORLDWIDE_WOE = 1L;


    @Autowired
    public Twitter twitter;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Inject
    public TwitterController(Twitter twitter, ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
    }


    @RequestMapping(method= RequestMethod.GET)
    String index(){
        return "twitter";
    }


    @RequestMapping(value = "/getFollowersCount/{username}", method= RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public int getFollowersCount(@PathVariable(name = "userName") String userName) {
        return TwitterHelper.getFollowersCount(twitter, userName);
    }

    @RequestMapping(value = "/gethashtags/{hashTag}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> getTweets(@PathVariable final String hashTag)
    {
        return TwitterHelper.getHashTagTweets(twitter, hashTag);
    }

    @RequestMapping(value = "/getmytimeline/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> getMyTimeLine(@PathVariable final String name)
    {
        return TwitterHelper.getMyTimeLine(twitter, name);
    }

    @RequestMapping(value = "/getuserprofile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TwitterProfile getUserProfile()
    {
        return TwitterHelper.getUserProfile(twitter);
    }

    @RequestMapping(value = "/getscreenname",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getScreenName()
    {
        return TwitterHelper.getScreenName(twitter);
    }

    @RequestMapping(value = "/getfriends", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CursoredList<TwitterProfile> getFriends()
    {
        CursoredList<TwitterProfile> friends = TwitterHelper.getFriends(twitter);
        return friends;
    }

    @RequestMapping(value = "/gethometimeline", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> getHomeTimeLine()
    {
        return TwitterHelper.getHomeTimeLine(twitter);
    }

    @RequestMapping(value = "/uploadtimeline", method = RequestMethod.POST ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void uploadTimeLine()
    {
        TwitterHelper.updateStatus(twitter, "Test");
    }

    @RequestMapping(value="/timeline", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> showTimeline() {

        return showTimeline("Home");
    }

    @RequestMapping(value="/timeline/{timelineType}", method=RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> showTimeline(@PathVariable("timelineType") String timelineType) {
        if (timelineType.equals("Home")) {
            return twitter.timelineOperations().getHomeTimeline();
        } else if(timelineType.equals("User")) {
            return twitter.timelineOperations().getUserTimeline();
        } else if(timelineType.equals("Mentions")) {
            return twitter.timelineOperations().getMentions();
        } else if(timelineType.equals("Favorites")) {
            return twitter.timelineOperations().getFavorites();
        }
        return null;
    }


    @RequestMapping(value="/tweet", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Tweet postTweet(String message) {
        return twitter.timelineOperations().updateStatus(message);
    }

    @RequestMapping(value="/search", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Tweet> showTrends(@RequestParam("query") String query) {
        return twitter.searchOperations().search(query).getTweets();
    }

    @RequestMapping(value="/trends", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Trends showTrends() {
        return twitter.searchOperations().getLocalTrends(WORLDWIDE_WOE);
    }

    @RequestMapping(value="/friends", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CursoredList<TwitterProfile> friends() {
        return twitter.friendOperations().getFriends();
    }

    @RequestMapping(value="/followers", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CursoredList<TwitterProfile> followers(Model model) {
        return twitter.friendOperations().getFollowers();
    }

    @RequestMapping("/twitter/revoked")
    public void simulateExpiredToken() {
        throw new ExpiredAuthorizationException("twitter");
    }
}
