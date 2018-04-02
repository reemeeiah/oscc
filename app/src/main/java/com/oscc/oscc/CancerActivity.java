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
import com.oscc.oscc.Adapters.CancerAdapter;
import com.oscc.oscc.Models.Cancer;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.onNavigationItemSelected2;
import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.setNav;
import static com.oscc.oscc.MainActivity.user;

public class CancerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    CancerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Cancer cancer = new Cancer();
               viewItem(cancer,true);
            }
        });
        if(user.UserType ==1){fab.setVisibility(View.VISIBLE);}else {fab.setVisibility(View.INVISIBLE);}
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
         adapter = new CancerAdapter(CancerActivity.this,data.cancers);
        ((ListView)findViewById(R.id.cancer_lv)).setAdapter(adapter);
        ((ListView)findViewById(R.id.cancer_lv)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cancer cancer = (Cancer)view.getTag();
                viewItem(cancer,user.UserType ==1);
            }
        });
    }

    void viewItem(final Cancer item, boolean canEdit)
    {
        final Dialog dialog = new Dialog(CancerActivity.this,R.style.Dialog);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cancer_dialog);
        dialog.setTitle(item.Id>0? "Cancer ": "New Cancer ");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ((EditText)dialog.findViewById(R.id.CancerName_tx)).setFocusable(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerDescription_tx)).setFocusable(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerTreatment_tx)).setFocusable(canEdit);

        ((EditText)dialog.findViewById(R.id.CancerName_tx)).setFocusableInTouchMode(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerDescription_tx)).setFocusableInTouchMode(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerTreatment_tx)).setFocusableInTouchMode(canEdit);

        ((EditText)dialog.findViewById(R.id.CancerName_tx)).setClickable(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerDescription_tx)).setClickable(canEdit);
        ((EditText)dialog.findViewById(R.id.CancerTreatment_tx)).setClickable(canEdit);

        if(item.Id>0)
        {
            ((EditText)dialog.findViewById(R.id.CancerName_tx)).setText(""+item.CancerName);
            ((EditText)dialog.findViewById(R.id.CancerDescription_tx)).setText(""+item.CancerDescription);
            ((EditText)dialog.findViewById(R.id.CancerTreatment_tx)).setText(""+item.CancerTreatment);
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

                    item.CancerName = ((EditText)dialog.findViewById(R.id.CancerName_tx)).getText().toString();
                    item.CancerDescription = ((EditText)dialog.findViewById(R.id.CancerDescription_tx)).getText().toString();
                    item.CancerTreatment = ((EditText)dialog.findViewById(R.id.CancerTreatment_tx)).getText().toString();
                    if(item.Id>0)
                    {
                        server.updateCancer(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                for (Cancer cancer: data.cancers)
                                {
                                    if(cancer.Id== item.Id){ cancer=item; }
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
                        server.postCancer(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                try {
                                    data.cancers.add(new Cancer(new String(responseBody, "UTF-8")));
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
                    new AlertDialog.Builder(CancerActivity.this)
                            .setTitle("warning")
                            .setMessage("Do you really want to Delete this Cancer?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int whichButton) {
                                    server.deleteCancer(item, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                                            data.cancers.remove(item);
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
            onNavigationItemSelected2(CancerActivity.this,item);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
