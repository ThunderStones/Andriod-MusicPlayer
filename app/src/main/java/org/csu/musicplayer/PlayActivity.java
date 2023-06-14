package org.csu.musicplayer;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.csu.musicplayer.bean.Song;

import org.csu.musicplayer.service.MusicService;
import org.csu.musicplayer.util.PlayMode;
import org.csu.musicplayer.utils.MusicProviderUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timer;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Service Connected");
            controller = (MusicService.MusicController) service;
            Song song = controller.getCurrentSong();
            Log.d(TAG, "onServiceConnected: " + song);
            if (song != null) {
                updateSongInfo(song);
            }
            if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(controller.isPlaying()){
                        long duration = controller.getDuration();
                        long currentPosition = controller.getCurrentPosition();
                        mSeekBar.setMax((int) duration);
                        mSeekBar.setProgress((int) currentPosition);

                    }

                }
            };
            timer.schedule(timerTask, 5, 500);
        }
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


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unbindService(connection);
        unregisterReceiver(musicReceive);
    }

    private void initView() {
        mSeekBar = (SeekBar) this.findViewById(R.id.seek_bar);
        mPlayOrPause = (Button) this.findViewById(R.id.play_or_pause_btn);
        mPlayMode = (Button) this.findViewById(R.id.play_mode_btn);
        mPlayPrevious = (Button) this.findViewById(R.id.play_previous_btn);
        mPlayNext = (Button) this.findViewById(R.id.play_next_btn);
        mPlayList = (Button) this.findViewById(R.id.play_list_btn);
        mQuit = (Button) this.findViewById(R.id.quit_btn);
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
            if (!controller.isMediaPlayerInit()) {
                controller.play();
                mSeekBar.setMax((int) controller.getDuration());
                mPlayOrPause.setBackgroundResource(R.drawable.play);
            } else if (controller.isPlaying()) {
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
                    playMode = PlayMode.REPEAT_ONE;
                    mPlayMode.setBackgroundResource(R.drawable.repeat_one);
                    controller.setPlayMode(PlayMode.REPEAT_ONE);
                    break;
                case REPEAT_ONE:
                    playMode = PlayMode.SHUFFLE;
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
            View view = getLayoutInflater().inflate( R.layout.playlist, null);
            ListView listView = view.findViewById(R.id.list_view);
            List<Song> playlist = MusicProviderUtils.getPlaylist();
            List<HashMap<String, String>> list = new ArrayList<>();
            String[] from = new String[]{"title", "subtitle"};
            int[] to = new int[]{android.R.id.text1, android.R.id.text2};
            for (Song song :
                    playlist) {
                HashMap<String, String> item1 = new HashMap<>();
                item1.put("title", song.song);
                item1.put("subtitle", song.singer);
                list.add(item1);
            }


            SimpleAdapter adapter = new SimpleAdapter(this, list,
                    android.R.layout.simple_list_item_2, from, to);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(PlayActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    controller.playIndex(position);
                }
            });
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        });

        mQuit.setOnClickListener(v -> {
            finish();
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

//    public static Handler handler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            Bundle bundle = msg.getData();
//            int duration = bundle.getInt("duration");
//            int currentPosition = bundle.getInt("currentPosition");
//            mSeekBar.setMax(duration);
//            mSeekBar.setProgress(currentPosition);
//        }
//    };

    //内部类，实现BroadcastReceiver
    public class MusicReceiver extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (intentAction.equals("music")) {
                int index = intent.getIntExtra("index", -1);
                if (index != -1) {
                    Song song = MusicProviderUtils.getPlaylist().get(index);
                    Log.d(TAG, "onReceive: " + song.toString());
                    updateSongInfo(song);
                }
            }
        }
    }

    private void updateSongInfo(Song song) {
        if (song == null)
            return;
        mMusicName.setText(song.song);
        mMusicArtist.setText(song.singer);
        mMusicPic.setImageBitmap(song.albumBitmap);
        mSeekBar.setMax(song.duration);
        if (controller.isPlaying()) {
            mPlayOrPause.setBackgroundResource(R.drawable.play);
        } else {
            mPlayOrPause.setBackgroundResource(R.drawable.pause);
        }
        switch (controller.getPlayMode()) {
            case REPEAT_LIST:
                mPlayMode.setBackgroundResource(R.drawable.repeat_list);
                break;
            case REPEAT_ONE:
                mPlayMode.setBackgroundResource(R.drawable.repeat_one);
                break;
            case SHUFFLE:
                mPlayMode.setBackgroundResource(R.drawable.shuffle);
                break;
        }

    }

}

