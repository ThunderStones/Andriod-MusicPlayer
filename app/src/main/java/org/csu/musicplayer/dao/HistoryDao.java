package org.csu.musicplayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import org.csu.musicplayer.entity.History;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(History... histories);
    @Delete
    void deleteHistory(History... histories);
    @Query("SELECT * FROM history ORDER BY ID DESC")
    List<History> getHistories();
}
