package com.oscc.oscc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.R;

import static com.oscc.oscc.R.layout.hospital_list_for_user;

/**
 * Created by Nona on 3/17/2018.
 */

public class HospitalAdapter extends ArrayAdapter<Hospital>{
    public HospitalAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        // TODO find resource
    }

    public View getView(int position , View convertview, ViewGroup parent){
        Hospital H = getItem(position);
        if(convertview == null) {
            convertview = LayoutInflater.from(getContext()).inflate(hospital_list_for_user, parent, false);
        }
        ((TextView)convertview.findViewById(R.id.hospital_name_in_list)).setText(H.HospitalName);
        convertview.setTag(H);

        return convertview;


    }
}
