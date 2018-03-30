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
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.AwarenessAdapter;
import com.oscc.oscc.Models.Awareness;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.onNavigationItemSelected2;
import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.setNav;
import static com.oscc.oscc.MainActivity.user;

public class AwarenessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    AwarenessAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Awareness awareness = new Awareness();
               viewItem(awareness,true);
            }
        });



        if(user.UserType ==1 || user.UserType==2){fab.setVisibility(View.VISIBLE);}else {fab.setVisibility(View.INVISIBLE);}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView  navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNav(navigationView);
        setData();
    }

    void setData()
    {
         adapter = new AwarenessAdapter(AwarenessActivity.this,data.awarenesses);
        ((ListView)findViewById(R.id.awareness_lv)).setAdapter(adapter);
        ((ListView)findViewById(R.id.awareness_lv)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Awareness awareness=(Awareness)view.getTag();
                viewItem(awareness, user.UserType ==1);
            }
        });

    }

    void viewItem(final Awareness item, boolean canEdit)
    {
        final Dialog dialog = new Dialog(AwarenessActivity.this,R.style.Dialog);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.awareness_dialog);
        dialog.setTitle(item.Id>0? "Aware "+item.Id: "New Aware ");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);



        ((EditText)dialog.findViewById(R.id.AwareTitle_tx)).setEnabled(canEdit);
        ((EditText)dialog.findViewById(R.id.AwareDescription_tx)).setEnabled(canEdit);

        if(item.Id>0)
        {

            ((TextView)dialog.findViewById(R.id.AwareTime_tv)).setText(""+item.AwareTime);
            ((EditText)dialog.findViewById(R.id.AwareTitle_tx)).setText(""+item.AwareTitle);
            ((EditText)dialog.findViewById(R.id.AwareDescription_tx)).setText(""+item.AwareDescription);
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
                public void onClick(View v)
                {
                    item.AwareTitle = ((EditText)dialog.findViewById(R.id.AwareTitle_tx)).getText().toString();
                    item.AwareDescription = ((EditText)dialog.findViewById(R.id.AwareDescription_tx)).getText().toString();

                    if(item.Id>0)
                    {
                        server.updateAwareness(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                data.updateAwareness(item);
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
                        server.postAwareness(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    data.awarenesses.add(new Awareness(new String(responseBody, "UTF-8")));
                                    setData();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                               // dialog.cancel();
                            }
                        });
                    }


                }
            });

            ((Button)dialog.findViewById(R.id.delete_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(AwarenessActivity.this)
                            .setTitle("warning")
                            .setMessage("Do you really want to Delete this Aware?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int whichButton) {
                                    server.deleteAwareness(item, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            data.awarenesses.remove(item);
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
            onNavigationItemSelected2(AwarenessActivity.this,item);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
