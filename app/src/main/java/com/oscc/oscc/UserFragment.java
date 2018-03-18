package com.oscc.oscc;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.StoryAdapter;
import com.oscc.oscc.Models.Story;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.user;


public class UserFragment extends Fragment {
    ListView stories_lv;
    Button add_story;
    ArrayList<Story> stories;
    public UserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_user, container, false);
        stories_lv = (ListView)v.findViewById(R.id.stories_lv);
        add_story = (Button)v.findViewById(R.id.add_story);


        return v;

    }
    public void setStories()
    {
        stories = new ArrayList<>();
        server.getAllStories(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                Log.e("Stories","we got it");
                try {
                    JSONArray jstories = new JSONArray(new String(responseBody, "UTF-8"));
                    for (int i=0;i<jstories.length();i++)
                    {
                        Story s = new Story(jstories.get(i).toString());

                        stories.add(s);


                    }

                    StoryAdapter adapter = new StoryAdapter(getContext(),stories);
                    stories_lv.setAdapter(adapter);
                    stories_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Story s = (Story)view.getTag();
                            Toast.makeText(getContext(),s.StoryDescription,Toast.LENGTH_LONG).show();
                            final Dialog dialog = new Dialog(getContext());

                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT);

                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.story_view);

                            ((TextView)dialog.findViewById(R.id.dettails_tv)).setText(s.StoryDescription);
                            ((TextView)dialog.findViewById(R.id.story_title_tv)).setText(s.StoryTitle);
                            ((Button)dialog.findViewById(R.id.cancel_action)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            dialog.show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Stories",e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("Stories2",e.getMessage());
                }
                add_story.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final  Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.add_story);

                        ((Button)dialog.findViewById(R.id.submit_story)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Story story = new Story();
                                story.StoryUserId = user.Id;
                                story.StoryTime = "2018-01-01T00:00:00";
                                story.StoryTitle = ((EditText)dialog.findViewById(R.id.titleneme)).getText().toString();
                                story.StoryDescription = ((EditText)dialog.findViewById(R.id.storytitle)).getText().toString();
                                server.postStory(story, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        dialog.cancel();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        Log.e("error",""+statusCode);
                                    }
                                });
                            }
                        });

                        dialog.show();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



    }

   // void setDrawer(Nav)

}
