package com.coen445FinalProject.server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Server server = new Server(5001);
        User user = new User("Bob", "Password123");
        user.addInterest("Soccer");
        user.addInterest("Football");
        user.addInterest("Hockey");
        user.addInterest("Rugby");
        User user1 = new User("Jim", "Password123");
        user1.addInterest("Soccer");
        user1.addInterest("Football");
        user1.addInterest("Hockey");
        user1.addInterest("Rugby");
        JSONHelper helper = new JSONHelper();
        helper.saveUserToJSON(user);
        helper.saveUserToJSON(user1);
    }
}
