package com.oscc.oscc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Cancer;
import com.oscc.oscc.R;

import java.util.ArrayList;

/**
 * Created by Nona on 3/17/2018.
 */

public class CancerAdapter extends ArrayAdapter<Cancer>{
    public CancerAdapter(@NonNull Context context, ArrayList<Cancer> cancers) {
        super(context, 0,cancers);
        //TODO resource
    }

    public View getView(int position, View converView, ViewGroup parent){
        Cancer c = getItem(position);
        if (converView == null){
            converView = LayoutInflater.from(getContext()).inflate(R.layout.cancer_list_item,parent,false);
        }
        ((TextView)converView.findViewById(R.id.title_cancer)).setText(c.CancerName);
       converView.setTag(c);
        return converView;
    }
}
