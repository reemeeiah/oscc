package com.oscc.oscc;

import android.app.Dialog;
import android.os.Bundle;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oscc.oscc.Models.Awareness;
import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.Models.Specialist;
import com.oscc.oscc.Models.Story;
import com.oscc.oscc.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public  static Server  server;
    public static User user;
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

    NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        server = new Server(MainActivity.this);
        fillData();

    }

    public void login()
    {
        final Dialog loginDialog = new Dialog(MainActivity.this);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.loginview);
        //loginDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        loginDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loginDialog.setCancelable(false);

        final EditText userName_tx = (EditText) loginDialog.findViewById(R.id.userName_tx);
        final EditText userPassword_tx = (EditText) loginDialog.findViewById(R.id.userPassword_tx);




        Button button_login= (Button) loginDialog.findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send user name and pass to server in order to mach the cridentils


                server.login(userName_tx.getText().toString(),userPassword_tx.getText().toString(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            String res = new String(responseBody, "UTF-8");
                            Log.i("OK", res);
                            JSONObject juser = new JSONObject(res);
                            user = new User(juser.toString());
                            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userName_tv)).setText(user.UserName);
                            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail_tv)).setText(user.UserEmail);

                            Toast.makeText(MainActivity.this,"Welcome "+ user.UserName,Toast.LENGTH_LONG).show();
                            switch (user.UserType)
                            {
                                case 1:
                                {
                                    //TODO go to admin activity

//
//                                    Intent hospital = new Intent(MainActivity.this,HospitalActivity.class);
//                                    MainActivity.this.startActivity(hospital);
                                    navigationView.getMenu().setGroupVisible(R.id.admin_group,true);



                                  //  fragmentManager.beginTransaction().replace(R.id.container, new AdminFragment()).commit();

                                    break;
                                }

                                case 2:
                                {
                                    //TODO go to hospital admin activity
                                    navigationView.getMenu().setGroupVisible(R.id.hospital_admin_group,true);
                                   // fragmentManager.beginTransaction().replace(R.id.container, new HospitalFragment()).commit();
                                    break;
                                }
                                case 3:
                                {
                                    //TODO go to user activity
                                    navigationView.getMenu().setGroupVisible(R.id.user_group,true);

                                    UserFragment userFragment = new UserFragment();
                                    fragmentManager.beginTransaction().replace(R.id.container,userFragment).commit();
                                    userFragment.setStories();
                                    break;
                                }
                            }
                            loginDialog.cancel();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("LoginFailed", "User name or password not correct ");
                        Toast.makeText(MainActivity.this,"User name or password not correct ",Toast.LENGTH_LONG).show();

                    }
                });
                //then if server return ok and get the user type also id

            }
        });

        Button sinup_bt = (Button) loginDialog.findViewById(R.id.sinup_bt);
        sinup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinup();
            }
        });

        try
        {
            loginDialog.show();

        }catch (Exception ex)
        {

        }
    }


    public void sinup()
    {
        final Dialog signupDialog = new Dialog(MainActivity.this);

        signupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signupDialog.setContentView(R.layout.signupview);
        signupDialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT ,ViewGroup.LayoutParams.WRAP_CONTENT);
        signupDialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        signupDialog.setCancelable(false);

        final EditText editText_username = (EditText) signupDialog.findViewById(R.id.editText_username);
        final EditText pass = (EditText) signupDialog.findViewById(R.id.pass);
        final EditText re_pass = (EditText) signupDialog.findViewById(R.id.re_pass);
        final EditText email = (EditText) signupDialog.findViewById(R.id.email);
        final EditText mobile = (EditText) signupDialog.findViewById(R.id.mobile);

        Button button_signup= (Button) signupDialog.findViewById(R.id.signup_b);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send user Data to server tobe reg as new user    name and pass to server in order to mach the cridentils
                if(pass.getText().toString().equals(re_pass.getText().toString()))
                {
                    sendSignupRequest(editText_username.getText().toString(),pass.getText().toString(), email.getText().toString(),mobile.getText().toString(),signupDialog);

                }
                else
                {
                    re_pass.setText("");
                    Toast.makeText(MainActivity.this,"The password is not identical ",Toast.LENGTH_LONG).show();

                }

                //then if server return ok and get the user type also id
               // signupDialog.cancel();
            }
        });

        try
        {
            signupDialog.show();

        }catch (Exception ex)
        {

        }
    }

    void sendSignupRequest(String username, String userpass, String mail, String phone , final Dialog dialog)
    {
        final User[] tu = {new User()};
        tu[0].UserName = username;
        tu[0].UserType =3;
        tu[0].UserEmail =mail;
        tu[0].UserPhone= phone;
        tu[0].UserPassword =userpass;
        Log.e("out", tu[0].toJson().toString());
        server.sinUpUser(tu[0], new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String res = null;
                try {
                    res = new String(responseBody, "UTF-8");
                    Log.i("user created ...", res);
                    JSONObject juser = new JSONObject(res);
                   user = new User(juser.toString());

                    dialog.cancel();
                    Toast.makeText(MainActivity.this,"Welcome "+ user.UserName,Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.sign_out) {

            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fillData()
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

        login();

    }
}
