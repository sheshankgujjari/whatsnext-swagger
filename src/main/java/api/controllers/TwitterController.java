package api.controllers;

import api.model.Customer;
import api.model.TwitterPost;
import api.utilities.DbHelper;
import api.utilities.TwitterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.social.twitter.api.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/twitter")
public class TwitterController {


    @Autowired
    public Twitter twitter;

    @RequestMapping(value="/tweet", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Tweet postTweet(@RequestHeader  String userName,
                           @RequestHeader String message) throws SQLException {
        TwitterHelper twitterHelper = new TwitterHelper();
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            twitter = twitterHelper.twitterTemplate(customer.getTwitterConsumerKey(), customer.getTwitterConsumerSecret(),
                    customer.getTwitterAccessToken(), customer.getTwitterAccessTokenSecret());
            System.out.println("Customer Id " + customer.getCustomerId());
            String twitterUrl = "";
            DbHelper.insertTwitterPost(customer.getCustomerId(), twitterUrl);
        }
        return twitter.timelineOperations().updateStatus(message);
    }

    @RequestMapping(value = "/uploadTimeLine", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Status uploadTimeLine(@RequestHeader String userName,
                                 @RequestParam("file") MultipartFile file) throws Exception {
        try {
            TwitterHelper twitterHelper = new TwitterHelper();
            if(DbHelper.checkUserExist(userName)) {
                Customer customer = DbHelper.getCustomerDetails(userName);
                Status status = twitterHelper.postContent(customer.getTwitterConsumerKey(), customer.getTwitterConsumerSecret(),
                        customer.getTwitterAccessToken(), customer.getTwitterAccessTokenSecret(), file);
                String twitterUrl= "https://twitter.com/" + status.getUser().getScreenName()  + "/status/" + status.getId();

                System.out.println(twitterUrl);
                DbHelper.insertTwitterPost(customer.getCustomerId(), twitterUrl);
                return status;
            } else {
                return null;
            }
        }catch (Exception ex){
            System.out.println("ERROR Creating File from MultiPartFile: " + ex.getMessage());
            return null;
        }

    }

    @RequestMapping(value = "/getPosts", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<TwitterPost> getPosts(@RequestHeader String userName)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        List<TwitterPost> listOfPost = new ArrayList<TwitterPost>();
        if(DbHelper.checkUserExist(userName)) {
            listOfPost = DbHelper.getTwitterPosts(userName);
        }
        return listOfPost;
    }

    @RequestMapping(value = "/getComments/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void getComments(@RequestHeader String userName, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            //TODO
        } else {
            //TODO
        }
    }

    @RequestMapping(value = "/getCount/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void getCount(@RequestHeader String userName, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            //TODO
        } else {
            //TODO
        }
    }
}
