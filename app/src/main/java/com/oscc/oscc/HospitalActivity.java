package com.oscc.oscc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Adapters.HospitalAdapter;
import com.oscc.oscc.Adapters.SpecialistAdapter;
import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

import static com.oscc.oscc.MainActivity.data;
import static com.oscc.oscc.MainActivity.onNavigationItemSelected2;
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

        if(user.UserType ==1 ){fab.setVisibility(View.VISIBLE);}else {fab.setVisibility(View.INVISIBLE);}
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

        ((CheckBox)findViewById(R.id.filter_cb)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refreshHospitals();
            }
        });

        ((Spinner)findViewById(R.id.cancer_sp)).setAdapter(new ArrayAdapter<Cancer>(HospitalActivity.this, android.R.layout.simple_spinner_dropdown_item, data.cancers));
        ((Spinner)findViewById(R.id.cancer_sp)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(((CheckBox)findViewById(R.id.filter_cb)).isChecked())
                {
                    HospitalAdapter adapter = new HospitalAdapter(HospitalActivity.this,MainActivity.data.getHospitalsByCancerId(((Cancer)((Spinner)findViewById(R.id.cancer_sp)).getSelectedItem()).Id));
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
                                        .setTitle("warning")
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void refreshHospitals()
    {
        if(((CheckBox)findViewById(R.id.filter_cb)).isChecked())
        {
            HospitalAdapter adapter = new HospitalAdapter(HospitalActivity.this,MainActivity.data.getHospitalsByCancerId(((Cancer)((Spinner)findViewById(R.id.cancer_sp)).getSelectedItem()).Id));
            hospitals_lv.setAdapter(adapter);
        }
        else
        {
            HospitalAdapter adapter = new HospitalAdapter(HospitalActivity.this,MainActivity.data.hospitals);
            hospitals_lv.setAdapter(adapter);
        }

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
                            .setTitle("warning")
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


    void showHospital(final Hospital hospital, final boolean canEdit)
    {
        final Dialog dialog = new Dialog(HospitalActivity.this,R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.hospital_dialog);
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
        ((ListView)dialog.findViewById(R.id.hospital_specialist_lv)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Specialist specialist = (Specialist)view.getTag();
                viewItem(specialist,canEdit,hospital,((ListView)dialog.findViewById(R.id.hospital_specialist_lv)));
            }
        });
        if(!canEdit)
        {
            ((EditText)dialog.findViewById(R.id.HospitalName_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalAddress_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalPhone_tx)).setEnabled(false);
            ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setFocusable(false);
            ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setFocusableInTouchMode(false);
            ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setClickable(true);
            ((EditText)dialog.findViewById(R.id.HospitalLatLong_tx)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+hospital.HospitalLatLong);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            });
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
        specialist.SpecialistHospitalId = hospital.Id;
        viewItem(specialist,true,hospital,lv);
    }

    void viewItem(final Specialist item, boolean canEdit, final Hospital hospital, final ListView lv)
    {
        final Dialog dialog = new Dialog(HospitalActivity.this,R.style.Dialog);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.specialist_dialog);
        dialog.setTitle(item.Id>0? "Specialist  ": "New Specialist ");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ((EditText)dialog.findViewById(R.id.SpecialistName_tx)).setEnabled(canEdit);
        ((EditText)dialog.findViewById(R.id.SpecialistMajor_tx)).setEnabled(canEdit);
        ((EditText)dialog.findViewById(R.id.SpecialistEmail_tx)).setEnabled(canEdit);

        ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setAdapter(new ArrayAdapter<Hospital>(HospitalActivity.this, android.R.layout.simple_spinner_dropdown_item, data.hospitals));
        ((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).setAdapter(new ArrayAdapter<Cancer>(HospitalActivity.this, android.R.layout.simple_spinner_dropdown_item, data.cancers));

        ((Spinner)dialog.findViewById(R.id.SpecialistCancerId_sp)).setEnabled(canEdit);
        ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setEnabled(false);

        ((Spinner)dialog.findViewById(R.id.SpecialistHospitalId_sp)).setSelection(data.getHospitalIndexById(item.SpecialistHospitalId));
        if(item.Id>0)
        {
            ((EditText)dialog.findViewById(R.id.SpecialistName_tx)).setText(item.SpecialistName);
            ((EditText)dialog.findViewById(R.id.SpecialistMajor_tx)).setText(item.SpecialistMajor);
            ((EditText)dialog.findViewById(R.id.SpecialistEmail_tx)).setText(item.SpecialistEmail);

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
                    item.SpecialistHospitalId = hospital.Id;

                    if(item.Id>0)
                    {
                        server.updateSpecialist(item, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                for (Specialist specialist: data.specialists)
                                {
                                    if(specialist.Id== item.Id){ specialist=item; }
                                }

                                for (Specialist specialist: hospital.Specialists)
                                {
                                    if(specialist.Id== item.Id){ specialist=item; }
                                }

                                data.updateHospital(hospital);
                                SpecialistAdapter specialistAdapter = new SpecialistAdapter(HospitalActivity.this,hospital.Specialists);
                                lv.setAdapter(specialistAdapter);
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

                                    hospital.Specialists.add(new Specialist(new String(responseBody, "UTF-8")));
                                   data.updateHospital(hospital);
                                    SpecialistAdapter specialistAdapter = new SpecialistAdapter(HospitalActivity.this,hospital.Specialists);
                                    lv.setAdapter(specialistAdapter);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

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

                    new AlertDialog.Builder(HospitalActivity.this)
                            .setTitle("warning")
                            .setMessage("Do you really want to Delete this Hospital?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface d, int whichButton) {
                                    server.updateSpecialist(item, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            data.specialists.remove(item);

                                            hospital.Specialists.remove(item);
                                            data.updateHospital(hospital);
                                            SpecialistAdapter specialistAdapter = new SpecialistAdapter(HospitalActivity.this,hospital.Specialists);
                                            lv.setAdapter(specialistAdapter);

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
        int id = item.getItemId();

        if (id == R.id.sign_out) {

            finish();
        }
        else
        {
            onNavigationItemSelected2(HospitalActivity.this,item);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
