package com.study.self.spring.boot.jpa.dto;

import javax.persistence.Column;
import javax.persistence.Id;

public class User {

    private String username;
    private String password;
    private String realname;
    private String mobile;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public static class Builder{

        private String username;
        private String password;
        private String realname;
        private String mobile;

        public static Builder newBuilder(){
            return new Builder();
        }
        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder realname(String realname) {
            this.realname = realname;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public User build(){
            User user = new User();
            user.mobile = this.mobile;
            user.password = this.password;
            user.realname = this.realname;
            user.username = this.username;
            return user;
        }
    }
}
