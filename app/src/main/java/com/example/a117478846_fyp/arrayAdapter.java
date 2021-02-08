package com.example.a117478846_fyp;
//potential matches to user video https://www.youtube.com/watch?v=xTGyUUsOTAQ&t=92s&ab_channel=SimCoder
// adapted this video for matching users https://www.youtube.com/watch?v=Xp61U9sCiTw&list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf&index=9&pbjreload=101&ab_channel=SimCoder

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);


        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);



        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getFname());
        switch (card_item.getProfileImageUrl()){
            case "default":
                image.setImageResource(R.mipmap.ic_launcher);
                break;

            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);

        }

        return convertView;

    }
}
