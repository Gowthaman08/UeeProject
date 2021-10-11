package com.example.busbooking.Model;

public class UserInfo {
    private String name;
    private String phone;
    private String email;
    private String password;

    public UserInfo() {
    }

    public UserInfo( String email,String name, String password, String phone) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
