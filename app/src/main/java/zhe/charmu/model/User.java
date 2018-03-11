package zhe.charmu.model;

import java.util.Set;

public class User {
    private String username;
    private String status;
    private String profile;
    private String small_image;
    private String userId;
    private double calroies;
    public Set<User> friend;


    public User() {

    }

    public User(String username, String status, String profile, String small_image, String userId) {
        this.username = username;
        this.status = status;
        this.profile = profile;
        this.small_image = small_image;
        this.userId = userId;
    }
   public void setCalroies(double calroies){
        this.calroies = calroies;
   }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public void setThumbnail(String small_image) {
        this.small_image = small_image;
    }

    public String getThumbnail() {
        return small_image;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public double getCalroies(){
         return this.calroies;
    }
}
