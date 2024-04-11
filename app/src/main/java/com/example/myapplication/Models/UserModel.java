package com.example.myapplication.Models;

public class UserModel {
    private String username, password, fullname, birthday, gender, email, phonenumber;
    private byte[] avatar;  // Thay đổi kiểu dữ liệu của avatar từ String sang byte array

    public UserModel(String username, String password, String fullname, String birthday, String gender, String email, String phonenumber, byte[] avatar) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.phonenumber = phonenumber;
        this.avatar = avatar;  // Chấp nhận avatar dưới dạng byte array
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public byte[] getAvatar() {  // Sửa đổi phương thức để trả về byte array
        return avatar;
    }

    public void setAvatar(byte[] avatar) {  // Sửa đổi phương thức để nhận byte array
        this.avatar = avatar;
    }
}
