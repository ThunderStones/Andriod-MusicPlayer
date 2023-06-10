package org.csu.musicplayer.player;

import org.csu.musicplayer.util.PlayState;

public interface PlayerViewControl {
    void onPlayerStateChange(PlayState playState);

    void onSeekChange(int seek);
}
