package org.quarkus.dto;

import java.time.ZonedDateTime;

public class UserDto {
    private Integer userId;

    private String userName;
    private String password;
    private String role;

    private ZonedDateTime created_at;

    private ZonedDateTime updated_at;

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public ZonedDateTime getCreated_at() {
        return created_at;
    }

    public ZonedDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreated_at(ZonedDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(ZonedDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
