package com.oscc.oscc.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 10/03/18.
 */

public class Story {


    public int Id,StoryUserId;
    public String StoryTitle,StoryDescription,StoryTime;

    public Story()
    {

    }
    public Story(String jsonStory)
    {
        try {
            JSONObject s = new JSONObject(jsonStory);
            this.Id = s.getInt("Id");
            
            this.StoryUserId = s.getInt("StoryUserId");
            this.StoryTime = s.getString("StoryTime");
            this.StoryTitle =   s.getString("StoryTitle");
            this.StoryDescription = s.getString("StoryDescription");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject s = new JSONObject();
        try {
            s.put("Id",this.Id);
            s.put("StoryTime",this.StoryTime);
            s.put("StoryUserId",this.StoryUserId);
            s.put("StoryTitle",this.StoryTitle);
            s.put("StoryDescription",this.StoryDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return s;
    }



}
