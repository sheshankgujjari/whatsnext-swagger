package api.controllers;

import api.model.Count;
import api.utilities.FacebookHelper;
import com.restfb.Connection;
import com.restfb.types.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/facebook")
public class FacebookController {

    @RequestMapping(value = "/publishMessage", method = RequestMethod.POST ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GraphResponse publishMessage(@RequestHeader String accessToken, @RequestBody String text)
    {
        System.out.println("Get user data");
        System.out.println("Access Token :" + accessToken);
        GraphResponse graphResponse = FacebookHelper.publishMessage(accessToken, text);
        return graphResponse;
    }

    @RequestMapping(value = "/publishVideo", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GraphResponse publishVideo(@RequestHeader String accessToken,
                                      @RequestHeader String title,
                                      @RequestHeader String description,
                                      @RequestParam("file") MultipartFile file)
            throws Exception {
        System.out.println("Get user data");
        System.out.println("Access Token :" + accessToken);
        FacebookHelper helper = new FacebookHelper();
        GraphResponse response = helper.publishVideo(accessToken, file, title, description);
        return response;
    }


//    @RequestMapping(value = "/publishPhoto", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public GraphResponse publishPhoto(@RequestHeader String accessToken,
//                                      @RequestHeader String photoName,
//                                      @RequestHeader String photoDescription,
//                                      @RequestParam("file") MultipartFile file)
//            throws Exception {
//        DataInputStream is =new DataInputStream(
//                new FileInputStream(new File(file.getOriginalFilename())));
//        System.out.println("Get user data");
//        System.out.println("Access Token :" + accessToken);
//        FacebookHelper helper = new FacebookHelper();
//        return helper.publishPhoto(accessToken, file.getOriginalFilename(), is, photoName, photoDescription);
//    }


    @RequestMapping(value = "/getPosts", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Connection<Post> getPosts(@RequestHeader String accessToken)
    {
        System.out.println("Get user data");
        System.out.println("Access Token :" + accessToken);
        Connection<Post> posts = FacebookHelper.getPosts(accessToken);
        return posts;
    }

    @RequestMapping(value = "/getComments/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Connection<Comment> getComments(@RequestHeader String accessToken, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("Access Token :" + accessToken);
        Connection<Comment> comments = FacebookHelper.getCommentsByPostId(accessToken, postId);
        return comments;
    }

    @RequestMapping(value = "/getCount/{postId}", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Count getCount(@RequestHeader String accessToken, @PathVariable(name = "postId") String postId)
    {
        System.out.println("Get user data");
        System.out.println("Access Token :" + accessToken);
        Count count = FacebookHelper.getCountOfCommentsLikesAndShares(accessToken, postId);
        return count;
    }
//
//    @RequestMapping(value = "/getUser", method = RequestMethod.GET ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public User getUser(@RequestHeader String accessToken)
//    {
//        System.out.println("Get user data");
//        System.out.println("Access Token :" + accessToken);
//        User user = FacebookHelper.getUserInformation(accessToken);
//        return user;
//    }
//
//    @RequestMapping(value = "/getInsights", method = RequestMethod.GET ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public Connection<Insight> getCount(@RequestHeader String accessToken)
//    {
//        System.out.println("Get user data");
//        System.out.println("Access Token :" + accessToken);
//        Connection<Insight> insights = FacebookHelper.getInsights(accessToken);
//        return insights;
//    }
}
