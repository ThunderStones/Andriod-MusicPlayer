package org.csu.musicplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.csu.musicplayer.dao.PlaylistItemDao;
import org.csu.musicplayer.entity.PlaylistItem;

@Database(entities = {PlaylistItem.class}, version = 1, exportSchema = false)
public abstract class PlaylistItemDatabase extends RoomDatabase {
    public abstract PlaylistItemDao getPlaylistItemDao();
    private static volatile PlaylistItemDatabase playlistItemDatabase;

    // TODO 在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，几乎不需要在单个进程中访问多个实例。
    public static PlaylistItemDatabase getInstance(Context context) {
        if (playlistItemDatabase == null) {
            synchronized (HistoryDatabase.class) {
                if (playlistItemDatabase == null) {
                    playlistItemDatabase = Room.databaseBuilder(context.getApplicationContext(), PlaylistItemDatabase.class, "playlistItem.db")
                            .addMigrations()
                            // 默认不允许在主线程中连接数据库
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return playlistItemDatabase;
    }

}
