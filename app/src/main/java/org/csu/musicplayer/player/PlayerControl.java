package org.csu.musicplayer.player;

import org.csu.musicplayer.util.PlayState;

public interface PlayerControl {
    void playOrPause(PlayState playState);

    void playPrevious();

    void playNext();

    void stop();

    void seekTo(int seek);
}
