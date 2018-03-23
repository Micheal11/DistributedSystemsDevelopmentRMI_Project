// package com.company;

import java.util.ArrayList;
/**
 * Created by IVAN   FRANK on 3/2/2018.
 */
//...................................class that stores the messages received........................
public class History {
    public final int threshold = 1024;
    ArrayList<String> messages = new ArrayList<>();

    //.............method that adds  messages to the ArrayList.......................................
    public void addMessage(String message){
        messages.add(message);
        checkMessages();
    }

    //.....................method that removes the first message in the ArrayList when it is full
    public void checkMessages(){
        if (messages.size() > threshold){
            messages.remove(0);
        }
    }


}

