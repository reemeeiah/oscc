package com.oscc.oscc;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Models.Awareness;
import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;
import com.oscc.oscc.Models.Story;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.server;

/**
 *
 */

public class Data
{
    public  ArrayList<Cancer> cancers;
    public  ArrayList<Hospital> hospitals ;
    public  ArrayList<Story> stories ;
    public  ArrayList<Awareness> awarenesses ;
    public  ArrayList<Specialist> specialists ;

    public Data()
    {
        cancers = new ArrayList<>();
        hospitals = new ArrayList<>();
        stories = new ArrayList<>();
        awarenesses = new ArrayList<>();
        specialists = new ArrayList<>();
    }

    public  void fillAwareness()
    {
        // Fill Awarenesses
        server.getAllAwarenesses(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Awarenesses","we got it");
                try {
                    JSONArray jAwarenesses = new JSONArray(new String(responseBody, "UTF-8"));
                    awarenesses.clear();
                    for (int i=0;i<jAwarenesses.length();i++)
                    {
                        Awareness awareness = new Awareness(jAwarenesses.get(i).toString());
                        awarenesses.add(awareness);
                        Log.e("Awarenesses"," "+awareness.AwareTitle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Awarenesses","server error "+ statusCode);
            }
        });
    }

    public  void fillCancers()
    {
        // Fill Cancers
        server.getAllCancers(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Cancers","we got it");
                try {
                    JSONArray jCancers = new JSONArray(new String(responseBody, "UTF-8"));
                    cancers.clear();
                    for (int i=0;i<jCancers.length();i++)
                    {
                        Cancer cancer  = new Cancer(jCancers.get(i).toString());
                       cancers.add(cancer);
                        Log.e("Cancers","  "+cancer.CancerName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Cancers","server error "+ statusCode);
            }
        });

    }
    public  void fillHospitals()
    {
        // Fill Hospitals
        server.getAllHospitals(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Hospitals","we got it");
                try {
                    JSONArray jHospitals = new JSONArray(new String(responseBody, "UTF-8"));
                    hospitals.clear();
                    for (int i=0;i<jHospitals.length();i++)
                    {
                        Hospital hospital   = new Hospital(jHospitals.get(i).toString());
                        hospitals.add(hospital);
                        Log.e("Hospitals",hospital.HospitalName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Hospitals","server error "+ statusCode);
            }
        });

    }

    public  void fillSpecialists()
    {
        // Fill Specialists
        server.getAllSpecialists(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Specialists","we got it");
                try {
                    JSONArray jSpecialists = new JSONArray(new String(responseBody, "UTF-8"));
                    specialists.clear();
                    for (int i=0;i<jSpecialists.length();i++)
                    {
                        Specialist specialist    = new Specialist(jSpecialists.get(i).toString());
                        specialists.add(specialist);
                        Log.e("Specialists",specialist.SpecialistName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Specialists","server error "+ statusCode);
            }
        });

    }

    public  void fillStories()
    {
        // Fill Stories
        server.getAllStories(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Stories","we got it");
                try {
                    JSONArray jStories = new JSONArray(new String(responseBody, "UTF-8"));
                    stories.clear();
                    for (int i=0;i<jStories.length();i++)
                    {
                        Story story     = new Story(jStories.get(i).toString());
                       stories.add(story);
                        Log.e("Stories",story.StoryTitle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Stories","server error "+ statusCode);
            }
        });

    }

    public  void fillData()
    {
        fillAwareness();
        fillCancers();
        fillHospitals();
        fillSpecialists();
        fillStories();
    }

    public String getUserName(int userId)
    {

        return "";
    }

    public int getAwarenessIndexById(int id)
    {

        int i=0;
        for (Awareness a: awarenesses)
        {
            if(a.Id== id){
                return i;
            }
            i++;

        }
        return 0;
    }

    public void updateAwareness(Awareness awareness)
    {
        for (Awareness a: awarenesses)
        {
            if(a.Id== awareness.Id){
                a=awareness;
            }


        }
    }

    public void updateHospital(Hospital hospital )
    {
        for (Hospital a: hospitals)
        {
            if(a.Id== hospital.Id){
                a=hospital;
            }


        }
    }

    public Hospital getHospitalById(int id)
    {
        for (Hospital a: hospitals)
        {
            if(a.Id== id){
               return a;
            }
        }

        return new Hospital();
    }

    public Cancer getCancerById(int id)
    {
        for (Cancer a: cancers)
        {
            if(a.Id== id){
                return a;
            }
        }

        return new Cancer();
    }
    public int getCancerIndexById(int id)
    {
        int i=0;
        for (Cancer a: cancers)
        {
            if(a.Id== id){
                return i;
            }
            i++;

        }
        return 0;
    }

    public int getHospitalIndexById(int id)
    {
        int i=0;
        for (Hospital a: hospitals)
        {
            if(a.Id== id){
                return i;
            }
            i++;

        }
        return 0;
    }

    public  ArrayList<Hospital> getHospitalsByCancerId(int cancerId)
    {
        ArrayList<Hospital> res = new ArrayList<>();

        for (Hospital a: hospitals)
        {
            for (Specialist s: a.Specialists)
            {
                if(s.SpecialistCancerId==cancerId)
                {
                    res.add(a);
                    break;
                }

            }

        }


        return res;
    }
}