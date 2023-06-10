package org.csu.musicplayer.bean;

import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class Album {
    public long albumId;
    public String albumName;
    public Bitmap albumBitmap;
    public List<Song> songs = new ArrayList<>();
    public String artistName;

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", albumBitmap=" + albumBitmap +
                ", songs=" + songs +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
