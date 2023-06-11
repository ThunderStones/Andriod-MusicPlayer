package org.csu.musicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.csu.musicplayer.bean.Song;

import org.csu.musicplayer.service.MusicService;
import org.csu.musicplayer.util.PlayMode;
import org.json.JSONArray;

import java.util.List;

public class PlayActivity extends Activity {
    private static final String TAG = "PlayActivity";
    public int musicId;
    public int songNum = 0;
    public String playAddress;
    public PlayMode playMode = PlayMode.REPEAT_LIST;
    private boolean isUserTouchSeekBar = false;
    private Song currentSong;
    private MusicReceiver musicReceive;
    private MusicService.MusicController controller;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller = (MusicService.MusicController) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private static SeekBar mSeekBar;
    private Button mPlayOrPause;
    private Button mPlayMode;
    private Button mPlayPrevious;
    private Button mPlayNext;
    private Button mPlayList;
    private Button mQuit;
    public TextView mMusicName;
    public TextView mMusicArtist;
    public ImageView mMusicPic;
    public static JSONArray musicList;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        initView();
        initEvent();
        initService();
        initBroadcast();
    }

    private void initBroadcast() {
        musicReceive = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("music");
        registerReceiver(musicReceive, filter);
    }

    private void initService() {
        Intent intent = new Intent(PlayActivity.this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void updateView(MediaMetadataCompat metadata) {
        mMusicName.setText(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
        mMusicArtist.setText(metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        mMusicPic.setImageBitmap(metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void initView() {
        mSeekBar = (SeekBar) this.findViewById(R.id.seek_bar);
        mPlayOrPause = (Button) this.findViewById(R.id.play_or_pause_btn);
        mPlayMode = (Button) this.findViewById(R.id.play_mode_btn);
        mPlayPrevious= (Button) this.findViewById(R.id.play_previous_btn);
        mPlayNext = (Button) this.findViewById(R.id.play_next_btn);
        mPlayList = (Button) this.findViewById(R.id.play_list_btn);
        mQuit=(Button) this.findViewById(R.id.quit_btn);
        mMusicName = (TextView) this.findViewById(R.id.name_txt);
        mMusicArtist = (TextView) this.findViewById(R.id.artist_txt);
        mMusicPic = (ImageView) this.findViewById(R.id.iv_icon);

        if (PlayMode.REPEAT_LIST == playMode) {
            mPlayMode.setBackgroundResource(R.drawable.repeat_list);
        } else if (PlayMode.REPEAT_ONE == playMode) {
            mPlayMode.setBackgroundResource(R.drawable.repeat_one);
        } else if (PlayMode.SHUFFLE == playMode) {
            mPlayMode.setBackgroundResource(R.drawable.shuffle);
        }

    }



    private void initEvent() {
        mPlayOrPause.setOnClickListener(v -> {
            Log.d(TAG, "initEvent: PLAY");
            if(!controller.isMediaPlayerInit()) {
                controller.play();
                mSeekBar.setMax((int) controller.getDuration());
                mPlayOrPause.setBackgroundResource(R.drawable.play);
            } else if (controller.isPlaying()){
                controller.pause();
                mPlayOrPause.setBackgroundResource(R.drawable.pause);
            } else {
                controller.resume();
                mPlayOrPause.setBackgroundResource(R.drawable.play);
            }


        });

        mPlayPrevious.setOnClickListener(v -> {
            controller.previous();
        });

        mPlayNext.setOnClickListener(v -> {
            controller.next();
        });

        mPlayMode.setOnClickListener(v -> {
            switch (playMode) {
                case REPEAT_LIST:
                    playMode= PlayMode.REPEAT_ONE;
                    mPlayMode.setBackgroundResource(R.drawable.repeat_one);
                    controller.setPlayMode(PlayMode.REPEAT_ONE);
                    break;
                case REPEAT_ONE:
                    playMode= PlayMode.SHUFFLE;
                    mPlayMode.setBackgroundResource(R.drawable.shuffle);
                    controller.setPlayMode(PlayMode.SHUFFLE);
                    break;
                case SHUFFLE:
                    playMode = PlayMode.REPEAT_LIST;
                    mPlayMode.setBackgroundResource(R.drawable.repeat_list);
                    controller.setPlayMode(PlayMode.REPEAT_LIST);
                    break;
            }
        });

        mPlayList.setOnClickListener(v -> {

        });

        mQuit.setOnClickListener(v -> {

        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekBarProgress = seekBar.getProgress();
                controller.seekTo(seekBarProgress);
                isUserTouchSeekBar = false;
            }
        });
    }

    public static Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPosition);
        }
    };
    //内部类，实现BroadcastReceiver
    public class MusicReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals("music")) {
                Song song = intent.getExtras().getSerializable("song", Song.class);
                Log.d(TAG, "onReceive: " + song.toString());
            }
        }
    }

}

