package org.csu.musicplayer.ui.main.local.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.csu.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleSongFragment extends Fragment {


    public SingleSongFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_song, container, false);

    }
}