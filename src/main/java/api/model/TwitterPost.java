package api.model;

public class TwitterPost {
    public int twitterPostId;

    public int customerId;

    public String twitterPostUrl;

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTwitterPostId() { return twitterPostId; }

    public void setTwitterPostId(int twitterPostId) { this.twitterPostId = twitterPostId; }

    public String getTwitterPostUrl() { return twitterPostUrl; }

    public void setTwitterPostUrl(String twitterPostUrl) { this.twitterPostUrl = twitterPostUrl; }
}
