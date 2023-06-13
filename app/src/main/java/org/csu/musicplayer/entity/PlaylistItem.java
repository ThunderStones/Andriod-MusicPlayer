package org.csu.musicplayer.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_item")
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "playlist_id")
    private int playlistId;
    @ColumnInfo(name = "media_id")
    private int mediaId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }
}
