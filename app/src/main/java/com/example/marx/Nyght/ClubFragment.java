package com.example.marx.Nyght;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marx on 2/13/2017.
 */

public class ClubFragment extends Fragment {

    String[] club_names;
    TypedArray club_profile_pic;
    String[] club_description;

    List<ClubItem> clubItemList;
    ListView myListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_club, container, false);

        clubItemList = new ArrayList<ClubItem>();
        club_profile_pic = getResources().obtainTypedArray(R.array.Club_pics);
        club_names = getResources().getStringArray(R.array.Club_names);
        club_description = getResources().getStringArray(R.array.Club_descriptions);

        for (int i = 0; i < club_names.length; i++) {
            ClubItem club = new ClubItem(club_names[i],
                    club_profile_pic.getResourceId(i, -1),
                    club_description[i]);
            clubItemList.add(club);
        }
        myListView = (ListView) v.findViewById(R.id.list);
        CustomAdapter adapter = new CustomAdapter(this.getContext(), clubItemList);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String club_name = clubItemList.get(position).getClub_name();
                Toast.makeText(getContext(), "" + club_name, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
