package org.csu.musicplayer.utils;

import org.csu.musicplayer.bean.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicProviderUtils {
    private static List<Song> playlist = new ArrayList<>();

    public static List<Song> getPlaylist() {
        return playlist;
    }
    public static void addToPlaylist(Song song) {
        playlist.add(song);
    }
}
