package com.kitekite.initahunnyakita.model;

import java.io.Serializable;

/**
 * Created by Florian on 2/26/2015.
 */
public class Discussion implements Serializable{
    public String name;
    public String profile_url;
    public int no_of_discussions;
    public Conversation[] discussions;

    public String getDiscussionsText(){
        return no_of_discussions+" discussions";
    }

    public static class Conversation extends Discussion{
        public String title;
        public String image_url;
        public String last_message;

        public Conversation(String title, String image_url, String last_message){
            this.title = title;
            this.image_url = image_url;
            this.last_message = last_message;
        }
    }
}
