package org.csu.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.bean.Song;
import org.csu.musicplayer.util.PlayMode;
import org.csu.musicplayer.utils.MusicProviderUtils;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private int indexOfPlaying = -1;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private int nextSongIndex = 0;
    private int preSongIndex = 0;
    private boolean isPlaying = false;
    private boolean isMediaPlayerInit = false;
    private PlayMode playMode;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int next = getNextSongIndex();
                playMusicInIndex(next);
                indexOfPlaying = next;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MusicController extends Binder {
        public void play() {
            if (MusicProviderUtils.getPlaylist().size() > 0) {
                playMusicInIndex(0);
                addTimer();
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

        public boolean isMediaPlayerInit() {
            return isMediaPlayerInit;
        }

        public void next() {
            int next = getNextSongIndex();
            playMusicInIndex(next);
            indexOfPlaying = next;
        }

        public void previous() {
            Log.d(TAG, "previous: " + indexOfPlaying);
            int prev = getPrevSongIndex();
            Log.d(TAG, "previous: " + prev);

            playMusicInIndex(prev);
            indexOfPlaying = prev;
        }

        public void setPlayMode(PlayMode pm) {
            playMode = pm;
        }

        public long getDuration() {
            return mediaPlayer.getDuration();
        }
        public Song getCurrentSong() {
            return MusicProviderUtils.getPlaylist().get(indexOfPlaying);
        }

    }

    private void playMusicInIndex(int next) {
        mediaPlayer.reset();
        List<Song> songs = MusicProviderUtils.getPlaylist();

        Song song = songs.get(next);
        try {
            mediaPlayer.setDataSource(songs. get(next).path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
        intent.putExtras(bundle);
        intent.setAction("music");
        sendBroadcast(intent);
        isMediaPlayerInit = true;
        isPlaying = true;
    }

    private int getNextSongIndex() {
        if (MusicProviderUtils.getPlaylist().size() <= indexOfPlaying + 1) {
            return 0;
        } else {
            return indexOfPlaying + 1;
        }
    }

    private int getPrevSongIndex() {
        if (indexOfPlaying - 1 < 0) {
            return MusicProviderUtils.getPlaylist().size() - 1;
        } else {
            return indexOfPlaying - 1;
        }
    }

    private void addTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(isPlaying){
                        int duration = mediaPlayer.getDuration();
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        Message message = PlayActivity.handler.obtainMessage();

                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentPosition", currentPosition);
                        message.setData(bundle);
                        PlayActivity.handler.sendMessage(message);
                    }

                }
            };
            timer.schedule(timerTask, 5, 500);
        }
    }
}