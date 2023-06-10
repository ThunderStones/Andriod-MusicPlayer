package org.csu.musicplayer.ui.main.local.list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LocalPagerAdapter extends FragmentStateAdapter {
    public LocalPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new LocalSongFragment();
            case 1:
                return new AlbumFragment();
            case 2:
                return new SingerFragment();
            case 3:
                return new FolderFragment();
            default:
                return new Fragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
