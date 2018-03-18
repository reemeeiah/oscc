package com.oscc.oscc.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 10/03/18.
 */

public class Cancer {


    public int Id;
    public String CancerName,CancerDescription,CancerTreatment;

    public Cancer()
    {

    }
    public Cancer(String jsonCancer)
    {
        try {
            JSONObject c = new JSONObject(jsonCancer);
            this.Id = c.getInt("Id");

            this.CancerName = c.getString("CancerName");
            this.CancerDescription = c.getString("CancerDescription");
            this.CancerTreatment = c.getString("CancerTreatment");

        } catch (JSONException e)
        {
            Log.e("Cancer", e.getMessage());
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject c = new JSONObject();
        try {
            c.put("Id",this.Id);
           c.put("CancerName",this.CancerName);
            c.put("CancerDescription",this.CancerDescription);
            c.put("CancerTreatment",this.CancerTreatment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return c;
    }




}


