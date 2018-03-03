package api.controllers;

import api.utilities.YoutubeHelper;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Video;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {
//    @RequestMapping(method= RequestMethod.GET)
//    String index(){
//        return "youtube";
//    }

//    @RequestMapping(value = "/getYoutubeChannels", method = RequestMethod.GET ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<Channel> getYoutubeChannels() throws IOException {
//        List<Channel> channelList = YoutubeHelper.getYoutubeChannels();
//        return channelList;
//    }
//
//    @RequestMapping(value = "/getListOfUploadedYouTubeVideos", method = RequestMethod.GET ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<PlaylistItem> getListOfUploadedYouTubeVideos() throws IOException {
//         return YoutubeHelper.getListOfUploadedYouTubeVideos();
//    }

    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST , consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String uploadVideo(@RequestHeader String videoFileName,
                             @RequestParam("file") MultipartFile file)
            throws Exception {
        Video video = YoutubeHelper.uploadVideo(videoFileName, file);
        String youtubeUrl = "https://www.youtube.com/watch?v=" + video.getId();
        return youtubeUrl;
    }
}
