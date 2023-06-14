package org.csu.musicplayer.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.csu.musicplayer.R;
import org.csu.musicplayer.ui.main.local.LocalFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {
    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LocalFragment();
            case 1:
                return new PlaylistFragment();
            default:
                return new LocalFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}