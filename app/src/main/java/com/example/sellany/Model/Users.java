package com.example.sellany.Model;

public class Users {
    private String Email, Password, Phone, Name;
    private String user_id;

    public Users() {

    }

    public Users(String email, String password, String phone, String name, String user_id) {
        Email = email;
        Password = password;
        Phone = phone;
        Name = name;
        user_id = user_id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { user_id = user_id; }
    @Override
    public String toString() {
        return "Users{" +
                "user_id='" + user_id + '\'' +
                ", name='" + Name + '\'' +
                '}';
        }


}