package org.csu.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.csu.musicplayer.player.PlayerControl;
import org.csu.musicplayer.player.PlayerPresenter;
import org.csu.musicplayer.player.PlayerViewControl;
import org.csu.musicplayer.util.MusicPlayUtil;
import org.csu.musicplayer.util.PlayMode;
import org.csu.musicplayer.util.PlayState;
import org.json.JSONArray;

public class PlayActivity extends Activity {

    public int musicId;
    public int songNum = 0;
    public String playAddress;
    public PlayMode playMode;
    private boolean isUserTouchSeekBar = false;

    private SeekBar mSeekBar;
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

    public PlayerControl mPlayerControl = new PlayerPresenter(this);
    private MusicPlayUtil musicPlayUtil = MusicPlayUtil.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        musicPlayUtil.setPlayActivity(this);
        initView();
        initEvent();
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

        getMusicListThread();
    }

    private void getMusicListThread() {
        new Thread() {
            @Override
            public void run() {

            }
        }.start();
    }

    private void initEvent() {
        mPlayOrPause.setOnClickListener(v -> {
            if (mPlayerControl != null) {
                mPlayerControl.playOrPause(PlayState.PLAY_STATE_STOP);
            }
        });

        mPlayPrevious.setOnClickListener(v -> {
            if (mPlayerControl != null) {
                mPlayerControl.playPrevious();
            }
        });

        mPlayNext.setOnClickListener(v -> {
            if (mPlayerControl != null) {
                mPlayerControl.playNext();
            }
        });

        mPlayMode.setOnClickListener(v -> {
            if (mPlayerControl != null) {
                if (PlayMode.REPEAT_LIST == playMode) {
                    playMode = PlayMode.REPEAT_ONE;
                    mPlayMode.setBackgroundResource(R.drawable.repeat_one);
                } else if (PlayMode.REPEAT_ONE == playMode) {
                    playMode = PlayMode.SHUFFLE;
                    mPlayMode.setBackgroundResource(R.drawable.shuffle);
                } else if (PlayMode.SHUFFLE == playMode) {
                    playMode = PlayMode.REPEAT_LIST;
                    mPlayMode.setBackgroundResource(R.drawable.repeat_list);
                }
            }
        });

        mPlayList.setOnClickListener(v -> {

        });

        mQuit.setOnClickListener(v -> {

        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekBarProgress = seekBar.getProgress();
                if (mPlayerControl != null) {
                    mPlayerControl.seekTo(seekBarProgress);
                }
                isUserTouchSeekBar = false;
            }
        });
    }

    public PlayerViewControl mPLayerViewControl = new PlayerViewControl() {
        @Override
        public void onPlayerStateChange(PlayState playState) {
            switch (playState) {
                case PLAY_STATE_PLAY:
                    mPlayOrPause.setBackgroundResource(R.drawable.play);
                    break;
                case PLAY_STATE_PAUSE:
                case PLAY_STATE_STOP:
                    mPlayOrPause.setBackgroundResource(R.drawable.pause);
                    break;
            }
        }

        @Override
        public void onSeekChange(int seek) {
            runOnUiThread(() -> {
                if (!isUserTouchSeekBar) {
                    mSeekBar.setProgress(seek);
                    if (seek == 100) {
                        mPlayerControl.playNext();
                    }
                }
            });
        }
    };

}
