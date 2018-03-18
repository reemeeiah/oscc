package com.oscc.oscc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Specialist;
import com.oscc.oscc.R;

/**
 * Created by Nona on 3/17/2018.
 */

public class SpecialistAdapter extends ArrayAdapter<Specialist> {


    public SpecialistAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        //TODO resource
    }

    public View getVeiw(int position, View convertView , ViewGroup parent){

        Specialist s = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hospital_info_when_click_hospital_list_view,parent,false);

        }
        ((TextView)convertView.findViewById(R.id.Specialist_Name)).setText(s.SpecialistName);
        ((TextView)convertView.findViewById(R.id.Specialist_Major)).setText(s.SpecialistMajor);
        ((TextView)convertView.findViewById(R.id.Specialist_Email)).setText(s.SpecialistEmail);
        return convertView;


    }
}
