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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.SpecialistAdapter;
import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.onNavigationItemSelected2;
import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.setNav;
import static com.oscc.oscc.MainActivity.user;

public class SpecialistActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    SpecialistAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Specialist specialist = new Specialist();
                viewItem(specialist,true);
            }
        });
       // fab.setVisibility(View.INVISIBLE);
        if(user.UserType ==1){fab.setVisibility(View.VISIBLE);}else {fab.setVisibility(View.INVISIBLE);}
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
         adapter = new SpecialistAdapter(SpecialistActivity.this,data.specialists);
        ((ListView)findViewById(R.id.specialist_lv)).setAdapter(adapter);
        ((ListView)findViewById(R.id.specialist_lv)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Specialist specialist = (Specialist)view.getTag();
                viewItem(specialist,(user.UserType ==1 || user.Id == data.getHospitalById(specialist.SpecialistHospitalId).HospitalAdminUserId));
            }
        });

    }

    void viewItem(final Specialist item, boolean canEdit)
    {
        final Dialog dialog = new Dialog(SpecialistActivity.this,R.style.Dialog);
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.specialist_dialog);
        dialog.setTitle(item.Id>0? "Specialist "+item.Id: "New Specialist ");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ((EditText)dialog.findViewById(R.id.SpecialistName_tx)).setEnabled(canEdit);
        ((EditText)dialog.findViewById(R.id.SpecialistMajor_tx)).setEnabled(canEdit);
        ((EditText)dialog.findViewById(R.id.SpecialistEmail_tx)).setEnabled(canEdit);

        ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setAdapter(new ArrayAdapter<Hospital>(SpecialistActivity.this, android.R.layout.simple_spinner_dropdown_item, data.hospitals));
        ((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).setAdapter(new ArrayAdapter<Cancer>(SpecialistActivity.this, android.R.layout.simple_spinner_dropdown_item, data.cancers));

        ((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).setEnabled(canEdit);
        ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setEnabled(canEdit);

        if(item.Id>0)
        {
            ((EditText)dialog.findViewById(R.id.SpecialistName_tx)).setText(item.SpecialistName);
            ((EditText)dialog.findViewById(R.id.SpecialistMajor_tx)).setText(item.SpecialistMajor);
            ((EditText)dialog.findViewById(R.id.SpecialistEmail_tx)).setText(item.SpecialistEmail);
            ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setSelection(data.getHospitalIndexById(item.SpecialistHospitalId));
            ((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).setSelection(data.getCancerIndexById(item.SpecialistCancerId));
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

                    item.SpecialistName = ((EditText)dialog.findViewById(R.id.SpecialistName_tx)).getText().toString();
                    item.SpecialistMajor = ((EditText)dialog.findViewById(R.id.SpecialistMajor_tx)).getText().toString();
                    item.SpecialistEmail = ((EditText)dialog.findViewById(R.id.SpecialistEmail_tx)).getText().toString();
                    item.SpecialistCancerId = ((Cancer)((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).getSelectedItem()).Id;
                    item.SpecialistHospitalId = ((Hospital)((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).getSelectedItem()).Id;

                    if(item.Id>0)
                    {
                        server.updateSpecialist(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                for (Specialist specialist: data.specialists)
                                {
                                    if(specialist.Id== item.Id){ specialist=item; }
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
                        server.postSpecialist(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    data.specialists.add(new Specialist(new String(responseBody, "UTF-8")));
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

                    new AlertDialog.Builder(SpecialistActivity.this)
                            .setTitle("warning")
                            .setMessage("Do you really want to Delete this Specialist?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int whichButton) {
                                    server.updateSpecialist(item, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            data.specialists.remove(item);
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
            onNavigationItemSelected2(SpecialistActivity.this,item);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
