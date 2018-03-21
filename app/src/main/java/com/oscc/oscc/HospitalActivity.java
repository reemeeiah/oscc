package com.oscc.oscc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.HospitalAdapter;
import com.oscc.oscc.Adapters.SpecialistAdapter;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.server;
import static com.oscc.oscc.MainActivity.setNav;
import static com.oscc.oscc.MainActivity.user;

public class HospitalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView hospitals_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.UserType ==1)
                {
                    Hospital hospital = new Hospital();
                    showHospital(hospital,true);
                }



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView  navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNav(navigationView);
        hospitals_lv = (ListView)findViewById(R.id.hospitals_lv);

        refreshHospitals();

    }
    public void refreshHospitals()
    {
        HospitalAdapter adapter = new HospitalAdapter(HospitalActivity.this,MainActivity.data.hospitals);
        hospitals_lv.setAdapter(adapter);
        hospitals_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hospital hospital = (Hospital) view.getTag();

                showHospital(hospital,(user.Id == hospital.HospitalAdminUserId));
            }
        });

        hospitals_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Hospital hospital = (Hospital) view.getTag();
                if(hospital.HospitalAdminUserId == user.Id)
                {

                    new AlertDialog.Builder(HospitalActivity.this)
                            .setTitle("Title")
                            .setMessage("Do you really want to Delete this Hospital?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    server.DeleteHospital(hospital, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            for(int i=0;i<data.hospitals.size();i++)
                                            {
                                                if(data.hospitals.get(i).Id == hospital.Id)
                                                {
                                                    data.hospitals.remove(i);
                                                }
                                            }
                                            refreshHospitals();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                        }
                                    });
                                }})
                            .setNegativeButton(android.R.string.no, null).show();




                }

                return true;
            }
        });
    }


    void showHospital(final Hospital hospital, boolean canEdit)
    {
        final Dialog dialog = new Dialog(HospitalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_hospital);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        //dialog.setCancelable(false);
        if(hospital.Id >0)
        {
            ((LinearLayout)dialog.findViewById(R.id.specialist_lay)).setVisibility(View.VISIBLE);
        }
        else
        {
            ((LinearLayout)dialog.findViewById(R.id.specialist_lay)).setVisibility(View.INVISIBLE);
        }



        ((EditText)dialog.findViewById(R.id.HospitalName_tx)).setText(hospital.HospitalName);
        ((EditText)dialog.findViewById(R.id.HospitalAddress_tx)).setText(hospital.HospitalAddress);
        ((EditText)dialog.findViewById(R.id.HospitalPhone_tx)).setText(hospital.HospitalPhone);
        ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setText(hospital.HospitalLatLong);
         SpecialistAdapter specialistAdapter = new SpecialistAdapter(HospitalActivity.this,hospital.Specialists);
        ((ListView)dialog.findViewById(R.id.hospital_specialist_lv)).setAdapter(specialistAdapter);
        if(!canEdit)
        {
            ((EditText)dialog.findViewById(R.id.HospitalName_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalAddress_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalPhone_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setEnabled(false);
        }

        if(hospital.Id ==0&& canEdit)
        {
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setText("Add New");
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hospital.HospitalName = ((EditText)dialog.findViewById(R.id.HospitalName_tx)).getText().toString();
                    hospital.HospitalAddress = ((EditText)dialog.findViewById(R.id.HospitalAddress_tx)).getText().toString();
                    hospital.HospitalAdminUserId = user.Id;
                    hospital.HospitalLatLong = ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).getText().toString();
                    hospital.HospitalPhone = ((EditText)dialog.findViewById(R.id.HospitalPhone_tx)).getText().toString();
                    server.postHospital(hospital, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            try {
                                data.hospitals.add(new Hospital(new JSONObject(new String(responseBody, "UTF-8")).toString()));
                                refreshHospitals();
                                dialog.cancel();

                            } catch (JSONException e) {

                                Log.e("Hospital",e.getMessage());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.e("Hospital",e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            refreshHospitals();
                            dialog.cancel();
                        }
                    });

                }
            });
        }
        else if(hospital.Id >0&& canEdit)
        {
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setText("Update");
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hospital.HospitalName = ((EditText)dialog.findViewById(R.id.HospitalName_tx)).getText().toString();
                    hospital.HospitalAddress = ((EditText)dialog.findViewById(R.id.HospitalAddress_tx)).getText().toString();
                    hospital.HospitalAdminUserId = user.Id;
                    hospital.HospitalLatLong = ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).getText().toString();
                    hospital.HospitalPhone = ((EditText)dialog.findViewById(R.id.HospitalPhone_tx)).getText().toString();
                    server.UpdateHospital(hospital, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if(statusCode ==204)
                            {
                                //search in the main activity data <hospitals array list> to find the matched id same like ours to be replased by the updated object
                                for(int i=0;i<data.hospitals.size();i++)
                                {
                                    if(data.hospitals.get(i).Id == hospital.Id)
                                    {
                                        data.hospitals.set(i,hospital);
                                    }
                                }
                                refreshHospitals();
                                dialog.cancel();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            if(statusCode ==204)
                            {
                                dialog.cancel();
                            }
                        }
                    });

                }
            });
            ((Button)dialog.findViewById(R.id.add_specialist_bt)).setVisibility(View.VISIBLE);
            ((Button)dialog.findViewById(R.id.add_specialist_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addSpecialist(hospital , ((ListView)dialog.findViewById(R.id.hospital_specialist_lv)));
                }
            });
        }
        else
        {
            ((Button)dialog.findViewById(R.id.add_specialist_bt)).setVisibility(View.INVISIBLE);
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setText("Cancel");
            ((Button)dialog.findViewById(R.id.add_hospital_bt)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }

        dialog.show();
    }

    void addSpecialist(Hospital hospital, ListView lv)
    {
        Specialist specialist = new Specialist();
        specialist.SpecialistName="dd";
        specialist.SpecialistHospitalId = hospital.Id;
        specialist.SpecialistCancerId=1;
        specialist.SpecialistEmail ="a@a.a";
        specialist.SpecialistMajor = "sss";
        hospital.Specialists.add(specialist);
        for(int i=0;i<data.hospitals.size();i++)
        {
            if(data.hospitals.get(i).Id == hospital.Id)
            {
                data.hospitals.set(i,hospital);
            }
        }
        SpecialistAdapter specialistAdapter = new SpecialistAdapter(HospitalActivity.this,hospital.Specialists);
        lv.setAdapter(specialistAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_story) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
