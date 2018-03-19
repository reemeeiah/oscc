package com.oscc.oscc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Hospital;
import com.oscc.oscc.R;

import java.util.ArrayList;

/**
 * Created by Nona on 3/17/2018.
 */

public class HospitalAdapter extends ArrayAdapter<Hospital>{
    public HospitalAdapter(Context context, ArrayList<Hospital> hospitals) {
        super(context,0, hospitals);
        // TODO find resource
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hospital H = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hospital_list_item2, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.hospital_name_tv)).setText(H.HospitalName);
        ((TextView)convertView.findViewById(R.id.HospitalAddress_tv)).setText(H.HospitalAddress);
        convertView.setTag(H);
        return convertView;
    }

}
