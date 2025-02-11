package com.darshilmehta.springboot.helpers;

import com.darshilmehta.springboot.entity.Follower;
import com.darshilmehta.springboot.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class NotificationFollowerHelper {

    private final String CHAR_POOL_FOR_CREATOR;
    private final String CHAR_POOL_FOR_FOLLOWER;

    public NotificationFollowerHelper() {
        CHAR_POOL_FOR_CREATOR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        CHAR_POOL_FOR_FOLLOWER = "abcdefghijklmnopqrstuvwxyz0123456789";
    }

    public Notification getRandomNotification() {
        return new Notification(generateRandomName(CHAR_POOL_FOR_CREATOR), getRandomQuote());
    }

    public List<Follower> getFollowersForACreator() {
        List<Follower> followers = new ArrayList<>();
        Random random = new Random();
        int numberOfFollowers = random.nextInt(1, 10);

        for (int i=1; i<=numberOfFollowers; i++) {
            followers.add(new Follower(generateRandomName(CHAR_POOL_FOR_FOLLOWER), random.nextBoolean()));
        }

        return followers;
    }

    private static String generateRandomName(String characters) {
        int usernameLength = 8;

        Random random = new Random();
        StringBuilder username = new StringBuilder();

        for (int i = 0; i < usernameLength; i++) {
            int index = random.nextInt(characters.length());
            username.append(characters.charAt(index));
        }

        return username.toString();
    }

    private static final List<String> quotes = List.of(
            "The only way to do great work is to love what you do. – Steve Jobs",
            "Life is what happens when you're busy making other plans. – John Lennon",
            "In the end, we will remember not the words of our enemies, but the silence of our friends. – Martin Luther King Jr.",
            "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment. – Ralph Waldo Emerson",
            "It is never too late to be what you might have been. – George Eliot",
            "The future belongs to those who believe in the beauty of their dreams. – Eleanor Roosevelt"
    );

     public static String getRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.size());
        return quotes.get(index);
    }

}
