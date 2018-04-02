package com.oscc.oscc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.StoryAdapter;
import com.oscc.oscc.Models.Story;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.onNavigationItemSelected2;
import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.setNav;
import static com.oscc.oscc.MainActivity.user;

public class StoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    StoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Story story = new Story();
                story.StoryUserId= user.Id;
                viewItem(story,true);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNav(navigationView);
        setData();
    }

    void setData()
    {
         adapter = new StoryAdapter(StoryActivity.this,data.stories);
        ((ListView)findViewById(R.id.story_lv)).setAdapter(adapter);
        ((ListView)findViewById(R.id.story_lv)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story =(Story)view.getTag();
                viewItem(story,story.StoryUserId == user.Id);
            }
        });

    }
    void viewItem(final Story item, boolean canEdit)
    {
        final Dialog dialog = new Dialog(StoryActivity.this,R.style.Dialog);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.story_dialog);
        //dialog.setTitle(item.Id>0? "Story "+item.Id: "New Story ");


        dialog.setTitle(item.Id>0?"Story of ": "New Story ");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ((EditText)dialog.findViewById(R.id.StoryTitle_tx)).setFocusable(canEdit);
        ((EditText)dialog.findViewById(R.id.StoryDescription_tx)).setFocusable(canEdit);

        ((EditText)dialog.findViewById(R.id.StoryTitle_tx)).setFocusableInTouchMode(canEdit);
        ((EditText)dialog.findViewById(R.id.StoryDescription_tx)).setFocusableInTouchMode(canEdit);

        ((EditText)dialog.findViewById(R.id.StoryTitle_tx)).setClickable(canEdit);
        ((EditText)dialog.findViewById(R.id.StoryDescription_tx)).setClickable(canEdit);


        if(item.Id>0)
        {

            ((EditText)dialog.findViewById(R.id.StoryTitle_tx)).setText(""+item.StoryTitle);
            ((EditText)dialog.findViewById(R.id.StoryDescription_tx)).setText(""+item.StoryDescription);

        }



        if(canEdit)
        {
            ((Button)dialog.findViewById(R.id.save_bt)).setVisibility(View.VISIBLE);
            if(item.Id>0)
            {
                ((Button)dialog.findViewById(R.id.delete_bt)).setVisibility(View.VISIBLE);
            }

            ((Button)dialog.findViewById(R.id.save_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.StoryTitle = ((EditText)dialog.findViewById(R.id.StoryTitle_tx)).getText().toString();
                    item.StoryDescription = ((EditText)dialog.findViewById(R.id.StoryDescription_tx)).getText().toString();

                    if(item.Id>0)
                    {

                        server.updateStory(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                for (Story story : data.stories)
                                {
                                    if(story.Id== item.Id){ story=item; }
                                }
                                setData();
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                    else
                    {

                        server.postStory(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    data.stories.add(new Story(new String(responseBody, "UTF-8")));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                setData();
                                dialog.cancel();

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }

                }
            });

            ((Button)dialog.findViewById(R.id.delete_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(StoryActivity.this)
                            .setTitle("warning")
                            .setMessage("Do you really want to Delete this Story?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int whichButton) {

                                    server.deleteStory(item, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                            data.stories.remove(item);
                                            setData();
                                            dialog.cancel();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                        }
                                    });
                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });
        }

        ((Button)dialog.findViewById(R.id.cancel_bt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_out) {

            finish();
        }
        else
        {
            onNavigationItemSelected2(StoryActivity.this,item);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
