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
 * Created by Anwar on 2018-03-18.
 */

public class Data
{
    public static ArrayList<Cancer> cancers = new ArrayList<>();
    public static ArrayList<Hospital> hospitals = new ArrayList<>();
    public static ArrayList<Story> stories = new ArrayList<>();
    public static ArrayList<Awareness> awarenesses = new ArrayList<>();
    public static ArrayList<Specialist> specialists = new ArrayList<>();

    public static void fillAwareness()
    {
        // Fill Awarenesses
        server.getAllAwarenesses(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Awarenesses","we got it");
                try {
                    JSONArray jAwarenesses = new JSONArray(new String(responseBody, "UTF-8"));
                    Data.awarenesses.clear();
                    for (int i=0;i<jAwarenesses.length();i++)
                    {
                        Awareness awareness = new Awareness(jAwarenesses.get(i).toString());
                        Data.awarenesses.add(awareness);
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

    public static void fillCancers()
    {
        // Fill Cancers
        server.getAllCancers(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Cancers","we got it");
                try {
                    JSONArray jCancers = new JSONArray(new String(responseBody, "UTF-8"));
                    Data.cancers.clear();
                    for (int i=0;i<jCancers.length();i++)
                    {
                        Cancer cancer  = new Cancer(jCancers.get(i).toString());
                        Data.cancers.add(cancer);
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
    public static void fillHospitals()
    {
        // Fill Hospitals
        server.getAllHospitals(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Hospitals","we got it");
                try {
                    JSONArray jHospitals = new JSONArray(new String(responseBody, "UTF-8"));
                    Data.hospitals.clear();
                    for (int i=0;i<jHospitals.length();i++)
                    {
                        Hospital hospital   = new Hospital(jHospitals.get(i).toString());
                        Data.hospitals.add(hospital);
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

    public static void fillSpecialists()
    {
        // Fill Specialists
        server.getAllSpecialists(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Specialists","we got it");
                try {
                    JSONArray jSpecialists = new JSONArray(new String(responseBody, "UTF-8"));
                    Data.specialists.clear();
                    for (int i=0;i<jSpecialists.length();i++)
                    {
                        Specialist specialist    = new Specialist(jSpecialists.get(i).toString());
                        Data.specialists.add(specialist);
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

    public static void fillStories()
    {
        // Fill Stories
        server.getAllStories(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("Stories","we got it");
                try {
                    JSONArray jStories = new JSONArray(new String(responseBody, "UTF-8"));
                    Data.stories.clear();
                    for (int i=0;i<jStories.length();i++)
                    {
                        Story story     = new Story(jStories.get(i).toString());
                        Data.stories.add(story);
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

    public static void fillData()
    {
        fillAwareness();
        fillCancers();
        fillHospitals();
        fillSpecialists();
        fillStories();
    }
}