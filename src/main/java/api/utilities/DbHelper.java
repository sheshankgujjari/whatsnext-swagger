package api.utilities;

import api.model.Customer;
import api.model.FacebookPost;
import api.model.TwitterPost;
import api.model.YoutubePost;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DbHelper {

    private static final String url = "jdbc:postgresql://whatsnext.cqee8ex7ewfk.us-west-2.rds.amazonaws.com:5432/whatsnext";
    private static final String user = "postgres";
    private static final String password = "postgres";
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean checkUserExist(String userName) {
        String SQL = "SELECT * FROM customer WHERE user_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if(rs != null) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static Customer getCustomerDetails(String userName) {
        String SQL = "SELECT * FROM customer WHERE user_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            Customer customer = new Customer();
            while (rs.next()) {
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setMiddleName(rs.getString("middle_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setAddress(rs.getString("address"));
                customer.setFbAppId(rs.getString("fb_app_id"));
                customer.setFbAppSecretToken(rs.getString("fb_app_secret_token"));
                customer.setFbAccessToken(rs.getString("fb_access_token"));
                customer.setGoogleClientSecret(rs.getString("google_client_secret"));
                customer.setTwitterConsumerKey(rs.getString("twtr_consumer_key"));
                customer.setTwitterConsumerSecret(rs.getString("twtr_consumer_secret"));
                customer.setTwitterAccessToken(rs.getString("twtr_access_token"));
                customer.setTwitterAccessTokenSecret(rs.getString("twtr_access_token_secret"));
                customer.setUserName(rs.getString("user_name"));
                customer.setPassword(rs.getString("pwd"));
                customer.setAppUniqueId(rs.getString("application_unique_id"));
            }
            return customer;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static List<FacebookPost> getFacebookPosts(String userName) {
        String SQL = "select fb_post_id, c.customer_id, fb_post_url from facebook_post fp inner join customer c on fp.customer_id = c.customer_id where c.user_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            List<FacebookPost> listOfPosts = new ArrayList<FacebookPost>();
            while (rs.next()) {
                FacebookPost fbPost = new FacebookPost();
                fbPost.setCustomerId(rs.getInt("customer_id"));
                fbPost.setFacebookPostId(rs.getInt("fb_post_id"));
                fbPost.setFacebookPostUrl(rs.getString("fb_post_url"));
                listOfPosts.add(fbPost);
            }
            return listOfPosts;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static List<TwitterPost> getTwitterPosts(String userName) {
        String SQL = "select twtr_post_id, c.customer_id, twtr_post_url from twtr_post tp inner join customer c on tp.customer_id = c.customer_id where c.user_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            List<TwitterPost> listOfPosts = new ArrayList<TwitterPost>();
            while (rs.next()) {
                TwitterPost twtrPost = new TwitterPost();
                twtrPost.setCustomerId(rs.getInt("customer_id"));
                twtrPost.setTwitterPostId(rs.getInt("twtr_post_id"));
                twtrPost.setTwitterPostUrl(rs.getString("twtr_post_url"));
                listOfPosts.add(twtrPost);
            }
            return listOfPosts;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static List<YoutubePost> getYoutubePosts(String userName) {
        String SQL = "select ytb_post_id, c.customer_id, ytb_post_url from youtube_post yp inner join customer c on yp.customer_id = c.customer_id where c.user_name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            List<YoutubePost> listOfPosts = new ArrayList<YoutubePost>();
            while (rs.next()) {
                YoutubePost youtubePost = new YoutubePost();
                youtubePost.setCustomerId(rs.getInt("customer_id"));
                youtubePost.setYoutubePostId(rs.getInt("ytb_post_id"));
                youtubePost.setYoutubePostUrl(rs.getString("ytb_post_url"));
                listOfPosts.add(youtubePost);
            }
            return listOfPosts;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static void insertYoutubePost(int customerId, String youtubePostUrl) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;
        String sql = "INSERT INTO youtube_post (customer_id, ytb_post_url, created_date) VALUES (customerId, youtubePostUrl,to_timestamp(getCurrentTimeStamp(), 'yyyy/mm/dd hh24:mi:ss'))";
        try {
            statement = dbConnection.createStatement();
            System.out.println(sql);
            statement.executeUpdate(sql);
            System.out.println("Record is inserted into youtube table!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    public static void insertFacebookPost(int customerId, String facebookPostUrl) throws SQLException {

        Connection dbConnection = null;
        Statement statement = null;
        String sql = "INSERT INTO facebook_post (customer_id, fb_post_url, created_date) VALUES (customerId, facebookPostUrl,to_timestamp(getCurrentTimeStamp(), 'yyyy/mm/dd hh24:mi:ss'))";
        try {
            statement = dbConnection.createStatement();
            System.out.println(sql);
            statement.executeUpdate(sql);
            System.out.println("Record is inserted into facebook table!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    public static void insertTwitterPost(int customerId, String twitterPostUrl) throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;
        String sql = "INSERT INTO facebook_post (customerId, fb_post_url, created_date) VALUES (customerId, twitterPostUrl,to_timestamp(getCurrentTimeStamp(), 'yyyy/mm/dd hh24:mi:ss'))";
        try {
            statement = dbConnection.createStatement();
            System.out.println(sql);
            statement.executeUpdate(sql);
            System.out.println("Record is inserted into twitter table!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            if (statement != null) {
                statement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    public static String getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return dateFormat.format(today.getTime());
    }

}
