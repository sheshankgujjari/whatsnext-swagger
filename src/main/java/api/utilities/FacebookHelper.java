package api.utilities;


import api.model.Count;
import com.restfb.*;
import com.restfb.types.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static java.lang.System.out;

public class FacebookHelper {

    public static FacebookClient getFaceBookClient(String accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
        return facebookClient;
    }

    public static User getUserInformation(String accessToken) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        out.println("* Fetching single objects *");

        User user = facebookClient.fetchObject("me", User.class);

        System.out.println("User name: " + user.getName());
        return user;
    }

    public static Connection<Post> getPosts(String accessToken) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class);
        return myFeed;
    }

    public static Connection<Comment> getCommentsByPostId(String accessToken, String postId) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        Connection<Comment> comments
                = facebookClient.fetchConnection(postId + "/comments",
                Comment.class, Parameter.with("limit", 100));

        return comments;
    }

    public static Count getCountOfCommentsLikesAndShares(String accessToken, String postId) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        Post post = facebookClient.fetchObject(postId,
                Post.class,
                Parameter.with("fields", "from,to,likes.limit(0).summary(true),comments.limit(0).summary(true),shares.limit(0).summary(true)"));

        System.out.println("Likes count: " + post.getLikesCount());
        System.out.println("Shares count: " + post.getSharesCount());
        System.out.println("Comments count: " + post.getCommentsCount());
        Count count = new Count();
        count.setCommentsCount(post.getCommentsCount());
        count.setLikesCount(post.getLikesCount());
        count.setSharesCount(post.getSharesCount());
        return count;
    }

    public static Connection<Insight> getInsights(String accessToken) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        Connection<Insight> insights =
                facebookClient.fetchConnection("PAGE_ID/insights",
                        Insight.class, // the insight type
                        Parameter.with("metric", "page_fan_adds_unique,page_fan_adds"));
        return insights;
    }

    public static GraphResponse publishMessage(String accessToken, String text) {
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        GraphResponse publishMessageResponse =
                facebookClient.publish("me/feed", GraphResponse.class,
                        Parameter.with("message", text));

        System.out.println("Published message ID: " + publishMessageResponse.getId());
        return publishMessageResponse;
    }

    public GraphResponse publishPhoto(String accessToken, String imageFile) {
        byte[] imageBytes = fetchBytesFromImage(imageFile);
        FacebookClient facebookClient = getFaceBookClient(accessToken);
        GraphResponse publishPhotoResponse = facebookClient.publish("me/photos", GraphResponse.class,
                BinaryAttachment.with("cat.png", getClass().getResourceAsStream("/cat.png")),
                Parameter.with("message", "Test cat"));

        out.println("Published photo ID: " + publishPhotoResponse.getId());
        return publishPhotoResponse;
    }

    private byte[] fetchBytesFromImage(String imageFile) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte fileContent[] = null;

        try{

            BufferedImage image = ImageIO.read(new File(imageFile));

            ImageIO.write(image, "png", baos);
            baos.flush();
            fileContent = baos.toByteArray();

        }catch (Exception e){
            e.printStackTrace();
        }
        return  fileContent;

    }

}
