package com.example.a117478846_fyp;

public class MatchesObject {
    private String userId;
    private String fname;
    private String profileImageUrl;
    public MatchesObject(String userId, String fname, String profileImageUrl){
        this.userId=userId;
        this.fname=fname;
        this.profileImageUrl=profileImageUrl;

    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId=userId;
    }


    public String getFname(){
        return fname;
    }
    public void setFname(String fname){
        this.fname=fname;
    }


    public String getProfileImageUrl(){return profileImageUrl;}
    public void setProfileImageUrl(String profileImageUrl){this.profileImageUrl=profileImageUrl;}
        }


