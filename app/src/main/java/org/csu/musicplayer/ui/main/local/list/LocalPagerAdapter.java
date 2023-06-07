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
        return new Fragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
