package de.dennisguse.opentracks.data.models;
import android.content.Context;
import android.content.SharedPreferences;

public class UserModel {

    private String nickname; //User key that uniquely identifies user
    private String country;
    private long dateOfBirth;
    private String gender;
    private int height;
    private int weight;
    private boolean socialAllow; //Bool flag for leaderboard appearance permission
    private String profilePicURL; //To be replaced by firestore path - https://

    private static final String SHARED_PREFS_NAME = "UserPrefs";
    private static final String PREF_SOCIAL_ALLOW = "socialAllow";
    private transient Context context;

    public UserModel(String nickname, String country, long dateOfBirth, String gender, int height, int weight, boolean socialAllow, String profilePicURL) {

        this.nickname = nickname;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.socialAllow = socialAllow;
        this.profilePicURL = profilePicURL;
    }


    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void saveSharingPreference(boolean socialAllow) {
        this.socialAllow = socialAllow;
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_SOCIAL_ALLOW, socialAllow);
        editor.apply();
    }

    // Method to get the user's sharing preference
    public boolean getSharingPreference() {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        // Default value is false if not set
        return prefs.getBoolean(PREF_SOCIAL_ALLOW, false);
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSocialAllow() {
        return socialAllow;
    }
    public void setSocialAllow(boolean socialAllow) {
        this.socialAllow = socialAllow;
    }

    public String getPictureURL() {
        return profilePicURL;
    }
    public void setPictureURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
