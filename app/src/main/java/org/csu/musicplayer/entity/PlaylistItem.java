package org.csu.musicplayer.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_item")
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "playlist_id")
    private int playlistId;
    @ColumnInfo(name = "media_id")
    private int mediaId;

}
