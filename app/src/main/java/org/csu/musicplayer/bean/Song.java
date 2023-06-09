package org.csu.musicplayer.bean;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public class Song {
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