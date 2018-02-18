package api.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebook")
public class FacebookController {
    @RequestMapping(method= RequestMethod.GET)
    String index(){
        return "facebook";
    }
}
