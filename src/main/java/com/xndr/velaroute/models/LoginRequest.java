package com.xndr.velaroute.models;

import org.jspecify.annotations.Nullable;

public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {return username;}
    public void setEmail(String email) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
