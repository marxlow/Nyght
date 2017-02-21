package com.example.marx.Nyght;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marx on 2/21/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<ClubItem> clubItems;

    CustomAdapter(Context context, List<ClubItem> clubItems) {
        this.context = context;
        this.clubItems = clubItems;
    }
    @Override
    public int getCount() {
        return clubItems.size();
    }

    @Override
    public Object getItem(int position) {
        return clubItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return clubItems.indexOf(getItem(position));
    }

    // Private view holder class
    private class ViewHolder {
        ImageView club_cover_picture;
        TextView club_name;
        TextView club_description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.club_list, null);
            holder = new ViewHolder();

            holder.club_cover_picture = (ImageView) convertView.findViewById(R.id.club_cover_picture);
            holder.club_name = (TextView) convertView.findViewById(R.id.club_names);
            holder.club_description = (TextView) convertView.findViewById(R.id.club_description);

            ClubItem club_pos = clubItems.get(position);
            holder.club_cover_picture.setImageResource(club_pos.getClub_cover_picture_id());
            holder.club_name.setText(club_pos.getClub_name());
            holder.club_description.setText(club_pos.getClub_description());

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;

    }
}
