package com.oscc.oscc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Specialist;
import com.oscc.oscc.R;

import java.util.ArrayList;

import static com.oscc.oscc.MainActivity.data;

/**
 * Created by Nona on 3/17/2018.
 */

public class SpecialistAdapter extends ArrayAdapter<Specialist>{
    public SpecialistAdapter(Context context, ArrayList<Specialist> specialists) {
        super(context,0, specialists);
        // TODO find resource
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Specialist specialist = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.specialist_list_item, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.specialistName_tv)).setText(specialist.SpecialistName);
        ((TextView)convertView.findViewById(R.id.SpecialistCancerId_tv)).setText(data.getCancerById(specialist.SpecialistCancerId).CancerName);

        convertView.setTag(specialist);
        return convertView;
    }

}
