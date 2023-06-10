package org.csu.musicplayer.player;

import android.media.MediaPlayer;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.util.MusicPlayUtil;
import org.csu.musicplayer.util.PlayMode;
import org.csu.musicplayer.util.PlayState;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerPresenter implements PlayerControl {

    private MediaPlayer mMediaPlayer = null;

    private PlayerViewControl mViewController = null;
    private MusicPlayUtil musicPlayUtil = MusicPlayUtil.getInstance();
    private PlayActivity mPlayActivity = null;

    public PlayState mCurrentState = PlayState.PLAY_STATE_STOP;

    private Timer mTimer;
    private SeekTimeTask mTimeTask;

    public PlayerPresenter(PlayActivity playActivity) {
        this.mPlayActivity = playActivity;
    }

    @Override
    public void playOrPause(PlayState playState) {
        if (mViewController == null) {
            this.mViewController = mPlayActivity.mPLayerViewControl;
        }

        if (mCurrentState == PlayState.PLAY_STATE_STOP || playState == PlayState.PLAY_STATE_PLAY) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource();
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(mp -> {
                    mMediaPlayer.start();
                });
                mCurrentState = PlayState.PLAY_STATE_PLAY;
                startTimer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mCurrentState == PlayState.PLAY_STATE_PLAY) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                mCurrentState = PlayState.PLAY_STATE_PAUSE;
                stopTimer();
            }
        } else if (mCurrentState == PlayState.PLAY_STATE_PAUSE) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
                mCurrentState = PlayState.PLAY_STATE_PLAY;
                startTimer();
            }
        }

        mViewController.onPlayerStateChange(mCurrentState);
    }

    @Override
    public void playPrevious() {
        if (mPlayActivity.playMode == PlayMode.REPEAT_LIST) {
            if (mPlayActivity.musicId == 0) {
                mPlayActivity.musicId = mPlayActivity.songNum - 1;
                musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
            } else {
                mPlayActivity.musicId = mPlayActivity.musicId - 1;
                musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
            }
        } else if (mPlayActivity.playMode == PlayMode.REPEAT_ONE) {
            musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
        } else if (mPlayActivity.playMode == PlayMode.SHUFFLE) {
            mPlayActivity.musicId = (mPlayActivity.musicId + (int) (1 + Math.random() * 19)) % mPlayActivity.songNum;
            musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
        }
    }

    @Override
    public void playNext() {
        if (mPlayActivity.playMode == PlayMode.REPEAT_LIST) {
            if (mPlayActivity.musicId == 0) {
                mPlayActivity.musicId = (mPlayActivity.songNum + 1) % mPlayActivity.songNum;
                musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
            } else {
                mPlayActivity.musicId = mPlayActivity.musicId + 1;
                musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
            }
        } else if (mPlayActivity.playMode == PlayMode.REPEAT_ONE) {
            musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
        } else if (mPlayActivity.playMode == PlayMode.SHUFFLE) {
            mPlayActivity.musicId = (mPlayActivity.musicId + (int) (1 + Math.random() * 20)) % mPlayActivity.songNum;
            musicPlayUtil.setMusicView(PlayState.PLAY_STATE_PLAY);
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mCurrentState = PlayState.PLAY_STATE_STOP;
            stopTimer();
            if (mViewController != null) {
                mViewController.onPlayerStateChange(mCurrentState);
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void seekTo(int seek) {
        if (mMediaPlayer != null) {
            int targetSeek = (int) (seek * 1f /100 * mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(targetSeek);
        }
    }

    private class SeekTimeTask extends TimerTask {
        @Override
        public void run() {
            if (mMediaPlayer != null && mViewController != null) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                int currentPositionPercent = (int)(currentPosition * 1.0f / mMediaPlayer.getDuration() * 100);
                if (currentPositionPercent <= 100) {
                    mViewController.onSeekChange(currentPositionPercent);
                }
            }
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimeTask == null) {
            mTimeTask = new SeekTimeTask();
        }
        mTimer.schedule(mTimeTask, 0, 500);
    }

    private void stopTimer() {
        if (mTimeTask != null) {
            mTimeTask.cancel();
            mTimeTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
