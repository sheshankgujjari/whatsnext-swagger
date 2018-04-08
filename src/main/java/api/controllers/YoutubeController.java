package api.controllers;

import api.model.Customer;
import api.model.YoutubePost;
import api.utilities.DbHelper;
import api.utilities.YoutubeHelper;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Video;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String uploadVideo(@RequestHeader String userName,
                              @RequestHeader String videoFileName,
                             @RequestParam("file") MultipartFile file)
            throws Exception {
        if(DbHelper.checkUserExist(userName)) {
            Customer customer = DbHelper.getCustomerDetails(userName);
            Video video = YoutubeHelper.uploadVideo(videoFileName, file, customer.googleClientSecret);
            String youtubeUrl = "https://www.youtube.com/watch?v=" + video.getId();
            DbHelper.insertYoutubePost(customer.getCustomerId(), youtubeUrl);
            return youtubeUrl;
        } else {
            return "Customer doesn't exist";
        }
    }

    @RequestMapping(value = "/getPosts", method = RequestMethod.GET ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<YoutubePost> getPosts(@RequestHeader String userName)
    {
        System.out.println("Get user data");
        System.out.println("UserName :" + userName);
        List<YoutubePost> listOfPost = new ArrayList<YoutubePost>();
        if(DbHelper.checkUserExist(userName)) {
            listOfPost = DbHelper.getYoutubePosts(userName);
        }
        return listOfPost;
    }

    //TODO Get all comments and likes
}
