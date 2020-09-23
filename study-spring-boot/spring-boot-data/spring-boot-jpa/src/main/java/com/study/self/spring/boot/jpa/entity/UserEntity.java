package com.study.self.spring.boot.jpa.entity;

import com.study.self.spring.boot.jpa.dto.User;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "realname")
    private String realname;

    @Column(name = "mobile")
    private String mobile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public static UserEntity parseFrom(User user){
        UserEntity userEntity = new UserEntity();
        userEntity.password = user.getPassword();
        userEntity.realname = user.getRealname();
        userEntity.username = user.getUsername();
        userEntity.mobile = user.getMobile();
        return userEntity;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", realname='" + realname + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
