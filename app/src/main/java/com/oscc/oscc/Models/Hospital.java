package com.oscc.oscc.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by asus on 10/03/18.
 */

public class Hospital {
    public int Id,HospitalAdminUserId;
    public String HospitalName,HospitalAddress,HospitalPhone,HospitalLatLong;
    public ArrayList<Specialist> Specialists;

    public Hospital()
    {
        Specialists = new ArrayList<>();
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
            this.Specialists = new ArrayList<>();
            for(int i =0;i< t.getJSONArray("Specialists").length() ;i++ )
            {
                Specialists.add(new Specialist(t.getJSONArray("Specialists").get(i).toString()));
            }


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
            JSONArray jSpecialists = new JSONArray();
            for (Specialist s: Specialists )
            {
                jSpecialists.put(s.toJson());
            }
            t.put("Specialists",jSpecialists);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return t;
    }

    public void addSpecialist(Specialist specialist)
    {
        if(this.Id >0)
        {
            specialist.SpecialistHospitalId = this.Id;
            this.Specialists.add(specialist);
        }

    }

}
