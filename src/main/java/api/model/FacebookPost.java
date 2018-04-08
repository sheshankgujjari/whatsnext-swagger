package api.model;

public class FacebookPost {

    public int facebookPostId;

    public int customerId;

    public String facebookPostUrl;

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getFacebookPostId() { return facebookPostId; }

    public void setFacebookPostId(int facebookPostId) { this.facebookPostId = facebookPostId; }

    public String getFacebookPostUrl() { return facebookPostUrl; }

    public void setFacebookPostUrl(String facebookPostUrl) { this.facebookPostUrl = facebookPostUrl; }
}
