package com.oscc.oscc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oscc.oscc.Models.Story;
import com.oscc.oscc.R;

import java.util.ArrayList;

/**
 * Created by Anwar on 2018-03-14.
 */

public class StoryAdapter extends ArrayAdapter<Story> {
    public StoryAdapter(Context context, ArrayList<Story> stories) {
        super(context,0,stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Story story = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.story_list_item, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.title_tx)).setText(story.StoryTitle);
        ((TextView)convertView.findViewById(R.id.textView1)).setText(story.StoryTime);
        convertView.setTag(story);
        return convertView;
    }
}
