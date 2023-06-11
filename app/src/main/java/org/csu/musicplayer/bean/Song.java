package org.csu.musicplayer.bean;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Song implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Bitmap getAlbumBitmap() {
        return albumBitmap;
    }

    public void setAlbumBitmap(Bitmap albumBitmap) {
        this.albumBitmap = albumBitmap;
    }

    public int id;
    public String song;//歌曲名
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度
    public String path;//歌曲地址
    public String album;//专辑
    public long albumId;//专辑海报Id
    public Bitmap albumBitmap;

    @NonNull
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", albumBitmap='" + albumBitmap + '\'' +
                '}';
    }
}