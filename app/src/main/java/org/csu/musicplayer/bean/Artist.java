package org.csu.musicplayer.bean;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    public String ArtistName;
    public List<Song> songs = new ArrayList<>();

    @Override
    public String toString() {
        return "Artist{" +
                "ArtistName='" + ArtistName + '\'' +
                ", songs=" + songs +
                '}';
    }
}
