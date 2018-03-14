package com.oscc.oscc;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

}
