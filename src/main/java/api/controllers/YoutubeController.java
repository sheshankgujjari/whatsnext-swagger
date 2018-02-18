package api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {
    @RequestMapping(method= RequestMethod.GET)
    String index(){
        return "youtube";
    }
}
