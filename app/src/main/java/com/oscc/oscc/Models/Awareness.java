package com.oscc.oscc.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by norah3mair on 03/03/2018 AD.
 */

public class Awareness
{
    public int Id;

    public String AwareTitle, AwareDiscription,AwareTime;

    public Awareness()
    {

    }
    public Awareness(String jsonAwareness)
    {
        try {
            JSONObject a = new JSONObject(jsonAwareness);
            this.Id = a.getInt("Id");
            this.AwareTime = a.getString("AwareTime");
            this.AwareTitle = a.getString(" AwareTitle");
            this.AwareDiscription = a.getString("AwareDiscription");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject a = new JSONObject();
        try {
            a.put("Id",this.Id);
            a.put("StoryTime",this.AwareTime);
            a.put("StoryTitle",this.AwareTitle);
            a.put("StoryDescription",this.AwareDiscription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return a;
    }



}
