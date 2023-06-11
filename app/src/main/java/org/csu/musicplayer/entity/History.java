package org.csu.musicplayer.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "media_id")
    private long mediaId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }
}
