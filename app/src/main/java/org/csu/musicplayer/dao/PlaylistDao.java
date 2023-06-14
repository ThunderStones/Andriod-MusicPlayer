package org.csu.musicplayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import org.csu.musicplayer.entity.Playlist;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    void insertPlaylist(Playlist... histories);
    @Delete
    void deletePlaylist(Playlist... histories);
    @Query("SELECT * FROM PLAYLIST")
    List<Playlist> getPlaylist();
    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    Playlist getPlaylistById(int playlistId);
}
