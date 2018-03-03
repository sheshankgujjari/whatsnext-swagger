package api.controllers;

import api.utilities.TwitterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/twitter")
public class TwitterController {


    @Autowired
    public Twitter twitter;

    @RequestMapping(value="/tweet", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Tweet postTweet(@RequestHeader  String message,
                           @RequestHeader String consumerKey,
                           @RequestHeader String consumerSecret,
                           @RequestHeader String accessToken,
                           @RequestHeader String accessTokenSecret) {
        TwitterHelper helper = new TwitterHelper();
        twitter = helper.twitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        return twitter.timelineOperations().updateStatus(message);
    }

    @RequestMapping(value = "/uploadTimeLine", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Status uploadTimeLine(@RequestParam("file") MultipartFile file,
                                 @RequestHeader String consumerKey,
                                 @RequestHeader String consumerSecret,
                                 @RequestHeader String accessToken,
                                 @RequestHeader String accessTokenSecret) throws Exception {
        try {
            TwitterHelper client = new TwitterHelper();
            return client.postContent(consumerKey, consumerSecret, accessToken, accessTokenSecret, file);
        }catch (Exception ex){
            System.out.println("ERROR Creating FIle from MultiPartFile: " + ex.getMessage());
            return null;
        }

    }

    private Resource getUploadResource(final String filename, String content) {
        Resource resource = new ByteArrayResource(content.getBytes()) {
            @Override
            public String getFilename() throws IllegalStateException {
                return filename;
            };
        };
        return resource;
    }

//    @RequestMapping(value = "/getFollowersCount/{username}", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public int getFollowersCount(@PathVariable(name = "userName") String userName) {
//        return TwitterHelper.getFollowersCount(twitter, userName);
//    }
//
//    @RequestMapping(value = "/gethashtags/{hashTag}", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> getTweets(@PathVariable final String hashTag)
//    {
//        return TwitterHelper.getHashTagTweets(twitter, hashTag);
//    }
//
//    @RequestMapping(value = "/getmytimeline/{name}", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> getMyTimeLine(@PathVariable final String name)
//    {
//        return TwitterHelper.getMyTimeLine(twitter, name);
//    }
//
//    @RequestMapping(value = "/getuserprofile", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public TwitterProfile getUserProfile()
//    {
//        return TwitterHelper.getUserProfile(twitter);
//    }
//
//    @RequestMapping(value = "/getscreenname", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public String getScreenName()
//    {
//        return TwitterHelper.getScreenName(twitter);
//    }
//
//    @RequestMapping(value = "/getfriends", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public CursoredList<TwitterProfile> getFriends()
//    {
//        CursoredList<TwitterProfile> friends = TwitterHelper.getFriends(twitter);
//        return friends;
//    }
//
//    @RequestMapping(value = "/gethometimeline", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> getHomeTimeLine()
//    {
//        return TwitterHelper.getHomeTimeLine(twitter);
//    }

//    @RequestMapping(value="/timeline", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> showTimeline() {
//
//        return showTimeline("Home");
//    }
//
//    @RequestMapping(value="/timeline/{timelineType}", method=RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> showTimeline(@PathVariable("timelineType") String timelineType) {
//        if (timelineType.equals("Home")) {
//            return twitter.timelineOperations().getHomeTimeline();
//        } else if(timelineType.equals("User")) {
//            return twitter.timelineOperations().getUserTimeline();
//        } else if(timelineType.equals("Mentions")) {
//            return twitter.timelineOperations().getMentions();
//        } else if(timelineType.equals("Favorites")) {
//            return twitter.timelineOperations().getFavorites();
//        }
//        return null;
//    }



//    @RequestMapping(value="/search", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Tweet> showTrends(@RequestParam("query") String query) {
//        return twitter.searchOperations().search(query).getTweets();
//    }
//
//    @RequestMapping(value="/trends", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public Trends showTrends() {
//        return twitter.searchOperations().getLocalTrends(WORLDWIDE_WOE);
//    }
//
//    @RequestMapping(value="/friends", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public CursoredList<TwitterProfile> friends() {
//        return twitter.friendOperations().getFriends();
//    }
//
//    @RequestMapping(value="/followers", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public CursoredList<TwitterProfile> followers(Model model) {
//        return twitter.friendOperations().getFollowers();
//    }
//
//    @RequestMapping(value = "/twitter/revoked", method= RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public void simulateExpiredToken() {
//        throw new ExpiredAuthorizationException("twitter");
//    }
}
