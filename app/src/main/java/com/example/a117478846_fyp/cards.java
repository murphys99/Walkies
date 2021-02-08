package com.example.a117478846_fyp;
//potential matches to user video https://www.youtube.com/watch?v=xTGyUUsOTAQ&t=92s&ab_channel=SimCoder
// adapted this video for matching users https://www.youtube.com/watch?v=Xp61U9sCiTw&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=9&pbjreload=101&ab_channel=SimCoder

public class cards {
    private String userId;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String phone;
    private String profileImageUrl;

    public cards (String userId, String fname, String profileImageUrl){
        //String lname, String email, String password, String phone
        this.userId = userId;
        this.fname = fname;
       // this.lname = lname;
       // this.email= email;
       // this.password = password;
       // this.phone= phone;
        this.profileImageUrl = profileImageUrl;
    }
        public String getUserId(){
            return userId;
        }
        public void setUserId(String userId){
        this.userId = userId;
        }

        public String getFname(){
        return fname;
        }
        public void setFname(String fname){
            this.fname = fname;
        }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){ this.profileImageUrl = profileImageUrl;
    }






    public String getLname(){
        return lname;
        }
        public void setLname(String lname){
        this.lname = lname;
        }

        public String getEmail(){
        return email;
        }
        public void setEmail(String email){
            this.email = email;
        }

        public String getPassword(){
        return email;
        }
        public void setPassword(String password){
            this.password = password;
        }

        public String getPhone(){
        return phone;
        }
        public void setPhone(String phone){
            this.phone = phone;
        }
}

