
import java.util.Objects;

public class User {
    private String username;
    private String name;
    private int followersCount;
    private int followingCount;
    public String language;
    public String region;
    String [] tweets;
    String[] followers;
    String[] following;
    User[] followingUser;
    User[] followersUser;
    String[] interest;
    User[] relatedFollowers;

    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String[] getFollowing() {
        return following;
    }

    public void setFollowing(String[] following) {
        this.following = following;
    }

    public User[] getFollowingUser() {
        return followingUser;
    }

    public void setFollowingUser(User[] followingUser) {
        this.followingUser = followingUser;
    }

    public User[] getFollowersUser() {
        return followersUser;
    }

    public void setFollowersUser(User[] followersUser) {
        this.followersUser = followersUser;
    }

    /*public void addFollowing(String followedUsername) {
        // Eğer following listesi null ise, yeni bir String dizisi oluştur
        if (following == null) {
            following = new String[1];
            following[0] = followedUsername;
        } else {
            // Eğer following listesi null değilse, mevcut boyutunu bir artırarak yeni kullanıcıyı ekle
            String[] newFollowing = new String[following.length + 1];
            System.arraycopy(following, 0, newFollowing, 0, following.length);
            newFollowing[following.length] = followedUsername;
            following = newFollowing;
        }
    }*/
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", followersCount=" + followersCount +
                ", followingCount=" + followingCount +
                ", language='" + language + '\'' +
                ", region='" + region + '\'' +
                ", tweets=" + tweets +
                ", following=" + following +
                ", followers=" + followers +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String[] getTweets() {
        return tweets;
    }

    public void setTweets(String [] tweets) {
        this.tweets = tweets;
    }

    
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
