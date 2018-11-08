package com.r.acetcseassignments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//CUSTOM ADAPTER TO LOAD FOLLOWER AND FOLLOWING LIST
public class CustomAdapter extends ArrayAdapter<roomData> {
    Context mContext;
    private int lastPosition = -1;

    public CustomAdapter(Context context, ArrayList<roomData> roomData) {

        super(context, 0, roomData);
        mContext = context;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result;
        View v=convertView;



        roomData room = getItem(position);
        if (v == null) {

           // convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_info, parent, false);
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_room, null);







        }
        roomData p = getItem(position);
        //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
        //convertView.startAnimation(animation);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.listitem_up);
        animation.setStartOffset(position * 100);
        v.startAnimation(animation);
        if(p!=null) {
            TextView className = (TextView) v.findViewById(R.id.className);
            TextView availability = (TextView) v.findViewById(R.id.availability);
            ImageView imageView = (ImageView) v.findViewById(R.id.imageview);
            className.setText(room.roomName);
            availability.setText(room.availability);
            if(room.img.equals("class_available")) {
                imageView.setImageResource(R.drawable.class_available);
            }
            else if(room.img.equals("class_not")) {
                imageView.setImageResource(R.drawable.class_available_not);
            }
            else if (room.img.equals("lab_available")) {
                imageView.setImageResource(R.drawable.lab_available);
            }
            else if(room.img.equals("lab_not")) {
                imageView.setImageResource(R.drawable.lab_not_available);
            }

        }
            return v;

    }

}
