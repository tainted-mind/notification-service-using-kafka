package com.darshilmehta.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private boolean notificationEnabled;

    public Follower(String username, boolean notificationEnabled) {
        this.username = username;
        this.notificationEnabled = notificationEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    @Override
    public String toString() {
        return "Follower{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", notificationEnabled=" + notificationEnabled +
                '}';
    }
}
