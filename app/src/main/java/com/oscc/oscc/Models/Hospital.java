package com.oscc.oscc.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asus on 10/03/18.
 */

public class Hospital {
    public int Id,HospitalAdminUserId;
    public String HospitalName,HospitalAddress,HospitalPhone,HospitalLatLong;

    public Hospital()
    {

    }
    public Hospital(String jsonHospital)
    {
        try {
            JSONObject t = new JSONObject(jsonHospital);
            this.Id = t.getInt("Id");
            this.HospitalAdminUserId = t.getInt("HospitalAdminUserId");
            this.HospitalName = t.getString("HospitalName");
            this.HospitalAddress = t.getString("HospitalAddress");
            this.HospitalPhone = t.getString("HospitalPhone");
            this.HospitalLatLong = t.getString("HospitalLatLong");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject t = new JSONObject();
        try {
            t.put("Id",this.Id);
            t.put("HospitalAdminUserId",this.HospitalAdminUserId);
            t.put("HospitalName",this.HospitalName);
            t.put("HospitalAddress",this.HospitalAddress);
            t.put("HospitalPhone",this.HospitalPhone);
            t.put("HospitalLatLong",this.HospitalLatLong);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return t;
    }

}
