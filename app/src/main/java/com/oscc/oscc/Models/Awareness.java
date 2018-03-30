package com.oscc.oscc.Models;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.oscc.oscc.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by norah3mair on 03/03/2018 AD.
 */

public class Awareness
{
    public int Id;

    public String AwareTitle, AwareDescription,AwareTime;

    public Awareness()
    {

    }
    public Awareness(String jsonAwareness)
    {
        try {
            JSONObject a = new JSONObject(jsonAwareness);
            this.Id = a.getInt("Id");
            this.AwareTime = a.getString("AwareTime");
            this.AwareTitle = a.getString("AwareTitle");
            this.AwareDescription = a.getString("AwareDescription");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject toJson()
    {
        JSONObject a = new JSONObject();
        try {
            a.put("Id",this.Id);
            a.put("AwareTime",this.AwareTime);
            a.put("AwareTitle",this.AwareTitle);
            a.put("AwareDescription",this.AwareDescription);
        } catch (JSONException e)
        {
            Log.e("Aware", e.getMessage());
            e.printStackTrace();
        }

        return a;
    }

    public View getView(Context context, boolean canEdit)
    {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        EditText AwareTitle_tx = new EditText(context);
        AwareTitle_tx.setId(R.id.AwareTitle_tx);
        AwareTitle_tx.setHint("Aware Title");
        AwareTitle_tx.setEnabled(canEdit);

        EditText AwareDescription_tx = new EditText(context);
        AwareDescription_tx.setId(R.id.AwareDescription_tx);
        AwareDescription_tx.setHint("Aware Description");
        AwareDescription_tx.setEnabled(canEdit);

        AwareTitle_tx.setText(this.AwareTitle);
        AwareDescription_tx.setText(this.AwareDescription);


        view.addView(AwareTitle_tx);
        view.addView(AwareDescription_tx);

        return view;
    }


}
