package org.csu.musicplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import org.csu.musicplayer.dao.PlaylistDao;
import org.csu.musicplayer.entity.Playlist;

@Database(entities = {Playlist.class}, version = 1, exportSchema = false)
public abstract class PlaylistDatabase extends RoomDatabase {
    public abstract PlaylistDao getPlaylistDao();
    private static volatile PlaylistDatabase playlistDatabase;

    // TODO 在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，几乎不需要在单个进程中访问多个实例。
    public static PlaylistDatabase getInstance(Context context) {
        if (playlistDatabase == null) {
            synchronized (HistoryDatabase.class) {
                if (playlistDatabase == null) {
                    playlistDatabase = Room.databaseBuilder(context.getApplicationContext(), PlaylistDatabase.class, "playlist.db")
                            .addMigrations()
                            // 默认不允许在主线程中连接数据库
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return playlistDatabase;
    }
}
