package com.oscc.oscc;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Models.Awareness;
import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;
import com.oscc.oscc.Models.Story;
import com.oscc.oscc.Models.User;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Anwar on 2018-03-13.
 */

public class Server {


    Context context;
    public static AsyncHttpClient client = new AsyncHttpClient();

    static final String API_URL = "http://oscc.4dsis.com/api/";



    public Server(Context con)
    {
        context= con;


        client.setConnectTimeout(30000);
        client.setResponseTimeout(30000);
    }

    public void login(String userName, String userPassword,AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"users/Login?userName="+userName+"&userPassword="+userPassword,handler);

    }

    public void sinUpUser(User user, AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(user.toJson().toString(), "UTF-8");
        Log.i(" out ", user.toJson().toString());
        client.post(context, API_URL + "users/PostUser", entity, "application/json", handler);
    }


    public void getAllStories(AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"Stories/",handler);
    }

    public void postStory(Story story, AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(story.toJson().toString(), "UTF-8");
        Log.i(" out ", story.toJson().toString());
        client.post(context, API_URL + "Stories/", entity, "application/json", handler);
    }

    public void updateStory(Story story, AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(story.toJson().toString(), "UTF-8");
        Log.i(" out ", story.toJson().toString());
        client.put(context, API_URL + "Stories/?id="+story.Id, entity, "application/json", handler);
    }

    public void deleteStory(Story story, AsyncHttpResponseHandler handler)
    {

        client.delete(context, API_URL + "Stories/?id="+story.Id, null, "application/json", handler);
    }


    // Cancers
    public void getAllCancers(AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"Cancers/",handler);
    }

    public void postCancer(Cancer cancer, AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(cancer.toJson().toString(), "UTF-8");
        Log.i(" out ", cancer.toJson().toString());
        client.post(context, API_URL + "Cancers/", entity, "application/json", handler);
    }

    public void updateCancer(Cancer cancer, AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(cancer.toJson().toString(), "UTF-8");
        Log.i(" out ", cancer.toJson().toString());
        client.put(context, API_URL + "Cancers/?id="+cancer.Id, entity, "application/json", handler);
    }
    public void deleteCancer(Cancer cancer, AsyncHttpResponseHandler handler)
    {

        client.delete(context, API_URL + "Cancers/?id="+cancer.Id, null, "application/json", handler);
    }

    // Hospitals
    public void getAllHospitals(AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"Hospitals/",handler);
    }

    public void postHospital(Hospital hospital , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(hospital.toJson().toString(), "UTF-8");
        Log.i(" out ", hospital.toJson().toString());
        client.post(context, API_URL + "Hospitals/", entity, "application/json", handler);
    }

    public void UpdateHospital(Hospital hospital , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(hospital.toJson().toString(), "UTF-8");
        Log.i(" out ", hospital.toJson().toString());
        client.put(context, API_URL + "Hospitals/?Id="+hospital.Id, entity, "application/json", handler);
    }
    public void DeleteHospital(Hospital hospital , AsyncHttpResponseHandler handler)
    {
        client.delete(context, API_URL + "Hospitals/?Id="+hospital.Id, null, "application/json", handler);
    }
    // Specialists
    public void getAllSpecialists(AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"Specialists/",handler);
    }

    public void postSpecialist(Specialist specialist  , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(specialist.toJson().toString(), "UTF-8");
        Log.i(" out ", specialist.toJson().toString());
        client.post(context, API_URL + "Specialists/", entity, "application/json", handler);
    }

    public void updateSpecialist(Specialist specialist  , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(specialist.toJson().toString(), "UTF-8");
        Log.i(" out ", specialist.toJson().toString());
        client.put(context, API_URL + "Specialists/?id="+specialist.Id, entity, "application/json", handler);
    }

    public void deleteSpecialist(Specialist specialist  , AsyncHttpResponseHandler handler)
    {
        client.put(context, API_URL + "Specialists/?id="+specialist.Id, null, "application/json", handler);
    }

    // Awarenesses
    public void getAllAwarenesses(AsyncHttpResponseHandler handler)
    {
        client.get(API_URL+"Awarenesses/",handler);
    }

    public void postAwareness(Awareness awareness   , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(awareness.toJson().toString(), "UTF-8");
        Log.i(" out ", awareness.toJson().toString());
        client.post(context, API_URL + "Awarenesses/", entity, "application/json", handler);
    }

    public void updateAwareness(Awareness awareness   , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(awareness.toJson().toString(), "UTF-8");
        Log.i(" out ", awareness.toJson().toString());
        client.put(context, API_URL + "Awarenesses/?id="+awareness.Id, entity, "application/json", handler);
    }
    public void deleteAwareness(Awareness awareness   , AsyncHttpResponseHandler handler)
    {
        StringEntity entity = new StringEntity(awareness.toJson().toString(), "UTF-8");
        Log.i(" out ", awareness.toJson().toString());
        client.delete(context, API_URL + "Awarenesses/?id="+awareness.Id, null, "application/json", handler);
    }

}
