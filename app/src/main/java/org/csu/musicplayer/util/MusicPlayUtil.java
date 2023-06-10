package org.csu.musicplayer.util;

import android.net.Uri;

import org.csu.musicplayer.PlayActivity;
import org.csu.musicplayer.R;
import org.csu.musicplayer.player.PlayerControl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class MusicPlayUtil {
    private PlayActivity playActivity = null;
    private PlayerControl mPlayerControl = null;

    private MusicPlayUtil() {}

    public static MusicPlayUtil musicPlayUtil = new MusicPlayUtil();

    public static MusicPlayUtil getInstance() {
        return musicPlayUtil;
    }

    public void setPlayActivity(PlayActivity playActivity) {
        this.playActivity = playActivity;
        mPlayerControl = playActivity.mPlayerControl;
    }

    public JSONArray getMusicList() {
        return playActivity.musicList;
    }

    public int getMusicId() {
        return playActivity.musicId;
    }

    public int getMusicNum() {
        return playActivity.songNum;
    }

    public void setMusicId(int id) {
        playActivity.musicId = id;
    }

    public void setMusicView(PlayState playState) {
        try {
            JSONObject musicInfo = (JSONObject) playActivity.musicList.get(playActivity.musicId);
            String name = musicInfo.optString("name");
            String author = musicInfo.optString("author");
            String img = musicInfo.optString("img");
            playActivity.playAddress=musicInfo.optString("address");
            playActivity.mMusicPic.setImageURI(Uri.fromFile);
            playActivity.mMusicName.setText(name);
            playActivity.mMusicArtist.setText(author);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (playState == PlayState.PLAY_STATE_PLAY) {
            if (mPlayerControl != null) {
                mPlayerControl.stop();
            }
            mPlayerControl.playOrPause(playState);
        }
    }
}
