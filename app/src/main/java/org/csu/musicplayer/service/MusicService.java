package org.csu.musicplayer.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.R;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.dao.HistoryDao;
import org.csu.musicplayer.database.HistoryDatabase;
import org.csu.musicplayer.entity.History;
import org.csu.musicplayer.util.PlayMode;
import org.csu.musicplayer.utils.MusicProviderUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private final String channelId = "Music";
    private static final String TAG = "MusicService";
    private int indexOfPlaying = -1;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private int nextSongIndex = 0;
    private int preSongIndex = 0;
    private boolean isPlaying = false;
    private boolean isMediaPlayerInit = false;
    private PlayMode playMode = PlayMode.REPEAT_LIST;
    private HistoryDao historyDao;
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Service Create");
        historyDao = HistoryDatabase.getInstance(getApplicationContext()).getHistoryDao();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int next = getNextSongIndex();
                playMusicInIndex(next);
                indexOfPlaying = next;
            }
        });
        createChannel();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle("Music Player")
                .setContentText("没有正在播放的音乐");

        startForeground(NOTIFICATION_ID, mBuilder.build());

    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String name = "Music Channel";
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: Service Destroy");
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public class MusicController extends Binder {
        public void play() {
            if (MusicProviderUtils.getPlaylist().size() > 0) {
                indexOfPlaying = 0;
                playMusicInIndex(0);
            }
        }
        public void playIndex(int index) {
            if (index >= 0 && index < MusicProviderUtils.getPlaylist().size()) {
                indexOfPlaying = index;
                playMusicInIndex(index);
            }
        }
        public void pause() {
            mediaPlayer.pause();
            isPlaying = false;
        }

        public void resume() {
            mediaPlayer.start();
            isPlaying = true;
        }

        public void stop() {

        }

        public void seekTo(int ms) {
            mediaPlayer.seekTo(ms);
        }

        public boolean isPlaying() {
            return isPlaying;
        }
        public PlayMode getPlayMode() {
            return playMode;
        }
        public boolean isMediaPlayerInit() {
            return isMediaPlayerInit;
        }

        public void next() {
            int next = getNextSongIndex();
            playMusicInIndex(next);
            indexOfPlaying = next;
        }

        public void previous() {
            int prev = getPrevSongIndex();
            playMusicInIndex(prev);
            indexOfPlaying = prev;
        }

        public void setPlayMode(PlayMode pm) {
            playMode = pm;
        }

        public long getDuration() {
            return mediaPlayer.getDuration();
        }
        public long getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }
        public Song getCurrentSong() {
            Log.d(TAG, "getCurrentSong: " + isMediaPlayerInit);
            if (isMediaPlayerInit && indexOfPlaying != -1)
                return MusicProviderUtils.getPlaylist().get(indexOfPlaying);
            else
                return null;
        }

    }

    private void playMusicInIndex(int next) {
        mediaPlayer.reset();
        List<Song> songs = MusicProviderUtils.getPlaylist();

        Song song = songs.get(next);
        try {
            mediaPlayer.setDataSource(song.path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        addHistory(song);

        Intent intent = new Intent();
        intent.putExtra("index", next);
        intent.setAction("music");
        sendBroadcast(intent);
        isMediaPlayerInit = true;
        isPlaying = true;
        addTimer();
        mBuilder.setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle("Music Player")
                .setContentText("正在播放"+ song.song + " - " + song.singer);
        startForeground(NOTIFICATION_ID ,mBuilder.build());
    }

    private void addHistory(Song song) {
        Log.d(TAG, "addHistory: " + song.song);
        History history = new History();
        history.setMediaId(song.id);
        history.setPlayTime(new Date());
        historyDao.insertHistory(history);

    }

    private int getNextSongIndex() {
        switch (playMode){
            case REPEAT_LIST:
                if (MusicProviderUtils.getPlaylist().size() <= indexOfPlaying + 1) {
                    return 0;
                } else {
                    return indexOfPlaying + 1;
                }
            case SHUFFLE:
                int totalSongCount = MusicProviderUtils.getPlaylist().size();
                return new Random().nextInt(totalSongCount);
            case REPEAT_ONE:
                return indexOfPlaying;
        }
        return 0;
    }

    private int getPrevSongIndex() {
        switch (playMode){
            case REPEAT_LIST:
                if (indexOfPlaying - 1 < 0) {
                    return MusicProviderUtils.getPlaylist().size() - 1;
                } else {
                    return indexOfPlaying - 1;
                }
            case SHUFFLE:
                int totalSongCount = MusicProviderUtils.getPlaylist().size();
                return new Random().nextInt(totalSongCount);
            case REPEAT_ONE:
                return indexOfPlaying;
        }
        return 0;
    }

    private void addTimer() {
//        if (timer == null) {
//            timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    if(isPlaying && PlayActivity.handler.getLooper() != null){
//                        int duration = mediaPlayer.getDuration();
//                        int currentPosition = mediaPlayer.getCurrentPosition();
//                        Message message = PlayActivity.handler.obtainMessage();
//
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("duration", duration);
//                        bundle.putInt("currentPosition", currentPosition);
//                        message.setData(bundle);
//                        PlayActivity.handler.sendMessage(message);
//                    }
//
//                }
//            };
//            timer.schedule(timerTask, 5, 500);
//        }
    }
}