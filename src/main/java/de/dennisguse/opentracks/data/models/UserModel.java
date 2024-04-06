package de.dennisguse.opentracks.data.models;

public class UserModel {

    private String nickname; //User key that uniquely identifies user
    private String country;
    private long dateOfBirth;
    private String gender;
    private int height;
    private int weight;
    private boolean socialAllow; //Bool flag for leaderboard appearance permission
    private String profilePicURL; //To be replaced by firestore path - https://

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
