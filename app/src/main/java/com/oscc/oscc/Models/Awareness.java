package com.oscc.oscc.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by norah3mair on 03/03/2018 AD.
 */

public class Awareness
{
    public int Id;

    public String AwareTitle, AwareDescription,AwareTime;

    public Awareness()
    {

    }
    public Awareness(String jsonAwareness)
    {
        try {
            JSONObject a = new JSONObject(jsonAwareness);
            this.Id = a.getInt("Id");
            this.AwareTime = a.getString("AwareTime");
            this.AwareTitle = a.getString("AwareTitle");
            this.AwareDescription = a.getString("AwareDescription");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject a = new JSONObject();
        try {
            a.put("Id",this.Id);
            a.put("AwareTime",this.AwareTime);
            a.put("AwareTitle",this.AwareTitle);
            a.put("AwareDescription",this.AwareDescription);
        } catch (JSONException e)
        {
            Log.e("Aware", e.getMessage());
            e.printStackTrace();
        }

        return a;
    }



}
