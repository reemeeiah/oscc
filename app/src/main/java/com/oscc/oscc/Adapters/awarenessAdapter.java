package com.oscc.oscc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Awareness;
import com.oscc.oscc.R;

import java.util.ArrayList;

/**
 * Created by Nona on 3/17/2018.
 */

public class awarenessAdapter extends ArrayAdapter<Awareness>{


    public awarenessAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        //find resource name
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Awareness A = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.awareness_list_item, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.title_awareness)).setText(A.AwareTitle);
        ((TextView)convertView.findViewById(R.id.awareness_time)).setText(A.AwareTime);
        convertView.setTag(A);
        return convertView;
    }

}
