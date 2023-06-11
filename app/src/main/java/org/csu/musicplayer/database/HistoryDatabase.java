package org.csu.musicplayer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.csu.musicplayer.dao.HistoryDao;
import org.csu.musicplayer.entity.History;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDao getHistoryDao();
    private static volatile HistoryDatabase historyDatabase;

    // TODO 在实例化 AppDatabase 对象时应遵循单例设计模式。每个 RoomDatabase 实例的成本相当高，几乎不需要在单个进程中访问多个实例。
    static HistoryDatabase getInstance(Context context) {
        if (historyDatabase == null) {
            synchronized (HistoryDatabase.class) {
                if (historyDatabase == null) {
                    historyDatabase = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "history.db")
                            .addMigrations()
                            // 默认不允许在主线程中连接数据库
                            // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return historyDatabase;
    }

}
