package org.csu.musicplayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import org.csu.musicplayer.entity.PlaylistItem;

import java.util.List;

@Dao
public interface PlaylistItemDao {
    @Insert
    void insertPlaylistItem(PlaylistItem... playlistItems);
    @Delete
    void deletePlaylistItem(PlaylistItem... playlistItems);
    @Query("SELECT * FROM playlist_item WHERE playlist_id = :playlistId")
    List<PlaylistItem> getPlaylistItemsByPlaylistId(int playlistId);
}
