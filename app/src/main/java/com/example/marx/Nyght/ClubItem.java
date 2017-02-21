package com.example.marx.Nyght;

/**
 * Created by Marx on 2/21/2017.
 */

public class ClubItem {

    private String club_name;
    private int club_cover_picture_id;
    private String club_description;

    public ClubItem(String club_name, int club_cover_picture_id, String club_description) {
        this.club_name = club_name;
        this.club_cover_picture_id = club_cover_picture_id;
        this.club_description = club_description;
    }

    public String getClub_name() {
        return this.club_name;
    }

    public int getClub_cover_picture_id() {
        return this.club_cover_picture_id;
    }

    public String getClub_description() {
        return this.club_description;
    }
}
