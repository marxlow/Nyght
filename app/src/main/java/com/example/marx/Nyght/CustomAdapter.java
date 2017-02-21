package com.example.marx.Nyght;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marx on 2/21/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<ClubItem> clubItems;
    private List<ClubItem> filteredData;
    private Filter mfilter;

    CustomAdapter(Context context, List<ClubItem> clubItems) {
        this.context = context;
        this.clubItems = clubItems;
        this.filteredData = clubItems;
    }
    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return filteredData.indexOf(getItem(position));
    }

    public List<ClubItem> getClubItems() {
        return this.filteredData;
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

            ClubItem club_pos = filteredData.get(position);
            holder.club_cover_picture.setImageResource(club_pos.getClub_cover_picture_id());
            holder.club_name.setText(club_pos.getClub_name());
            holder.club_description.setText(club_pos.getClub_description());

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;

    }
}
