package org.csu.musicplayer.utils;

import org.csu.musicplayer.bean.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicProviderUtils {
    private static List<Song> playlist = new ArrayList<>();
    private static Map<Integer, Integer> idMapIndex = new HashMap<>();
    public static List<Song> getPlaylist() {
        return playlist;
    }

    /*
    @return true add to playlist
            false already in playlist
     */
    public static boolean addToPlaylist(Song song) {
        if (!idMapIndex.containsKey(song.id)) {
            playlist.add(song);
           int index = playlist.indexOf(song);
           idMapIndex.put(song.id, index);
           return true;
        } else {
            return false;
        }
    }
    //TO-DO 切换歌单
    public static void setPlaylist() {

    }

}
