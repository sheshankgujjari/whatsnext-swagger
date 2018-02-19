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
    public static final String TWITTER_BASE_URI = "/twitter";

    private static final long WORLDWIDE_WOE = 1L;


    @Autowired
    public Twitter twitter;

    @Autowired
    private ConnectionRepository connectionRepository;

    public String consumerKey = "cGHb4UlsqYHHvqrjFReyVud4W";
    public String consumerSecret = "KfuQNcFtq894tW0NJX6p1DKjYMxeaedhcB4CEAq4LwD8iaugM3";
    public String accessToken = "965455322746892288-kxvpWjr7aSrVQyk9av2OFmYwcet8mBz";
    public String accessTokenSecret = "9VprzuKa4kVgZshjlZHgJy9UBy7N9F5sDLqVbdWbhcjxC";


    @RequestMapping(value="/tweet", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Tweet postTweet(@RequestHeader  String message) {
        TwitterHelper helper = new TwitterHelper();
        twitter = helper.twitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        return twitter.timelineOperations().updateStatus(message);
    }

    @RequestMapping(value = "/uploadTimeLine", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Status uploadTimeLine(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            File newFile = new File(file.getOriginalFilename());
            newFile.createNewFile();
            DataInputStream is =new DataInputStream(new FileInputStream(newFile));
            newFile.delete();
            TwitterHelper client = new TwitterHelper();
            return client.postContent(consumerKey, consumerSecret, accessToken, accessTokenSecret, file, is);

//            File newFile = new File(file.getOriginalFilename());
//            newFile.createNewFile();
//            FileOutputStream fos = new FileOutputStream(newFile);
//            fos.write(file.getBytes());
//            fos.close();
//            DataInputStream is =new DataInputStream(new FileInputStream(newFile));
//            Resource media = getUploadResource(file.getOriginalFilename(), fos.toString());
//            //getUploadResource("photo.jpg", "PHOTO DATA");
//            TwitterHelper helper = new TwitterHelper();
//            twitter = helper.twitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
//            MultiValueMap<String, Object> uploadParams = new LinkedMultiValueMap<>();
//            uploadParams.set("media_data", imageData.getImageBase64()); //its encoded
//            MediaUploadResponse response = executePostCommandWithReturn(() -> twitter.restOperations().postForObject("https://upload.twitter.com/1.1/media/upload.json", uploadParams, MediaUploadResponse.class));
//            Tweet tweet = twitter.timelineOperations().updateStatus(new TweetData(file.getOriginalFilename()).withMedia(media));
//            return tweet;
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
