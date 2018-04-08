package api.controllers;

import api.model.Count;
import api.model.Customer;
import api.model.FacebookPost;
import api.model.YoutubePost;
import api.utilities.DbHelper;
import api.utilities.FacebookHelper;
import com.restfb.Connection;
import com.restfb.types.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/facebook")
public class FacebookController {

    @RequestMapping(value = "/publishMessage", method = RequestMethod.POST ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GraphResponse publishMessage(@RequestHeader String userName, @RequestBody String text) throws Exception {
        System.out.println("Get user data");
        System.out.println("User Name :" + userName);
        GraphResponse graphResponse = new GraphResponse();
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            graphResponse = FacebookHelper.publishMessage(customer.getFbAccessToken(), text);
            //TODO
            String facebookUrl = "";
            DbHelper.insertFacebookPost(customer.getCustomerId(), facebookUrl);
        }
        return graphResponse;
    }

    @RequestMapping(value = "/publishVideo", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GraphResponse publishVideo(@RequestHeader String userName,
                                      @RequestHeader String title,
                                      @RequestHeader String description,
                                      @RequestParam("file") MultipartFile file) throws Exception {
        System.out.println("Get user data");
        System.out.println("UserName  :" + userName);
        FacebookHelper helper = new FacebookHelper();
        GraphResponse graphResponse = new GraphResponse();
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            graphResponse = helper.publishVideo(customer.getFbAccessToken(), file, title, description);
            String facebookUrl = "";
            DbHelper.insertFacebookPost(customer.getCustomerId(), facebookUrl);
            return graphResponse;
        }
        return graphResponse;
    }

    @RequestMapping(value = "/getPosts", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FacebookPost> getPosts(@RequestHeader String userName)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        List<FacebookPost> listOfPost = new ArrayList<FacebookPost>();
        if(DbHelper.checkUserExist(userName)) {
             listOfPost = DbHelper.getFacebookPosts(userName);
        }
        return listOfPost;
    }

    @RequestMapping(value = "/getComments/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Connection<Comment> getComments(@RequestHeader String userName, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            Connection<Comment> comments = FacebookHelper.getCommentsByPostId(customer.getFbAccessToken(), postId);
            return comments;
        }
        return null;
    }

    @RequestMapping(value = "/getCount/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Count getCount(@RequestHeader String userName, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            Count count = FacebookHelper.getCountOfCommentsLikesAndShares(userName, postId);
            return count;
        }
        return null;
    }
}
